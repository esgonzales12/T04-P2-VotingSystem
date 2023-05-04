package org.teamfour.registry.server;

import org.apache.commons.lang3.StringUtils;
import org.teamfour.registry.dao.RegistryDao;
import org.teamfour.registry.data.RegisteredVoter;
import org.teamfour.registry.data.Registry;
import org.teamfour.registry.data.RegistryMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class RequestHandler implements Runnable {
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
    }
    private final Logger log;
    private final RegistryDao registryDao;
    private final AtomicBoolean running;
    private final Socket client;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;

    public RequestHandler(Socket client) throws IOException {
        this.client = client;
        this.registryDao = new RegistryDao();
        this.running = new AtomicBoolean(false);
        this.outputStream = new ObjectOutputStream(client.getOutputStream());
        this.inputStream = new ObjectInputStream(client.getInputStream());
        this.log = Logger.getLogger("Request Handler");
    }

    @Override
    public void run() {
        running.set(true);
        while (running.get()) {
            RegistryMessage request = null;
            try {
                request = (RegistryMessage) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                log.warning("ERROR ON READING OBJECT FROM OBJECT INPUT STREAM");
                disconnect();
            }

            log.info("REQUEST RECEIVED");

            if (request == null) {
                log.warning("REGISTRY REQUEST IS NULL");
                disconnect();
                break;
            }

            log.info("RECEIVED REQUEST: \n" + request);
            handleRequest(request);
        }
    }

    private void handleRequest(RegistryMessage request) {
        RegistryMessage.Builder response = new RegistryMessage.Builder();

        if (request.getType() == null) {
            sendError("request type required");
            return;
        } else if (request.getVoterAccessCode() == null) {
            sendError("voter access code required");
            return;
        }

        switch (request.getType()) {
            case GET_VOTER_STATUS -> {
                Optional<RegisteredVoter> voterOptional = registryDao.find(request.getVoterAccessCode());
                voterOptional.ifPresentOrElse(
                        registeredVoter -> {
                            response.setVoterStatus(registeredVoter.getVoteStatus());
                            response.setType(Registry.MessageType.VOTER_STATUS);
                            log.info("VOTER FOUND");
                        },
                        () -> {
                            response.setType(Registry.MessageType.ERROR);
                            response.setMessage("unable to locate voter");
                        });
            }
            case MARK_VOTE_COUNTED -> {
                boolean updated = registryDao.update(request.getVoterAccessCode(), Registry.VoteStatus.VOTED);
                if (updated) {
                    response.setType(Registry.MessageType.VOTER_STATUS_UPDATED);
                    response.setMessage("status successfully updated");
                } else {
                    response.setType(Registry.MessageType.ERROR);
                    response.setMessage("unable to mark voter " + request.getVoterAccessCode() + " as voted");
                }
            }
        }

        sendResponse(response.build());
        disconnect();
    }

    private void sendError(String message) {
        RegistryMessage errResponse = new RegistryMessage.Builder()
                .setType(Registry.MessageType.ERROR)
                .setMessage(StringUtils.isEmpty(message) ? "unknown error" : message)
                .build();
        sendResponse(errResponse);
    }

    private void sendResponse(RegistryMessage response) {
        try {
            outputStream.reset();
            outputStream.writeObject(response);
            outputStream.flush();
            log.info("WROTE MESSAGE: " + response.toString());
        } catch (IOException e) {
            log.warning("ERROR WRITING OUTPUT TO AUCTION CLIENT");
            disconnect();
        }
    }


    private void disconnect() {
        log.info("ATTEMPTING TO DISCONNECT");
        try {
            outputStream.close();
            inputStream.close();
            client.close();
            running.set(false);
            log.info("SUCCESSFULLY DISCONNECTED REQUEST HANDLER");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
