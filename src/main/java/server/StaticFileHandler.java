package server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class StaticFileHandler implements Handler {
    private static final Map<String, String> CONTENT_TYPES = Map.of(
            "html", "text/html; charset=utf-8",
            "css", "text/css; charset=utf-8",
            "js", "text/javascript; charset=utf-8",
            "json", "application/json",
            "png", "image/png",
            "jpg", "image/jpeg",
            "txt", "text/plain; charset=utf-8");

    private final Path root;

    public StaticFileHandler(Path root) {
        this.root = root.toAbsolutePath().normalize();
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        if (!request.method().equals("GET")) {
            return HttpResponse.text(405, "Method Not Allowed", "method not allowed\n");
        }
        String requested = request.path().equals("/") ? "/index.html" : request.path();
        Path file = root.resolve(requested.substring(1)).normalize();
        if (!file.startsWith(root) || !Files.isRegularFile(file)) {
            return HttpResponse.text(404, "Not Found", "not found\n");
        }
        try {
            return new HttpResponse(200, "OK", contentType(file), Files.readAllBytes(file));
        } catch (IOException e) {
            return HttpResponse.text(500, "Internal Server Error", "error\n");
        }
    }

    private String contentType(Path file) {
        String name = file.getFileName().toString();
        String extension = name.substring(name.lastIndexOf('.') + 1);
        return CONTENT_TYPES.getOrDefault(extension, "application/octet-stream");
    }
}