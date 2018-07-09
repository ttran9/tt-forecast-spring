package tran.example.weatherforecast.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class provides a default implementation for creating a HTTP GET request to grab
 * information from an API such as Darksky and Google Geocoding.
 */
@Slf4j
public abstract class ApiService {

    /**
     * Information about the application type or software version of the system making the API
     * request.
     */
    private final static String USER_AGENT = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 " +
            "Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Mobile Safari/537.36";

    /**
     * Makes an HTTP GET request with the specified URL and returns content in JSON format.
     * @param url The URL to query information from.
     * @return The JSON content from the target API.
     * @throws IOException Throws an exception if there is an error making the request or reading
     * the content returned from the GET request.
     */
    protected String getData(String url) throws IOException {
        String line;
        StringBuilder result = new StringBuilder();

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        // add request header
        request.addHeader("User-Agent", USER_AGENT);
        HttpResponse response = client.execute(request);

        log.debug("API Call Status Code: " + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        while ((line = rd.readLine()) != null) {
            result.append(line).append("\n");
        }
        return result.toString();
    }
}
