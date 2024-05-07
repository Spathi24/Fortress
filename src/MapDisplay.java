import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MapDisplay extends JFrame {
    private Tile[][] worldMap; // Instance variable for world map


    private Tile[][] villageMap; // Village-specific map
    private boolean isInVillage; // Flag to track if the player is in a village
    private static final int REFRESH_INTERVAL_MS = 100; // Refresh interval in milliseconds
    private static TileType[] tileTypes;
    private final JLabel[][] tileLabels; // Labels to represent tiles
    private Human human; // Instance of Human
    private Cursor cursor; // Cursor instance
    private int cursorX; // X position of the cursor
    private int cursorY; // Y position of the cursor

    public MapDisplay(Tile[][] worldMap) throws IOException {
        tileTypes = new TileType[]{
                new TileType("Grass", 1, true),
                new TileType("Mountain", 3, false),
                new TileType("Water", 99, false)
        };
        this.worldMap = worldMap;
        this.villageMap = new Tile[worldMap.length][worldMap[0].length];

        TileGenerator generator = new TileGenerator(0);
        generator.generateWorld();

        isInVillage = false;

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

                // Customize label based on tile type
                Tile tile = worldMap[x][y];
                if (tile.getType().equals(tileTypes[0])) label.setBackground(Color.GREEN);
                else if (tile.getType().equals(tileTypes[1])) label.setBackground(Color.BLACK);
                else if (tile.getType().equals(tileTypes[2])) label.setBackground(Color.BLUE);

                tileLabels[x][y] = label; // Store label reference
                mapPanel.add(label); // Add label to panel
            }
        }

        // Initialize cursor
        cursor = new Cursor(25, 25);

        // Add key listener to the frame for arrow key navigation and enter key selection
        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_UP:
                        moveCursor(0, -1); // Move cursor up
                        break;
                    case KeyEvent.VK_DOWN:
                        moveCursor(0, 1); // Move cursor down
                        break;
                    case KeyEvent.VK_LEFT:
                        moveCursor(-1, 0); // Move cursor left
                        break;
                    case KeyEvent.VK_RIGHT:
                        moveCursor(1, 0); // Move cursor right
                        break;
                    case KeyEvent.VK_ENTER:
                        selectCell(cursor.getX(), cursor.getY()); // Select cell at cursor position
                        break;
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        // Set focusable to true to enable key listener
        setFocusable(true);


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
                if (isInVillage) {
                    System.out.println("Updating village map display1");
                    updateMapDisplay(villageMap);
                } else {
                    System.out.println("Updating world map display");
                    updateMapDisplay(worldMap);
                }
            }
        }, 0, REFRESH_INTERVAL_MS);
    }


    // Method to move the cursor
// Method to move the cursor
    private void moveCursor(int dx, int dy) {
        // Calculate new cursor position
        int newX = cursor.getX() + dx;
        int newY = cursor.getY() + dy;

        // Check if the new position is within bounds
        if (newX >= 0 && newX < tileLabels.length && newY >= 0 && newY < tileLabels[0].length) {
            // Update cursor position
            cursor.move(dx, dy);

            // Update map display to show cursor
            if (isInVillage) {
                System.out.println("Updating village map display2");
                updateMapDisplay(villageMap);
            } else {
                System.out.println("Updating world map display");
                updateMapDisplay(worldMap);
            }

        }
    }


    // Method to handle cell selection and action options
    private void selectCell(int x, int y) {
        // Retrieve the tile at the selected cell position
        Tile selectedTile = worldMap[x][y];


        if (!isInVillage) {

            // Display tile properties
            String message = "Tile Properties:\n" +
                    "Type: " + selectedTile.getType().getName() + "\n" +
                    "Is Passable: " + selectedTile.getType().isPassable() + "\n" +
                    "Resources: " + selectedTile.getResources() + "\n";
            if (selectedTile.hasEmplacement()) {
                message += "Emplacement: " + selectedTile.getEmplacement().getName() + "\n";
                // If there's an emplacement, you can provide additional details
            }

            JOptionPane.showMessageDialog(this, message, "Tile Info", JOptionPane.INFORMATION_MESSAGE);

            // Perform actions related to the emplacement
            // Check if the tile already contains an emplacement
            if (selectedTile.hasEmplacement()) {
                selectedTile.getEmplacement().performAction();
            } else {
                // Prompt the user to place an emplacement on the tile
                int choice = JOptionPane.showConfirmDialog(this, "Place emplacement on this tile?", "Emplacement Placement", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    // Place an emplacement on the selected tile
                    placeEmplacement(x, y);
                }
            }
        } else switchToMainWorldMap();
    }

    // Method to place an emplacement on a tile
    private void placeEmplacement(int x, int y) {
        // Check if the selected tile is empty or already contains an emplacement
        if (!worldMap[x][y].hasEmplacement()) {
            // Create an emplacement and place it on the selected tile
            Village newVillage = new Village(x, y, "Town1", this);
            worldMap[x][y].setEmplacement(newVillage);

            // Update map display to visually represent the emplacement
            System.out.println("nigger");
            updateMapDisplay(worldMap);
        } else {
            // Notify the user that the tile is not suitable for placing an emplacement
            JOptionPane.showMessageDialog(this, "Cannot place emplacement on this tile.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to switch between main world map and village map
    public void switchToVillageMap() {
        System.out.println("Switching to village map");

        isInVillage = true;
        // Generate the village map
        TileGenerator generator = new TileGenerator(0);
        villageMap = generator.generateVillageMap(50, 50); // Adjust the size as needed
        // Update the map display with the village map
        System.out.println("Updating village map display3");
        updateMapDisplay(villageMap);

    }

    public void switchToMainWorldMap() {
        System.out.println("Switching to main world map");

        isInVillage = false;
        // Update the map display with the main world map
        System.out.println("Updating world map display");
        updateMapDisplay(worldMap);
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
    private void updateMapDisplay(Tile[][] worldMap) {
        System.out.println(worldMap);

        // Update the background color of existing labels based on the presence of humans
        for (int y = 0; y < worldMap[0].length; y++) {
            for (int x = 0; x < worldMap.length; x++) {
                Tile tile = worldMap[x][y];
                JLabel label = tileLabels[x][y]; // Retrieve existing label

                if (tile.getType().equals(tileTypes[0])) label.setBackground(Color.GREEN);
                else if (tile.getType().equals(tileTypes[1])) label.setBackground(Color.BLACK);
                else if (tile.getType().equals(tileTypes[2])) label.setBackground(Color.BLUE);
                if (tile.hasHuman()) label.setBackground(Color.RED);
                if (cursor.getX() == x && cursor.getY() == y) label.setBackground(Color.YELLOW); // Highlight cursor position
                if (tile.getEmplacement() != null) label.setBackground(Color.MAGENTA);
            }
        }
        System.out.println(isInVillage);
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
                //System.out.println("Moving human to position: (" + newX + ", " + newY + ")");
                updateEntityPosition(human.getX(), human.getY(), newX, newY);
                // Update human position in the Human instance
                human.setX(newX);
                human.setY(newY);
            }

            // Print human's state
            //System.out.println("Human state - Health: " + human.getHealth() +
            //        ", Hunger: " + human.getHunger() +
            //        ", Thirst: " + human.getThirst() +
            //        ", Alive: " + human.isAlive());

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

        TileGenerator generator = new TileGenerator(0);
        generator.generateWorld();

        Tile[][] worldMap = generator.getWorldMap();

        // Example: Create and display the map UI
        SwingUtilities.invokeLater(() -> {
            try {
                new MapDisplay(worldMap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

