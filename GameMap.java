package DOM;
import java.util.Arrays;

public class GameMap {
    public static final int MAPSIZEX = 10;
    public static final int MAPSIZEY = 10;

    private final String[] worldMap;

    public GameMap(String[] worldMap) {
        this.worldMap = Arrays.copyOf(worldMap, worldMap.length);
    }

    public GameMap() {
        worldMap = new String[MAPSIZEY];
        worldMap[0] = "##########";
        worldMap[1] = "#........#";
        worldMap[2] = "#........#";
        worldMap[3] = "#........#";
        worldMap[4] = "#######..#";
        worldMap[5] = "#.....#..#";
        worldMap[6] = "#........#";
        worldMap[7] = "#...#....#";
        worldMap[8] = "#...#....#";
        worldMap[9] = "##########";
    }

    public boolean isWall(int coordX, int coordY) {
        if (coordX < 0 || coordX >= MAPSIZEX || coordY < 0 || coordY >= MAPSIZEY) {
            return true; // Out of bounds is considered a wall
        }
        return worldMap[coordX].charAt(coordY) == '#';
    }

    public boolean isWall(double coordX, double coordY) {
        int x = (int) Math.round(coordX);
        int y = (int) Math.round(coordY);
        return isWall(x, y); // Delegate to integer method
    }
}
