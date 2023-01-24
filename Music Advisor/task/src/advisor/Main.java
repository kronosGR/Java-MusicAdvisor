package advisor;

import com.beust.jcommander.*;

import java.util.Scanner;
import java.util.TreeSet;


/*
        implementation 'com.google.code.gson:gson:+'
        implementation group: 'com.beust', name: 'jcommander', version: '1.69'
 */

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static String AUTH_URL = "https://accounts.spotify.com/authorize?client_id=afc222d2075c4e5083c715d5817967e4&redirect_uri=http://localhost:8080&response_type=code";

    private static boolean isAuthenticated = false;

    static Args arguments = new Args();
    static Server server;

    public static void main(String[] args) {
        JCommander.newBuilder().addObject(arguments)
                .build()
                .parse(args);

        server = new Server(arguments.getAccess(), arguments.getResource());
        boolean active = true;

        while (active) {
            String choiceTmp = sc.nextLine();
            String[] choices = choiceTmp.split(" ",2);
            String choice = choices[0];
            String playlist = "";

            if (choices.length > 1) {
                playlist = choices[1];
            }

            switch (choice) {
                case "new":
                    newEndpoint();
                    break;
                case "featured":
                    featuredEndpoint();
                    break;
                case "auth":
                    server.setAuthentication();
                    isAuthenticated = server.isAuthenticated();
                    break;
                case "categories":
                    categoriesEndpoint();
                    break;
                case "playlists":
                    playlistEndpoint(playlist);
                    break;
                case "exit":
                    active = false;
                    System.out.println("---GOODBYE!---");
                    break;
            }
        }
    }

    private static void playlistEndpoint(String playlist) {
        if (!isAuthenticated) {
            System.out.println("Please, provide access for application.");
            return;
        }
        server.getPlaylist(playlist);
    }

    private static void categoriesEndpoint() {
        if (!isAuthenticated) {
            System.out.println("Please, provide access for application.");
            return;
        }
        server.getCategories();
    }

    private static void newEndpoint() {
        if (!isAuthenticated) {
            System.out.println("Please, provide access for application.");
            return;
        }
        ;
        server.getNews();
    }

    private static void featuredEndpoint() {
        if (!isAuthenticated) {
            System.out.println("Please, provide access for application.");
            return;
        }
        ;
        server.getFeatured();
    }


}
