package org.teamfour.registry;

import org.teamfour.registry.dao.RegistryDao;

import java.util.concurrent.atomic.AtomicBoolean;

public class RequestHandler implements Runnable {

    private final RegistryDao registryDao;
    private final AtomicBoolean running;
    public RequestHandler() {
        registryDao = new RegistryDao();
        running = new AtomicBoolean(false);
    }
    @Override
    public void run() {

    }
}
