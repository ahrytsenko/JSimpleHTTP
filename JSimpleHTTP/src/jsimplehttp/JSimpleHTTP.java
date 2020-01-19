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
    private String responceBody;

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

    public void setRequestURL(String requestURL, boolean isSaveHeaders) throws IOException {
        this.url = new URL(requestURL);
        if (!isSaveHeaders) {
            clearRequestHeaders();
        }
    }

    public void setRequestBody(String requestBody) throws IOException {
        this.requestBody = requestBody;
    }

    private void setRequestHeaders(HttpURLConnection httpURLConnection) {
        requestHeader.forEach((k, v) -> httpURLConnection.setRequestProperty(k, v));
    }

    private void setDefaultRequestHeader() {
        addRequestHeaders("User-Agent", this.userAgent);
    }

    public void clearRequestHeaders() {
        requestHeader.clear();
        setDefaultRequestHeader();
    }

    public void addRequestHeaders(String headerName, String headerValue) {
        headerName = headerName.trim();
        headerValue = headerValue.trim();
        if (!headerName.isEmpty() && !headerValue.isEmpty()) {
            requestHeader.put(headerName, headerValue);
        }
    }

    public void addRequestHeaders(HashMap<String, String> headers) {
        headers.forEach((k, v) -> addRequestHeaders(k, v));
    }

    public void replaceRequestHeaders(String headerName, String headerValue) {
        clearRequestHeaders();
        addRequestHeaders(headerName, headerValue);
    }

    public void replaceRequestHeaders(HashMap<String, String> headers) {
        clearRequestHeaders();
        addRequestHeaders(headers);
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

            responceBody = response.toString();
        } else {
            responceBody = "";
        }
        return responseCode;
    }

    public int sendPOST() throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        addRequestHeaders("Accept-Language", "uk-UA");
        addRequestHeaders("Content-Type", "application/json; charset=UTF-8");
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

            responceBody = response.toString();
        } else {
            responceBody = "";
        }
        return responseCode;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public String getResponseBody() {
        return responceBody;
    }
}
