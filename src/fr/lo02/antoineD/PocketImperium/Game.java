package fr.lo02.antoineD.PocketImperium;

import fr.lo02.antoineD.PocketImperium.Exception.AlreadyInitedException;
import fr.lo02.antoineD.PocketImperium.Exception.UndefinedActionException;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Game {
    private static final List<Sector> sectors = new ArrayList<>();
    private int firstPlayerIndex;
    private final List<Player> players;
    private final List<Tile> tiles = new ArrayList<>();

    public Game(int firstPlayerIndex, int nbPlayers){
        this.firstPlayerIndex = firstPlayerIndex;
        this.players = new ArrayList<>(3);
        for (int i = 0; i < nbPlayers; i++) {
            players.add(new Player(i));
        }
        for (int i = nbPlayers; i < 3; i++) {
            int rd = new Random().nextInt(3);
            players.add(new PlayerIA(i, PlayerIA.strategies.values()[rd]));
        }
        generateMap();
        startGame();
        int i = 0;
        while (i < 9 && players.size() == 3){
            plan();
            perform();
            exploit();
            nextRound();
            i++;
        }
        endGame();
    }

    public static List<Sector> getSectors() {
        return sectors;
    }

    public void startGame(){
        printMap();
        for (int i = 0; i < 3; i++) {
            Player player = players.get((firstPlayerIndex + i) % 3);
            player.startGame(sectors);
        }
        printMap();
        for (int i = 0; i < 3; i++) {
            Player player = players.get((2 - i + firstPlayerIndex) % 3);
            player.startGame(sectors);
        }
    }

    public void generateTiles(){
        Data sectorProp = new Data("src/fr/lo02/antoineD/PocketImperium/Map/sectors.properties");
        List<List<Integer>> borderSectorTiles = sectorProp.getData("BorderSector.SectorTiles");
        List<List<Integer>> middleSectorTiles = sectorProp.getData("MiddleSector.SectorTiles");
        List<List<Integer>> triPrimeSectorTiles = sectorProp.getData("TriPrimeSector.SectorTiles");
        List<Integer> halfTiles = sectorProp.getData("HalfTiles").getFirst();
        Data tilesNeighbours = new Data("src/fr/lo02/antoineD/PocketImperium/Map/neighbour.properties");

        for(Sector sector : sectors){
            int[] sectorPattern = sector.getSectorPattern();
            if(sector instanceof TriPrimeSector){
                int[] tilePattern = triPrimeSectorTiles.getFirst().stream().mapToInt(c -> c).toArray();
                triPrimeSectorTiles.removeFirst();
                for(int j = 0; j < sectorPattern.length; j++){
                    Tile t = new Tile(sectorPattern[j], tilePattern[j]);
                    this.tiles.add(t);
                    sector.addSectorTiles(t);
                }
            } else if (sector instanceof  MiddleSector) {
                int[] tilePattern = middleSectorTiles.getFirst().stream().mapToInt(c -> c).toArray();
                middleSectorTiles.removeFirst();
                for(int j = 0; j < sectorPattern.length; j++){
                    Tile t = new Tile(sectorPattern[j], tilePattern[j]);
                    this.tiles.add(t);
                    sector.addSectorTiles(t);
                }
            } else {
                int[] tilePattern = borderSectorTiles.getFirst().stream().mapToInt(c -> c).toArray();
                borderSectorTiles.removeFirst();
                for(int j = 0; j < sectorPattern.length; j++){
                    Tile t = new Tile(sectorPattern[j], tilePattern[j]);
                    this.tiles.add(t);
                    sector.addSectorTiles(t);
                }
            }
        }

        for (int i : halfTiles) {
            Tile t = new Tile(0, i);
            this.tiles.add(t);
        }

        tiles.sort(Comparator.comparingInt(Tile::getTileIndex));

        for (Tile tile : tiles) {
            List<List<Integer>> rawNeighbours = tilesNeighbours.getData(""+tile.getTileIndex());
            List<Integer> neighbours = rawNeighbours.getFirst();
            Tile[] tileNeighbours = new Tile[neighbours.size()];
            for (int i = 0; i < neighbours.size(); i++) {
                tileNeighbours[i] = tiles.get(neighbours.get(i)-1);
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

        for(int i = 1; i <= 9; i++){
            if(i == 5){
                int index = random.nextInt(triPrimeSectorPattern.size());
                List<Integer> pattern = triPrimeSectorPattern.get(index);
                triPrimeSectorPattern.remove(index);
                sectors.add(new TriPrimeSector(pattern.stream().mapToInt(c -> c).toArray(), i));
            } else if (i == 4 || i == 6) {
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
        for (int i = 0; i < 3; i++) {
            printMap();
            Player player = players.get((firstPlayerIndex + i) % 3);
            player.plan();
        }
    }

    public void perform(){
        for (int i = 0; i < 3; i++) {
            printMap();
            int expend = 0;
            int explore = 0;
            int exterminate = 0;
            for (int j = 0; j < 3; j++) {
                Player player = players.get((firstPlayerIndex + j) % 3);
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
            for (int j = 0; j < 3; j++) {
                Player player = players.get((firstPlayerIndex + j) % 3);
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
        printMap();
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
        List<Sector> countSectors = sectors;
        for(int i = 0; i < players.size(); i++){
            int index = (firstPlayerIndex + i)%3;
            Sector sector = players.get(index).countPoints(countSectors);
            countSectors.remove(sector);
        }
    }

    public void printMap(){
        int[] pattern = new int[]{6, 5};
        int index = 0;
        for (int i = 0; i < 9; i++) {
            StringBuilder str = new StringBuilder();
            if (pattern[i%2] == 5) {
                str.append("    ");
            }
            str.append("|");
            int k = 0;
            for (int j = 0; j+k < pattern[i%2]; j++) {
                str.append(" lvl ").append(tiles.get(index + j).getTilePoints()).append("  |");
                if (index+j == 18 || index+j == 23 || index+j == 27) {
                    str.append(" lvl 3 |");
                    k++;
                }
            }
            System.out.println(str);
            str = new StringBuilder();
            if (pattern[i%2] == 5) {
                str.append("    ");
            }
            str.append("|");
            k = 0;
            for (int j = 0; j+k < pattern[i%2]; j++) {
                str.append(" ship ").append(tiles.get(index + j).getShips().size()).append(" |");
                if (index+j == 18 || index+j == 23 || index+j == 27) {
                    str.append(" ship ").append(tiles.get(24).getShips().size()).append(" |");
                    k++;
                }
            }
            System.out.println(str);
            str = new StringBuilder();
            if (pattern[i%2] == 5) {
                str.append("    ");
            }
            str.append("|");
            k = 0;
            for (int j = 0; j+k < pattern[i%2]; j++) {
                if (tiles.get(index + j).getTileOccupant() != null) {
                    str.append(" play ").append(tiles.get(index + j).getTileOccupant().getPlayerIndex()+1).append(" |");
                } else {
                    str.append("  null  |");
                }
                if (index+j == 18 || index+j == 23 || index+j == 27) {
                    if (tiles.get(24).getTileOccupant() != null) {
                        str.append(" play ").append(tiles.get(24).getTileOccupant().getPlayerIndex()+1).append(" |");
                    } else {
                        str.append("  null  |");
                    }
                    k++;
                }
            }
            System.out.println(str);
            index += pattern[i%2]-k;
        }
    }

    public void nextRound(){
        for (int i = 0; i < 3; i++) {
            Player player = players.get((firstPlayerIndex + i) % 3);
            if (player.getShips().isEmpty()){
                players.remove(player);
            }
        }
        firstPlayerIndex = (firstPlayerIndex + 1) % players.size();
    }

    public void endGame(){
        Player winner = players.getFirst();
        for (Tile tile : tiles) {
            if (tile.getTileOccupant() != null) {
                tile.getTileOccupant().addPoints(tile.getTilePoints() * 2);
            }
        }

        for (Player player : players) {
            if (player.getPoints() > winner.getPoints()) {
                winner = player;
            }
        }

        System.out.println("Le joueur n°" + (winner.getPlayerIndex()+1) + " a gagné avec " + winner.getPoints() + " points");
    }

}
