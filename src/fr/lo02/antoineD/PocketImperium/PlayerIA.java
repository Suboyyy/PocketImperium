package fr.lo02.antoineD.PocketImperium;

public class PlayerIA extends Player {
    public enum strategies {AGRESSIVE, DEFENSIVE, NEUTRAL};
    private final strategies strategy;

    public PlayerIA(int playerIndex, strategies strategy){
        super(playerIndex);
        this.strategy = strategy;
    }

    public void summonShips(int nbShips) {

    }

    public void moveFleet(Ship[] fleet, int destination) {

    }

    public void attack(Ship[] fleet, int destination) {

    }

}
