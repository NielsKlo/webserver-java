package nl.sogyo.webserver;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class HttpRequestTest {
    final HttpRequest request = makeHttpRequest();

    public static HttpRequest makeHttpRequest(){
        String[] array = {
                "GET /books HTTP/1.1",
                "Host: localhost:9090",
                "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; nl; rv:1.9.0.11) Gecko/20100101 Firefox/40",
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                "Accept-Language: nl,en-us;q=0.7,en;q=0.3",
                "Accept-Encoding: gzip,deflate",
                "Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7",
                "Keep-Alive: 300",
                "Connection: keep-alive"
        };
        ArrayList<String> list = new ArrayList<>(Arrays.asList(array));
        return new HttpRequest(list);
    }

    @Test
    public void request_Has_Expected_Starting_Line(){
        assertEquals("GET", request.getHTTPMethod().name());
        assertEquals("/books", request.getResourcePath());
        assertEquals("HTTP/1.1", request.getVersion());
    }

    @Test
    public void request_Has_Expected_Amount_Of_Headers(){
        assertEquals(8, request.getHeaderParameterNames().size());
    }

    @Test
    public void request_Provides_Expected_Value_For_Header_Parameter(){
        String headerOne = request.getHeaderParameterNames().get(0);
        String headerSeven = request.getHeaderParameterNames().get(6);
        assertEquals("localhost:9090", request.getHeaderParameterValue(headerOne));
        assertEquals("300", request.getHeaderParameterValue(headerSeven));
    }
}
