import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class MyHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {

        // CORS headers
        Headers headers = he.getResponseHeaders();
        headers.set("Access-Control-Allow-Origin", "*");
        headers.set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        headers.set("Access-Control-Allow-Headers", "Content-Type");

        // Handle browser preflight request
        if ("OPTIONS".equalsIgnoreCase(he.getRequestMethod())) {
            he.sendResponseHeaders(204, -1);
            return;
        }

        headers.set("Content-Type", "application/json; charset=utf-8");
        headers.set("Connection", "close");

        String requestMethod = he.getRequestMethod();
        String requestBody = "";

        if ("POST".equalsIgnoreCase(requestMethod)) {
            InputStream is = he.getRequestBody();
            requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }

        System.out.println("Received JSON: " + requestBody);

        String safeBody = requestBody
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");

        String response = "{"
                + "\"message\":\"JSON received successfully, \"{\\\"name\\\":\\\"Japan\\\",\\\"gold\\\":27,\\\"silver\\\":14,\\\"bronze\\\":17,\\\"\r\n" + //
                                        "total\\\":58}\"\","
                + "\"method\":\"" + requestMethod + "\","
                + "\"data\":\"" + safeBody + "\""
                + "}";

        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

        he.sendResponseHeaders(200, responseBytes.length);
        OutputStream os = he.getResponseBody();
        os.write(responseBytes);
        os.close();
    }
}