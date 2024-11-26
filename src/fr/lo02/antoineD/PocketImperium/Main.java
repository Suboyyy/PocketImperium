package fr.lo02.antoineD.PocketImperium;

public class Main {

    private static Main instance;
    private Game game;


    public static void main(String[] args) {
        getInstance().getGame().startGame();
        UserInterface ui = UserInterface.getInstance();
    }

    private Main() {
        game = new Game(0);
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
