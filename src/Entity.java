public abstract class Entity {
    // Common properties for all entities
    protected int x; // X-coordinate
    protected int y; // Y-coordinate
    protected String name;

    // Constructor
    public Entity(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    // Abstract method for performing entity-specific actions
    public abstract void performAction();

    // Getters and setters for x, y, and name
    public int getX() {
        return x;
    }

    public void setX(int x) {
        getTile().setHasHuman(false);
        this.x = x;
        getTile().setHasHuman(true);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        getTile().setHasHuman(false);
        this.y = y;
        getTile().setHasHuman(true);
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

