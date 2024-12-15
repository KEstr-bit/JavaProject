package DOM;

import java.util.Vector;

public class Utils {
    static double calcDistance(double firstX, double firstY, double secondX, double secondY) {
        return Math.sqrt((firstX - secondX) * (firstX - secondX) + (firstY - secondY) * (firstY - secondY));
    }

    static double degToRad(double degrees) {
        return degrees * 3.14 / 180;
    }

    static double radToDeg(double radians) {
        return  radians * 180 / 3.14;
    }

    static double projectToX(double length, double radians) {
        return length * Math.cos(radians);
    }

    static double projectToY(double length, double radians) {
        return length * Math.sin(radians);
    }

    static double interpolateCords(double startCoordinate, double finalCoordinate, double step, double distance) {
        return (step * finalCoordinate + (distance - step) * startCoordinate) / distance;
    }

    static double getRotationAngle(double radians, double lineCos, double lineSin) {
        double rotationSin = Math.sin(radians) * lineCos - Math.cos(radians) * lineSin;
        double rotationCos = Math.cos(radians) * lineCos + Math.sin(radians) * lineSin;

        if (rotationCos > 1)
            rotationCos = 1;

        if (rotationCos < -1)
            rotationCos = -1;

        double rotationAngle = radToDeg(Math.acos(rotationCos));

        if (rotationSin < 0)
            rotationAngle *= -1;

        return rotationAngle;
    }


    public static <T extends Comparable<T>, Q> void dependSorting(Vector<T> mainList, Vector<Q> sideList, int left, int right) {
        // Указатели в начало и в конец массива
        int i = left, j = right;

        // Центральный элемент массива
        T mid = mainList.get((left + right) / 2);

        // Делим массив
        do {
            while (mainList.get(i).compareTo(mid) > 0) {
                i++;
            }

            while (mainList.get(j).compareTo(mid) < 0) {
                j--;
            }

            // Меняем элементы местами
            if (i <= j) {
                // Перестановка в основном и боковом списках
                Q tempSide = sideList.get(i);
                sideList.set(i, sideList.get(j));
                sideList.set(j, tempSide);

                T tempMain = mainList.get(i);
                mainList.set(i, mainList.get(j));
                mainList.set(j, tempMain);

                i++;
                j--;
            }
        } while (i <= j);

        // Рекурсивные вызовы, если осталось, что сортировать
        if (left < j) {
            dependSorting(mainList, sideList, left, j);
        }
        if (i < right) {
            dependSorting(mainList, sideList, i, right);
        }
    }
}
