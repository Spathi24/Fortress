import java.util.Random;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

public class TileGenerator {
    private static final double SCALE = 0.1; // Adjust the scale to control the terrain roughness
    private static final long SEED = 0;
    private static final double SEA_LEVEL = -0.2; // Threshold for determining water vs land
    private final int width;
    private final int height;
    private final TileType[] tileTypes;
    private final Tile[][] worldMap;
    private final Random random;

    public TileGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        this.tileTypes = new TileType[]{
                new TileType("Grass", 1, true),
                new TileType("Mountain", 3, false),
                new TileType("Water", 99, false)
                // Add more tile types as needed
        };
        this.worldMap = new Tile[width][height];
        this.random = new Random();
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
                } else {
                    int index = random.nextInt(2);
                    TileType type = tileTypes[index];
                    int rgb = 0x00FF00 * (1 - index);
                    image.setRGB(x, y, rgb);
                    worldMap[x][y] = new Tile(x, y, type);
                }
            }
        }
        ImageIO.write(image, "png", new File("noise.png"));
    }

    public Tile[][] getWorldMap() {
        return worldMap;
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

        int width = 50;
        int height = 50;
        TileGenerator generator = new TileGenerator(width, height);
        generator.generateWorld();
        generator.printWorld();
    }
}
