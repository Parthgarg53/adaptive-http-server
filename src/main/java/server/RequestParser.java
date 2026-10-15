package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {

    public static HttpRequest parse(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        if (requestLine == null) return null;
        String[] parts = requestLine.split(" ");
        if (parts.length != 3) return null;

        Map<String, String> headers = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            int colon = line.indexOf(':');
            if (colon > 0) {
                headers.put(line.substring(0, colon).trim().toLowerCase(), line.substring(colon + 1).trim());
            }
        }

        String target = parts[1];
        int question = target.indexOf('?');
        String path = question < 0 ? target : target.substring(0, question);
        String query = question < 0 ? "" : target.substring(question + 1);
        return new HttpRequest(parts[0], path, query, headers);
    }
}