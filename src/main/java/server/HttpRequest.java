package server;

import java.util.Map;

public record HttpRequest(String method, String path, String query, Map<String, String> headers) {}