package fr.lo02.antoineD.PocketImperium;

import java.util.List;

public class Tile {
    private final int tileIndex;
    private final Tile[] tileNeighbours;
    private List<Ship> ships;
    private final int tilePoints;
    private Player tileOccupant;

    public Tile(int tilePoints, int tileIndex, Tile[] tileNeighbours) {
        this.tilePoints = tilePoints;
        this.tileIndex = tileIndex;
        this.tileNeighbours = tileNeighbours;
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
