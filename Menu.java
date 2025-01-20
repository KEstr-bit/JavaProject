package DOM;

import org.jsfml.graphics.Font;
import org.jsfml.graphics.Image;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Mouse;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static DOM.Constants.SCREEN_HEIGHT;
import static DOM.Constants.SCREEN_WIDTH;

public abstract class Menu {
    protected Texture menuTexture;
    protected Font font;
    protected ArrayList<Button> buttons;
    protected boolean isRunning;
    protected float centerX;
    protected float topY;

    public Menu(String fontPath, String menuTexturePath) {
        this.centerX = SCREEN_WIDTH / 2.0f;
        this.topY = SCREEN_HEIGHT / 1.8f;
        this.isRunning = false;

        loadFont(fontPath);
        loadMenuTexture(menuTexturePath);
        buttons = new ArrayList<>();
    }

    public void loadFont(String filename) {
        try {
            Path path = Paths.get(filename).toAbsolutePath();
            font = new Font();
            font.loadFromFile(path);
        } catch (Exception _) {
        }
    }

    public void loadMenuTexture(String filename) {
        try {
            Image img = new Image();
            Path path = Paths.get(filename).toAbsolutePath();
            img.loadFromFile(path);
            menuTexture = new Texture();
            menuTexture.loadFromImage(img);
        } catch (Exception _) {
        }
    }

    public void addButton(String buttonText) {
        Button button = new Button(
                new Vector2f(SCREEN_WIDTH / 4.0f, SCREEN_HEIGHT / 18.0f),
                new Vector2f(centerX, topY + SCREEN_HEIGHT / 12.0f * buttons.size()),
                font,
                buttonText
        );
        buttons.add(button);
    }

    public void setPosition(float centerX, float topY) {
        this.centerX = centerX;
        this.topY = topY;

        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setPosition(new Vector2f(centerX, topY + SCREEN_HEIGHT * i / 12.0f));
        }
    }

    protected void updateButtons(RenderWindow window) {
        Vector2f mousePos = window.mapPixelToCoords(Mouse.getPosition(window));
        for (Button button : buttons) {
            button.update(mousePos);
        }
    }

    protected void drawButtons(RenderWindow window) {
        for (Button button : buttons) {
            button.draw(window);
        }
    }

    public abstract void run(RenderWindow window);
}