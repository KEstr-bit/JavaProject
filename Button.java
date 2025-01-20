package DOM;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

import static DOM.Constants.SCREEN_HEIGHT;

public class Button {
    private final RectangleShape shape;
    private final Text text;
    private boolean isHovered = false;

    public Button(Vector2f size, Vector2f position, Font font, String buttonText) {
        shape = new RectangleShape(size);
        shape.setPosition(position.x - size.x / 2, position.y);
        shape.setFillColor(Color.TRANSPARENT);

        text = new Text(buttonText, font, 48);
        float scale = SCREEN_HEIGHT / 2048.0f;
        text.setScale(scale, scale);

        // Центрируем текст в кнопке
        FloatRect textRect = text.getLocalBounds();
        text.setOrigin(textRect.left + textRect.width / 2, textRect.top + textRect.height / 2); // Установка центра
        text.setPosition(position.x, position.y + size.y / 2); // Центрируем текст по кнопке
    }

    public void setPosition(Vector2f position) {
        shape.setPosition(position);
        text.setPosition(position.x + shape.getSize().x / 2, position.y + shape.getSize().y / 2);
    }

    public void update(Vector2f mousePos) {
        if (shape.getGlobalBounds().contains(mousePos)) {
            isHovered = true;
            text.setColor(Color.RED);
        } else {
            isHovered = false;
            text.setColor(Color.WHITE);
        }
    }

    public void draw(RenderWindow window) {
        window.draw(shape);
        window.draw(text);
    }

    public boolean isHovered() {
        return isHovered;
    }
}