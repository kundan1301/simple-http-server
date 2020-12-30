import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String args[]){
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true){
                Socket conn = serverSocket.accept();
                System.out.println("Accepted Connection: "+ conn.getRemoteSocketAddress());
                Runnable handleReq = new HandleReq(conn);
                executorService.execute(handleReq);
            }
        }catch (Exception e){
            System.out.println(e);
        }

    }


}