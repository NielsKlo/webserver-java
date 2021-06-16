package nl.sogyo.webserver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponse implements Response{
    private final String version;
    private final String method;
    private final String resource;
    private final HttpStatusCode statusCode;
    private final HashMap<String, String> customHeaders = new HashMap<>();
    private final ZonedDateTime dateTime;
    private String content;

    public HttpResponse(HttpRequest request){
        this.statusCode = request.getStatusCode();
        this.method = request.getHTTPMethod().name();
        this.resource = request.getResourcePath();
        this.version = request.getVersion();
        this.dateTime = ZonedDateTime.now();
        makeContent(request);
        makeCustomHeaders();
    }

    private void makeContent(HttpRequest request){
        String contentHeader = makeContentHeader();
        String headerParameters = makeHeaderParametersString(request);
        String parameters = makeParametersString(request);
        content = "<html>\r\n<body>\r\n" + contentHeader + headerParameters + parameters + "</body>\r\n</html>\r\n";
    }

    private String makeContentHeader(){
        return "You did an " + version + " " + method + " and you requested the following resource: " + resource + "<br><br>\r\n";
    }

    private String makeHeaderParametersString(HttpRequest request){
        String parameters = "<br>\r\n";
        List<String> headerParameterNames = request.getHeaderParameterNames();

        for(String name : headerParameterNames){
            String value = request.getHeaderParameterValue(name);
            parameters += name + ": " + value + "<br>\r\n";
        }
        return parameters;
    }

    private String makeParametersString(HttpRequest request){
        String parameters = "<br>\r\n";
        List<String> parameterList = request.getParameterNames();

        for(String name : parameterList){
             String value = request.getParameterValue(name);
             parameters += name + ": " + value + "<br>\r\n";
        }
        return parameters;
    }

    private void makeCustomHeaders(){
        customHeaders.put("Date", dateTime.format(DateTimeFormatter.RFC_1123_DATE_TIME));
        customHeaders.put("Server", "localhost");
        customHeaders.put("Connection", "close");
        customHeaders.put("Content-Type", "text/html; charset=UTF-8");
        customHeaders.put("Content-Length", "" + content.length());
    }

    public HttpStatusCode getStatus(){
        return statusCode;
    }

    public Map<String, String> getCustomHeaders(){
        return customHeaders;
    }

    public ZonedDateTime getDate(){
        return dateTime;
    }

    public String getContent(){
        return content;
    }

    public String getResponse(){
        String response = "";

        response += writeFirstLine();
        response += writeHeaders();
        response += content;

        return response;
    }

    private String writeFirstLine(){
        return version + " " + statusCode.getCode() + " " + statusCode.getDescription() + "\r\n";
    }

    private String writeHeaders(){
        String allHeaders = "";

        for(Map.Entry<String, String> pair : customHeaders.entrySet()){
            String parameter = pair.getKey();
            String value = pair.getValue();

            String header = parameter + ": " + value + "\r\n";
            allHeaders += header;
        }

        allHeaders += "\r\n";

        return allHeaders;
    }
}
