package org.teamfour.registry.client;

import org.teamfour.registry.data.RegistryMessage;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RegistryFacadeImpl {
    private static final int CORE_POOL = 2;
    private static final int MAX_POOL = 4;
    private static final int KEEP_ALIVE = 10;
    private final ThreadPoolExecutor executor;
    private final int port;
    private final String host;

    public RegistryFacadeImpl(int port, String host) {
        this.port = port;
        this.host = host;
        executor = new ThreadPoolExecutor(CORE_POOL,
                MAX_POOL,
                KEEP_ALIVE,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>());
    }

    public RegistryMessage handleRequest(RegistryMessage request) {
        RegistryClient client;

        try {
            client = new RegistryClient(host, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        executor.submit(client);
        client.sendRequest(request);

        synchronized (this) {
            while (!client.responseReceived.get()) {
                try {
                    wait(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return client.getResponse();
    }

    public void shutdown() {
        executor.shutdownNow();
    }

}
