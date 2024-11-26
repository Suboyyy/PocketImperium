package fr.lo02.antoineD.PocketImperium;

import java.io.IOException;
import java.util.List;

public class Game {
    private List<Sector> sectors;
    private int firstPlayerIndex;
    private Player[] players;
    private Tile[] tiles;

    public Game(int firstPlayerIndex){

    }

    public void startGame(){

    }

    public void chooseAction(){

    }

    public void executeAction(){

    }

    public void supplyShip(){

    }

    public void countPoints() throws IOException {
        List<Sector> countSectors = this.sectors;
        for(int i = 0; i < players.length; i++){
            int index = (firstPlayerIndex + i)%3;
            Sector sector = players[index].countPoints(countSectors);
            countSectors.remove(sector);
        }
    }

    public void nextRound(){

    }

    public void endGame(){

    }

}
