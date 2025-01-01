package fr.lo02.antoineD.PocketImperium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Player {
    private final int playerIndex;
    private int points;
    private List<Ship> ships;
    public enum actions {EXPAND, EXPLORE, EXTERMINATE}
    private actions[] orderedActions;

    public Player(int playerIndex) {
        this.playerIndex = playerIndex;
        this.points = 0;
        this.ships = new ArrayList<Ship>();
        this.orderedActions = new actions[3];
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public int getPoints() {
        return points;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public actions[] getOrderedActions() {
        return orderedActions;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void plan(){
        this.orderedActions = new actions[3];
        Scanner sc = new Scanner(System.in);
        System.out.println("Choisissez l'ordre de vos actions :");
        System.out.println("1. Expand");
        System.out.println("2. Explore");
        System.out.println("3. Exterminate");
        for (int i = 0; i < 3; i++) {
            int actionIndex = sc.nextInt();
            switch (actionIndex) {
                case 1:
                    if (Arrays.asList(orderedActions).contains(actions.EXPAND)) {
                        System.out.println("Cette action a déjà été choisie");
                        i--;
                    } else {
                        orderedActions[i] = actions.EXPAND;
                    }
                    break;
                case 2:
                    if (Arrays.asList(orderedActions).contains(actions.EXPLORE)) {
                        System.out.println("Cette action a déjà été choisie");
                        i--;
                    } else {
                        orderedActions[i] = actions.EXPLORE;
                    }
                    break;
                case 3:
                    if (Arrays.asList(orderedActions).contains(actions.EXTERMINATE)) {
                        System.out.println("Cette action a déjà été choisie");
                        i--;
                    } else {
                        orderedActions[i] = actions.EXTERMINATE;
                    }
                    break;
                default:
                    System.out.println("Cette action n'est pas valide");
                    i--;
                    break;
            }
        }
    }

    public void expand(int level){
        int nbShips = getShips().size();
        if (nbShips == 25) {
            System.out.println("Vous avez déjà le nombre maximum de vaisseaux");
            return;
        } else if (nbShips + level > 25) {
            level = 25 - nbShips;
            System.out.println("Le niveau de votre expansion a été réduit à " + level);
        }
        List<Sector> sectors = Game.getSectors();
        summonShips(level, sectors);
    }

    public void explore(int level){
        for (int i = 0; i < level; i++) {
            List<Ship> fleet = selectFleet(ships);
            moveFleet(fleet);
        }
    }

    public void exterminate(int level){
        // TODO : exterminate logic
    }

    public void summonShips(int nbShips, List<Sector> sectors){
        System.out.println("Création de nouveaux vaisseaux :");
        Tile tile = selectTile(sectors, false);
        for (int i = 0; i < nbShips; i++) {
            Ship ship = new Ship(i, tile);
            ships.add(ship);
            tile.addShip(ship);
        }
    }

    public void removeShip(Ship ship){
        ships.remove(ship);
    }

    public List<Ship> selectFleet(List<Ship> ship) {
        System.out.println("Voici vos vaisseaux, quelle flotte voulez vous utiliser ?");
        for (Ship s : ships) {
            System.out.println("Vaisseau n°" + s.getShipIndex() + " sur la tuile " + s.getShipPosition().getTileIndex());
        }
        Scanner sc = new Scanner(System.in);
        int shipIndex = sc.nextInt();
        int tileIndex = -1;
        for (Ship s : ships) {
            if (s.getShipIndex() == shipIndex) {
                tileIndex = s.getShipPosition().getTileIndex();
                break;
            }
        }
        if (tileIndex == -1) {
            System.out.println("Ce vaisseau n'existe pas");
            return selectFleet(ships);
        }
        List<Ship> fleet = new ArrayList<>();
        for (Ship s : ships) {
            if (s.getShipPosition().getTileIndex() == tileIndex) {
                fleet.add(s);
            }
        }
        return fleet;
    }

    public void moveFleet(List<Ship> fleet) {
        // TODO : Can't move through Tri-Prime
        //  Auto occupy system
        //  can't move half hex
        System.out.println("Où voulez vous déplacer votre flotte ?");
        Tile destination = selectTile(Game.getSectors(), false);
        for (Ship ship : fleet) {
            if (ship.getShipPosition().hasNeighbour(destination))
                ship.moveShip(destination);
            else {
                System.out.println("La destination n'est pas un voisin du vaisseau");
            }
        }
    }

    public void attack(List<Ship> fleet, Tile destination) {
        // List<Ship> enemyShips = destination.getShips();
        // moveFleet(fleet, destination);
        // TODO : implement attack logic
    }

    public Sector selectSector(List<Sector> sectors){
        // TODO : test Scanner class
        Scanner sc = new Scanner(System.in);
        if (sectors.size() == 9) {
            System.out.println("Choisissez un secteur entre 1 et 9 : ");
        } else {
            System.out.println("Choisissez un secteur parmis les suivants :");
            for (Sector sector : sectors) {
                System.out.println(sector.getSectorIndex());
            }
        }
        int sectorIndex = sc.nextInt();
        for (Sector sector : sectors) {
            if (sector.getSectorIndex() == sectorIndex) {
                return sector;
            }
        }
        System.out.println("Ce secteur n'est pas valide");
        return selectSector(sectors);
    }

    public Tile selectTile(List<Sector> sectors, boolean bypass){
        Sector sector = selectSector(sectors);
        List<Tile> tiles = sector.getSectorTiles();
        Scanner sc = new Scanner(System.in);
        System.out.println("Choisissez une tuile parmis les suivantes :");
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).getTileOccupant() == this) {
                System.out.println("Tuile n°" + i + " du secteur " + sector.getSectorIndex());
            }
        }
        int tileIndex = sc.nextInt();
        if (tiles.get(tileIndex).getTileOccupant() == this || bypass) {
            return tiles.get(tileIndex);
        }
        System.out.println("Cette tuile n'est pas valide");
        return selectTile(sectors, false);
    }

    public Sector countPoints(List<Sector> sectors){
        // TODO : Tri Prime sector case
        Sector sector = selectSector(sectors);
        Player[] occupants = sector.getTileOccupants();
        boolean occupiedByThis = false;
        for (Player occupant : occupants) {
            if (occupant == this) {
                occupiedByThis = true;
            }
        }
        if (Arrays.stream(occupants).toList().isEmpty()){
            System.out.println("Ce secteur est vide");
            return countPoints(sectors);
        } else if (!occupiedByThis) {
            System.out.println("Ce secteur n'est pas occupé par vous");
            return countPoints(sectors);
        } else {
            List<Tile> tiles = sector.getSectorTiles();
            for (Tile tile : tiles) {
                if (tile.getTileOccupant() == this) {
                    this.addPoints(tile.getTilePoints());
                }
            }
        }
        return sector;
    }

    public void endTurn(){
        // TODO : End turn logic
    }

}
