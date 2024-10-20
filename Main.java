package DOM;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static DOM.CardinalDirections.*;

public class Main {

    private static Game game;
    private static Map map;
    private static Drawer drawer;
    private static Final ending;

    public static void main(String[] args) {
        // Инициализация объектов
        game = new Game();
        map = new Map();

        ending = new Final();

        // Создание массива для карты
        char[][] gameMap = new char[10][10];
        map.getWorldMap(gameMap); // Заполняем карту

        drawer = new Drawer(gameMap);

        // Настройка ввода с клавиатуры
        java.awt.Frame frame = new java.awt.Frame();
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        game.you.gamePlayerStep(gameMap, 10, North);
                        break;
                    case KeyEvent.VK_DOWN:
                        game.you.gamePlayerStep(gameMap, 10, South);
                        break;
                    case KeyEvent.VK_RIGHT:
                        game.you.gamePlayerStep(gameMap, 10, East);
                        break;
                    case KeyEvent.VK_LEFT:
                        game.you.gamePlayerStep(gameMap, 10, West);
                        break;
                    case KeyEvent.VK_CONTROL: // Ctrl
                        game.you.changeActiveWeapon();
                        break;
                    case KeyEvent.VK_SHIFT: // Shift
                        game.you.shot();
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
        frame.setVisible(true); // Делаем фрейм видимым (нужно для обработки нажатий клавиш)
        frame.toFront();  // Ставим фрейм поверх других окон

        // Основной цикл игры
        boolean running = true;
        while (running) {
            // Обработка взаимодействия объектов
            game.interaction(gameMap);

            // Проверка окончания игры
            if (game.you.getEntityHitPoints() <= 0) {
                ending.changeType(EndingOption.LooseGame);
                running = false;
            }
            if (game.monster.getEntityHitPoints() <= 0) {
                ending.changeType(EndingOption.WinGame);
                running = false;
            }

            // Отрисовка состояния игры
            drawer.updateMap(gameMap, game);
            try {
                Thread.sleep(50); // Задержка для плавности
            } catch (InterruptedException _) {}
        }

        ending.outputFinal();
        // Завершение игры
        // ... (Очистка экрана, вывод финального сообщения)
    }
}

