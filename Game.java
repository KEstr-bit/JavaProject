package DOM;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;


public class Game {
    public Player you;             // Игрок
    public TexturePack tPack;     //хранилище текстур

    Map<Integer, Entity> entities;

    public Game() {
        you = new Player();
        entities = new HashMap<>();

        try {
            tPack = new TexturePack();
        } catch (Exception _) {
            tPack = new TexturePack(1);
        }

        entities.put(Entity.lastID, new Enemy());
        entities.put(Entity.lastID, new Enemy(1, 5, 0.01, 100, 50));
    }

    public void allEntityMovment(GameMap map)
    {
        double[] playerCoordXY = new double[2];
        this.you.getEntityCoord(playerCoordXY);

        Iterator<Map.Entry<Integer, Entity>> iterator = entities.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Entity> entry = iterator.next();
            Entity entity = entry.getValue();

            if (entity.entityMovement(map, playerCoordXY[0], playerCoordXY[1])) {
                // Удаление сущности из Map
                iterator.remove(); // Используем метод iterator.remove() для корректного удаления
            }
        }
    }

    // Взаимодействие объектов
    public void interaction(GameMap map)
    {
        this.allEntityMovment(map);                    //движение всех лбъектов

        int[] monsterCoordXY = new int[2];                               //координаты врагов
        int[][] monstersMap = new int[GameMap.MAPSIZEX][GameMap.MAPSIZEY];  //карта id врагов

        for (int i = 0; i < GameMap.MAPSIZEX; i++) {
            for (int j = 0; j < GameMap.MAPSIZEY; j++) {
                monstersMap[i][j] = -1;
            }
        }

        // Запись координат врагов на карту
        for (Map.Entry<Integer, Entity> entry : entities.entrySet()) {
            if (entry.getValue() instanceof Enemy e) {

                e.getEntityCoord(monsterCoordXY);
                monstersMap[monsterCoordXY[0]][monsterCoordXY[1]] = entry.getKey();
            }
        }


        int[] playerCoordXY = new int[2];
        int id = -1;

        //проверка столкновения игрока с врагом
        if (!this.you.getEntityCoord(playerCoordXY))
            id = monstersMap[playerCoordXY[0]][playerCoordXY[1]];

        if (id != -1)
        {
            Entity e = entities.get(id);
            this.you.attackEntity(e.getEntityDamage());
        }

        //перебор пуль
        for (Map.Entry<Integer, Entity> entry : entities.entrySet()) {
            if (entry.getValue() instanceof Bullet b) {
                int[] bulletCoordXY = new int[2];

                b.getEntityCoord(bulletCoordXY);

                //провекра столкновения пули с врагом
                try {
                    id = monstersMap[bulletCoordXY[0]][bulletCoordXY[1]];
                } catch (ArrayIndexOutOfBoundsException ex) {
                    id = -1;
                }

                if (id == -1)
                    continue;

                //поиск врага по id
                Entity e = entities.get(id);

                //нанесение урона врагу
                e.attackEntity(b.getEntityDamage());

                b.setRemLen(0);
                break;
            }

        }

    }

    public int getCountEntity() {
        return  entities.size();
    }

    public void playerShot()
    {
        you.shot(entities);
    }

    public Entity getEntityByIndex(int index) {
        if (index < 0 || index >= entities.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        Iterator<Entity> iterator = entities.values().iterator();
        Entity entity = null;
        for (int i = 0; i <= index; i++) {
            if (iterator.hasNext()) {
                entity = iterator.next();
            } else {
                throw new IndexOutOfBoundsException("Index out of bounds");
            }
        }
        return entity;
    }
}