package DOM;
import org.jsfml.graphics.*;

import java.util.Vector;

import static DOM.TextureType.WALLS;

public class Drawer {

    public static void drawMessage(RenderWindow window, String message, Font font, Color color,
                                   float centerX, float centerY, float sizeX, float sizeY,
                                   int fontSize) {
        Text text = new Text(message, font, fontSize);
        text.setPosition(centerX, centerY);
        text.setColor(color);

        // Get the local bounds of the text
        org.jsfml.graphics.FloatRect textRect = text.getLocalBounds();

        // Calculate scale based on sizeX and sizeY
        float scale = Math.min(sizeX / textRect.width, sizeY / textRect.height);

        // Set scale
        text.setScale(scale, scale);

        // Set the origin based on textRect
        text.setOrigin(textRect.width / 2, textRect.height / 2);

        // Draw the text on the window
        window.draw(text);
    }

    public static void drawImage(RenderWindow window, Texture texture, float x, float y,
                                 float sizeX, float sizeY, Color color) {
        Sprite sprite = new Sprite(texture);
        sprite.setColor(color);

        // Set the texture rectangle to the entire texture
        sprite.setTextureRect(new org.jsfml.graphics.IntRect(0, 0,
                texture.getSize().x, texture.getSize().y));

        sprite.setPosition(x, y);
        sprite.setScale(sizeX / texture.getSize().x, sizeY / texture.getSize().y);

        // Draw the sprite on the window
        window.draw(sprite);
    }

    public static void drawImage(RenderWindow window, Texture texture, float x, float y,
                                 float sizeX, float sizeY,
                                 int rectLeft, int rectTop,
                                 int rectWidth, int rectHeight,
                                 Color color) {
        Sprite sprite = new Sprite(texture);
        sprite.setColor(color);

        // Set texture rectangle based on provided values
        sprite.setTextureRect(new org.jsfml.graphics.IntRect(rectLeft, rectTop,
                rectWidth, rectHeight));

        sprite.setPosition(x, y);
        sprite.setScale(sizeX / rectWidth, sizeY / rectHeight);

        // Draw the sprite on the window
        window.draw(sprite);
    }

    // Overloaded method without color parameter defaulting to white\
    public static void drawImage(RenderWindow window, Texture texture, float x, float y,
                                 float sizeX, float sizeY,
                                 int rectLeft, int rectTop,
                                 int rectWidth, int rectHeight) {
        drawImage(window, texture, x, y, sizeX, sizeY, rectLeft, rectTop,
        rectWidth, rectHeight, Color.WHITE);
    }


    public static void drawImage(RenderWindow window, Texture texture, float x, float y,
                                 float sizeX, float sizeY) {
        drawImage(window, texture, x, y, sizeX, sizeY, Color.WHITE);
    }
}