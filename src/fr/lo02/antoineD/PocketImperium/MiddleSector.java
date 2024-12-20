package fr.lo02.antoineD.PocketImperium;

import fr.lo02.antoineD.PocketImperium.Exception.AlreadyInitedException;

public class MiddleSector extends Sector{
    public MiddleSector(Tile[] sectorTiles, int[] sectorPattern, int sectorIndex) {
        super(sectorTiles, sectorPattern, sectorIndex);
    }

    public Player[] getTileOccupants() {
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
            throw new AlreadyInitedException("Middle sector already initialized");
        }
    }
}
