package fr.lo02.antoineD.PocketImperium;

import java.util.Scanner;

public class Main {

    private static Main instance;
    private final Game game;


    public static void main(String[] args) {
        getInstance().getGame().startGame();
    }

    private Main() {
        System.out.println("Combien de joueurs êtes vous ?");
        Scanner sc = new Scanner(System.in);
        int nbPlayers = sc.nextInt();
        if (nbPlayers < 1 || nbPlayers > 3) {
            System.out.println("Le nombre de joueurs doit être compris entre 2 et 4");
            System.exit(0);
        }
        game = new Game(0, nbPlayers);
    }

    public static Main getInstance() {
        if (instance == null) {
            instance = new Main();
        }
        return instance;
    }

    public Game getGame() {
        return game;
    }

    public void saveGame(){

    }
}
