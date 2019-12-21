package agh.cs.main;

public class World {
    public static void main(String[] args) {
        int width = 10;
        int height = 10;
        Plant.PLANT_ENERGY = 10;
        Animal.INITIAL_ENERGY = 50;
        Animal.MIN_ENERGY_TO_POPULATE = 5;
        Animal.MOVE_ENERGY_COST = 2;
        double jungleRatio = 0.1; // From 0 to 1 range
        IWorldMap map = new Map(width, height, jungleRatio);
        map.spawnOrigin(4); // Spawns first animals
        System.out.print(map.toString());

        int i = 0;
        while (true) {
            map.nextDay();
            System.out.print(map.toString());
        }
    }
}
