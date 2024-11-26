package fr.lo02.antoineD.PocketImperium;

public class UserInterface {
    private static UserInterface instance;
    private Tile[] tiles;
    private Player[] players;

    private UserInterface(){
        this.tiles = new Tile[9];
        this.players = new Player[4];
    }

    public static UserInterface getInstance(){
        if(instance == null){
            instance = new UserInterface();
        }
        return instance;
    }

    public void displayGame(){
        System.out.println("Displaying game...");
    }
}
