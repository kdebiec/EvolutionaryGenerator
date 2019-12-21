package agh.cs.main;

public enum MoveDirection {
    NORTH,
    NORTH_WEST,
    NORTH_EAST,
    SOUTH,
    SOUTH_WEST,
    SOUTH_EAST,
    WEST,
    EAST;

    @Override
    public String toString() {
        switch (this) {
            case EAST:
                return "EAST";
            case WEST:
                return "WEST";
            case NORTH:
                return "NORTH";
            case NORTH_WEST:
                return "NORTH_WEST";
            case NORTH_EAST:
                return "NORTH_EAST";
            case SOUTH:
                return "SOUTH";
            case SOUTH_WEST:
                return "SOUTH_WEST";
            case SOUTH_EAST:
                return "SOUTH_EAST";
        }
        return "";
    }
/*
    public MapDirection next() {
        switch (this) {
            case EAST:
                return SOUTH;
            case WEST:
                return NORTH;
            case NORTH:
                return EAST;
            case SOUTH:
                return WEST;
        }
        return this;
    }

    public MapDirection previous() {
        switch (this) {
            case EAST:
                return NORTH;
            case WEST:
                return SOUTH;
            case NORTH:
                return WEST;
            case SOUTH:
                return EAST;
        }
        return this;
    }*/

    public Vector2d toUnitVector() {
        switch (this) {
            case EAST:
                return new Vector2d(1, 0);
            case WEST:
                return new Vector2d(-1,0);
            case NORTH:
                return new Vector2d(0, 1);
            case NORTH_WEST:
                return new Vector2d(-1, 1);
            case NORTH_EAST:
                return new Vector2d(1, 1);
            case SOUTH:
                return new Vector2d(0, -1);
            case SOUTH_WEST:
                return new Vector2d(-1, -1);
            case SOUTH_EAST:
                return new Vector2d(1, -1);
        }
        return new Vector2d(0,0);
    }
}
