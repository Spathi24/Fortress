public abstract class Emplacement {
    // Common properties for all emplacements
    protected int x; // X-coordinate
    protected int y; // Y-coordinate
    protected String name;

    // Constructor
    public Emplacement(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    // Abstract method for performing emplacement-specific actions
    public abstract void performAction();

    // Getters and setters for x, y, and name
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

    public Tile getTile(){
        return TileGenerator.getTileAt(x, y);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
