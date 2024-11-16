package DOM;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

import java.util.Arrays;
import java.util.Vector;

import static java.util.Collections.swap;

public class Drawer {

    public static final double RAY_STEP = 0.01;			//шаг луча
    public static final int SCREEN_WIDTH = 800;	//ширина экрана
    public static final int SCREEN_HEIGHT = 600;	//высота экрана

    private final double[] mas = new double[SCREEN_WIDTH];

    public Drawer()
    {
        Arrays.fill(mas, 0);
    }

    public void drawVerticalSegment(RenderWindow window, float length, float width, float x, float y, Color color) {
        //вертикальный отрезок
        RectangleShape segment = new RectangleShape(new Vector2f(width, length));

        segment.setPosition(x, y);

        //цвет отрезка
        segment.setFillColor(color);

        //рисование
        window.draw(segment);
    }

    public void drawImage(RenderWindow window, final Texture texture, float x, float y, float width, float height) {
        // Создаем спрайт и устанавливаем текстуру
        Sprite sprite = new Sprite();
        sprite.setTexture(texture);

        // Устанавливаем позицию спрайта
        sprite.setPosition(x, y);

        // Рассчитываем масштаб для изменения размера
        float scaleX = width / (texture).getSize().x;  // Масштаб по оси X
        float scaleY = height / (texture).getSize().y; // Масштаб по оси Y
        sprite.setScale(scaleX, scaleY);                // Применяем масштаб

        // Рисуем спрайт в окне
        window.draw(sprite);
    }

    public void dependSorting(Vector<Double> mainMas, Vector<Entity> sideMas, int left, int right) {
        //Указатели в начало и в конец массива
        int i = left, j = right;

        //Центральный элемент массива
        double mid = mainMas.get((left + right) / 2);

        //Делим массив
        do {

            while (mainMas.get(i) > mid) {
                i++;
            }

            while (mainMas.get(j) < mid) {
                j--;
            }

            //Меняем элементы местами
            if (i <= j) {

                swap(sideMas, i, j);
                swap(mainMas, i, j);

                i++;
                j--;
            }
        } while (i <= j);


        //Рекурсивные вызовы, если осталось, что сортировать
        if (left < j)
            dependSorting(mainMas, sideMas, left, j);
        if (i < right)
            dependSorting(mainMas, sideMas, i, right);
    }

    public void entityDraw(Game gm, RenderWindow window) throws IllegalAccessException {
        double[] EntityCoordXY = new double[2];
        double[] PlayerCoordXY = new double[2];

        int countEnt = gm.getCountEntity();
        if (countEnt < 1)
            throw new IndexOutOfBoundsException("В игре не осталось entity");

        if(gm.you.getEntityCoord(PlayerCoordXY))
            throw new IllegalAccessException("Игрок погиб");

        Vector<Double> distToEntity = new Vector<>();       //вектор расстояний до объектов
        Vector<Entity> pointersEntity = new Vector<>();    //вектор указателей на объекты

        //заполнение векторов distToEntity и pointersEntity
        for (int i = 0; i < countEnt; i++)
        {
            Entity e = gm.getEntityByIndex(i);
            e.getEntityCoord(EntityCoordXY);
            distToEntity.add(Utils.calcDistance(EntityCoordXY[0], EntityCoordXY[1], PlayerCoordXY[0], PlayerCoordXY[1]));
            pointersEntity.add(e);
        }

        //сортировка по убыванию расстояний
        dependSorting(distToEntity, pointersEntity, 0, distToEntity.size() - 1);

        double playerAngle = gm.you.getEntityAngle();

        for (int i = 0; i < countEnt; i++)
        {
            double distance = distToEntity.get(i);
            Entity e = pointersEntity.get(i);

            e.getEntityCoord(EntityCoordXY);

            double spriteSize = e.getSize();

            double cosPlEnLine = (EntityCoordXY[0] - PlayerCoordXY[0]) / distance;
            double sinPlEnLine = (EntityCoordXY[1] - PlayerCoordXY[1]) / distance;

            //разность угла взгляда игрока и угла прямой, соединяющей игрока и объект
            double rotAngle = getRotAngle(playerAngle, cosPlEnLine, sinPlEnLine);
            if (Math.abs(rotAngle) > Player.FOV)
                continue;

            int vertLineNum = (int) (SCREEN_WIDTH * rotAngle / Player.FOV);       //номер вертикальной полосы

            vertLineNum += SCREEN_WIDTH / 2;

            spriteSize *= SCREEN_HEIGHT / distance;

            //отрисовка объекта
            drawImage(window, gm.tPack.getTexture(e.getTextureType()), (float) (vertLineNum - spriteSize / 2), (float) ((SCREEN_HEIGHT - spriteSize) / 2), (float) spriteSize, (float) spriteSize);

            vertLineNum -= (int) (spriteSize / 2) + 1;
            int rightBorder = (int) (vertLineNum + spriteSize + 2);

            if (vertLineNum < 0)
                vertLineNum = 0;

            if (rightBorder > SCREEN_WIDTH)
                rightBorder = SCREEN_WIDTH;

            //отрисовка вертикальных полос, перекрывающих объект
            for (; vertLineNum < rightBorder; vertLineNum++)
            {
                //если вертикальная полоса находится позади объекта
                if (mas[vertLineNum] > distance)
                    continue;

                double len = SCREEN_HEIGHT / distance;

                //вычисление цвета вертикальной полосы
                int Ws = (int) (255 / Math.sqrt(mas[vertLineNum]));
                if (Ws > 255)
                    Ws = 255;

                drawVerticalSegment(window, (float) len, 1F, (float) vertLineNum, (float) ((SCREEN_HEIGHT - len) / 2), new Color( Ws, Ws, Ws));
            }



        }
    }

    private static double getRotAngle(double playerAngle, double cosPlEnLine, double sinPlEnLine) {
        double cosRotAngle = Math.cos(Utils.degToRad(playerAngle)) * cosPlEnLine + Math.sin(Utils.degToRad(playerAngle)) * sinPlEnLine;
        double sinRotAngle = Math.sin(Utils.degToRad(playerAngle)) * cosPlEnLine - Math.cos(Utils.degToRad(playerAngle)) * sinPlEnLine;

        if (cosRotAngle > 1)
            cosRotAngle = 1;

        if (cosRotAngle < -1)
            cosRotAngle = -1;

        //угол на который игроку нужно повернуться, чтобы смотреть ровно на объект
        double rotAngle = Utils.radToDeg(Math.acos(cosRotAngle));

        if (sinRotAngle < 0)
            rotAngle *= -1;

        //если объект находится за обзором игрока
        rotAngle = Math.round(rotAngle * 1000) / 1000.0;
        return rotAngle;
    }

    public void drawWalls(GameMap map, Game gm, RenderWindow window) {
        double[] EntityCoordXY = new double[2];


        if (!gm.you.getEntityCoord(EntityCoordXY))
        {
            double realPlayerAngle = gm.you.getEntityAngle();
            double curentPlayerAngle = realPlayerAngle;

            curentPlayerAngle += Player.FOV / 2.0;

            for (int i = 0; i < SCREEN_WIDTH; i++)
            {
                //флаг найденной стены в этом направлении
                boolean flNotWall = true;

                double currentCosinus = Math.cos(Utils.degToRad(curentPlayerAngle));
                double currentSinus = Math.sin(Utils.degToRad(curentPlayerAngle));


                //поиск стены на пути луча
                for (double distance = RAY_STEP; distance < 10 && flNotWall; distance += RAY_STEP)
                {
                    double x = distance * currentCosinus;
                    double y = distance * currentSinus;

                    x += EntityCoordXY[0];
                    y += EntityCoordXY[1];

                    //если стена
                    if (map.isWall(x, y))
                    {
                        //исправление эффекта рыбьего глаза по оси Y
                        distance = distance * Math.cos(Utils.degToRad(curentPlayerAngle - realPlayerAngle));

                        mas[i] = distance;

                        double len = SCREEN_HEIGHT / distance;

                        //цвет полосы
                        int Ws = (int) (255 / Math.sqrt(distance));
                        if (Ws > 255)
                            Ws = 255;

                        drawVerticalSegment(window, (float) len, 1F, (float) i, (float) ((SCREEN_HEIGHT - len)/2), new Color(Ws, Ws, Ws));

                        flNotWall = false;
                    }

                }
                //исправление эффекта рыбьего глаза по оси X
                curentPlayerAngle = Math.atan(Math.tan(Utils.degToRad(curentPlayerAngle - realPlayerAngle)) - (2 * Math.tan(Utils.degToRad(Player.FOV * 0.5)) / SCREEN_WIDTH));
                curentPlayerAngle = Utils.radToDeg(curentPlayerAngle);
                curentPlayerAngle += realPlayerAngle;
            }
        }
    }
}