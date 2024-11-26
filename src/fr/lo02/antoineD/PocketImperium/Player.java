package fr.lo02.antoineD.PocketImperium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

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

    public void addPoints(int points) {
        this.points += points;
    }

    public void orderActions(){

    }

    public void executeActions(int actionId){

    }

    public void summonShips(int nbShips, Sector[] sectors) throws IOException {
        System.out.println("Création de nouveaux vaisseaux :");
        Tile tile = selectTile(sectors);
        for (int i = 0; i < nbShips; i++) {
            Ship ship = new Ship(i, tile);
            ships.add(ship);
            tile.addShip(ship);
        }
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

    public Sector selectSector(Sector[] sectors) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        if (sectors.length == 9) {
            System.out.println("Choisissez un secteur entre 1 et 9 : ");
        } else {
            System.out.println("Choisissez un secteur parmis les suivants :");
            for (Sector sector : sectors) {
                System.out.println(sector.getSectorIndex());
            }
        }
        String output = reader.readLine();
        try {
            int sectorIndex = Integer.parseInt(output);
            for (Sector sector : sectors) {
                if (sector.getSectorIndex() == sectorIndex) {
                    return sector;
                }
            }
            System.out.println("Ce secteur n'est pas valide");
            return selectSector(sectors);
        } catch (NumberFormatException e) {
            // TODO : Exeption handling
            System.out.println("Ceci n'est pas un nombre valide");
            return selectSector(sectors);
        }
    }

    public Tile selectTile(Sector[] sectors) throws IOException {
        Sector sector = selectSector(sectors);
        Tile[] tiles = sector.getSectorTiles();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        System.out.println("Choisissez une tuile parmis les suivantes :");
        for (Tile tile : tiles) {
            if (tile.getTileOccupant() == this) {
                System.out.println("Tuile n°" + tile.getTileIndex());
            }
        }
        String output = reader.readLine();
        try {
            int tileIndex = Integer.parseInt(output);
            if (tiles[tileIndex].getTileOccupant() == this) {
                return tiles[tileIndex];
            }
            System.out.println("Cette tuile n'est pas valide");
            return selectTile(sectors);
        } catch (NumberFormatException e) {
            // TODO : Exeption handling
            System.out.println("Ceci n'est pas un nombre valide");
            return selectTile(sectors);
        }
    }

    public void countPoints(Sector[] sectors) throws IOException {
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
            countPoints(sectors);
        } else if (!occupiedByThis) {
            System.out.println("Ce secteur n'est pas occupé par vous");
            countPoints(sectors);
        } else {
            Tile[] tiles = sector.getSectorTiles();
            for (Tile tile : tiles) {
                if (tile.getTileOccupant() == this) {
                    this.addPoints(tile.getTilePoints());
                }
            }
        }

    }

    public void endTurn(){
        // TODO : End turn logic
    }

}
