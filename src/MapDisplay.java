import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MapDisplay extends JFrame {
    private static final int REFRESH_INTERVAL_MS = 1000; // Refresh interval in milliseconds
    private static TileType[] tileTypes;
    private final JLabel[][] tileLabels; // Labels to represent tiles
    private Human human; // Instance of Human

    public MapDisplay(Tile[][] worldMap) {
        tileTypes = new TileType[]{
                new TileType("Grass", 1, true),
                new TileType("Mountain", 3, false),
                new TileType("Water", 99, false)
        };

        setTitle("Map Display");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel to hold the tile labels
        JPanel mapPanel = new JPanel(new GridLayout(worldMap.length, worldMap[0].length));
        tileLabels = new JLabel[worldMap.length][worldMap[0].length];

        // Populate the panel with labels representing tiles
        for (int y = 0; y < worldMap[0].length; y++) {
            for (int x = 0; x < worldMap.length; x++) {
                JLabel label = new JLabel(); // Create a label for the tile
                label.setOpaque(true);
                label.setPreferredSize(new Dimension(10, 10)); // Set size
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border

                // Customize label based on tile type
                Tile tile = worldMap[x][y];
                if (tile.getType().equals(tileTypes[0])) label.setBackground(Color.GREEN);
                else if (tile.getType().equals(tileTypes[1])) label.setBackground(Color.WHITE);
                else if (tile.getType().equals(tileTypes[2])) label.setBackground(Color.BLUE);

                tileLabels[x][y] = label; // Store label reference
                mapPanel.add(label); // Add label to panel
            }
        }

        // Add the map panel to the frame
        add(mapPanel);
        pack(); // Pack components
        setLocationRelativeTo(null); // Center the frame
        setVisible(true); // Make the frame visible

        // Create a Human instance
        human = new Human(25, 25, "John", 100, 50, 50); // Example parameters

        // Start a thread to move the human randomly
        new Thread(this::moveHumanRandomly).start();

        // Start a timer to refresh the frame periodically
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateMapDisplay(worldMap);
            }
        }, 0, REFRESH_INTERVAL_MS);
    }

    // Method to update entity positions on the map
    public void updateEntityPosition(int oldX, int oldY, int newX, int newY) {
        ImageIcon humanIcon = new ImageIcon("path_to_human_icon"); // Provide the path to the human icon image

        // Clear the old position
        tileLabels[oldX][oldY].setIcon(null);

        // Set the icon for the human at the new position
        tileLabels[newX][newY].setIcon(humanIcon);
    }

    // Method to update the map display
// Method to update the map display
    private void updateMapDisplay(Tile[][] worldMap) {
        // Update the background color of existing labels based on the presence of humans
        for (int y = 0; y < worldMap[0].length; y++) {
            for (int x = 0; x < worldMap.length; x++) {
                Tile tile = worldMap[x][y];
                JLabel label = tileLabels[x][y]; // Retrieve existing label

                if (tile.getType().equals(tileTypes[0])) label.setBackground(Color.GREEN);
                else if (tile.getType().equals(tileTypes[1])) label.setBackground(Color.WHITE);
                else if (tile.getType().equals(tileTypes[2])) label.setBackground(Color.BLUE);
                if (tile.hasHuman()) label.setBackground(Color.RED);
            }
        }
        // Repaint the frame to reflect changes
        repaint();
    }

    // Method to move the human randomly
    private void moveHumanRandomly() {
        Random random = new Random();
        while (true) {
            // Randomly select a direction (up, down, left, right)
            int dx = random.nextInt(3) - 1; // -1, 0, or 1
            int dy = random.nextInt(3) - 1; // -1, 0, or 1

            // Calculate new position
            int newX = human.getX() + dx;
            int newY = human.getY() + dy;

            // Check if the new position is within bounds
            if (newX >= 0 && newX < tileLabels.length && newY >= 0 && newY < tileLabels[0].length) {
                // Update human position on the map
                System.out.println("Moving human to position: (" + newX + ", " + newY + ")");
                updateEntityPosition(human.getX(), human.getY(), newX, newY);
                // Update human position in the Human instance
                human.setX(newX);
                human.setY(newY);
            }

            // Print human's state
            System.out.println("Human state - Health: " + human.getHealth() +
                    ", Hunger: " + human.getHunger() +
                    ", Thirst: " + human.getThirst() +
                    ", Alive: " + human.isAlive());

            // Pause for a while before the next move
            try {
                Thread.sleep(1000); // Adjust the delay as needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // Example: Create a world map

        TileGenerator generator = new TileGenerator();
        generator.generateWorld();

        Tile[][] worldMap = generator.getWorldMap();

        // Example: Create and display the map UI
        SwingUtilities.invokeLater(() -> new MapDisplay(worldMap));
    }
}

