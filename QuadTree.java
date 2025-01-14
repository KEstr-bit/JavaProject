package DOM;

import java.util.Vector;

class QuadTree<T extends Entity> {
    static final int MAX_OBJECTS = 4;  // Максимальное количество объектов в узле
    static final int MAX_LEVELS = 3;    // Максимальная глубина дерева

    private final int level;  // Уровень узла
    private final Vector<T> objects;  // Объекты, хранящиеся в узле
    private final float x;
    private final float y;  // Позиция узла
    private final float width;
    private final float height;  // Размер узла
    private final QuadTree[] nodes;  // Дочерние узлы

    public QuadTree(int level, float x, float y, float width, float height)
    {
        this.level = level;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.objects = new Vector<>();
        this.nodes = new QuadTree[4];
    }

    public void clear() {
        objects.clear();
        for (int i = 0; i < 4; ++i) {
            if (nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }

    void split() {
        float subWidth = width / 2;
        float subHeight = height / 2;

        nodes[0] = new QuadTree<T>(level + 1, x, y, subWidth, subHeight); // Северо-западный
        nodes[1] = new QuadTree<T>(level + 1, x + subWidth, y, subWidth, subHeight); // Северо-восточный
        nodes[2] = new QuadTree<T>(level + 1, x, y + subHeight, subWidth, subHeight); // Юго-западный
        nodes[3] = new QuadTree<T>(level + 1, x + subWidth, y + subHeight, subWidth, subHeight); // Юго-восточный
    }

    int getIndex(T entity) {
        int index = -1;
        double verticalMidpoint = x + (width / 2);
        double horizontalMidpoint = y + (height / 2);

        double[] entityCords = new double[2];
        entity.getCords(entityCords);

        boolean topQuad = (entityCords[0] < horizontalMidpoint);
        boolean bottomQuad = (entityCords[1] > horizontalMidpoint);

        if (entityCords[0] < verticalMidpoint) {
            if (topQuad) {
                index = 0; // Северо-западный
            } else if (bottomQuad) {
                index = 2; // Юго-западный
            }
        } else {
            if (topQuad) {
                index = 1; // Северо-восточный
            } else if (bottomQuad) {
                index = 3; // Юго-восточный
            }
        }

        return index;
    }

    void insert(T entity) {
        if (nodes[0] != null) {
            int index = getIndex(entity);
            if (index != -1) {
                nodes[index].insert(entity);
                return;
            }
        }

        objects.add(entity);

        if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
            if (nodes[0] == null) {
                split();
            }

            for (int i = 0; i < objects.size(); ++i) {
                int index = getIndex(objects.get(i));
                if (index != -1) {
                    nodes[index].insert(objects.get(i));
                    objects.remove(objects.get(i));
                    --i;
                }
            }
        }
    }

    Vector<T> retrieve(T entity) {
        Vector <T> returnObjects = new Vector<>();
        int index = getIndex(entity);
        if (index != -1 && nodes[0] != null) {
            returnObjects = nodes[index].retrieve(entity);
        } else {
                returnObjects.addAll(objects);
        }
        return returnObjects;
    }
}
