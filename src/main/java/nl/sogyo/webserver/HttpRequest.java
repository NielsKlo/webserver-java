package nl.sogyo.webserver;

import java.util.HashMap;
import java.util.List;

public class HttpRequest {
    HttpMethod method;
    String resourcePath;
    List<String> headerParameters;
    HashMap<String, String> headerValues;


    HttpMethod getHTTPMethod(){
        return null;
    }

    String getResourcePath(){
        return null;
    }

    List<String> getHeaderParameters(){
        return null;
    }

    String getHeaderParameterValues(String name){
        return null;
    }

    List<String> getParameterNames(){
        return null;
    }

    String getParameterValue(String name){
        return null;
    }
}
