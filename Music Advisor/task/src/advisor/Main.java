package advisor;

import java.util.Scanner;
import java.util.TreeSet;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static String AUTH_URL = "https://accounts.spotify.com/authorize?client_id=afc222d2075c4e5083c715d5817967e4&redirect_uri=http://localhost:8080&response_type=code";

    private static boolean isAuthenticated = false;


    public static void main(String[] args) {

        boolean active = true;

        while (active) {
            String choice = sc.nextLine();


            switch (choice) {
                case "new":
                    newEndpoint();
                    break;
                case "featured":
                    featuredEndpoint();
                    break;
                case "auth":
                    authEndpoint();
                    break;
                case "exit":
                    active = false;
                    System.out.println("---GOODBYE!---");
                    break;
            }
        }
    }
    private static void newEndpoint(){
        if (!isAuthenticated){
            System.out.println("Please, provide access for application.");
            return;
        };
        System.out.println("---NEW RELEASES---");
        System.out.println("Mountains [Sia, Diplo, Labrinth]");
        System.out.println("Runaway [Lil Peep]");
        System.out.println("The Greatest Show [Panic! At The Disco]");
        System.out.println("All Out Life [Slipknot]");
    }

    private static void featuredEndpoint(){
        if (!isAuthenticated) {
            System.out.println("Please, provide access for application.");
            return;
        };
        System.out.println("---FEATURED---");
        System.out.println("Mellow Morning");
        System.out.println("Wake Up and Smell the Coffee");
        System.out.println("Monday Motivation");
        System.out.println("Songs to Sing in the Shower");
    }

    private static void authEndpoint(){
        System.out.println(AUTH_URL);
        System.out.println("---SUCCESS---");
        isAuthenticated = true;
    }
}
