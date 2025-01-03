package fr.lo02.antoineD.PocketImperium;

import fr.lo02.antoineD.PocketImperium.Exception.InvalidParameterException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Player {
    private final int playerIndex;
    private int points;
    private final List<Ship> ships;
    public enum actions {EXPAND, EXPLORE, EXTERMINATE}
    private actions[] orderedActions;

    public Player(int playerIndex) {
        this.playerIndex = playerIndex;
        this.points = 0;
        this.ships = new ArrayList<>();
        this.orderedActions = new actions[3];
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public int getPoints() {
        return points;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public actions[] getOrderedActions() {
        return orderedActions;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void startGame(List<Sector> sectors){
        System.out.println("Déploiement initial pour le joueur n°" + playerIndex+1);
        System.out.println("Choix d'une tuile de niveau 1 :");
        List<Sector> sector = new ArrayList<>();
        Sector selectedSector = selectSector(sectors);
        while (!selectedSector.getTileOccupants().isEmpty() || selectedSector instanceof TriPrimeSector) {
            System.out.println("Ce secteur est déjà occupé");
            selectedSector = selectSector(sectors);
        }
        sector.add(selectedSector);
        System.out.println("Veuillez sélectionner une tuile de niveau 1");
        Tile tile = selectTile(sector, true);
        while (tile.getTilePoints() != 1){
            System.out.println("La tuile sélectionnée n'est pas de niveau 1");
            tile = selectTile(sector, true);
        }
        for (int i = 0; i < 2; i++) {
            Ship ship = new Ship(i, tile);
            ships.add(ship);
            tile.addShip(ship);
        }
    }

    public void plan(){
        this.orderedActions = new actions[3];
        Scanner sc = new Scanner(System.in);
        System.out.println("Choisissez l'ordre de vos actions :");
        System.out.println("1. Expand");
        System.out.println("2. Explore");
        System.out.println("3. Exterminate");
        for (int i = 0; i < 3; i++) {
            int actionIndex = sc.nextInt();
            switch (actionIndex) {
                case 1:
                    if (Arrays.asList(orderedActions).contains(actions.EXPAND)) {
                        System.out.println("Cette action a déjà été choisie");
                        i--;
                    } else {
                        orderedActions[i] = actions.EXPAND;
                    }
                    break;
                case 2:
                    if (Arrays.asList(orderedActions).contains(actions.EXPLORE)) {
                        System.out.println("Cette action a déjà été choisie");
                        i--;
                    } else {
                        orderedActions[i] = actions.EXPLORE;
                    }
                    break;
                case 3:
                    if (Arrays.asList(orderedActions).contains(actions.EXTERMINATE)) {
                        System.out.println("Cette action a déjà été choisie");
                        i--;
                    } else {
                        orderedActions[i] = actions.EXTERMINATE;
                    }
                    break;
                default:
                    System.out.println("Cette action n'est pas valide");
                    i--;
                    break;
            }
        }
    }

    public void expand(int level){
        int nbShips = getShips().size();
        if (nbShips == 25) {
            System.out.println("Vous avez déjà le nombre maximum de vaisseaux");
            return;
        } else if (nbShips + level > 25) {
            level = 25 - nbShips;
            System.out.println("Le niveau de votre expansion a été réduit à " + level);
        }
        List<Sector> sectors = Game.getSectors();
        summonShips(level, sectors);
    }

    public void explore(int level){
        // TODO : Auto occupy system
        //  Add ship during move
        //  Only one move per ship
        for (int i = 0; i < level; i++) {
            System.out.println("Exploration n°" + i);
            List<Ship> fleet = selectFleet();
            System.out.println("1er déplacement de la flotte :");
            moveFleet(fleet, false);
            if (fleet.getFirst().getShipPosition().getTilePoints() == 3) {return;}
            System.out.println("2ème déplacement de la flotte :");
            moveFleet(fleet, false);
        }
    }

    public void exterminate(int level){
        for (int i = 0; i < level; i++) {
            System.out.println("Attaque n°" + i);
            System.out.println("Sélection de la case à attaquer :");
            Tile destination = selectTile(Game.getSectors(), true);
            while (destination.getTileOccupant() == this) {
                System.out.println("Vous ne pouvez pas attaquer une case occupée par vous même");
                destination = selectTile(Game.getSectors(), true);
            }
            List<Ship> fleet = selectFleet(destination);
            attack(fleet, destination);
        }
    }

    public void summonShips(int nbShips, List<Sector> sectors){
        System.out.println("Création de nouveaux vaisseaux :");
        Tile tile = selectTile(sectors, false);
        for (int i = 0; i < nbShips; i++) {
            Ship ship = new Ship(i, tile);
            ships.add(ship);
            tile.addShip(ship);
        }
    }

    public void removeShip(Ship ship){
        ships.remove(ship);
    }

    public List<Ship> selectFleet() {
        System.out.println("Voici vos vaisseaux, quelle flotte voulez vous utiliser ?");
        for (Ship s : ships) {
            System.out.println("Vaisseau n°" + s.getShipIndex() + " sur la tuile " + s.getShipPosition().getTileIndex());
        }
        Scanner sc = new Scanner(System.in);
        int shipIndex = sc.nextInt();
        int tileIndex = -1;
        for (Ship s : ships) {
            if (s.getShipIndex() == shipIndex) {
                tileIndex = s.getShipPosition().getTileIndex();
                break;
            }
        }
        if (tileIndex == -1) {
            System.out.println("Ce vaisseau n'existe pas");
            return selectFleet();
        }
        List<Ship> fleet = new ArrayList<>();
        for (Ship s : ships) {
            if (s.getShipPosition().getTileIndex() == tileIndex) {
                fleet.add(s);
            }
        }
        return fleet;
    }

    public List<Ship> selectFleet(Tile tile) {
        List<Ship> fleet = new ArrayList<>();
        for (Ship s : ships) {
            if (Arrays.asList(s.getShipPosition().getTileNeighbours()).contains(tile)) {
                System.out.println("Vaisseau n°" + s.getShipIndex() + " sur la tuile " + s.getShipPosition().getTileIndex());
                System.out.println("Voulez vous ajouter ce vaisseau à la flotte ? (y/n)");
                String answer;
                Scanner sc = new Scanner(System.in);
                do {
                    answer = sc.nextLine();
                } while (!answer.equals("y") && !answer.equals("n"));
                if (answer.equals("y")) {
                    fleet.add(s);
                }
            }
        }
        return fleet;
    }

    public void moveFleet(List<Ship> fleet, boolean bypass) {
        System.out.println("Où voulez vous déplacer votre flotte ?");
        Tile destination = selectTile(Game.getSectors(), bypass);
        for (Ship ship : fleet) {
            if (ship.getShipPosition().hasNeighbour(destination))
                ship.moveShip(destination);
            else {
                System.out.println("La destination n'est pas un voisin du vaisseau");
            }
        }
    }

    public void moveFleet(List<Ship> fleet, Tile destination) {
        for (Ship ship : fleet) {
            if (ship.getShipPosition().hasNeighbour(destination))
                ship.moveShip(destination);
            else {
                System.out.println("La destination n'est pas un voisin du vaisseau");
            }
        }
    }

    public void attack(List<Ship> fleet, Tile destination) {
        List<Ship> defenderShips = destination.getShips();
        Player defender = destination.getTileOccupant();
        int min = Math.min(fleet.size(), defenderShips.size());
        for (int i = 0; i < min; i++) {
            this.removeShip(fleet.get(i));
            defender.removeShip(defenderShips.get(i));
            fleet.remove(fleet.get(i));
            defenderShips.remove(defenderShips.get(i));
        }
        if (defenderShips.isEmpty()) {
            moveFleet(fleet, destination);
            destination.setTileOccupant(this);
        }
    }

    public Sector selectSector(List<Sector> sectors){
        // TODO : test Scanner class
        Scanner sc = new Scanner(System.in);
        if (sectors.size() == 9) {
            System.out.println("Choisissez un secteur entre 1 et 9 : ");
        } else {
            System.out.println("Choisissez un secteur parmi les suivants :");
            for (Sector sector : sectors) {
                System.out.println(sector.getSectorIndex());
            }
        }
        int sectorIndex = sc.nextInt();
        for (Sector sector : sectors) {
            if (sector.getSectorIndex() == sectorIndex) {
                return sector;
            }
        }
        System.out.println("Ce secteur n'est pas valide");
        return selectSector(sectors);
    }

    public Tile selectTile(List<Sector> sectors, boolean bypass){
        Sector sector;
        if (sectors.isEmpty()) {
            throw new InvalidParameterException("La liste des secteurs ne doit pas être vide");
        } else if (sectors.size() == 1) {
            sector = sectors.getFirst();
        } else {
            sector = selectSector(sectors);
        }
        List<Tile> tiles = sector.getSectorTiles();
        Scanner sc = new Scanner(System.in);
        System.out.println("Choisissez une tuile parmi les suivantes :");
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).getTileOccupant() == this || bypass) {
                System.out.println("Tuile n°" + i + " du secteur " + sector.getSectorIndex());
            }
        }
        int tileIndex = sc.nextInt();
        if (tileIndex < 0 || tileIndex >= tiles.size()) {
            System.out.println("Cette tuile n'est pas valide");
            return selectTile(sectors, bypass);
        }
        if (tiles.get(tileIndex).getTileOccupant() == this || bypass) {
            return tiles.get(tileIndex);
        }
        System.out.println("Cette tuile n'est pas valide");
        return selectTile(sectors, false);
    }

    public Sector countPoints(List<Sector> sectors){
        Sector sector = selectSector(sectors);
        List<Player> occupants = sector.getTileOccupants();
        int points = 0;
        if (!occupants.contains(this)) {
            System.out.println("Ce secteur n'est pas occupé par vous");
            return countPoints(sectors);
        } else {
            List<Tile> tiles = sector.getSectorTiles();
            for (Tile tile : tiles) {
                if (tile.getTileOccupant() == this) {
                    points += tile.getTilePoints();
                }
            }
            for (Sector s : sectors) {
                if (s instanceof TriPrimeSector && s.getTileOccupants().contains(this)) {
                    points += 3;
                }
            }
        }
        this.addPoints(points);
        System.out.println("Vous avez gagné " + points + " points");
        return sector;
    }

}
