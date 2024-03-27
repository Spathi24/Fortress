public class Tile {
    // Properties of the tile
    private int x;
    private int y;
    private TileType type;
    // Add more properties as needed, such as terrain type, resources, etc.

    // Constructor
    public Tile(int x, int y, TileType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    // Getters and setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    // Add more getters and setters for other properties as needed

    // Override toString() method for debugging purposes
    @Override
    public String toString() {
        return "Tile at (" + x + ", " + y + ") - Type: " + type;
    }
}

