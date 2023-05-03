package org.teamfour.registry;

import org.teamfour.logging.StaticLogBase;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class VoterRegistry extends StaticLogBase {
    public static final String hostName = "";
    public static final int port = 1234;
    static final int CORE_POOL = 2;
    static final int MAX_POOL = 4;
    static final int KEEP_ALIVE = 10;
    static ThreadPoolExecutor executor;
    static ServerSocket registryServerSocket;

    public static void main(String[] args) {

        try {
            InetAddress address = InetAddress.getByName(hostName);
            registryServerSocket = new ServerSocket(port, 0, address);
        } catch (IOException e) {
            log.error(e.getMessage());
            log.error("UNABLE TO OPEN SERVER SOCKET, SHUTTING DOWN");
            System.exit(0);
        }

        log.info("SUCCESSFULLY OPENED PORT AT " + hostName);

        executor = new ThreadPoolExecutor(CORE_POOL,
                MAX_POOL,
                KEEP_ALIVE,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>());

        while (true) {
            try {
                Socket client = registryServerSocket.accept();
                RequestHandler handler = new RequestHandler(client);
                executor.submit(handler);
            } catch (IOException e) {
                log.error(e.getMessage());
                triggerShutdown();
                break;
            }
        }

    }

    private static void triggerShutdown() {
        try {
            log.info("SHUTDOWN TRIGGERED");
            executor.shutdownNow();
            registryServerSocket.close();
        } catch (IOException e) {
            log.error("SHUTDOWN FAILED SYSTEM EXITING");
            System.exit(1);
        }

    }

}
