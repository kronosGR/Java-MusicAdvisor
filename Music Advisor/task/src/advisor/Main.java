package advisor;

import java.util.Scanner;

public class Main {
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        boolean active = true;

        while (active) {
            String choice = sc.nextLine();


            switch (choice) {
                case "new":
                    System.out.println("---NEW RELEASES---");
                    System.out.println("Mountains [Sia, Diplo, Labrinth]");
                    System.out.println("Runaway [Lil Peep]");
                    System.out.println("The Greatest Show [Panic! At The Disco]");
                    System.out.println("All Out Life [Slipknot]");
                    break;
                case "featured":
                    System.out.println("---FEATURED---");
                    System.out.println("Mellow Morning");
                    System.out.println("Wake Up and Smell the Coffee");
                    System.out.println("Monday Motivation");
                    System.out.println("Songs to Sing in the Shower");
                    break;
                case "categories":
                    System.out.println("---CATEGORIES---");
                    System.out.println("Top Lists");
                    System.out.println("Pop");
                    System.out.println("Mood");
                    System.out.println("Latin");
                    break;
                case "playlists Mood":
                    System.out.println("---MOOD PLAYLISTS---");
                    System.out.println("Walk Like A Badass");
                    System.out.println("Rage Beats");
                    System.out.println("Arab Mood Booster");
                    System.out.println("Sunday Stroll");
                    break;
                case "exit":
                    active = false;
                    System.out.println("---GOODBYE!---");
                    break;
            }
        }
    }
}
