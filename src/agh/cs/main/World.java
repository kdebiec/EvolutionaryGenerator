package agh.cs.main;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class World {
    public static void main(String[] args) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("parameters.json"));
        } catch (FileNotFoundException e) {
            System.out.println("No file found! It should be named parameters.json");
            return;
        }

        Gson gson = new Gson();
        InitialParameters initialParameters = gson.fromJson(reader, InitialParameters.class);

        Plant.PLANT_ENERGY = initialParameters.getPlantEnergy();
        Animal.INITIAL_ENERGY = initialParameters.getStartEnergy();
        Animal.MIN_ENERGY_TO_POPULATE = initialParameters.getAnimalMinimumEnergyToProcreate();
        Animal.MOVE_ENERGY_COST = initialParameters.getMoveEnergy();
        Map.PLANTS_TO_SPAWN_IN_JUNGLE_PER_DAY = initialParameters.getPlantsToSpawnInJunglePerDay();
        Map.PLANTS_TO_SPAWN_IN_OUTSKIRTS_PER_DAY = initialParameters.getPlantsToSpawnInOutskirtsPerDay();

        IWorldMap map = new Map(initialParameters.getWidth(),
                initialParameters.getHeight(),
                initialParameters.getJungleRatio());
        map.spawnOrigin(initialParameters.getInitialNumberOfAnimals()); // Spawns first animals
        System.out.print(map.toString());

        while (true) {
            map.nextDay();
            System.out.print(map.toString());
        }
    }
}
