package DOM;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.Keyboard;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

import static DOM.CardinalDirections.*;

public class Main {

    public static void main(String[]args){
        String[] worldMap = new String[GameMap.MAPSIZEX];
        worldMap[0] = "##########";
        worldMap[1] = "#........#";
        worldMap[2] = "#........#";
        worldMap[3] = "#...##...#";
        worldMap[4] = "#...##...#";
        worldMap[5] = "#...##...#";
        worldMap[6] = "#...##...#";
        worldMap[7] = "#..#..#..#";
        worldMap[8] = "#........#";
        worldMap[9] = "##########";

        GameMap wMap = new GameMap(worldMap);
        Drawer dr = new Drawer();
        Final ending = new Final();
        Game DOM = new Game();

        RenderWindow window = new RenderWindow(new VideoMode(Drawer.SCREEN_WIDTH, Drawer.SCREEN_HEIGHT), "Graphic Test");

        boolean endFl = false;         //флажок работы игры
        boolean resProcFl = true;
        boolean shotfl = true;
        boolean swapfl = true;

        while (window.isOpen()) {
            for (Event event: window.pollEvents())
            {
                if (event.type == Event.Type.CLOSED)
                    window.close();
            }

            window.clear(new Color(0,0, 0));

            if (!endFl)
            {
                //обработка действий игрока
                if (Keyboard.isKeyPressed(Keyboard.Key.UP))
                {
                    DOM.you.playerMapStep(North, wMap);
                }
                if (Keyboard.isKeyPressed(Keyboard.Key.DOWN))
                {
                    DOM.you.playerMapStep(South, wMap);
                }
                if (Keyboard.isKeyPressed(Keyboard.Key.RIGHT))
                {
                    DOM.you.playerMapStep(East, wMap);
                }
                if (Keyboard.isKeyPressed(Keyboard.Key.LEFT))
                {

                    DOM.you.playerMapStep(West, wMap);
                }
                if (Keyboard.isKeyPressed(Keyboard.Key.LCONTROL))
                {
                    if (swapfl)
                    {
                        DOM.you.changeActiveWeapon();
                        swapfl = false;
                    }
                }
                else
                    swapfl = true;
                if (Keyboard.isKeyPressed(Keyboard.Key.LSHIFT))
                {
                    if (shotfl)
                    {
                        DOM.playerShot();
                        shotfl = false;
                    }
                }
                else
                    shotfl = true;
                if (Keyboard.isKeyPressed(Keyboard.Key.A))
                {
                    DOM.you.changeVision(West);
                }
                if (Keyboard.isKeyPressed(Keyboard.Key.D))
                {
                    DOM.you.changeVision(East);
                }

                //взаимодействие объектов
                DOM.interaction(wMap);

                //рисование кадра
                dr.drawWalls(wMap, DOM, window);

                try
                {
                    dr.entityDraw(DOM, window);
                }
                catch (IndexOutOfBoundsException _)
                {
                    ending.changeFinal(EndingOption.WinGame);
                    endFl = true;
                }
                catch (IllegalAccessException _) {
                    ending.changeFinal(EndingOption.LooseGame);
                    endFl = true;
                }

            }
            else
                ending.outputFinal(window);

            window.display();
        }
    }
}