package DOM;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PhysicsEngine {
    private static final List<Entity> entities = new ArrayList<>();

    // Prevent instantiation and copying
    private PhysicsEngine() { }

    public static void terminate() {
        entities.clear();
    }

    public static void addEntity(Entity entity) {
        entities.add(entity);
    }

    public static void update(double delta) {
        QuadTree<Entity> quadTree = new QuadTree<>(0,0, 0, GameMap.MAP_SIZE_X, GameMap.MAP_SIZE_Y);

        for (Iterator<Entity> it = entities.iterator(); it.hasNext(); ) {
            Entity entity = it.next();
            if (entity.isAlive()) {
                quadTree.insert(entity);
            } else {
                it.remove();
            }
        }

        for (Entity entity : entities) {
            if (!entity.isAlive()) {
                continue;
            }

            List<Entity> potentialCollisions = quadTree.retrieve(entity);

            for (Entity potentialCollision : potentialCollisions) {
                if (entity == potentialCollision) {
                    continue;
                }

                if (!entity.intersects(potentialCollision, 0.7f)) {
                    continue;
                }

                if (entity instanceof Bullet) {
                    if (!potentialCollision.isAlive()) {
                        continue;
                    }

                    if (entity.isFriendly() == potentialCollision.isFriendly()) {
                        continue;
                    }

                    entity.takeDamage(potentialCollision.getDamage());
                    potentialCollision.takeDamage(entity.getDamage());
                    break;
                }

                if (potentialCollision instanceof Bullet) {
                    continue;
                }

                double[] colCords = new double[2];
                potentialCollision.getCords(colCords);

                double[] cords = new double[2];
                entity.getCords(cords);

                double distance = Helper.calcDistance(cords[0], cords[1], colCords[0], colCords[1]);
                double angle = Math.toDegrees(Math.acos((cords[0] - colCords[0]) / distance));

                if ((cords[1] - colCords[1]) / distance < 0) {
                    angle *= -1;
                }

                entity.pushAt(delta, angle);
            }
        }
    }
}