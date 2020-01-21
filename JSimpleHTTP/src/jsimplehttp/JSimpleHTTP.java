/**
 * Idea was taken from JournalDev
 * https://www.journaldev.com/7148/java-httpurlconnection-example-java-http-request-get-post
 */
package jsimplehttp;

import java.io.*;
import java.net.*;
import java.util.*;

public class JSimpleHTTP implements AutoCloseable {

    public static final boolean SAVE_REQUEST_HEADERS = true;
    public static final boolean DONOT_SAVE_REQUEST_HEADERS = !SAVE_REQUEST_HEADERS;

    private URL url;
    private final String userAgent;
    private HashMap<String, String> requestHeader;
    private String requestBody;
    private int responseCode;
    private String responseBody;

    public JSimpleHTTP() throws IOException {
        this("Java API");
    }

    public JSimpleHTTP(String userAgent) throws IOException {
        this.userAgent = userAgent;
        requestHeader = new HashMap();
        setDefaultRequestHeader();
    }

    @Override
    public void close() {
        url = null;
        requestHeader = null;
    }

    public JSimpleHTTP setRequestURL(String requestURL, boolean isSaveHeaders) throws IOException {
        this.url = new URL(requestURL);
        if (!isSaveHeaders) {
            clearRequestHeaders();
        }
        return this;
    }

    public JSimpleHTTP setRequestBody(String requestBody) throws IOException {
        this.requestBody = requestBody;
        return this;
    }

    public JSimpleHTTP clearRequestHeaders() {
        requestHeader.clear();
        setDefaultRequestHeader();
        return this;
    }

    public JSimpleHTTP addRequestHeader(String headerName, String headerValue) {
        headerName = headerName.trim();
        headerValue = headerValue.trim();
        if (!headerName.isEmpty() && !headerValue.isEmpty()) {
            requestHeader.put(headerName, headerValue);
        }
        return this;
    }

    public JSimpleHTTP addRequestHeaders(HashMap<String, String> headers) {
        headers.forEach((k, v) -> addRequestHeaders(k, v));
        return this;
    }

    public JSimpleHTTP replaceRequestHeader(String headerName, String headerValue) {
        clearRequestHeaders();
        addRequestHeaders(headerName, headerValue);
        return this;
    }

    public JSimpleHTTP replaceRequestHeaders(HashMap<String, String> headers) {
        clearRequestHeaders();
        addRequestHeaders(headers);
        return this;
    }

    public JSimpleHTTP delRequestHeader(String headerName) {
        if (requestHeader.containsKey(headerName)) {
            requestHeader.remove(headerName);
        }
        return this;
    }

    public JSimpleHTTP delRequestHeaders(HashMap<String, String> headers) {
        headers.forEach((k, v) -> {
            if (requestHeader.containsKey(k)) {
                requestHeader.remove(k);
            }
        });
        return this;
    }

    public int sendGET() throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        setRequestHeaders(con);
        responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            responseBody = response.toString();
        } else {
            responseBody = "";
        }
        return responseCode;
    }

    public int sendPOST() throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        setRequestHeaders(con);

        // For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(requestBody.getBytes());
        os.flush();
        os.close();
        // For POST only - END

        responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            responseBody = response.toString();
        } else {
            responseBody = "";
        }
        return responseCode;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
    
    private JSimpleHTTP setDefaultRequestHeader() {
        addRequestHeader("User-Agent", this.userAgent);
        return this;
    }

    private JSimpleHTTP setRequestHeaders(HttpURLConnection httpURLConnection) {
        requestHeader.forEach((k, v) -> httpURLConnection.setRequestProperty(k, v));
        return this;
    }
}
