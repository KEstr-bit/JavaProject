package DOM;

public class GameState {
    private boolean over;

    // Конструктор
    public GameState() {
        over = false;
    }

    // Метод для проверки состояния игры
    public boolean isGameOver() {
        return over;
    }

    // Метод для завершения игры
    public void gameOver() {
        over = true;
    }
}