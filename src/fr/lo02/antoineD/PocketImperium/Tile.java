package fr.lo02.antoineD.PocketImperium;

import java.util.ArrayList;
import java.util.List;

public class Tile {
    private final int tileIndex;
    private Tile[] tileNeighbours;
    private final List<Ship> ships = new ArrayList<>();
    private final int tilePoints;
    private Player tileOccupant;

    public Tile(int tilePoints, int tileIndex) {
        this.tilePoints = tilePoints;
        this.tileIndex = tileIndex;
    }

    public int getTileIndex() {
        return tileIndex;
    }

    public void setTileOccupant(Player tileOccupant) {
        this.tileOccupant = tileOccupant;
    }

    public Player getTileOccupant() {
        return tileOccupant;
    }

    public int getTilePoints() {
        return tilePoints;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setTileNeighbours(Tile[] tileNeighbours) {
        this.tileNeighbours = tileNeighbours;
    }

    public void addShip(Ship ship) {
        ships.add(ship);
    }

    public void removeShip(Ship ship) {
        ships.remove(ship);
    }

    public Tile[] getTileNeighbours() {
        return tileNeighbours;
    }

    public boolean hasNeighbour(Tile tile) {
        for (Tile neighbour : tileNeighbours) {
            if (neighbour == tile) {
                return true;
            }
        }
        return false;
    }

}
