package org.teamfour.registry.client;

import org.teamfour.registry.data.RegistryMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class RegistryClient implements Runnable {
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
    }

    private final Logger log;
    private final Socket clientSocket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    protected final AtomicBoolean responseReceived;
    protected final AtomicBoolean running;
    protected RegistryMessage response;

    public RegistryClient(String host, int port) throws IOException {
        responseReceived = new AtomicBoolean(false);
        clientSocket = new Socket(host, port);
        outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        inputStream = new ObjectInputStream(clientSocket.getInputStream());
        log = Logger.getLogger(RegistryClient.class.getName());
        running = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        running.set(true);
        while (!responseReceived.get() && running.get()) {
            RegistryMessage response;
            try {
                response = (RegistryMessage) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                log.warning(e.getMessage());
                log.warning("CLIENT UNABLE TO READ RESPONSE");
                break;
            }
            log.info("RECEIVED RESPONSE: " + response.toString());
            this.response = response;
            responseReceived.set(true);
        }
        disconnect();
    }

    public RegistryMessage getResponse() {
        return response;
    }

    public void sendRequest(RegistryMessage request) {
        try {
            outputStream.reset();
            outputStream.writeObject(request);
            outputStream.flush();
        } catch (IOException e) {
            log.warning(e.getMessage());
            log.warning("ERROR WRITING OUTPUT TO SERVER");
            disconnect();
        }
    }

    protected void disconnect() {
        try {
            outputStream.close();
            inputStream.close();
            clientSocket.close();
            running.set(false);
            log.info("SUCCESSFULLY DISCONNECTED CLIENT PROXY");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
