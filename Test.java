import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

public class Test {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/test", new MyHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8000");
    }
}