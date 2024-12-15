package DOM;

public class Cords {
    private double x;
    private double y;

    // Конструктор для инициализации координат
    public Cords(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Геттеры для x и y
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // Метод для установки координат
    public void setCords(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
