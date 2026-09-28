package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Listening on 8080");
            while (true) {
                Socket client = serverSocket.accept();
                pool.execute(() -> handle(client));
            }
        }
    }

    private static void handle(Socket client) {
        try (client) {
            client.getOutputStream().write("hello\n".getBytes());
        } catch (IOException ignored) {
        }
    }
}
