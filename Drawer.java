package DOM;
import org.jsfml.graphics.*;

import java.util.Vector;

import static DOM.TextureType.WALLS;

public class Drawer {

    public static final double RAY_STEP = 0.01;			//шаг луча
    public static final int SCREEN_WIDTH = 640;	//ширина экрана
    public static final int SCREEN_HEIGHT = 360;	//высота экрана

    private final double[][] lengthsAndIndexes = new double[SCREEN_WIDTH][2];

    public Drawer()
    {
    }

   private void drawVerticalSegment(RenderWindow window, float length, float stripIndex, float x, float y, Texture texture) {
       Sprite stripeSprite = new Sprite(texture);

       // Устанавливаем текстуру только для нужной полосы шириной в 1 пиксель
        IntRect textureRect = new IntRect((int) stripIndex, 0, 1, (texture).getSize().y); // 1 пиксель шириной, вся высота текстуры
       stripeSprite.setTextureRect(textureRect);

       // Устанавливаем позицию спрайта
       stripeSprite.setPosition(x, y);

       // Масштабируем спрайт по высоте, чтобы сделать полоску нужной длины
       stripeSprite.setScale(1.0F, length / (texture).getSize().y);

       // Рисуем полоску в окне
       window.draw(stripeSprite);
    }

    private void drawImage(RenderWindow window, Texture texture, float x, float y, float textureX, float textureY, float width, float height) {
        // Создаем спрайт и устанавливаем текстуру
        Sprite sprite = new Sprite(texture);

        IntRect textureRect = new IntRect(Math.round(textureX) * 128, (int) (textureY * 128), 128, 128);
        sprite.setTextureRect(textureRect);

        // Устанавливаем позицию спрайта
        sprite.setPosition(x, y);

        // Рассчитываем масштаб для изменения размера
        float scaleX = width / 128;  // Масштаб по оси X
        float scaleY = height / 128; // Масштаб по оси Y
        sprite.setScale(scaleX, scaleY);                // Применяем масштаб

        // Рисуем спрайт в окне
        window.draw(sprite);
    }

    public void entityDraw(Game gm, RenderWindow window) throws IllegalAccessException {
        Cords entityCords;
        Cords playerCords;

        int countEntities = gm.getCountEntity();
        if (countEntities < 1)
            throw new IndexOutOfBoundsException("В игре не осталось entity");

        playerCords = gm.player.getCords();
        if (!gm.player.isAlive())
            throw new IllegalAccessException("Игрок погиб");

        Vector<Double> distToEntity = new Vector<>();       //вектор расстояний до объектов
        Vector<Entity> pointersEntity = new Vector<>();    //вектор указателей на объекты

//заполнение векторов distToEntity и pointersEntity
        for (int i = 0; i < countEntities; i++) {
            Entity e = gm.getEntityByIndex(i);
            entityCords = e.getCords();
            distToEntity.add(Helper.calcDistance(entityCords.getX(), entityCords.getY(), playerCords.getX(), playerCords.getY()));
            pointersEntity.add(e);
        }

//сортировка по убыванию расстояний
        Helper.dependSorting(distToEntity, pointersEntity, 0, distToEntity.size() - 1);

        double playerAngle = gm.player.getAngle();

        for (int i = 0; i < countEntities; i++) {
            double distance = distToEntity.get(i);
            Entity e = pointersEntity.get(i);

            entityCords = e.getCords();

            double spriteSize = e.size;

            double cosPlEnLine = (entityCords.getX() - playerCords.getX()) / distance;
            double sinPlEnLine = (entityCords.getY() - playerCords.getY()) / distance;

            double rotAngle = Helper.getRotationAngle(Helper.degToRad(playerAngle), cosPlEnLine, sinPlEnLine);
            if (Math.abs(rotAngle) > Player.FOV)
                continue;

            int vertLineNum = (int) (Math.round(SCREEN_WIDTH * rotAngle / Player.FOV));       //номер вертикальной полосы

            vertLineNum += SCREEN_WIDTH / 2;

            spriteSize *= SCREEN_HEIGHT / distance;

            float animation;
            int frame;
            animation = e.getAnimation().ordinal();
            frame = e.getFrame();
            //отрисовка объекта
            if (e.size > 1) {
                drawImage(window, gm.texture_pack.getTexture(e.texture), (float) (vertLineNum - spriteSize / 2),
                        (float) (SCREEN_HEIGHT / 2.0 - spriteSize + SCREEN_HEIGHT / (2 * distance)), (float) frame, animation, (float) spriteSize, (float) spriteSize);
            } else {

                drawImage(window, gm.texture_pack.getTexture(e.texture), (float) (vertLineNum - spriteSize / 2),
                        (float) ((SCREEN_HEIGHT - spriteSize) / 2), (float) frame, animation, (float) spriteSize, (float) spriteSize);
            }
            vertLineNum -= (int) (spriteSize / 2);
            int rightBorder = (int) (vertLineNum + spriteSize + 2);

            if (vertLineNum < 0)
                vertLineNum = 0;

            if (rightBorder > SCREEN_WIDTH)
                rightBorder = SCREEN_WIDTH;

            //отрисовка вертикальных полос, перекрывающих объект
            for (; vertLineNum < rightBorder; vertLineNum++) {
                //если вертикальная полоса находится позади объекта
                if (lengthsAndIndexes[vertLineNum][0] > distance)
                    continue;

                double len = SCREEN_HEIGHT / lengthsAndIndexes[vertLineNum][0];


                drawVerticalSegment(window, (float) len, (float) lengthsAndIndexes[vertLineNum][1], (float) vertLineNum, (float) ((SCREEN_HEIGHT - len) / 2), gm.texture_pack.getTexture(WALLS));
            }


        }
    }

    public void drawWalls(GameMap map, Game gm, RenderWindow window) {
        Cords entityCords;

        entityCords = gm.player.getCords();
        if (gm.player.isAlive())
        {
            double realPlayerAngle = gm.player.getAngle();
            double currentPlayerAngle = realPlayerAngle;

            currentPlayerAngle += Player.FOV / 2.0;

            for (int i = 0; i < SCREEN_WIDTH; i++) {
                //флаг найденной стены в этом направлении
                boolean flNotWall = true;

                double currentCosines = Math.cos(Helper.degToRad(currentPlayerAngle));
                double currentSinus = Math.sin(Helper.degToRad(currentPlayerAngle));


                //поиск стены на пути луча
                for (double distance = RAY_STEP; distance < 10 && flNotWall; distance += RAY_STEP) {
                    double x = distance * currentCosines;
                    double y = distance * currentSinus;

                    x += entityCords.getX();
                    y += entityCords.getY();

                    //если стена
                    if (map.isWall(x, y)) {
                        //исправление эффекта рыбьего глаза по оси Y
                        distance = distance * Math.cos(Helper.degToRad(currentPlayerAngle - realPlayerAngle));


                 double len = SCREEN_HEIGHT / distance;

                        //цвет полосы
                 float dx = (float) (x - Math.round (x) + 0.5);
                 float dy = (float) (y - Math.round (y) + 0.5);
                        float stripIndex = (dx + dy) * 128;
                        if (stripIndex > 128)
                            stripIndex -= 128;

                        stripIndex += map.whatIsWall((int) x, (int) y) * 128;

                        lengthsAndIndexes[i][0] = distance;
                        lengthsAndIndexes[i][1] = stripIndex;


                        drawVerticalSegment(window, (float) len, stripIndex, (float) i, (float) ((SCREEN_HEIGHT - len) / 2), gm.texture_pack.getTexture(WALLS));

                        flNotWall = false;
                    }

                }
                //исправление эффекта рыбьего глаза по оси X
                currentPlayerAngle = Math.atan(Math.tan(Helper.degToRad (currentPlayerAngle - realPlayerAngle)) -(2 * Math.tan(Helper.degToRad (Player.FOV * 0.5)) /SCREEN_WIDTH));
                currentPlayerAngle = Helper.radToDeg (currentPlayerAngle);
                currentPlayerAngle += realPlayerAngle;
            }
        }
    }

    public void drawPlayerWeapon(Game gm, RenderWindow window)
    {
        float animation;
        int frame;
        animation = gm.player.getActiveWeapon().getAnimation().ordinal();
        frame = gm.player.getActiveWeapon().getFrame();
        drawImage(window, gm.texture_pack.getTexture(gm.player.getActiveWeapon().texture), (float) (SCREEN_WIDTH - SCREEN_HEIGHT),
            (float) SCREEN_HEIGHT / 3, (float) frame, animation, (float) (SCREEN_HEIGHT/1.5), (float) (SCREEN_HEIGHT/1.5));
    }
}