package fr.lo02.antoineD.PocketImperium;

public abstract class Sector {
    private final int sectorIndex;
    private final Tile[] sectorTiles;
    private final int[] sectorPattern;

    public Sector(Tile[] sectorTiles, int[] sectorPattern, int sectorIndex){
        this.sectorTiles = sectorTiles;
        this.sectorPattern = sectorPattern;
        this.sectorIndex = sectorIndex;
    }

    public abstract Player[] getTileOccupants();
    public abstract Tile[] generateTiles();
    public Tile[] getSectorTiles() {
        return this.sectorTiles;
    }
    public int getSectorIndex() {
        return this.sectorIndex;
    }
}
