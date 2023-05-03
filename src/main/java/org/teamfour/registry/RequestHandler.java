package org.teamfour.registry;

import org.teamfour.logging.LogBase;
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

public class RequestHandler extends LogBase implements Runnable {
    private final RegistryDao registryDao;
    private final AtomicBoolean running;
    private final Socket client;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;

    public RequestHandler(Socket client) throws IOException {
        super(RequestHandler.class.getName());
        this.client = client;
        this.registryDao = new RegistryDao();
        this.running = new AtomicBoolean(false);
        this.outputStream = new ObjectOutputStream(client.getOutputStream());
        this.inputStream = new ObjectInputStream(client.getInputStream());
    }

    @Override
    public void run() {
        while (running.get()) {
            RegistryMessage request = null;
            try {
                request = (RegistryMessage) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                log.warn("ERROR ON READING OBJECT FROM OBJECT INPUT STREAM");
                disconnect();
            }

            if (request == null) {
                log.warn("REGISTRY REQUEST IS NULL");
                disconnect();
                break;
            }

            handleRequest(request);
        }
    }

    private void handleRequest(RegistryMessage request) {
        RegistryMessage.Builder response = new RegistryMessage.Builder();
        switch (request.getType()) {
            case GET_VOTER_STATUS -> {
                Optional<RegisteredVoter> voterOptional = registryDao.find(request.getVoterAccessCode());
            }
            case MARK_VOTE_COUNTED -> {
                boolean updated = registryDao.update(request.getVoterAccessCode(), Registry.VoteStatus.VOTED);
            }
        }
        disconnect();
    }

    private void sendResponse(RegistryMessage response) {
        try {
            outputStream.reset();
            outputStream.writeObject(response);
            outputStream.flush();
            log.info("WROTE MESSAGE TYPE: " + response.getType());
        } catch (IOException e) {
            log.error("ERROR WRITING OUTPUT TO AUCTION CLIENT");
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
