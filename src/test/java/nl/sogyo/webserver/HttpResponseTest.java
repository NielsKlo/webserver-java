package nl.sogyo.webserver;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class HttpResponseTest {
    final HttpResponse GETResponse = new HttpResponse(HttpRequestTest.GETRequest);
    final HttpResponse GETUrlParametersResponse = new HttpResponse(HttpRequestTest.GETUrlParameters);
    final HttpResponse POSTResponse = new HttpResponse(HttpRequestTest.POSTRequest);
    final HttpResponse POSTContentParametersResponse = new HttpResponse(HttpRequestTest.POSTContentParameters);
    final HttpResponse POSTContentAndUrlParametersResponse = new HttpResponse(HttpRequestTest.POSTContentAndUrlParameters);

    @Test
    public void response_Has_Expected_Status_Code(){
        assertEquals(HttpStatusCode.OK, GETResponse.getStatus());
        assertEquals(HttpStatusCode.OK, GETUrlParametersResponse.getStatus());
        assertEquals(HttpStatusCode.OK, POSTResponse.getStatus());
        assertEquals(HttpStatusCode.OK, POSTContentParametersResponse.getStatus());
        assertEquals(HttpStatusCode.OK, POSTContentAndUrlParametersResponse.getStatus());
    }

    @Test
    public void response_Has_Expected_Amount_Of_Headers(){
        assertEquals(5, GETResponse.getCustomHeaders().size());
        assertEquals(5, GETUrlParametersResponse.getCustomHeaders().size());
        assertEquals(5, POSTResponse.getCustomHeaders().size());
        assertEquals(5, POSTContentParametersResponse.getCustomHeaders().size());
        assertEquals(5, POSTContentAndUrlParametersResponse.getCustomHeaders().size());
    }

    @Test
    public void response_Has_Expected_Value_For_A_Header(){
        assertEquals("close", GETResponse.getCustomHeaders().get("Connection"));
        assertEquals("close", GETUrlParametersResponse.getCustomHeaders().get("Connection"));
        assertEquals("close", POSTResponse.getCustomHeaders().get("Connection"));
        assertEquals("close", POSTContentParametersResponse.getCustomHeaders().get("Connection"));
        assertEquals("close", POSTContentAndUrlParametersResponse.getCustomHeaders().get("Connection"));
    }

    @Test
    public void response_Has_Not_Null_ZonedDateTime(){
        assertNotNull(GETResponse.getDate());
        assertNotNull(GETUrlParametersResponse.getDate());
        assertNotNull(POSTResponse.getDate());
        assertNotNull(POSTContentParametersResponse.getDate());
        assertNotNull(POSTContentAndUrlParametersResponse.getDate());
    }

    @Test
    public void response_Date_Header_Value_Is_In_RFC1123_Format(){
        assertEquals(GETResponse.getCustomHeaders().get("Date"), GETResponse.getDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
        assertEquals(GETUrlParametersResponse.getCustomHeaders().get("Date"), GETUrlParametersResponse.getDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
        assertEquals(POSTResponse.getCustomHeaders().get("Date"), POSTResponse.getDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
        assertEquals(POSTContentParametersResponse.getCustomHeaders().get("Date"), POSTContentParametersResponse.getDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
        assertEquals(POSTContentAndUrlParametersResponse.getCustomHeaders().get("Date"), POSTContentAndUrlParametersResponse.getDate().format(DateTimeFormatter.RFC_1123_DATE_TIME));
    }

    @Test
    public void response_Content_Is_Not_Null(){
        assertNotNull(GETResponse.getContent());
        assertNotNull(GETUrlParametersResponse.getContent());
        assertNotNull(POSTResponse.getContent());
        assertNotNull(POSTContentParametersResponse.getContent());
        assertNotNull(POSTContentAndUrlParametersResponse.getContent());
    }

    @Test
    public void response_Content_Is_Not_Empty(){
        assertNotEquals("", GETResponse.getContent());
        assertNotEquals("", GETUrlParametersResponse.getContent());
        assertNotEquals("", POSTResponse.getContent());
        assertNotEquals("", POSTContentParametersResponse.getContent());
        assertNotEquals("", POSTContentAndUrlParametersResponse.getContent());
    }
}
