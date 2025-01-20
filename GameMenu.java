package DOM;
import org.jsfml.graphics.Image;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.Event.Type;
import org.jsfml.window.Mouse;


import static DOM.Constants.SCREEN_HEIGHT;
import static DOM.Constants.SCREEN_WIDTH;

public class GameMenu extends Menu {
    private final GameState gameState;
    private boolean isRunning;

    public GameMenu(GameState gameState) {
        super("ComicSansMS.ttf", "gameMenu.png");
        this.gameState = gameState;

        setPosition(SCREEN_WIDTH / 4.6f, topY);
        addButton("Продолжить");
        addButton("Настройки");
        addButton("Вернуться в меню");
    }

    public void run(RenderWindow window) {
        window.setMouseCursorVisible(true);
        isRunning = true;

        Image img = new Image();
        img = window.capture();
        Texture backgroundTexture = new Texture();

        try {
            backgroundTexture.loadFromImage(img);
        } catch (TextureCreationException _) {
        }

        while (window.isOpen() && isRunning) {
            // Обработка событий
            for (Event event : window.pollEvents()) {
                if (event.type == Type.CLOSED) {
                    window.close();
                }

                // Обработка нажатия кнопок
                if (event.type == Type.MOUSE_BUTTON_PRESSED) {
                    if (event.asMouseButtonEvent().button == Mouse.Button.LEFT) {
                        click(window);
                    }
                }
            }

            // Обновление состояния кнопок
            updateButtons(window);
            Drawer.drawImage(window, backgroundTexture, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
            Drawer.drawImage(window, menuTexture, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
            drawButtons(window);
            window.display();
        }
    }

    private void click(RenderWindow window) {
        int index = -1;
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).isHovered()) {
                index = i;
                break;
            }
        }

        switch (index) {
            case 0:
                isRunning = false;
                break;
            case 1:
                // Логика для настроек
                break;
            case 2:
                isRunning = false;
                gameState.gameOver();
                break;
            default:
                break;
        }
    }
}