package fr.lo02.antoineD.PocketImperium;

public class TriPrimeSector extends Sector{
    private Player player;

    public TriPrimeSector(int[] sectorPattern, int sectorIndex) {
        super(sectorPattern, sectorIndex);
        this.player = null;
    }
}
