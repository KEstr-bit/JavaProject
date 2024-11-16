package DOM;

public class Utils {
    static double calcDistance(double first_X, double first_Y, double second_X, double second_Y) {
        return Math.sqrt((first_X - second_X) * (first_X - second_X) + (first_Y - second_Y) * (first_Y - second_Y));
    }

    static double degToRad(double deg) {
        return deg * 3.14 / 180;
    }

    static double radToDeg(double rad) {
        return rad * 180 / 3.14;
    }

    static double projectionToX(double len, double rad_Angle) {
        return len * Math.cos(rad_Angle);
    }

    static double projectionToY(double len, double rad_Angle) {
        return len * Math.sin(rad_Angle);
    }

    static double interpolateCoord(double startCoord, double finalCoord, double distance) {
        return (Enemy.VISSION_STEP * finalCoord + (distance - Enemy.VISSION_STEP) * startCoord) / distance;
    }
}
