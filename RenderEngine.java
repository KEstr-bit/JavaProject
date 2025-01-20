package DOM;

import org.jsfml.graphics.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import static DOM.Constants.*;
import static DOM.TexturePack.TextureType.*;
import static org.jsfml.graphics.Color.WHITE;

public class RenderEngine {
    public static final int MAX_RENDER_DISTANCE = 10;
    public static final int QUALITY = 1;

    private static final List<Entity> entities = new ArrayList<>();
    private final Font font;
    private final TexturePack textures;
    private final Player player;
    private final double[] distances = new double[SCREEN_WIDTH];      // Массив длин вертикальных полос
    private final int[] indexes = new int[SCREEN_WIDTH];
    private final int[] pixelData = new int[SCREEN_HEIGHT * SCREEN_WIDTH];

    private final double[] playerCords = new double[2];
    private double dirX;
    private double dirY;
    private double planeX;
    private double planeY;

    public RenderEngine(Player player, TexturePack textures, Font font) {
        this.player = player;
        this.textures = textures;
        this.font = font;

        for (int i = 0; i < distances.length; i++) {
            distances[i] = MAX_RENDER_DISTANCE;
            indexes[i] = 0;
        }

        Arrays.fill(pixelData, 0);
    }

    public static void addEntity(Entity entity) {
        entities.add(entity);
    }

    public static void terminate() {
        entities.clear();
    }

    public void render(RenderWindow window) {
        double RAD_FOV_2 = Helper.degToRad(player.getFov() / 2.0);
        double playerAngle = Helper.degToRad(player.getAngle());
        player.getCords(playerCords);
        dirX = Math.cos(playerAngle);
        dirY = Math.sin(playerAngle);
        planeX = Math.tan(RAD_FOV_2) * Math.cos(playerAngle - Math.PI / 2);
        planeY = Math.tan(RAD_FOV_2) * Math.sin(playerAngle - Math.PI / 2);

        Drawer.drawImage(window, textures.getTexture(SKY), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        drawFloor(window);
        drawWalls(window);
        drawEntities(window);
        drawInterface(window);
        window.display();
    }

    private void drawWalls(RenderWindow window) {
        for (int x = 0; x < SCREEN_WIDTH; x++) {
            double cameraX = 2.0 * x / SCREEN_WIDTH - 1;
            double currentCos = dirX + planeX * cameraX;
            double currentSin = dirY + planeY * cameraX;

            int mapX = (int) Math.round(playerCords[0]);
            int mapY = (int) Math.round(playerCords[1]);
            int side = 0;

            int stepX = currentCos < 0 ? -1 : 1;
            int stepY = currentSin < 0 ? -1 : 1;

            double ratioA = currentSin;
            double ratioB = -currentCos;
            double ratioC = playerCords[1] * (playerCords[0] + currentCos) - playerCords[0] * (playerCords[1] + currentSin);
            double denominator = Math.sqrt(ratioA * ratioA + ratioB * ratioB);

            boolean notWall = true;
            while (notWall) {
                double distanceX = Math.abs((ratioA * (mapX + stepX) + ratioB * mapY + ratioC) / denominator);
                double distanceY = Math.abs((ratioA * mapX + ratioB * (mapY + stepY) + ratioC) / denominator);

                if (distanceX < distanceY) {
                    mapX += stepX;
                    side = 0;
                } else {
                    mapY += stepY;
                    side = 1;
                }

                if (GameMap.isWall(mapX, mapY)) notWall = false;
            }

            double distance;
            double hitX;
            double hitY;

            if (side == 1) {
                double boundY = (stepY > 0) ? mapY - 0.5 : mapY + 0.5;

                distance = (boundY - playerCords[1]) / currentSin;
                hitY = boundY;
                hitX = playerCords[0] + distance * currentCos;
            } else {
                double boundX = (stepX > 0) ? mapX - 0.5 : mapX + 0.5;

                distance = (boundX - playerCords[0]) / currentCos;
                hitX = boundX;
                hitY =playerCords[1] + distance * currentSin;
            }

            int stripIndex = 0;
            if (distance < MAX_RENDER_DISTANCE) {
                double dx = hitX - Math.round(hitX) + 0.5;
                double dy = hitY - Math.round(hitY) + 0.5;

                stripIndex = (int) ((dx + dy) * 128);
                if (stripIndex > 128) stripIndex -= 128;
                stripIndex += GameMap.whatIsWall(mapX, mapY) * 128;
            }

            float len = (float) (SCREEN_HEIGHT / distance);
            int y = (SCREEN_HEIGHT - (int) len) / 2;

            drawVerticalSegment(window, len, stripIndex, x, y);
            indexes[x] = stripIndex;
            distances[x] = distance;
        }
    }

    private void drawVerticalSegment(RenderWindow window, float length, int stripIndex, float x, float y) {
        double black = 255 * MAX_RENDER_DISTANCE * length / SCREEN_HEIGHT - 255;

        if (black > 255) black = 255;
        if (black < 0) black = 0;

        int shading = (int) black;
        Color color = new Color(shading, shading, shading, 255);

        Drawer.drawImage(window, textures.getTexture(WALLS), x, y, 1, length, stripIndex, 0, 1,
                TexturePack.SIZE_WALL_TEXT, color);
    }

    private void drawEntities(RenderWindow window) {
        double[] entityCords = new double[2];

        if (entities.isEmpty()) return;

        Vector<Double> distToEntity = new Vector<>();
        Vector<Entity> pointersEntity = new Vector<>();

        for (var it = entities.iterator(); it.hasNext(); ) {
            Entity entity = it.next();
            if (!entity.isAlive() && !entity.isVisible()) {
                it.remove();
            } else {
                entity.getCords(entityCords);
                double dist = Helper.calcDistance(entityCords[0], entityCords[1],playerCords[0], playerCords[1]);

                if (dist < MAX_RENDER_DISTANCE) {
                    distToEntity.add(dist);
                    pointersEntity.add(entity);
                }
            }
        }

        if (distToEntity.isEmpty()) return;

        Helper.dependSorting(distToEntity, pointersEntity, 0, distToEntity.size() - 1);

        int countEntities = distToEntity.size();
        for (int i = 0; i < countEntities; i++) {
            double distance = distToEntity.get(i);
            Entity e = pointersEntity.get(i);

            e.getCords(entityCords);

            double spriteX = entityCords[0] - playerCords[0];
            double spriteY = entityCords[1] -playerCords[1];

            double transformX = (dirY * spriteX - dirX * spriteY);
            double transformY = (-planeY * spriteX + planeX * spriteY);

            if (transformY <= 0) continue;

            int vertLineNum = (int) (SCREEN_WIDTH_2 * (1 + transformX / transformY));
            float spriteSize = (float) (e.getSize() * SCREEN_HEIGHT / transformY);

            AnimationControl.Animations animation;
            int frame;
            animation = e.getAnimation();
            frame = e.getFrame();

            int shading = (int) (255 * MAX_RENDER_DISTANCE / distance - 255);
            if (shading > 255) shading = 255;
            if (shading < 0) shading = 0;

            Color color = new Color(255, 255, 255, shading);

            if (e.getSize() > 1) {
                Drawer.drawImage(window,
                        textures.getTexture(e.getTexture()),
                        vertLineNum - spriteSize / 2,
                        (float) (SCREEN_HEIGHT_2 - spriteSize + SCREEN_HEIGHT_2 / distance),
                        spriteSize, spriteSize,
                        frame * TexturePack.SIZE_ENTITY_TEXT,
                        animation.ordinal() * TexturePack.SIZE_ENTITY_TEXT,
                        TexturePack.SIZE_ENTITY_TEXT,
                        TexturePack.SIZE_ENTITY_TEXT,
                        color);
            } else {
                Drawer.drawImage(window,
                        textures.getTexture(e.getTexture()),
                        vertLineNum - spriteSize / 2,
                        SCREEN_HEIGHT_2 - spriteSize / 2,
                        spriteSize, spriteSize,
                        frame * TexturePack.SIZE_ENTITY_TEXT,
                        animation.ordinal() * TexturePack.SIZE_ENTITY_TEXT,
                        TexturePack.SIZE_ENTITY_TEXT,
                        TexturePack.SIZE_ENTITY_TEXT,
                        color);
            }

            vertLineNum -= (int) (spriteSize / 2);
            int rightBorder = (int) (vertLineNum + spriteSize + 2);
            if (vertLineNum < 0) vertLineNum = 0;
            if (rightBorder > SCREEN_WIDTH) rightBorder = SCREEN_WIDTH;

            for (; vertLineNum < rightBorder; vertLineNum++) {
                if (distances[vertLineNum] > distance) continue;

                float len = (float) (SCREEN_HEIGHT / distances[vertLineNum]);
                drawVerticalSegment(window, len, indexes[vertLineNum], vertLineNum, (float) (SCREEN_HEIGHT - (int) len) / 2);
            }
        }
    }

    private void drawInterface(RenderWindow window) {
        float sizeX = SCREEN_WIDTH / 10.0f;
        float sizeY = SCREEN_HEIGHT / 36.0f;
        float centerY = SCREEN_HEIGHT / 1.04f;
        float centerXScoreMessage = SCREEN_WIDTH / 18.28f;
        float centerXHpMessage = SCREEN_WIDTH / 3.72f;
        float centerXAmmoMessage = SCREEN_WIDTH / 6.09f;
        float gunX = SCREEN_WIDTH - SCREEN_HEIGHT;
        float gunY = SCREEN_HEIGHT / 3.0f;
        float gunSize = SCREEN_HEIGHT / 1.5f;

        AnimationControl.Animations animation;
        int frame;
        animation = player.getActiveGun().getAnimation();
        frame = player.getActiveGun().getFrame();

        Drawer.drawImage(window, textures.getTexture(player.getActiveGun().getTexture()), SCREEN_WIDTH - SCREEN_HEIGHT, (float) SCREEN_HEIGHT / 3, SCREEN_HEIGHT / 1.5f, SCREEN_HEIGHT / 1.5f, frame * TexturePack.SIZE_ENTITY_TEXT, animation.ordinal() * TexturePack.SIZE_ENTITY_TEXT, TexturePack.SIZE_ENTITY_TEXT, TexturePack.SIZE_ENTITY_TEXT);

        Drawer.drawImage(window, textures.getTexture(STATS), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        String b = String.valueOf(player.getScore());
        String s = String.valueOf(player.getPercentHp()) + '%';
        String a = String.valueOf(player.getActiveGun().getAmmo()) + '/' + String.valueOf(player.getActiveGun().getMagazineCapacity());
        Drawer.drawMessage(window, b, font, WHITE, centerXScoreMessage, centerY, sizeX, sizeY, 20);
        Drawer.drawMessage(window, s, font, WHITE, centerXHpMessage, centerY, sizeX, sizeY, 20);
        Drawer.drawMessage(window, a, font, WHITE, centerXAmmoMessage, centerY, sizeX, sizeY, 20);

    }

    private void drawFloor(RenderWindow window) {
        Image image = textures.getTexture(FLOORS).copyToImage();

        for (int i = SCREEN_HEIGHT / 2 / MAX_RENDER_DISTANCE; i < SCREEN_HEIGHT / 2; i += QUALITY) {
            int y = SCREEN_HEIGHT / 2 + i;

            double shading = 10 * (i - SCREEN_HEIGHT / 20.0) / SCREEN_HEIGHT_2;
            if (shading > 1) shading = 1;

            double p = y - (double) SCREEN_HEIGHT / 2;
            double rowDistance = (double) SCREEN_HEIGHT / 2 / p;

            double floorStepX = rowDistance * (2 * planeX) / SCREEN_WIDTH;
            double floorStepY = rowDistance * (2 * planeY) / SCREEN_WIDTH;

            double floorX = playerCords[0] + rowDistance * (dirX - planeX);
            double floorY = playerCords[1] + rowDistance * (dirY - planeY);

            for (int x = 0; x < SCREEN_WIDTH; x++) {
                int cellX = (int) Math.round(floorX);
                int cellY = (int) Math.round(floorY);

                int tx = (int) (TexturePack.SIZE_FLOOR_TEXT * (floorX - cellX + 0.5)) & (TexturePack.SIZE_FLOOR_TEXT - 1);
                int ty = (int) (TexturePack.SIZE_FLOOR_TEXT * (floorY - cellY + 0.5)) & (TexturePack.SIZE_FLOOR_TEXT - 1);

                floorX += floorStepX;
                floorY += floorStepY;

                int floorTexture = GameMap.whatIsFloor(cellX, cellY);
                int ceilingTexture = GameMap.whatIsPot(cellX, cellY);

                Color floorColor = image.getPixel(floorTexture * TexturePack.SIZE_FLOOR_TEXT + tx, ty);
                Color ceilingColor = image.getPixel(ceilingTexture * TexturePack.SIZE_FLOOR_TEXT + tx, ty);

                int floorR = (int) (floorColor.r * shading);
                int floorG = (int) (floorColor.g * shading);
                int floorB = (int) (floorColor.b * shading);
                int floorA = floorColor.a;

                int byte1 = floorR & 0xFF; // младший байт a
                int byte2 = floorG & 0xFF; // младший байт b
                int byte3 = floorB & 0xFF; // младший байт c
                int byte4 = floorA & 0xFF; // младший байт d

                // Объединяем в один int
                int floorResult = (byte3) | (byte2 << 8) | (byte1 << 16) | (byte4 << 24);


                int ceilingR = (int) (ceilingColor.r * shading);
                int ceilingG = (int) (ceilingColor.g * shading);
                int ceilingB = (int) (ceilingColor.b * shading);
                int ceilingA = ceilingColor.a;

                byte1 = ceilingR & 0xFF; // младший байт a
                byte2 = ceilingG & 0xFF; // младший байт b
                byte3 = ceilingB & 0xFF; // младший байт c
                byte4 = ceilingA & 0xFF; // младший байт d

                int ceilingResult = (byte3) | (byte2 << 8) | (byte1 << 16) | (byte4 << 24);

                for (int k = 0; k < QUALITY; k++) {
                    int y1 = y + k;

                    pixelData[(y1 * SCREEN_WIDTH + x)] = floorResult; // R

                    pixelData[((SCREEN_HEIGHT - y1 - 1) * SCREEN_WIDTH + x)] = ceilingResult;
                }
            }
        }

        Image imageS = new Image();
        imageS.create(SCREEN_WIDTH, SCREEN_HEIGHT, pixelData);
        Texture texture = new  Texture();

        try {
            texture.loadFromImage(imageS);

        } catch (TextureCreationException _) {

        }

        window.draw(new Sprite(texture));
    }
}
