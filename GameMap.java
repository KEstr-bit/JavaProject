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

    public static void init(int[][] wallMapInput, int[][] floorMapInput, int[][] potMapInput) {
        if (wallMapInput.length != MAP_SIZE_X || floorMapInput.length != MAP_SIZE_X || potMapInput.length != MAP_SIZE_X) {
            throw new IllegalArgumentException("Input arrays must have a length of " + MAP_SIZE_X);
        }

        for (int i = 0; i < MAP_SIZE_X; i++) {
            for (int j = 0; j < MAP_SIZE_Y; j++) {
                // Check if there's a valid index for j to prevent ArrayIndexOutOfBoundsException
                if (j < wallMapInput[i].length) {
                    wallMap[i].add(wallMapInput[i][j]);
                }
                if (j < floorMapInput[i].length) {
                    floorMap[i].add(floorMapInput[i][j]);
                }
                if (j < potMapInput[i].length) {
                    potMap[i].add(potMapInput[i][j]);
                }
            }
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