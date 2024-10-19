package DOM;

public class Utils {
    static boolean isWall(char[] world_Map, int map_Size_X, int coord_X, int coord_Y)
    {
        return world_Map[coord_X * map_Size_X + coord_Y] == '#';
    };

    static double calcDistance(double first_X, double first_Y, double second_X, double second_Y) {
        return Math.sqrt((first_X - second_X) * (first_X - second_X) + (first_Y - second_Y) * (first_Y - second_Y));
    };
}