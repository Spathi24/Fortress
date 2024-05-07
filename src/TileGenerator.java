import java.awt.*;
import java.util.Random;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;

public class TileGenerator {
    private static final double SCALE = 0.1; // Adjust the scale to control the terrain roughness
    private static final long SEED = 0;
    private static final double SEA_LEVEL = -0.2; // Threshold for determining water vs land
    private static final double MOUNT_LEVEL = 0.4;
    private static final int width = 50;
    private static final int height = 50;
    private final TileType[] tileTypes;
    private static final Tile[][] worldMap = new Tile[width][height];
    private final Random random;

    public TileGenerator(long seed) {
        this.tileTypes = new TileType[]{
                new TileType("Grass", 1, true),
                new TileType("Mountain", 3, false),
                new TileType("Water", 99, false)
                // Add more tile types as needed
        };
        this.random = new Random(seed);
    }

    public double[][] generateHeightmap() {
        double[][] heightmap = new double[width][height];

        // Generate Perlin noise values
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double nx = x * SCALE;
                double ny = y * SCALE;
                heightmap[x][y] = OpenSimplex2S.noise3_ImproveXY(SEED, nx, ny, 0); // Perlin noise value at (nx, ny)
            }
        }
        return heightmap;
    }

    public void generateWorld() throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        double[][] heightmap = generateHeightmap();

        // Assign terrain types based on heightmap values
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double height = heightmap[x][y];
                if (height < SEA_LEVEL) {
                    worldMap[x][y] = new Tile(x, y, tileTypes[2]);
                    int rgb = 0x0000FF;
                    image.setRGB(x, y, rgb);
                } else if (height > MOUNT_LEVEL) {
                    worldMap[x][y] = new Tile(x, y, tileTypes[1]);
                    int rgb = 0x808080;
                    image.setRGB(x, y, rgb);
                } else {
                    worldMap[x][y] = new Tile(x, y, tileTypes[0]);
                    int rgb = 0x00FF00;
                    image.setRGB(x, y, rgb);
                }
            }
        }
        ImageIO.write(image, "png", new File("noise.png"));
    }

    public Tile[][] getWorldMap() {
        return worldMap;
    }

    // Method to retrieve a tile by coordinates
    public static Tile getTileAt(int x, int y) {
        if (x >= 0 && x < worldMap.length && y >= 0 && y < worldMap[0].length) {
            return worldMap[x][y];
        } else {
            // Handle out-of-bounds coordinates or null world map
            return null;
        }
    }

    public Tile[][] generateVillageMap(int width, int height) {
        Tile[][] villageMap = new Tile[width][height];

        // Generate village map values
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Assign terrain types based on your village design
                // For example, let's make all tiles in the village "Grass"
                villageMap[x][y] = new Tile(x, y, tileTypes[0]);
            }
        }
        return villageMap;
    }

    public void printWorld() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(worldMap[x][y].getType().getName().charAt(0) + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {

        TileGenerator generator = new TileGenerator(0);
        generator.generateWorld();
        generator.printWorld();
    }
}
