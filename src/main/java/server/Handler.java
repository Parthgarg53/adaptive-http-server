package server;

@FunctionalInterface
public interface Handler {
    HttpResponse handle(HttpRequest request);
}