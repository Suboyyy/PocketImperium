package fr.lo02.antoineD.PocketImperium;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Game {
    private List<Sector> sectors;
    private int firstPlayerIndex;
    private Player[] players;
    private Tile[] tiles;

    public Game(int firstPlayerIndex){

    }

    public void startGame(){

    }

    public void generateMap(){
        // Reading properties
        Properties neighboursProp = new Properties();
        Properties sectorsProp = new Properties();
        try (InputStream input = new FileInputStream("src/fr/lo02/antoineD/PocketImperium/neighbours.properties")) {
            neighboursProp.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (InputStream input = new FileInputStream("src/fr/lo02/antoineD/PocketImperium/sectors.properties")) {
            sectorsProp.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sectors.add(new BorderSector(new Tile[5], new int[5], 0));
        sectors.add(new BorderSector(new Tile[5], new int[5], 1));
        sectors.add(new BorderSector(new Tile[5], new int[5], 2));
        sectors.add(new MiddleSector(new Tile[4], new int[4], 3));
        sectors.add(new TriPrimeSector(new Tile[1], new int[1], 4));
        sectors.add(new MiddleSector(new Tile[4], new int[4], 5));
        sectors.add(new BorderSector(new Tile[5], new int[5], 6));
        sectors.add(new BorderSector(new Tile[5], new int[5], 7));
        sectors.add(new BorderSector(new Tile[5], new int[5], 8));

        for(Sector sector : sectors){
            if (sector instanceof BorderSector){
                sector.generateTiles();
            } else if (sector instanceof MiddleSector){
                sector.generateTiles();
            } else if (sector instanceof TriPrimeSector){
                sector.generateTiles();
            }
        }
    }

    public void chooseAction(){

    }

    public void executeAction(){

    }

    public void supplyShip(){
        for(Tile tile : tiles){
            List<Ship> ships = tile.getShips();
            int to_delete_ships = ships.size() - 1 - tile.getTilePoints();
            for(int i = 0; i < to_delete_ships; i++){
                Player ship_player = tile.getTileOccupant();
                tile.removeShip(ships.get(i));
                ship_player.removeShip(ships.get(i));
            }
        }
    }

    public void countPoints(){
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
