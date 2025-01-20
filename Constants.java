package DOM;

public class Constants {
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final int SCREEN_WIDTH_2 = SCREEN_WIDTH / 2;
    public static final int SCREEN_HEIGHT_2 = SCREEN_HEIGHT / 2;
    public static final double PI = 3.141592653589793238;
    public static final double PI_2 = PI / 2;

    // Приватный конструктор предотвращает создание экземпляров этого класса
    private Constants() {
        throw new UnsupportedOperationException("This is a constants class and cannot be instantiated");
    }
}