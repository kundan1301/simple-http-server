import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Random;

public class HandleReq implements Runnable {
    private Socket socket;

    /*
    *  Consume first line of http request (Method, path and version)
    * */
    Map.Entry<String, String> parseMethodAndEndpoint(BufferedReader reader) throws Exception {
        String line = reader.readLine();
        System.out.println(line);
        String[] arr = line.split("\\s+");
        String[] path = arr[1].split("\\?"); //split into path and query string
        Map.Entry entry = new AbstractMap.SimpleImmutableEntry(arr[0], path[0]);
        return entry;
    }

    /*
    * Consume headers and return content length;
    * headers are separated from body via a newline
    * */

    int getContentLength(BufferedReader reader) throws Exception {
        String line = null;
        int len = 0;
        while (!"".equals(line)) {
            line = reader.readLine();
            System.out.println(line);
            if (line.toLowerCase().contains("content-length")) {
                String[] arr = line.split(":");
                len = Integer.parseInt(arr[1].trim());
            }
        }
        return len;
    }

    void sendResponse(PrintWriter writer, String data) {
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: text/plain");
        writer.println("Content-Length: " + data.length());
        writer.println("Keep-Alive: timeout=60, max=1000");
        writer.println();
        writer.println(data);
    }

    String parseBody(int len, BufferedReader reader) throws Exception {
        if (len <= 0) return "";
        int i = 0;
        StringBuilder sb = new StringBuilder();
        while (i < len) {
            int c = reader.read();
            if(c == -1) break;
            sb.append((char)c);
            i++;
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    @Override
    public void run() {
        if (socket == null) return;
        try {

            System.out.println("Connection accepted for client: " + socket.getRemoteSocketAddress());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            Map.Entry<String, String> entry = parseMethodAndEndpoint(reader);
            String method = entry.getKey();
            String url = entry.getValue();
            int contentLength = getContentLength(reader);
            Random r = new Random();
            if ("GET".equals(method)) {
                switch (url) {
                    case "/":
                        sendResponse(writer, "Hello from naive http-server");
                        break;
                    case "/id":
                        sendResponse(writer, "generated id is: " + r.nextInt());
                        break;
                    default:
                        sendResponse(writer, "Default response");
                }
            } else if ("POST".equals(method)) {
                String body = parseBody(contentLength, reader);
                sendResponse(writer, "Post method is called. body is: " + body);
            } else {
                sendResponse(writer, "Method not implemented");
            }
            reader.close();
            writer.close();
            socket.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public HandleReq(Socket socket) {
        this.socket = socket;
    }


}
