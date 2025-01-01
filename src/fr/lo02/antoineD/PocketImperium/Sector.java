package fr.lo02.antoineD.PocketImperium;

import java.util.ArrayList;
import java.util.List;

public abstract class Sector {
    private final int sectorIndex;
    private List<Tile> sectorTiles = new ArrayList<>();
    private final int[] sectorPattern;

    public Sector(int[] sectorPattern, int sectorIndex){
        this.sectorPattern = sectorPattern;
        this.sectorIndex = sectorIndex;
    }

    public List<Player> getTileOccupants() {
        List<Player> players = new ArrayList<>();
        for (Tile tile : sectorTiles) {
            if (tile.getTileOccupant() != null && !players.contains(tile.getTileOccupant())) {
                players.add(tile.getTileOccupant());
            }
        }
        return players;
    }

    public List<Tile> getSectorTiles() {
        return this.sectorTiles;
    }

    public int getSectorIndex() {
        return this.sectorIndex;
    }

    public int[] getSectorPattern() {
        return sectorPattern;
    }

    public void addSectorTiles(Tile sectorTiles) {
        this.sectorTiles.add(sectorTiles);
    }

    public void removeSectorTiles(Tile sectorTiles) {
        this.sectorTiles.remove(sectorTiles);
    }
}
