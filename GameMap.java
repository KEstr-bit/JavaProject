package DOM;
import java.util.Arrays;

public class GameMap {
    public static final int MAP_SIZE_X = 10;
    public static final int MAP_SIZE_Y = 10;

    private final String[] worldMap;

    public GameMap(String[] worldMap) {
        this.worldMap = Arrays.copyOf(worldMap, worldMap.length);
    }

    public boolean isWall(int cordX, int cordY) {
        if (cordX < 0 || cordX >= MAP_SIZE_X || cordY < 0 || cordY >= MAP_SIZE_Y) {
            return true; // Out of bounds is considered a wall
        }
        return worldMap[cordX].charAt(cordY) == '#';
    }

    public boolean isWall(double cordX, double cordY) {
        int x = (int) Math.round(cordX);
        int y = (int) Math.round(cordY);
        return isWall(x, y); // Delegate to integer method
    }

    public int whatIsWall(int cordX, int cordY)
    {
        if (cordX < 0 || cordX >= MAP_SIZE_X || cordY < 0 || cordY >= MAP_SIZE_Y) {
            return 0;
        }

        return switch (worldMap[cordX].charAt(cordY)) {
            case 'w' -> 1;
            case 'k' -> 2;
            case 'n' -> 3;
            case 'd' -> 4;
            default -> 0;
        };
    }
}
