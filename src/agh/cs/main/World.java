package agh.cs.main;

import com.google.gson.Gson;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class World extends Application {
    private BorderPane root = new BorderPane();
    private Pane tilesPane = new Pane();

    private static Map map;
    public static void main(String[] args) {
        setupMapParameters();
        launch(args);
    }

    private AnimationTimer timer = new AnimationTimer() {
        private long lastTimer = 0;

        @Override
        public void handle(long now) {
            if(now - lastTimer >= 100000000) {
                map.nextDay();
                update();
                lastTimer = now;
            }
        }
    };

    private static void setupMapParameters(){
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

        map = new Map(initialParameters.getWidth(),
                initialParameters.getHeight(),
                initialParameters.getJungleRatio());
        map.spawnOrigin(initialParameters.getInitialNumberOfAnimals()); // Spawns first animals
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
        timer.start();
    }

    private Parent createContent() {
        Tile.TILE_X_SIZE = 800/map.getWidth();
        Tile.TILE_Y_SIZE = 800/map.getHeight();

        root.setPrefSize(800, 800);

        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        List<Tile> tiles = new ArrayList<>();

        for(int i = 0;i<map.getWidth();i++){
            for(int j = 0;j<map.getHeight();j++){
                tiles.add(new Tile(i, j));
            }
        }

        tilesPane.getChildren().addAll(tiles);
        root.setCenter(tilesPane);
        update();

        return root;
    }

    private void update() {
        for(Node children : tilesPane.getChildren()){
            Tile tile = (Tile) children;
            Color colorOfElement = map.getColorOfElement(tile.getPosition());
            tile.setColor(colorOfElement);
        }
    }
}
