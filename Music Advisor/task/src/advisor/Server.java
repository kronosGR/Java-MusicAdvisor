package advisor;

import advisor.Models.Song;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Server {
    String SERVER = "https://accounts.spotify.com";
    String RESOURCE = "https://api.spotify.com";
    String AUTHORIZE = "/authorize";
    String NEW = "/v1/browse/new-releases";
    String FEATURED = "/v1/browse/featured-playlists";
    String CATEGORIES = "/v1/browse/categories";
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

    public Server(String server, String resource) {
        if (!Objects.isNull(server)) {
//            System.out.println(server);
            this.SERVER = server;
        }
        if (!Objects.isNull(resource)) {
//            System.out.println(resource);
            this.RESOURCE = resource;
        }
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public void setAuthentication() {
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
            if (response != null) {
                JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
                System.out.println(jsonObject.toString());
                ACCESS_TOKEN = jsonObject.get("access_token").getAsString();
            }
//            System.out.println("response:");
//            System.out.println("{\"access_token\":\"" + ACCESS_TOKEN + "\"}");
            System.out.println("Success!");
            isAuthenticated = true;
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
                    System.out.println(ACCESS_CODE);
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

    public void getNews() {
        List<Song> songs = new ArrayList<>();
        String response = sendRequestAndGetResponse(RESOURCE + NEW);

        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        JsonObject albums = jsonObject.getAsJsonObject("albums");

        for (JsonElement object : albums.getAsJsonArray("items")) {
            Song tmpSong = new Song();

            // add the name
            StringBuilder artists = new StringBuilder("[");

            for (JsonElement name : object.getAsJsonObject().getAsJsonArray("artists")) {
                if (!artists.toString().endsWith("[")) {
                    artists.append(", ");
                }
                artists.append(name.getAsJsonObject().get("name"));
            }
            tmpSong.setName(artists.append("]").toString().replaceAll("\"", ""));

            // add the album
            tmpSong.setAlbum(object.getAsJsonObject().get("name").toString().replaceAll("\"", ""));

            // add the link
            tmpSong.setLink(object.getAsJsonObject().get("external_urls")
                    .getAsJsonObject().get("spotify")
                    .toString().replaceAll("\"", ""));

            songs.add(tmpSong);
        }


        System.out.println(createResult(songs, true));
    }

    public void getFeatured() {
        List<Song> songs = new ArrayList<>();
        String response = sendRequestAndGetResponse(RESOURCE + FEATURED);

        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        JsonObject categories = jsonObject.getAsJsonObject("playlists");

        for (JsonElement item : categories.getAsJsonArray("items")) {
            Song element = new Song();
            element.setAlbum(item.getAsJsonObject().get("name").toString().replaceAll("\"", ""));

            element.setLink(item.getAsJsonObject().get("external_urls")
                    .getAsJsonObject().get("spotify")
                    .toString().replaceAll("\"", ""));

            songs.add(element);
        }

        System.out.println(createResult(songs, false));
    }


    public void getCategories() {
        List<Song> songs = new ArrayList<>();
        String response = sendRequestAndGetResponse(RESOURCE + CATEGORIES);

        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        JsonObject categories = jsonObject.getAsJsonObject("categories");
        for (JsonElement item : categories.getAsJsonArray("items")) {
            Song element = new Song();
            element.setCategories(item.getAsJsonObject().get("name").toString().replaceAll("\"", ""));
            songs.add(element);
        }

        StringBuilder result = new StringBuilder();
        for (Song song : songs) {
            result.append(song.getCategories()).append("\n").append("\n");
        }

        System.out.println(result);
    }

    private String createResult(List<Song> songs, boolean isNew) {
        //build the string
        StringBuilder result = new StringBuilder();
        for (Song song : songs) {
            result.append(song.getAlbum()).append("\n");

            if (isNew) result.append(song.getName()).append("\n");

            result.append(song.getLink()).append("\n")
                    .append("\n");
        }

        return result.toString();
    }

    private String sendRequestAndGetResponse(String url) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpClient httpClient = HttpClient.newBuilder().build();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return httpResponse.body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void getPlaylist(String playlist) {
        List<Song> songs = new ArrayList<>();
        String response = sendRequestAndGetResponse(RESOURCE + CATEGORIES);
        //System.out.println(response);
        String id_categories = "Unknown category name.";

        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        JsonObject categories = jsonObject.getAsJsonObject("categories");
        for (JsonElement item : categories.getAsJsonArray("items")) {
            if (item.getAsJsonObject().get("name").toString().replaceAll("\"", "").toLowerCase().equals(playlist.toLowerCase())) {
                id_categories = item.getAsJsonObject().get("id").toString().replaceAll("\"", "");
                break;
            }
        }
        if (id_categories.equals("Unknown category name.")) {
            System.out.println(id_categories);
            return;
        }
        String url = RESOURCE + CATEGORIES + "/"+id_categories + "/playlists";
        System.out.println(url);
        response = sendRequestAndGetResponse(url);
        // System.out.println(response);
        if (response.contains("Test unpredictable error message")) {
            System.out.println("Test unpredictable error message");
            return;
        }
        jsonObject = JsonParser.parseString(response).getAsJsonObject();
        categories = jsonObject.getAsJsonObject("playlists");

        for (JsonElement item : categories.getAsJsonArray("items")) {
            Song element = new Song();
            element.setAlbum(item.getAsJsonObject().get("name").toString().replaceAll("\"", ""));

            element.setLink(item.getAsJsonObject().get("external_urls")
                    .getAsJsonObject().get("spotify")
                    .toString().replaceAll("\"", ""));

            songs.add(element);
        }

        System.out.println(createResult(songs, false));
    }
}
