package fr.lo02.antoineD.PocketImperium;

import fr.lo02.antoineD.PocketImperium.Exception.AlreadyInitedException;

public class TriPrimeSector extends Sector{
    private Player player;

    public TriPrimeSector(Tile[] sectorTiles, int[] sectorPattern, int sectorIndex) {
        super(sectorTiles, sectorPattern, sectorIndex);
        this.player = null;
    }

    public Player[] getTileOccupants() {
        if (this.player != null) {
            return new Player[]{this.player};
        }
        return null;
    }

    public Tile[] generateTiles() {
        return null;
    }

    public void initSector() {
        if (!isInit()){
            setInit();
            // TODO: Implement initSector
        } else {
            throw new AlreadyInitedException("TriPrime sector already initialized");
        }
    }
}
