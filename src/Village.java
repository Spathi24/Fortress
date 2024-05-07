// Define the Village class that extends Emplacement
public class Village extends Emplacement {
    private final MapDisplay mapDisplay;
    private String name;

    public Village(int x, int y, String name, MapDisplay mapDisplay) {
        super(x, y, name);
        this.name = name;
        this.mapDisplay = mapDisplay;
        // Other initialization as needed
    }

    @Override
    public void performAction() {
        // Implement emplacement action logic for the village
        System.out.println("Interacting with village: " + name);

        // Switch isInVillage flag in MapDisplay
        mapDisplay.switchToVillageMap();
    }
}
