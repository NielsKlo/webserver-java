package nl.sogyo.webserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HttpRequest implements Request{
    private HttpMethod method;
    private String resourcePath;
    private String version;
    private final List<String> headerParameters = new ArrayList<>();
    private final HashMap<String, String> headerValues = new HashMap<>();
    private final HashMap<String, String> resourceParameters = new HashMap<>();

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
        parseResourcePath(parts[1]);
        version = parts[2];
    }

    private void parseResourcePath(String resourceString){
        String[] parts = resourceString.split("&");
        resourcePath = parts[0];

        for(int i = 1; i < parts.length; i++){
            String[] pair = parts[i].split("=");
            resourceParameters.put(pair[0], pair[1]);
        }
    }

    private void parseHeaderLine(String line){
        String[] header = line.split(": ");
        headerParameters.add(header[0]);
        headerValues.put(header[0], header[1]);
    }

    public HttpMethod getHTTPMethod(){
        return method;
    }

    public String getResourcePath(){
        return resourcePath;
    }

    public String getVersion(){ return version; }

    public List<String> getHeaderParameterNames(){
        return headerParameters;
    }

    public String getHeaderParameterValue(String name){
        return headerValues.get(name);
    }

    public List<String> getParameterNames(){
        return null;
    }

    public String getParameterValue(String name){
        return null;
    }
}
