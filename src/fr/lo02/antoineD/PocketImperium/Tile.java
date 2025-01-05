package fr.lo02.antoineD.PocketImperium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tile {
    private final int tileIndex;
    private Tile[] tileNeighbours;
    private final List<Ship> ships = new ArrayList<>();
    private final int tilePoints;
    private Player tileOccupant = null;

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

    public void addShip(Ship ship, Player p) {
        ships.add(ship);
        if (tileOccupant == null) {
            this.tileOccupant = p;
        }
    }

    public void removeShip(Ship ship) {
        ships.remove(ship);
        if (ships.isEmpty()) {
            this.tileOccupant = null;
        }
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

    @Override
    public String toString() {
        return "Tile{" +
                "tileIndex=" + tileIndex +
                ", tileNeighbours=" + Arrays.stream(tileNeighbours).map(Tile::getTileIndex).toList() +
                ", ships=" + ships +
                ", tilePoints=" + tilePoints +
                ", tileOccupant=" + tileOccupant +
                '}';
    }
}
