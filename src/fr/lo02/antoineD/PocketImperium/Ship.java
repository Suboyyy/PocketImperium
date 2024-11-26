package fr.lo02.antoineD.PocketImperium;

public class Ship {
    private final int shipIndex;
    private Tile shipPosition;

    public Ship(int shipIndex){
        this.shipIndex = shipIndex;
    }

    public void moveShip(Tile destination){
        this.shipPosition = destination;
    }

    public Tile getShipPosition(){
        return this.shipPosition;
    }
}
