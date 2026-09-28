package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
            client.setSoTimeout(5000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            HttpRequest request = RequestParser.parse(reader);
            HttpResponse response = request == null
                    ? HttpResponse.text(400, "Bad Request", "bad request\n")
                    : HttpResponse.text(200, "OK", "hello from " + request.path() + "\n");
            response.writeTo(client.getOutputStream());
        } catch (IOException ignored) {
        }
    }
}