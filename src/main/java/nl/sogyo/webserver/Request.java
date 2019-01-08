package nl.sogyo.webserver;

import java.util.List;
import java.util.Optional;

public interface Request {
    HttpMethod getHTTPMethod();
    String getResourcePath();
    List<String> getHeaderParameterNames();
    Optional<String> getHeaderParameterValue(String name);
    List<String> getParameterNames();
    Optional<String> getParameterValue(String name);
}