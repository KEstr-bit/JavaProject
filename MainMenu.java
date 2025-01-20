package DOM;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Texture;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.Event.Type;
import org.jsfml.window.Mouse;

import java.nio.file.Paths;

import static DOM.Constants.SCREEN_HEIGHT;
import static DOM.Constants.SCREEN_WIDTH;

public class MainMenu extends Menu {

    public MainMenu() {
        super("ComicSansMS.ttf", "mainMenu.png");
        addButton("Начать игру");
        addButton("Настройки");
        addButton("Выйти");
    }

    public void run(RenderWindow window) {

        while (window.isOpen()) {
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

            // Отрисовка фона и кнопок
            window.clear();
            Drawer.drawImage(window, menuTexture, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT); // Предполагаем, что есть метод для отрисовки изображения
            drawButtons(window);
            window.display();
        }
    }

    private void click(RenderWindow window) {
        int clickedIndex = -1;
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).isHovered()) {
                clickedIndex = i;
                break;
            }
        }

        switch (clickedIndex) {
            case 0: // "Начать игру"
                Game game = new Game();
                game.run(window);
                RenderEngine.terminate();
                PhysicsEngine.terminate();
                break;
            case 1: // "Настройки"
                // Логика для настроек
                break;
            case 2: // "Выйти"
                window.close();
                break;
            default:
                break;
        }
    }
}