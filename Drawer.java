package DOM;

public class Drawer {
    private char[][] firstBuffer;
    private char[][] secondBuffer;

    public Drawer() {
        firstBuffer = new char[10][10];
        secondBuffer = new char[10][10];
    }

    // Деструктор не нужен в Java

    public int draw(char[][] world_Map, Game gm) {
        int[] EntityCoordXY = new int[2];

//копирование карты в динамический массив
        for (int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                firstBuffer[i][j] = world_Map[i][j];


//если игрок живой
        if (!gm.you.getEntityCoord(EntityCoordXY))
        {
            CardinalDirections rotPlayer;
            rotPlayer = gm.you.getPlayerDirection();

            switch (rotPlayer)
            {
                case North: firstBuffer[EntityCoordXY[0]][EntityCoordXY[1]] = 'N'; break;
                case East: firstBuffer[EntityCoordXY[0]][EntityCoordXY[1]] = 'E'; break;
                case South: firstBuffer[EntityCoordXY[0]][EntityCoordXY[1]] = 'S'; break;
                case West: firstBuffer[EntityCoordXY[0]][EntityCoordXY[1]] = 'W'; break;
            }
        }

        if (!gm.monster.getEntityCoord(EntityCoordXY))
        {
            firstBuffer[EntityCoordXY[0]][EntityCoordXY[1]] = 'M';
        }

//отображение пуль
        for (int i = 0; i < 10; i++)
        {
            if(gm.you.firstGun.bullets[i] != null) {
                if (gm.you.firstGun.bullets[i].active) {
                    gm.you.firstGun.bullets[i].getEntityCoord(EntityCoordXY);
                    firstBuffer[EntityCoordXY[0]][EntityCoordXY[1]] = '0';
                }
            }
        }

        for (int i = 0; i < 10; i++)
        {
            if(gm.you.secondGun.bullets[i] != null) {
            if (gm.you.secondGun.bullets[i].active)
            {
                gm.you.secondGun.bullets[i].getEntityCoord(EntityCoordXY);
                firstBuffer[EntityCoordXY[0]][EntityCoordXY[1]] = '0';
            }
            }
        }

        Utils.setCursor(0, 0);
//вывод новой карты
        for (int i = 0; i < 10; i++)
        {
            for (int j = 0; j < 10; j++)
            {
                if (true)
                {

                    System.out.print(firstBuffer[i][j] + " ");
                }
            }
            System.out.println();
        }

        for (int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                secondBuffer[i][j] = firstBuffer[i][j];
        return 0;
    }
}
