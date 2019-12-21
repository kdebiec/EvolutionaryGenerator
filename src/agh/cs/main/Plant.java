package agh.cs.main;

public class Plant extends MapElement {
    public static int PLANT_ENERGY = 10;

    public Plant(Vector2d position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "*";
    }
}
