package DOM;

// Интерфейс AnimationControl
public interface AnimationControl {
    // Константа для скорости кадров
    float FRAME_SPEED = 0.25f;

    // Метод для начала анимации
    void startAnimation(Animations animation);

    // Метод для получения состояния анимации
    Animations getAnimation();
    int getFrame();

    // Метод для обновления анимации
    void updateAnimation();
}