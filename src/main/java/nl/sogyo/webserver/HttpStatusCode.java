package nl.sogyo.webserver;

/// Predefined status codes that inform the client about the handing of the request.
public enum HttpStatusCode {
    /// Processing of the request completed succesfully.
    OK(200, "OK"), 
    /// Requested resource path was not found.
    NotFound(404, "Not Found"), 
    /// An unexpected error occured while handling the request.
    ServerError(500, "Server Error");

    private int code;
    private String description;

    private HttpStatusCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /// The numeric status code that is used by the client
    /// to determine error handling.
    public int getCode() {
        return code;
    }

    /// A short textual representation of the status code.
    /// Generally not used by applications, but gives the number 
    /// a human-readable description.
    public String getDescription() {
        return description;
    }
}
