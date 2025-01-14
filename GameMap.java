package DOM;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class GameMap {
    static final int MAP_SIZE_X = 33;
    static final int MAP_SIZE_Y = 33;

    private static List<Integer>[] wallMap = new ArrayList[MAP_SIZE_X];
    private static List<Integer>[] floorMap = new ArrayList[MAP_SIZE_X];
    private static List<Integer>[] potMap = new ArrayList[MAP_SIZE_X];

    static {
        for (int i = 0; i < MAP_SIZE_X; i++) {
            wallMap[i] = new ArrayList<>();
            floorMap[i] = new ArrayList<>();
            potMap[i] = new ArrayList<>();
        }
    }

    public static void init(List<Integer>[] wallMapInput, List<Integer>[] floorMapInput, List<Integer>[] potMapInput) {
        for (int i = 0; i < MAP_SIZE_X; i++) {
            wallMap[i] = new ArrayList<>(wallMapInput[i]);
        }

        for (int i = 0; i < MAP_SIZE_X; i++) {
            floorMap[i] = new ArrayList<>(floorMapInput[i]);
        }

        for (int i = 0; i < MAP_SIZE_X; i++) {
            potMap[i] = new ArrayList<>(potMapInput[i]);
        }
    }

    public static int whatIsWall(int cordX, int cordY) {
        if (cordX < 0 || cordX >= MAP_SIZE_X || cordY < 0 || cordY >= MAP_SIZE_Y) {
            return 0;
        }
        return wallMap[cordX].get(cordY);
    }

    public static int whatIsFloor(int cordX, int cordY) {
        if (cordX < 0 || cordX >= MAP_SIZE_X || cordY < 0 || cordY >= MAP_SIZE_Y) {
            return 0;
        }
        return floorMap[cordX].get(cordY);
    }

    public static int whatIsPot(int cordX, int cordY) {
        if (cordX < 0 || cordX >= MAP_SIZE_X || cordY < 0 || cordY >= MAP_SIZE_Y) {
            return 0;
        }
        return potMap[cordX].get(cordY);
    }

    public static int whatIsWall(double cordX, double cordY) {
        int x = (int) Math.round(cordX);
        int y = (int) Math.round(cordY);
        return whatIsWall(x, y);
    }

    public static boolean isWall(int cordX, int cordY) {
        if (cordX < 0 || cordX >= MAP_SIZE_X || cordY < 0 || cordY >= MAP_SIZE_Y) {
            return true;
        }
        return wallMap[cordX].get(cordY) != 0;
    }

    public static boolean isWall(double cordX, double cordY) {
        int x = (int) Math.round(cordX);
        int y = (int) Math.round(cordY);
        return isWall(x, y);
    }
}