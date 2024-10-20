package DOM;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Drawer extends JFrame {
    private char[][] firstBuffer;

    private int cellSize = 30; // Размер ячейки

    public Drawer(char[][] map) {
        this.firstBuffer = new char[10][10];

        for(int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                firstBuffer[i][j] = map[i][j];
            }
        }


        setTitle("Map Visualizer");
        setSize(map[0].length * cellSize, map.length * cellSize);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // По центру экрана

        JPanel mapPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawMap(g);
            }
        };


        add(mapPanel);

        // Показ окна
        setVisible(true);
    }

    private void drawMap(Graphics g) {
        for (int row = 0; row < firstBuffer.length; row++) {
            for (int col = 0; col < firstBuffer[row].length; col++) {
                // Выбор цвета в зависимости от значения в массиве
                Color color = switch (firstBuffer[row][col]) {
                    case '#' -> Color.BLACK;
                    case '0' -> Color.BLUE;
                    case 'M' -> Color.RED;
                    case 'N' -> Color.pink;
                    case 'W' -> Color.yellow;
                    case 'E' -> Color.green;
                    case 'S' -> Color.lightGray;
                    default -> Color.WHITE;
                };

                // Рисование прямоугольника
                g.setColor(color);
                g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);

            }
        }
    }

    // Деструктор не нужен в Java

    public void updateMap(char[][] world_Map, Game gm) {
        int[] EntityCoordXY = new int[2];

//копирование карты в динамический массив
        for(int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                firstBuffer[i][j] = world_Map[i][j];
            }
        }



//если игрок живой
        if (!gm.you.getEntityCoord(EntityCoordXY)) {
            CardinalDirections rotPlayer;
            rotPlayer = gm.you.getPlayerDirection();

            switch (rotPlayer) {
                case North:
                    firstBuffer[EntityCoordXY[0]][EntityCoordXY[1]] = 'N';
                    break;
                case East:
                    firstBuffer[EntityCoordXY[0]][EntityCoordXY[1]] = 'E';
                    break;
                case South:
                    firstBuffer[EntityCoordXY[0]][EntityCoordXY[1]] = 'S';
                    break;
                case West:
                    firstBuffer[EntityCoordXY[0]][EntityCoordXY[1]] = 'W';
                    break;
            }
        }

        if (!gm.monster.getEntityCoord(EntityCoordXY)) {
            firstBuffer[EntityCoordXY[0]][EntityCoordXY[1]] = 'M';
        }

//отображение пуль
        for (int i = 0; i < gm.you.firstGun.bulletCount; i++) {
            if (gm.you.firstGun.bullets[i].active) {
                gm.you.firstGun.bullets[i].getEntityCoord(EntityCoordXY);
                firstBuffer[EntityCoordXY[0]][EntityCoordXY[1]] = '0';
            }

        }

        for (int i = 0; i < gm.you.firstGun.bulletCount; i++) {
            if (gm.you.secondGun.bullets[i].active) {
                gm.you.secondGun.bullets[i].getEntityCoord(EntityCoordXY);
                firstBuffer[EntityCoordXY[0]][EntityCoordXY[1]] = '0';

            }
        }

        repaint();


    }

}
