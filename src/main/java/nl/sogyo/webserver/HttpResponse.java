package nl.sogyo.webserver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse implements Response{
    String version;
    String method;
    String resource;
    HttpStatusCode statusCode;
    HashMap<String, String> customHeaders = new HashMap<>();
    ZonedDateTime dateTime;
    String content;

    public HttpResponse(HttpStatusCode status, HttpRequest request){
        this.version = request.getVersion();
        this.method = request.getHTTPMethod().name();
        this.resource = request.getResourcePath();
        this.statusCode = status;
        this.dateTime = ZonedDateTime.now();
        makeContent();
        makeCustomHeaders();
    }

    private void makeContent(){
        content = "<html>\r\n<body>\r\nYou did an " + version + " " + method + "<br/>\r\n" +
                "Requested resource: " + resource + "\r\n</body>\r\n</html>\r\n";
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

    public void getResponse(BufferedWriter writer){
        try {
            writer.write(writeResponse());
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    private String writeResponse(){
        String response = "";

        response += writeFirstLine();
        response += writeHeaders();
        response += writeContent();

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

    private String writeContent(){
        return content;
    }
}
