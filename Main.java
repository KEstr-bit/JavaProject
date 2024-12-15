package DOM;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;
import org.jsfml.system.Vector2i;

import static DOM.CardinalDirections.*;

public class Main {

    public static void main(String[]args){
        String[] worldMap = new String[GameMap.MAP_SIZE_X];
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
        Game game = new Game();

        RenderWindow window = new RenderWindow(new VideoMode(Drawer.SCREEN_WIDTH, Drawer.SCREEN_HEIGHT), "Graphic Test");

        window.setSize(new Vector2i(1920, 1080));

        window.setPosition(new Vector2i(320, 180));

        boolean endFl = false;         //флажок работы игры
        boolean shotFl = true;
        boolean swapFl = true;
        boolean reloadingFl = true;

        window.setMouseCursorVisible(false);
        Mouse.setPosition(new Vector2i(Drawer.SCREEN_WIDTH / 2, Drawer.SCREEN_HEIGHT / 2), window);

        while (window.isOpen()) {


            for (Event event: window.pollEvents())
            {
                if (event.type == Event.Type.CLOSED)
                    window.close();

                if (event.type == Event.Type.MOUSE_BUTTON_PRESSED && event.asMouseButtonEvent().button == Mouse.Button.LEFT)
                {
                    if (shotFl)
                    {
                        game.playerShot();

                        shotFl = false;
                    }
                }
                else
                    shotFl = true;


                if (event.type == Event.Type.MOUSE_MOVED)
                {
                    Vector2i currentMousePosition = Mouse.getPosition(window);
                    int deltaX = currentMousePosition.x - Drawer.SCREEN_WIDTH / 2;
                    game.player.changeVision(deltaX * 0.1);
                    Mouse.setPosition(new Vector2i(Drawer.SCREEN_WIDTH / 2, Drawer.SCREEN_HEIGHT / 2), window);
                }

                if(event.type == Event.Type.KEY_PRESSED && event.asKeyEvent().key == Keyboard.Key.ESCAPE)
                {
                    window.close();
                }

                if (event.type == Event.Type.KEY_PRESSED && event.asKeyEvent().key == Keyboard.Key.LCONTROL)
                {
                    if (swapFl)
                    {
                        game.player.changeActiveWeapon();
                        swapFl = false;
                    }
                }
                else
                    swapFl = true;

                if (event.type == Event.Type.KEY_PRESSED && event.asKeyEvent().key == Keyboard.Key.LSHIFT)
                {
                    if (reloadingFl)
                    {
                        game.player.getActiveWeapon().reloading();
                        reloadingFl = false;
                    }
                }
                else
                    reloadingFl = true;


            }


            window.clear(new Color(0,0, 0));

            if (!endFl)
            {
                //обработка действий игрока
                if (Keyboard.isKeyPressed(Keyboard.Key.W))
                {
                    game.player.playerMapStep(NORTH, wMap);
                }
                if (Keyboard.isKeyPressed(Keyboard.Key.S))
                {
                    game.player.playerMapStep(SOUTH, wMap);
                }
                if (Keyboard.isKeyPressed(Keyboard.Key.D))
                {
                    game.player.playerMapStep(EAST, wMap);
                }
                if (Keyboard.isKeyPressed(Keyboard.Key.A))
                {
                    game.player.playerMapStep(WEST, wMap);
                }


                //взаимодействие объектов
                game.interaction(wMap);

                //рисование кадра
                dr.drawWalls(wMap, game, window);

                try
                {
                    dr.entityDraw(game, window);
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

                dr.drawPlayerWeapon(game, window);
            }
            else
                ending.outputFinal(window);

            window.display();
        }
    }
}