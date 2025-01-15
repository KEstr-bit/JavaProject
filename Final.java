package DOM;

import org.jsfml.graphics.*;
import java.io.IOException;

import static DOM.Constants.SCREEN_HEIGHT;
import static DOM.Constants.SCREEN_WIDTH;
import static DOM.EndingOption.WinGame;

public class Final {

    private final Font font;
    private EndingOption endingOption;       //Парамтр окончания игры

    Final()
    {
        font = new Font();
        try {
            font.loadFromFile(java.nio.file.Paths.get("ComicSansMS.ttf").toAbsolutePath());
        }
        catch(IOException _) {
        }
        endingOption = WinGame;
    }

    private void displayMessage(RenderWindow window, final String message, Color color) {
        Text text;
        text = new Text(message, font, 70);
        //созданеие текстового блока
        text.setColor(color);
        FloatRect textRect = text.getLocalBounds();
        text.setOrigin(textRect.left + textRect.width / 2.0f, textRect.top + textRect.height / 2.0f);
        text.setPosition(SCREEN_WIDTH / 2.0f, SCREEN_HEIGHT / 2.0f);

        //вывод
        window.draw(text);
    }

    public void outputFinal(RenderWindow window) {
        switch (endingOption)
        {
            case LooseGame:
                displayMessage(window, "YOU LOOSE!!!", Color.RED);
                break;
            case WinGame:
                displayMessage(window, "You Win!", Color.WHITE);
                break;
            default:
                break;
        }

    }

    public void changeFinal(EndingOption option)
    {
        endingOption = option;
    }
}
