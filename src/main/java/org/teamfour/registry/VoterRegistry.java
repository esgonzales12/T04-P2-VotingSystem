package org.teamfour.registry;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class VoterRegistry {
    static ThreadPoolExecutor executor;
    static ServerSocket registryServerSocket;
    public static void main(String[] args) {
        try {
            registryServerSocket = new ServerSocket(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        executor = new ThreadPoolExecutor(2, 2, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
        while (true) {
            try {
                Socket client = registryServerSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
