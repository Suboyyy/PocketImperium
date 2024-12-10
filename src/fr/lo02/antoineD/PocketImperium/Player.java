package fr.lo02.antoineD.PocketImperium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

    public void addPoints(int points) {
        this.points += points;
    }

    public void orderActions(){

    }

    public void executeActions(int actionId){

    }

    public void summonShips(int nbShips, List<Sector> sectors){
        System.out.println("Création de nouveaux vaisseaux :");
        Tile tile = selectTile(sectors);
        for (int i = 0; i < nbShips; i++) {
            Ship ship = new Ship(i, tile);
            ships.add(ship);
            tile.addShip(ship);
        }
    }

    public void removeShip(Ship ship){
        ships.remove(ship);
    }

    public Ship[] selectShip(List<Ship> ship) {
        return null;
    }

    public void moveFleet(List<Ship> fleet, Tile destination) {
        for (Ship ship : fleet) {
            if (ship.getShipPosition().hasNeighbour(destination))
                ship.moveShip(destination);
            else {
                System.out.println("La destination n'est pas un voisin du vaisseau");
            }
        }
    }

    public void attack(List<Ship> fleet, Tile destination) {
        List<Ship> enemyShips = destination.getShips();
        moveFleet(fleet, destination);
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

    public Tile selectTile(List<Sector> sectors){
        Sector sector = selectSector(sectors);
        Tile[] tiles = sector.getSectorTiles();
        Scanner sc = new Scanner(System.in);
        System.out.println("Choisissez une tuile parmis les suivantes :");
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i].getTileOccupant() == this) {
                System.out.println("Tuile n°" + i + " du secteur " + sector.getSectorIndex());
            }
        }
        int tileIndex = sc.nextInt();
        if (tiles[tileIndex].getTileOccupant() == this) {
            return tiles[tileIndex];
        }
        System.out.println("Cette tuile n'est pas valide");
        return selectTile(sectors);
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
        if (occupants == null){
            System.out.println("Ce secteur est vide");
            return countPoints(sectors);
        } else if (!occupiedByThis) {
            System.out.println("Ce secteur n'est pas occupé par vous");
            return countPoints(sectors);
        } else {
            Tile[] tiles = sector.getSectorTiles();
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
