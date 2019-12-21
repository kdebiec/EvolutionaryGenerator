package agh.cs.main;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that agh.cs.main.Vector2d and agh.cs.main.MoveDirection classes are defined.
 *
 * @author apohllo
 * kdebiec
 *
 */
public interface IWorldMap {
    /**
     * Place a animal on the map.
     *
     * @param animal
     *            The animal to place on the map.
     * @return True if the animal was placed. The animal cannot be placed if the map is already occupied.
     */
    boolean place(Animal animal);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position
     *            Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Vector2d position);
    void spawnOrigin(int i);
    void nextDay();
    Object objectAt(Vector2d currentPosition);
    Vector2d inBoundaries(Vector2d proposedPos);
}
