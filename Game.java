package DOM;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse;
import org.jsfml.window.event.Event;
import org.jsfml.system.Clock;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Vector;
import java.util.ArrayList;

import static DOM.CardinalDirections.*;


public class Game {

    private Player player;
    private Entity boss;
    private RenderEngine drawer;
    private Clock clock;
    private List<Entity> entities;

    public Game() {
        clock = new Clock();

        player = new Player(31, 16);
        entities = new ArrayList<>();
        entities.add(player);
        PhysicsEngine.addEntity(player);

        boss = new Necromancer(6, 16, player);
        entities.add(boss);
        RenderEngine.addEntity(boss);
        PhysicsEngine.addEntity(boss);

        // Загрузка шрифта
        Font font = new Font();
        try {
            font.loadFromFile(Paths.get("ComicSansMS.ttf"));
        } catch (IOException _) {

        }

        TexturePack textures = null;
        try {
            textures = new TexturePack();
        } catch (Exception _) {

        }
        drawer = new RenderEngine(player, textures, font);

        entities.add(new Bomber(28, 16, player));
        RenderEngine.addEntity(entities.get(entities.size() - 1));
        PhysicsEngine.addEntity(entities.get(entities.size() - 1));
    }

    public void run(RenderWindow window) {
        double delta = 0;
        window.setMouseCursorVisible(false);
        while (window.isOpen()) {
            eventProcessing(window, delta);

            delta = clock.getElapsedTime().asMilliseconds();
            clock.restart();

            drawer.render(window);
            PhysicsEngine.update(delta);

            updateEntities(delta);

        }
        window.setMouseCursorVisible(true);
    }

    private void updateEntities(double delta) {
        entities.removeIf(entity -> {
            boolean removable = entity.update(delta);
            if (removable) {
                player.addScore(100);
            }
            return removable;
        });
    }

    private void eventProcessing(RenderWindow window, double delta) {
        boolean canShot = true;
        boolean canReload = true;
        boolean canChange = true;
        boolean canMove = true;

        for (Event event: window.pollEvents()){
            if (event.type == Event.Type.CLOSED) {
                window.close();
            }

            if (event.type == Event.Type.MOUSE_BUTTON_PRESSED && event.asMouseButtonEvent().button == Mouse.Button.LEFT) {
                if (canShot) {
                    player.shot();
                    canShot = false;
                }
            } else {
                canShot = true;
            }

            if (event.type == Event.Type.MOUSE_MOVED) {
                int x2 = window.getSize().x / 2;
                int y2 = window.getSize().y / 2;

                Vector2i currentMousePosition = Mouse.getPosition(window);
                int deltaX = currentMousePosition.x - x2;
                player.changeVision(deltaX * 0.1);
                Mouse.setPosition(new Vector2i(x2, y2), window);
            }


            if (canMove) {
                if (Keyboard.isKeyPressed(Keyboard.Key.W)) {
                    player.moveTo(delta, NORTH);
                }
                if (Keyboard.isKeyPressed(Keyboard.Key.S)) {
                    player.moveTo(delta, SOUTH);
                }
                if (Keyboard.isKeyPressed(Keyboard.Key.A)) {
                    player.moveTo(delta, WEST);
                }
                if (Keyboard.isKeyPressed(Keyboard.Key.D)) {
                    player.moveTo(delta, EAST);
                }
                canMove = false;
            }

            if (Keyboard.isKeyPressed(Keyboard.Key.LSHIFT)) {
                if (canReload) {
                    player.getActiveWeapon().reload();
                    canReload = false;
                }
            } else {
                canReload = true;
            }

            if (Keyboard.isKeyPressed(Keyboard.Key.LCONTROL)) {
                if (canChange) {
                    player.changeActiveWeapon();
                    canChange = false;
                }
            } else {
                canChange = true;
            }


        }

    }
}