package org.fhmdb.fhmdb_lijunamatata.exceptions;

import okhttp3.Response;
import java.io.IOException;


/**
 * Utility class for handling HTTP errors based on response status codes.
 */
public class HttpExceptionHandler {

    /**
     * Analyzes the HTTP status code and throws an appropriate IOException with a descriptive message.
     *
     * @param response   the HTTP response object (used in case of unexpected status codes)
     * @throws IOException an exception containing details about the error
     */
    public static void handle(Response response) throws IOException {
        switch (response.code()) {
            case 400:
                // Error: The request has bad syntax
                throw new IOException("400 Bad Request: The request cannot be fulfilled due to bad syntax.");
            case 401:
                // Error: Authentication is required and has failed or not been provided
                throw new IOException("401 Unauthorized: The request was legal, but the server is refusing to respond. Authentication is required.");
            case 403:
                // Error: Custom User-Agent header is missing
                throw new IOException("403 User-Agent Header fault: A custom User-Agent header was not set.");
            case 408:
                // Error: The server timed out waiting for the request
                throw new IOException("408 Request Timeout: The server timed out waiting for the request.");
            case 500:
                // Error: Internal server error
                throw new IOException("500 Internal Server Error: A generic error message when no more specific information is available.");
            case 502:
                // Error: Bad response from upstream server
                throw new IOException("502 Bad Gateway: The server was acting as a gateway or proxy and received an invalid response from the upstream server.");
            case 503:
                // Error: Service temporarily unavailable
                throw new IOException("503 Service Unavailable: The server is currently unavailable (overloaded or down).");
            case 511:
                // Error: Network authentication is required
                throw new IOException("511 Network Authentication Required: The client must authenticate to gain network access.");
            default:
                // Error: Unknown or unexpected status code
                throw new IOException("Unexpected response: " + response);
        }
    }
}

