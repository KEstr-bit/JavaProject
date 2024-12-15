package DOM;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;


public class Game {
    public Player player;             // Игрок
    public TexturePack texture_pack;     //хранилище текстур

    Vector<Entity> entities = new Vector<Entity>();

    public Game()
    {
        player = new Player(7, 1);

        try
        {
            texture_pack = new TexturePack();
        }
        catch (Exception e)
        {
            texture_pack = new TexturePack(1);
        }

        entities.add(new Necromancer(2, 7, player));
        entities.add(new Bomber(1, 5, player));
        entities.add(new Archer(1, 2, player));
    }

    private void allEntityMovement(GameMap map)
    {
        Vector<Entity> newEntities = new Vector<>();

        // если entity больше не может двигаться
        // удаляем entity
        entities.removeIf(entity -> entity.update(map, newEntities));

        player.update(map, newEntities);
        entities.addAll(newEntities);
    }


    public Entity getEntityByIndex(int index)
    {
        return entities.get(index);
    }

    public void playerShot()
    {
        player.shot(entities);
    }

    public int getCountEntity()
    {
        return entities.size();
    }

    public void interaction(GameMap map) {
        this.allEntityMovement(map);                        //движение всех лбъектов

        QuadTree<Entity> quadTree = new QuadTree<Entity>(0, 0, 0, GameMap.MAP_SIZE_X, GameMap.MAP_SIZE_Y);

        for (Entity it : entities) {
            quadTree.insert(it);
        }

        quadTree.insert(player);

        for (Entity it : entities) {
            if (!it.isAlive())
                continue;

            Vector<Entity> potentialCollisions = quadTree.retrieve(it);

            for (Entity potentialCollision : potentialCollisions) {

                if (!potentialCollision.isAlive())
                    continue;

                if (it == potentialCollision)
                    continue;

                if (!it.intersects(potentialCollision, 0.7f))
                    continue;

                if (it instanceof Bullet b) {
                    // Безопасное приведение к типу Bullet
                    if (b.isFriendly() != potentialCollision.isFriendly()) {
                        potentialCollision.dealDamage(b.getDamage());
                        b.kill();
                        break; // Завершить цикл после обработки столкновения
                    }
                }

                if (it instanceof Enemy e) {

                    Cords collisionCords;
                    collisionCords = potentialCollision.getCords();

                    Cords enemyCords;
                    enemyCords = e.getCords();

                    double distance = Utils.calcDistance(enemyCords.getX(), enemyCords.getY(), collisionCords.getX(), collisionCords.getY());
                    double angle = Utils.radToDeg(Math.acos((enemyCords.getX() - collisionCords.getX()) / distance));

                    if ((enemyCords.getY() - collisionCords.getY()) / distance < 0)
                        angle *= -1;

                    e.directionStep(map, angle);

                }
            }
        }
    }
}