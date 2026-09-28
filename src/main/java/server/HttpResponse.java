package server;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public record HttpResponse(int status, String reason, String contentType, byte[] body) {

    public static HttpResponse text(int status, String reason, String body) {
        return new HttpResponse(status, reason, "text/plain; charset=utf-8", body.getBytes(StandardCharsets.UTF_8));
    }

    public void writeTo(OutputStream out) throws IOException {
        String head = "HTTP/1.1 " + status + " " + reason + "\r\n"
                + "Content-Type: " + contentType + "\r\n"
                + "Content-Length: " + body.length + "\r\n"
                + "Connection: close\r\n\r\n";
        out.write(head.getBytes(StandardCharsets.UTF_8));
        out.write(body);
        out.flush();
    }
}
