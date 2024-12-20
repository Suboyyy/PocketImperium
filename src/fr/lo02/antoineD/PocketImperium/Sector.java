package fr.lo02.antoineD.PocketImperium;

import java.util.Arrays;

public abstract class Sector {
    private final int sectorIndex;
    private final Tile[] sectorTiles;
    private final int[] sectorPattern;
    private boolean inited = false;

    public Sector(Tile[] sectorTiles, int[] sectorPattern, int sectorIndex){
        this.sectorTiles = sectorTiles;
        this.sectorPattern = sectorPattern;
        this.sectorIndex = sectorIndex;
    }

    public abstract Player[] getTileOccupants();
    public abstract Tile[] generateTiles();
    public abstract void initSector();

    public Tile[] getSectorTiles() {
        return this.sectorTiles;
    }

    public int getSectorIndex() {
        return this.sectorIndex;
    }

    public boolean isInit() {
        return this.inited;
    }

    public void setInit() {
        this.inited = true;
    }
}
