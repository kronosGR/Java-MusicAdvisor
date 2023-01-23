package advisor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Server {
    String SERVER = "https://accounts.spotify.com";
    String AUTHORIZE = "/authorize";
    String TOKEN = "/api/token";
    String REDIRECT_URL = "http://localhost:8080";
    String CLIENT_ID = "afc222d2075c4e5083c715d5817967e4";
    String CLIENT_SECRET = "4b61bc8fcd5346db8dde99020d133912";

    String ACCESS_CODE = "";
    String ACCESS_TOKEN = "";

    String url = SERVER + AUTHORIZE + "?client_id=" + CLIENT_ID
            + "&redirect_uri=" + REDIRECT_URL
            + "&response_type=code";

    boolean isAuthenticated = false;

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public void setAuthentication(String server) {
        SERVER = server;
        // get the access code
        getCode();

        // get the access token
        getToken();
    }

    private void getToken() {
        System.out.println("Making http request for access_token...");

        // prepare the request
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(SERVER + TOKEN))
                .POST(HttpRequest.BodyPublishers.ofString(
                        "grant_type=authorization_code"
                                + "&code=" + ACCESS_CODE
                                + "&client_id=" + CLIENT_ID
                                + "&client_secret=" + CLIENT_SECRET
                                + "&redirect_uri=" + REDIRECT_URL))
                .build();

        HttpClient httpClient = HttpClient.newBuilder().build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response != null){
                JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
                System.out.println(jsonObject);
                ACCESS_TOKEN = jsonObject.get("access_token").getAsString();
            }
            System.out.println("response:");
            System.out.println("{\"access_token\":\""+ACCESS_TOKEN+"\"}");
            System.out.println("---SUCCESS---");
            isAuthenticated=true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void getCode() {
        System.out.println("use this link to request the access code:");
        System.out.println(url);

        try {
            HttpServer server = HttpServer.create();
            server.bind(new InetSocketAddress(8080), 0);
            server.createContext("/", exchange -> {
                String result = exchange.getRequestURI().getQuery();
                String response;
                if (result != null && result.contains("code")) {
                    ACCESS_CODE = result.substring(5);
                    System.out.println(result);
                    System.out.println("code received");
                    response = "Got the code. Return back to your program.";
                } else {
                    response = "Authorization code not found. Try again.";
                }

                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            });
            server.start();
            System.out.println("waiting for code...");
            while (ACCESS_CODE.length() == 0) {
                Thread.sleep(10);
            }
            server.stop(10);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
