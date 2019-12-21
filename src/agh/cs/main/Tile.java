package agh.cs.main;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends StackPane {

    public static int TILE_X_SIZE;
    public static int TILE_Y_SIZE;
    private Color color;
    private Rectangle border;

    private Vector2d position;

    public void setColor(Color color) {
        this.color = color;
        border.setFill(color);
    }

    public Tile(int x, int y){
        this.color = Color.BLACK;

        position = new Vector2d(x, y);
        border = new Rectangle(TILE_X_SIZE, TILE_Y_SIZE);
        border.setFill(color);

        setTranslateX(x*TILE_X_SIZE);
        setTranslateY(y*TILE_Y_SIZE);

        setAlignment(Pos.CENTER);
        getChildren().addAll(border);
    }

    public Vector2d getPosition() {
        return position;
    }
}