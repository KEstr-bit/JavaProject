package DOM;

public class Map {
    private final char[][] worldMap;              //карта



    public Map(){
        worldMap = new char[10][10];

        // Заполняем карту
        char[][] preMap = {
                {'#','#','#','#','#','#','#','#','#','#'},
                {'#','.','.','.','.','.','.','.','.','#'},
                {'#','.','.','.','.','.','.','.','.','#'},
                {'#','.','.','.','.','.','.','.','.','#'},
                {'#','#','#','#','#','#','#','.','.','#'},
                {'#','.','.','.','.','.','#','.','.','#'},
                {'#','.','.','.','.','.','.','.','.','#'},
                {'#','.','.','.','#','.','.','.','.','#'},
                {'#','.','.','.','#','.','.','.','.','#'},
                {'#','#','#','#','#','#','#','#','#','#'}
        };

        // Копируем preMap в worldMap
        for (int x = 0; x < 10; x++) {
            System.arraycopy(preMap[x], 0, worldMap[x], 0, 10);
        }
    }

    public void getWorldMap(char[][] getMap) {
        if (getMap.length != 10 || getMap[0].length != 10) {
            throw new IllegalArgumentException("Размер переданного массива не совпадает с размером карты");
        }

        for (int x = 0; x < 10; x++) {
            System.arraycopy(worldMap[x], 0, getMap[x], 0, 10);
        }
    }
}