package nl.sogyo.webserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ConnectionHandler implements Runnable {
    private final Socket socket;

    public ConnectionHandler(Socket toHandle) {
        this.socket = toHandle;
    }

    public void run() {
        try {
            HttpRequest request = readHttpRequestFromSocket();

            sendHttpResponseToSocket(request);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeSocket();
        }
    }

    private HttpRequest readHttpRequestFromSocket() throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        List<String> list = makeListFromInput(reader);

        return new HttpRequest(list);
    }

    private List<String> makeListFromInput(BufferedReader reader) throws IOException{
        ArrayList<String> list = new ArrayList<>();
        String contentLengthString = "Content-Length: ";
        int contentLength = 0;

        String line = reader.readLine();
        do {
            if (line.startsWith(contentLengthString)) {
                contentLength = Integer.parseInt(line.substring(contentLengthString.length()));
            }

            list.add(line);
            line = reader.readLine();
        } while (!line.isEmpty());

        list.add("");

        char[] content = new char[contentLength];
        reader.read(content);
        list.add(new String(content));

        return list;
    }

    private void sendHttpResponseToSocket(HttpRequest request) throws IOException{
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        HttpResponse response = new HttpResponse(request);

        writer.write(response.getResponse());
        writer.flush();
    }

    private void closeSocket(){
        try {
            socket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String... args) {
        try {
            ServerSocket socket = new ServerSocket(9090);
            System.out.println("Application started. Listening at localhost:9090");
            ExecutorService threadPool = Executors.newCachedThreadPool();

            while(true) {
                Socket newConnection = socket.accept();
                threadPool.submit(new ConnectionHandler(newConnection));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
