import java.util.Random;

public class TileGenerator {
    private int width;
    private int height;
    private TileType[] tileTypes;
    private Tile[][] worldMap;
    private Random random;

    public TileGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        this.tileTypes = new TileType[]{
                new TileType("Grass", 1, true),
                new TileType("Water", 99, false),
                new TileType("Mountain", 3, false)
                // Add more tile types as needed
        };
        this.worldMap = new Tile[width][height];
        this.random = new Random();
    }

    public void generateWorld() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int index = random.nextInt(tileTypes.length);
                TileType type = tileTypes[index];
                worldMap[x][y] = new Tile(x, y, type);
            }
        }
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

    public static void main(String[] args) {
        int width = 10;
        int height = 5;
        TileGenerator generator = new TileGenerator(width, height);
        generator.generateWorld();
        generator.printWorld();
    }
}
