
package jsimplehttp;
    
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class JSimpleHTTP implements AutoCloseable {

    public static final boolean SAVE_REQUEST_HEADERS = true;
    public static final boolean DONOT_SAVE_REQUEST_HEADERS = !SAVE_REQUEST_HEADERS;
    
    private URL url;
    private final String userAgent;
    private HashMap requestHeader;
    private String requestBody;
    private int responseCode;
    private String responceBody;

    public JSimpleHTTP() throws IOException {
        this("NetBeans JavaFX");
    }

    public JSimpleHTTP(String userAgent) throws IOException {
        this.userAgent = userAgent;
        requestHeader = new HashMap();
        requestHeader.put("User-Agent", this.userAgent);
    }

    @Override
    public void close() {
        url = null;
        requestHeader = null;
    }

    public void setRequestURL(String requestURL, boolean isSaveHeaders) throws IOException {
        this.url = new URL(requestURL);
        if (!isSaveHeaders) {
            requestHeader.clear();
            requestHeader.put("User-Agent", this.userAgent);
        }
    }

    public void setRequestBody(String requestBody) throws IOException {
        this.requestBody = requestBody;
    }
    
    private void setRequestHeaders(HttpURLConnection httpURLConnection) {
        // TODO: implement iteration through map keys and add headers
    }
    
    public void clearRequestHeaders() {
        // TODO: implement iteration through map keys and add headers
    }
    
    public void addRequestHeaders(String headerName, String headerValue) {
        // TODO: implement iteration through map keys and add headers
    }
    
    public void addRequestHeaders(HashMap headers) {
        // TODO: implement iteration through map keys and add headers
    }
    
    public void replaceRequestHeaders(String headerName, String headerValue) {
        // TODO: implement iteration through map keys and add headers
    }
    
    public void replaceRequestHeaders(HashMap headers) {
        // TODO: implement iteration through map keys and add headers
    }
    
    public int sendGET() throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        setRequestHeaders(con); 
        con.setRequestProperty("User-Agent", userAgent);
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
        }
        else {
            responceBody = "";
        }
        return responseCode;
    }

    public int sendPOST() throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", userAgent);
        con.setRequestProperty("Accept-Language", "uk-UA");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
      
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
        }
        else {
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
