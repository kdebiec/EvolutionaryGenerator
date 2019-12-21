package agh.cs.main;

import java.util.ArrayList;
import java.util.List;

public class Animal extends MapElement {
    public static int INITIAL_ENERGY = 50;
    public static int MOVE_ENERGY_COST = 2;
    public static int MIN_ENERGY_TO_POPULATE = 5;
    public static boolean canPopulate(Animal parent1, Animal parent2) {
        if(parent1.getEnergy() < MIN_ENERGY_TO_POPULATE || parent2.getEnergy() < MIN_ENERGY_TO_POPULATE)
            return false;

        return true;
    }

    private Genome genes;
    private MoveDirection moveDirection;
    private int energy;

    List<IPositionChangeObserver> observers = new ArrayList<>();
    IWorldMap map;

    public Animal(IWorldMap map) {
        this.map = map;
    }
    public Animal(IWorldMap map, Vector2d initialPosition) {
        this(map);
        this.position = initialPosition;
        this.energy = Animal.INITIAL_ENERGY;
        this.genes = new Genome();
        this.moveDirection = this.genes.chooseDirection();
    }

    public Animal(Vector2d initialPosition, Animal parent1, Animal parent2) {
        this(parent1.getMap());
        this.position = initialPosition;
        this.energy = parent1.getEnergy()/4 + parent2.getEnergy()/4;
        parent1.setEnergy(3*parent1.getEnergy()/4);
        parent2.setEnergy(3*parent2.getEnergy()/4);
        this.genes = new Genome(parent1.getGenes(), parent2.getGenes());
        this.moveDirection = this.genes.chooseDirection();
    }

    public Genome getGenes() {
        return genes;
    }

    public IWorldMap getMap() {
        return map;
    }

    public int getEnergy() { return energy; }
    public void setEnergy(int energy) { this.energy = energy; }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public boolean isDead() {
        return this.energy <= 0;
    }

    public void eat(int energyEaten) {
        energy += energyEaten;
    }

    public void move() {
        this.moveDirection = this.genes.chooseDirection();

        Vector2d proposedPos = position.add(this.moveDirection.toUnitVector());
        Vector2d newPos = this.map.inBoundaries(proposedPos);
        positionChanged(position, newPos);
        position = newPos;
        energy -= Animal.MOVE_ENERGY_COST;
    }

    @Override
    public String toString() {
        return String.valueOf(this.getEnergy());
    }

    /*@Override
    public String toString() {
        switch (mapDirection) {
            case EAST:
                return "⇒";
            case WEST:
                return "⇐";
            case NORTH:
                return "⇑";
            case SOUTH:
                return "⇓";
            case NORTH_WEST:
                return "⇖";
            case NORTH_EAST:
                return "⇗";
            case SOUTH_WEST:
                return "⇙";
            case SOUTH_EAST:
                return "⇘";
        }
        return null;
    }*/

    void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }

    void removeObserver(IPositionChangeObserver observer) {
        observers.remove(observer);
    }

    void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for(IPositionChangeObserver obs: observers) {
            obs.positionChanged(this, oldPosition, newPosition);
        }
    }
}
