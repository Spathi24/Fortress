import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class Tile {
    // Properties of the tile
    private int x;
    private int y;
    private TileType type;
    private boolean hasHuman;
    private Emplacement emplacement;
    private boolean hasEmplacement;
    private Map<String, Integer> resources; // Map to store resources by type

    // Constructor
    public Tile(int x, int y, TileType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.hasHuman = false;
        this.emplacement = null;
        this.hasEmplacement = false;
        this.resources = new HashMap<>();
        populateResources(type); // Populate resources based on tile type
    }

    public Tile(int x, int y, TileType type, HashMap<String, Integer> resources) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.hasHuman = false;
        this.emplacement = null;
        this.hasEmplacement = false;
        this.resources = resources;
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

    public boolean hasHuman() {
        return hasHuman;
    }

    public void setHasHuman(boolean hasHuman) {
        this.hasHuman = hasHuman;
    }

    public boolean hasEmplacement() {
        return hasEmplacement;
    }

    public void setEmplacement(Emplacement emplacement) {
        this.hasEmplacement = true;
        this.emplacement = emplacement;
    }

    public Emplacement getEmplacement() {
        return emplacement;
    }

    // Methods to manipulate resources
    public void addResource(String resourceType, int amount) {
        resources.put(resourceType, resources.getOrDefault(resourceType, 0) + amount);
    }

    public void removeResource(String resourceType, int amount) {
        int currentAmount = resources.getOrDefault(resourceType, 0);
        int newAmount = currentAmount - amount;
        if (newAmount <= 0) {
            resources.remove(resourceType); // Remove resource if amount becomes zero or negative
        } else {
            resources.put(resourceType, newAmount); // Update resource amount
        }
    }

    public Map<String, Integer> getResources() {
        return resources;
    }

    // Method to populate the tile with resources based on given resource types and probabilities
    public void populateResources(Map<String, Double> resourceProbabilities, int maxResources) {
        Random random = new Random();

        for (String resourceType : resourceProbabilities.keySet()) {
            double probability = resourceProbabilities.get(resourceType);
            for (int i = 0; i < maxResources; i++) {
                if (random.nextDouble() < probability) {
                    addResource(resourceType, 1);
                }
            }
        }
    }

    // Method to populate the tile with resources based on given resource types and probabilities
    public void populateResources(TileType type) {
        Map<String, Double> resourceProbabilities = getResourceProbabilities(type);
        int maxResources = 10; // Adjust this value as needed

        // Populate resources based on probabilities
        populateResources(resourceProbabilities, maxResources);
    }

    // Method to get resource probabilities based on tile type
    private Map<String, Double> getResourceProbabilities(TileType type) {
        Map<String, Double> resourceProbabilities = new HashMap<>();

        // Define resource probabilities for different tile types
        switch (type.getName()) {
            case "Grass":
                resourceProbabilities.put("Wood", 0.5); // Example probabilities, adjust as needed
                resourceProbabilities.put("Stone", 0.3);
                resourceProbabilities.put("Metal", 0.2);
                break;
            case "Mountain":
                resourceProbabilities.put("Stone", 0.8);
                resourceProbabilities.put("Metal", 0.2);
                break;
            case "Water":
                resourceProbabilities.put("Fish", 0.7);
                resourceProbabilities.put("Seaweed", 0.3);
                break;
            default:
                // Default probabilities for unknown tile types
                resourceProbabilities.put("UnknownResource", 1.0);
                break;
        }

        return resourceProbabilities;
    }

    // Override toString() method for debugging purposes
    @Override
    public String toString() {
        return "Tile at (" + x + ", " + y + ") - Type: " + type + ", Resources: " + resources;
    }
}

