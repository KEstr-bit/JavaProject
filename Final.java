package DOM;

import org.jsfml.graphics.*;
import java.nio.file.Paths;
import static DOM.Constants.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.*;
import org.jsfml.window.event.*;


public class Final {
    public enum Finals {
        WIN,
        LOOSE
    }

    private final Font font;
    private final Button button;

    public Final() {
        font = new Font();
        try {
            font.loadFromFile(Paths.get("ComicSansMS.ttf"));
        } catch (Exception _) {
        }
        button = new Button(new Vector2f(200, 20), new Vector2f(SCREEN_WIDTH_2, SCREEN_HEIGHT_2), font, "Продолжить");
    }

    public void run(RenderWindow window, Finals finalResult, int score) {
        window.setMouseCursorVisible(true);

        while (window.isOpen()) {

            for (Event event : window.pollEvents()) {
                Vector2f mousePos = window.mapPixelToCoords(Mouse.getPosition(window));
                button.update(mousePos);

                if (event.type == Event.Type.CLOSED) {
                    window.close();
                }

                if (event.type == Event.Type.MOUSE_BUTTON_PRESSED) {
                    if (button.isHovered()) {
                        return; // Возврат при нажатии на кнопку
                    }
                }
            }

            window.clear(Color.BLACK);

            // Отображаем "GAME OVER"
            Drawer.drawMessage(window, "GAME OVER", font, Color.RED, SCREEN_WIDTH_2, 50, 600, 50);

            String finalMessage;
            Color color;
            if (finalResult == Finals.WIN) {
                finalMessage = "You WIN";
                color = Color.WHITE;
            } else {
                finalMessage = "You LOOSE";
                color = Color.RED;
            }

            Drawer.drawMessage(window, finalMessage, font, color, SCREEN_WIDTH_2, 100, 600, 30);

            String scoreMessage = "SCORE: " + score;
            Drawer.drawMessage(window, scoreMessage, font, Color.WHITE, SCREEN_WIDTH_2, 150, 600, 15);

            button.draw(window);
            window.display();
        }
    }
}