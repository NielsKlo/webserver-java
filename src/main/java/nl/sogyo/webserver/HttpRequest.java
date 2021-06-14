package nl.sogyo.webserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HttpRequest {
    private HttpMethod method;
    private String resourcePath;
    private String version;
    private final List<String> headerParameters = new ArrayList<>();
    private final HashMap<String, String> headerValues = new HashMap<>();

    HttpRequest(ArrayList<String> list){
        parseStartLine(list.remove(0));

        for(String line : list){
            if(line.isEmpty()) break;
            parseHeaderLine(line);
        }
    }

    private void parseStartLine(String line){
        String[] parts = line.split(" ");
        method = HttpMethod.valueOf(parts[0]);
        resourcePath = parts[1];
        version = parts[2];
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

    String getVersion(){ return version; }

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
