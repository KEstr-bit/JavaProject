package DOM;
import org.jsfml.graphics.Font;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse;
import org.jsfml.system.Clock;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

import static DOM.Final.Finals.LOOSE;
import static DOM.Final.Finals.WIN;
import static DOM.Player.Directions.*;
import static org.jsfml.window.Mouse.Button.LEFT;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.event.*;


class Game {
    public enum Entities {
        ARCHER,
        BOMBER,
        NECROMANCER,
        PLAYER
    }

    private final Player player;
    private final Entity boss;
    private final RenderEngine renderer;
    private final GameMenu menu;
    private final Clock clock;
    private final GameState gameStatus;
    private final List<Entity> entities;

    public Game() {
        clock = new Clock();
        gameStatus = new GameState();
        menu = new GameMenu(gameStatus);
        entities = new ArrayList<>();

        player = new Player(31, 16);
        entities.add(player);
        RenderEngine.addEntity(player);
        PhysicsEngine.addEntity(player);

        boss = new Necromancer(6, 16, player);
        entities.add(boss);
        RenderEngine.addEntity(boss);
        PhysicsEngine.addEntity(boss);

        createEntity(Entities.BOMBER, 27, 10);
        createEntity(Entities.BOMBER, 25, 10);
        createEntity(Entities.BOMBER, 27, 22);
        createEntity(Entities.BOMBER, 25, 22);
        createEntity(Entities.ARCHER, 22, 8);
        createEntity(Entities.ARCHER, 23, 9);
        createEntity(Entities.ARCHER, 22, 24);
        createEntity(Entities.ARCHER, 23, 23);
        createEntity(Entities.BOMBER, 19, 12);
        createEntity(Entities.BOMBER, 19, 15);
        createEntity(Entities.BOMBER, 19, 18);
        createEntity(Entities.BOMBER, 19, 20);
        createEntity(Entities.BOMBER, 17, 12);
        createEntity(Entities.BOMBER, 17, 15);

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
        renderer = new RenderEngine(player, textures, font);
    }

    public void run(RenderWindow window) {
        float delta = 0;
        window.setMouseCursorVisible(false);
        while (window.isOpen() && !gameStatus.isGameOver()) {
            eventProcessing(window, delta);
            delta = clock.getElapsedTime().asMilliseconds();
            clock.restart();
            renderer.render(window);
            PhysicsEngine.update(delta);
            updateEntities(delta);

            if (!player.isAlive()) {
                Final finalScreen = new Final();
                finalScreen.run(window, LOOSE, player.getScore());
                window.setMouseCursorVisible(true);
                return;
            }
            if (!boss.isAlive()) {
                Final finalScreen = new Final();
                finalScreen.run(window, WIN, player.getScore());
                window.setMouseCursorVisible(true);
                return;
            }
        }
        window.setMouseCursorVisible(true);
    }

    private void createEntity(Entities entity, double x, double y) {
        switch (entity) {
            case ARCHER:
                entities.add(new Archer(x, y, player));
                break;
            case BOMBER:
                entities.add(new Bomber(x, y, player));
                break;
            case NECROMANCER:
                entities.add(new Necromancer(x, y, player));
                break;
            case PLAYER:
                entities.add(new Player(x, y));
                break;
        }

        RenderEngine.addEntity(entities.getLast());
        PhysicsEngine.addEntity(entities.getLast());
    }

    private void updateEntities(float delta) {
        for (int i = entities.size() - 1; i >= 0; i--) {
            Entity entity = entities.get(i);
            // Если entity больше не может двигаться
            if (entity.update(delta)) {
                entities.remove(i);
                player.addScore(100);
            }
        }
    }

    private void eventProcessing(RenderWindow window, float delta) {
        boolean canShot = true;
        boolean canReload = true;
        boolean canChange = true;
        boolean canMove = true;


        for (Event event: window.pollEvents()) {
            if (event.type == Event.Type.CLOSED) {
                window.close();
            }

            if (event.type == Event.Type.MOUSE_BUTTON_PRESSED && event.asMouseButtonEvent().button == LEFT) {
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
                player.changeVision(deltaX * 0.1f);
                Mouse.setPosition(new Vector2i(x2, y2), window);
            }

            if (event.type == Event.Type.KEY_PRESSED && event.asKeyEvent().key == Keyboard.Key.ESCAPE) {
                menu.run(window);
                int x2 = window.getSize().x / 2;
                int y2 = window.getSize().y / 2;
                Mouse.setPosition(new Vector2i(x2, y2), window);
                clock.restart();
                window.setMouseCursorVisible(false);
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
                    player.getActiveGun().reload();
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

        canMove = true;
    }
}