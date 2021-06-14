package nl.sogyo.webserver;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HttpRequest {
    private HttpMethod method;
    private String resourcePath;
    private final List<String> headerParameters = new ArrayList<>();
    private final HashMap<String, String> headerValues = new HashMap<>();

    HttpRequest(Socket socket){
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            parseStartLine(reader.readLine());

            String line = null;
            do {
                line = reader.readLine();
                if(!line.isEmpty()){
                    parseHeaderLine(line);
                }
            } while(!line.isEmpty());

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private void parseStartLine(String line){
        String[] parts = line.split(" ");
        method = HttpMethod.valueOf(parts[0]);
        resourcePath = parts[1];
    }

    private void parseHeaderLine(String line){
        String[] header = line.split(": ");
        headerParameters.add(header[0]);
        headerValues.put(header[0], header[1]);
    }

    HttpMethod getHTTPMethod(){
        return method;
    }

    String getResourcePath(){
        return resourcePath;
    }

    List<String> getHeaderParameters(){
        return headerParameters;
    }

    String getHeaderParameterValues(String name){
        return headerValues.get(name);
    }

    List<String> getParameterNames(){
        return null;
    }

    String getParameterValue(String name){
        return null;
    }
}
