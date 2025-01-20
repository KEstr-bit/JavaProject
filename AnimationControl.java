package DOM;

// Интерфейс AnimationControl
public interface AnimationControl {
    public enum Animations {
        ANIM_SPAWN,
        ANIM_BASE,
        ANIM_MOVE,
        ANIM_ATTACK1,
        ANIM_ATTACK2,
        ANIM_TAKING_DAMAGE,
        ANIM_DIE
    }
    // Константа для скорости кадров
    float FRAME_SPEED = 0.012f;

    // Метод для начала анимации
    void startAnimation(Animations animation);

    // Метод для получения состояния анимации
    Animations getAnimation();
    int getFrame();

    // Метод для обновления анимации
    void updateAnimation(double delta);
}