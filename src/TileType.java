import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class TileType {
    private String name;
    private int movementCost;
    private boolean isPassable;
    // Add more properties and methods as needed

    // Constructor
    public TileType(String name, int movementCost, boolean isPassable) {
        this.name = name;
        this.movementCost = movementCost;
        this.isPassable = isPassable;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMovementCost() {
        return movementCost;
    }

    public void setMovementCost(int movementCost) {
        this.movementCost = movementCost;
    }

    public boolean isPassable() {
        return isPassable;
    }

    public void setPassable(boolean passable) {
        isPassable = passable;
    }


    // Override toString() method for debugging purposes
    @Override
    public String toString() {

        return "TileType{" +
                "name='" + name + '\'' +
                ", movementCost=" + movementCost +
                ", isPassable=" + isPassable +
                '}';
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TileType other = (TileType) obj;
        return Objects.equals(name, other.name);
    }

}


