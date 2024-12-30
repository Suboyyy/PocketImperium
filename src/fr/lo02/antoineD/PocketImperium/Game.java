package fr.lo02.antoineD.PocketImperium;

import fr.lo02.antoineD.PocketImperium.Exception.AlreadyInitedException;
import fr.lo02.antoineD.PocketImperium.Exception.UndefinedActionException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Game {
    private List<Sector> sectors;
    private int firstPlayerIndex;
    private Player[] players;
    private List<Tile> tiles = new ArrayList<>();

    public Game(int firstPlayerIndex){

    }

    public void startGame(){

    }

    public void generateTiles(){
        Data sectorProp = new Data("src/fr/lo02/antoineD/PocketImperium/Map/sectors.properties");
        List<List<Integer>> borderSectorTiles = sectorProp.getData("BorderSector.SectorTiles");
        List<List<Integer>> middleSectorTiles = sectorProp.getData("MiddleSector.SectorTiles");
        List<List<Integer>> triPrimeSectorTiles = sectorProp.getData("TriPrimeSector.SectorTiles");
        Data tilesNeighbours = new Data("src/fr/lo02/antoineD/PocketImperium/Map/neighbour.properties");

        for(Sector sector : sectors){
            int index = sector.getSectorIndex();
            int[] sectorPattern = sector.getSectorPattern();
            if(sector instanceof TriPrimeSector){
                int[] tilePattern = triPrimeSectorTiles.get(index).stream().mapToInt(c -> c).toArray();
                for(int j = 0; j < sectorPattern.length; j++){
                    Tile t = new Tile(sectorPattern[j], tilePattern[j]);
                    this.tiles.add(t);
                    sector.addSectorTiles(t);
                }
            } else if (sector instanceof  MiddleSector) {
                int[] tilePattern = middleSectorTiles.get(index).stream().mapToInt(c -> c).toArray();
                for(int j = 0; j < sectorPattern.length; j++){
                    Tile t = new Tile(sectorPattern[j], tilePattern[j]);
                    this.tiles.add(t);
                    sector.addSectorTiles(t);
                }
            } else {
                int[] tilePattern = borderSectorTiles.get(index).stream().mapToInt(c -> c).toArray();
                for(int j = 0; j < sectorPattern.length; j++){
                    Tile t = new Tile(sectorPattern[j], tilePattern[j]);
                    this.tiles.add(t);
                    sector.addSectorTiles(t);
                }
            }
        }

        tiles.sort(Comparator.comparingInt(Tile::getTileIndex));

        for (Tile tile : tiles) {
            List<List<Integer>> rawNeighbours = tilesNeighbours.getData(""+tile.getTileIndex());
            List<Integer> neighbours = rawNeighbours.getFirst();
            Tile[] tileNeighbours = new Tile[neighbours.size()];
            for (int i = 0; i < neighbours.size(); i++) {
                tileNeighbours[i] = tiles.get(neighbours.get(i));
            }
            tile.setTileNeighbours(tileNeighbours);
        }
    }

    public void generateMap(){
        if (!sectors.isEmpty()) {
            throw new AlreadyInitedException("Map already generated");
        }

        Data sectorProp = new Data("src/fr/lo02/antoineD/PocketImperium/Map/sectors.properties");
        List<List<Integer>> borderSectorPattern = sectorProp.getData("BorderSector.SectorPattern");
        List<List<Integer>> middleSectorPattern = sectorProp.getData("MiddleSector.SectorPattern");
        List<List<Integer>> triPrimeSectorPattern = sectorProp.getData("TriPrimeSector.SectorPattern");

        Random random = new Random();

        for(int i = 0; i < 9; i++){
            if(i == 3 || i == 5){
                int index = random.nextInt(triPrimeSectorPattern.size());
                List<Integer> pattern = triPrimeSectorPattern.get(index);
                triPrimeSectorPattern.remove(index);
                sectors.add(new TriPrimeSector(pattern.stream().mapToInt(c -> c).toArray(), i));
            } else if (i == 4) {
                int index = random.nextInt(middleSectorPattern.size());
                List<Integer> pattern = middleSectorPattern.get(index);
                middleSectorPattern.remove(index);
                sectors.add(new MiddleSector(pattern.stream().mapToInt(c -> c).toArray(), i));
            } else {
                int index = random.nextInt(borderSectorPattern.size());
                List<Integer> pattern = borderSectorPattern.get(index);
                borderSectorPattern.remove(index);
                sectors.add(new BorderSector(pattern.stream().mapToInt(c -> c).toArray(), i));
            }
        }

        generateTiles();

    }

    public void plan(){
        for (Player player : players){
            player.plan();
        }
    }

    public void perform(){
        for (int i = 0; i < 3; i++) {
            int expend = 0;
            int explore = 0;
            int exterminate = 0;
            for (Player player : players){
                Player.actions[] actions = player.getOrderedActions();
                switch (actions[i]) {
                    case EXPAND:
                        expend++;
                        break;
                    case EXPLORE:
                        explore++;
                        break;
                    case EXTERMINATE:
                        exterminate++;
                        break;
                    default:
                        throw new UndefinedActionException("Action non définie");
                }
            }
            for (Player player : players){
                Player.actions[] actions = player.getOrderedActions();
                switch (actions[i]) {
                    case EXPAND:
                        player.expand(4-expend);
                        break;
                    case EXPLORE:
                        player.explore(4-explore);
                        break;
                    case EXTERMINATE:
                        player.exterminate(4-exterminate);
                        break;
                    default:
                        throw new UndefinedActionException("Action non définie");
                }
            }
        }
    }

    public void exploit(){
        supplyShip();
        countPoints();
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
