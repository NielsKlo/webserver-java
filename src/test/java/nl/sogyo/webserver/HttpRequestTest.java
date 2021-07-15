package nl.sogyo.webserver;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpRequestTest {
    public static final HttpRequest GETRequest = makeGetRequest();
    public static final HttpRequest GETUrlParameters = makeGetRequestWithURLParameters();
    public static final HttpRequest POSTRequest = makePostRequest();
    public static final HttpRequest POSTContentParameters = makePostRequestContentParameters();
    public static final HttpRequest POSTContentAndUrlParameters = makePostRequestWithContentAndUrlParameters();

    private static HttpRequest makeGetRequest(){
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
        return makeRequest(array);
    }

    private static HttpRequest makeGetRequestWithURLParameters(){
        String[] array = {
                "GET /books?fruit=banana&veggie=spinach HTTP/1.1",
                "Host: localhost:9090",
                "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; nl; rv:1.9.0.11) Gecko/20100101 Firefox/40",
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                "Accept-Language: nl,en-us;q=0.7,en;q=0.3",
                "Accept-Encoding: gzip,deflate",
                "Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7",
                "Keep-Alive: 300",
                "Connection: keep-alive"
        };
        return makeRequest(array);
    }

    private static HttpRequest makePostRequest(){
        String[] array = {
                "POST /books HTTP/1.1",
                "Host: localhost:9090",
                "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; nl; rv:1.9.0.11) Gecko/20100101 Firefox/40",
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                "Accept-Language: nl,en-us;q=0.7,en;q=0.3",
                "Accept-Encoding: gzip,deflate",
                "Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7",
                "Keep-Alive: 300",
                "Connection: keep-alive"
        };
        return makeRequest(array);
    }

    private static HttpRequest makePostRequestContentParameters(){
        String[] array = {
                "POST /books HTTP/1.1",
                "Host: localhost:9090",
                "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; nl; rv:1.9.0.11) Gecko/20100101 Firefox/40",
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                "Accept-Language: nl,en-us;q=0.7,en;q=0.3",
                "Accept-Encoding: gzip,deflate",
                "Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7",
                "Keep-Alive: 300",
                "Connection: keep-alive",
                "Content-Type: application/x-www-form-urlencoded",
                "Content-Length: 20",
                "",
                "color=red&shape=ball"
        };
        return makeRequest(array);
    }

    private static HttpRequest makePostRequestWithContentAndUrlParameters(){
        String[] array = {
                "POST /books?fruit=banana&veggie=spinach HTTP/1.1",
                "Host: localhost:9090",
                "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; nl; rv:1.9.0.11) Gecko/20100101 Firefox/40",
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                "Accept-Language: nl,en-us;q=0.7,en;q=0.3",
                "Accept-Encoding: gzip,deflate",
                "Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7",
                "Keep-Alive: 300",
                "Connection: keep-alive",
                "Content-Type: application/x-www-form-urlencoded",
                "Content-Length: 20",
                "",
                "color=red&shape=ball"
        };
        return makeRequest(array);
    }

    private static HttpRequest makeRequest(String[] array){
        ArrayList<String> list = new ArrayList<>(Arrays.asList(array));
        return new HttpRequest(list);
    }

    @Test
    public void request_Has_Expected_Method(){
        assertEquals(HttpMethod.GET, GETRequest.getHTTPMethod());
        assertEquals(HttpMethod.GET, GETUrlParameters.getHTTPMethod());
        assertEquals(HttpMethod.POST, POSTRequest.getHTTPMethod());
        assertEquals(HttpMethod.POST, POSTContentParameters.getHTTPMethod());
        assertEquals(HttpMethod.POST, POSTContentAndUrlParameters.getHTTPMethod());
    }

    @Test
    public void request_Has_Expected_ResourcePath(){
        assertEquals("/books", GETRequest.getResourcePath());
        assertEquals("/books", GETUrlParameters.getResourcePath());
        assertEquals("/books", POSTRequest.getResourcePath());
        assertEquals("/books", POSTContentParameters.getResourcePath());
        assertEquals("/books", POSTContentAndUrlParameters.getResourcePath());
    }

    @Test
    public void request_Has_Expected_Version(){
        assertEquals("HTTP/1.1", GETRequest.getVersion());
        assertEquals("HTTP/1.1", GETUrlParameters.getVersion());
        assertEquals("HTTP/1.1", POSTRequest.getVersion());
        assertEquals("HTTP/1.1", POSTContentParameters.getVersion());
        assertEquals("HTTP/1.1", POSTContentAndUrlParameters.getVersion());
    }

    @Test
    public void request_Has_StatusCode_OK(){
        assertEquals(HttpStatusCode.OK, GETRequest.getStatusCode());
        assertEquals(HttpStatusCode.OK, GETUrlParameters.getStatusCode());
        assertEquals(HttpStatusCode.OK, POSTRequest.getStatusCode());
        assertEquals(HttpStatusCode.OK, POSTContentParameters.getStatusCode());
        assertEquals(HttpStatusCode.OK, POSTContentAndUrlParameters.getStatusCode());
    }

    @Test
    public void request_Has_Expected_Amount_Of_Headers(){
        assertEquals(8, GETRequest.getHeaderParameterNames().size());
        assertEquals(8, GETUrlParameters.getHeaderParameterNames().size());
        assertEquals(8, POSTRequest.getHeaderParameterNames().size());
        assertEquals(10, POSTContentParameters.getHeaderParameterNames().size());
        assertEquals(10, POSTContentAndUrlParameters.getHeaderParameterNames().size());
    }

    @Test
    public void request_Provides_Expected_Value_For_Header_Parameter(){
        assertEquals("localhost:9090", GETRequest.getHeaderParameterValue("Host"));
        assertEquals("localhost:9090", GETUrlParameters.getHeaderParameterValue("Host"));
        assertEquals("localhost:9090", POSTRequest.getHeaderParameterValue("Host"));
        assertEquals("localhost:9090", POSTContentParameters.getHeaderParameterValue("Host"));
        assertEquals("localhost:9090", POSTContentAndUrlParameters.getHeaderParameterValue("Host"));
    }

    @Test
    public void request_Has_Expected_Amount_Of_Parameters(){
        assertEquals(0, GETRequest.getParameterNames().size());
        assertEquals(2, GETUrlParameters.getParameterNames().size());
        assertEquals(0, POSTRequest.getParameterNames().size());
        assertEquals(2, POSTContentParameters.getParameterNames().size());
        assertEquals(4, POSTContentAndUrlParameters.getParameterNames().size());
    }

    @Test
    public void request_Provides_Expected_Value_For_Parameter(){
        assertEquals("banana", GETUrlParameters.getParameterValue("fruit"));
        assertEquals("ball", POSTContentParameters.getParameterValue("shape"));
        assertEquals("banana", POSTContentAndUrlParameters.getParameterValue("fruit"));
    }

}
