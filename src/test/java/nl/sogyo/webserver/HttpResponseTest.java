package nl.sogyo.webserver;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HttpResponseTest {
    final HttpRequest request = HttpRequestTest.makeHttpRequest();
    final HttpResponse response = new HttpResponse(HttpStatusCode.OK, request);

    @Test
    public void response_Has_Expected_Status_Code(){
        assertEquals(HttpStatusCode.OK, response.getStatus());
    }

    @Test
    public void response_Has_Expected_Headers(){
    }
}
