package nl.sogyo.webserver;

import java.time.ZonedDateTime;
import java.util.Map;

public interface Response {
    HttpStatusCode getStatus();
    void setStatus(HttpStatusCode status);
    ZonedDateTime getDate();
    void setHeaderParameters(Map<String, String> parameters);
    String getContent();
    void setContent(String content);
}