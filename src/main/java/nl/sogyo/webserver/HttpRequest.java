package nl.sogyo.webserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HttpRequest implements Request{
    private HttpStatusCode statusCode;
    private HttpMethod method;
    private String resourcePath;
    private String version;
    private final HashMap<String, String> headers = new HashMap<>();
    private final HashMap<String, String> resourceParameters = new HashMap<>();

    HttpRequest(List<String> list){
        statusCode = HttpStatusCode.OK;

        try {
            parseStartLine(list.remove(0));
            parseHeaders(list);

            if (isPostRequestWithURLEncoded()) {
                parseURLParameters(list);
            }
        } catch(Exception e){
            statusCode = HttpStatusCode.BadRequest;
        }
    }

    private void parseStartLine(String line){
        String[] parts = line.split(" ");
        method = HttpMethod.valueOf(parts[0]);
        parseResourcePath(parts[1]);
        version = parts[2];
    }

    private void parseResourcePath(String resourceString){
        String[] parts = resourceString.split("\\?");
        resourcePath = parts[0];

        if(parts.length > 1){
            String[] parameters = parts[1].split("&");
            addParameters(parameters);
        }
    }

    private void addParameters(String[] parameters){
        for (String parameter : parameters) {
            String[] pair = parameter.split("=");
            resourceParameters.put(pair[0], pair[1]);
        }
    }

    private void parseHeaders(List<String> list){
        int length =  list.size();

        for(int i = 0; i < length; i++){
            String line = list.get(0);

            if(line.isEmpty()) {
                list.remove(line);
                break;
            }

            parseHeaderLine(line);
            list.remove(line);
        }
    }

    private void parseHeaderLine(String line){
        String[] header = line.split(": ");
        headers.put(header[0], header[1]);
    }

    private boolean isPostRequestWithURLEncoded(){
        return method == HttpMethod.POST &&
                headers.containsKey("Content-Type") &&
                headers.get("Content-Type").equals("application/x-www-form-urlencoded");
    }

    private void parseURLParameters(List<String> list){
        if(list.size() != 1){
            statusCode = HttpStatusCode.BadRequest;
        }
        String[] parameters = list.get(0).split("&");

        addParameters(parameters);
    }

    public HttpStatusCode getStatusCode(){
        return statusCode;
    }

    public HttpMethod getHTTPMethod(){
        return method;
    }

    public String getResourcePath(){
        return resourcePath;
    }

    public String getVersion(){ return version; }

    public List<String> getHeaderParameterNames(){
        return new ArrayList<>(headers.keySet());
    }

    public String getHeaderParameterValue(String name){
        return headers.get(name);
    }

    public List<String> getParameterNames(){
        return new ArrayList<>(resourceParameters.keySet());
    }

    public String getParameterValue(String name){
        return resourceParameters.get(name);
    }
}
