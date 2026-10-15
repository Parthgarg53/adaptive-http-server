package server;

import java.util.HashMap;
import java.util.Map;

public class Router {
    private final Map<String, Map<String, Handler>> routes = new HashMap<>();

    public void add(String method, String path, Handler handler) {
        routes.computeIfAbsent(path, key -> new HashMap<>()).put(method, handler);
    }

    public HttpResponse route(HttpRequest request) {
        Map<String, Handler> handlers = routes.get(request.path());
        if (handlers == null) return HttpResponse.text(404, "Not Found", "not found\n");
        Handler handler = handlers.get(request.method());
        if (handler == null) return HttpResponse.text(405, "Method Not Allowed", "method not allowed\n");
        return handler.handle(request);
    }
}