package fr.lo02.antoineD.PocketImperium;

public class Ship {
    public static int nextShipIndex = 1;
    private final int shipIndex;
    private Tile shipPosition;

    public Ship(int shipIndex, Tile shipPosition){
        this.shipIndex = shipIndex;
        this.shipPosition = shipPosition;
    }

    public void moveShip(Tile destination){
        this.shipPosition = destination;
    }

    public int getShipIndex(){
        return this.shipIndex;
    }

    public Tile getShipPosition(){
        return this.shipPosition;
    }
}
