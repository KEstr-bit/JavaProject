package DOM;

import static DOM.CardinalDirections.*;

public class Enemy extends Entity{
    Enemy(double coord_X, double coord_Y, double entity_Speed, int hit_Points, int entity_Damage)
    {
        damage = entity_Damage;
        hitPoints = hit_Points;
        coordX = coord_X;
        coordY = coord_Y;
        speed = entity_Speed;
    }

    Enemy()
    {
        damage = 50;
        hitPoints = 100;
        coordX = 1;
        coordY = 8;
        speed = 0.2;
    }


    public int enemyMovment(char[][] world_Map, int map_Size_X, double player_X, double player_Y)
    {
        int[] enemyRoundXY;
        enemyRoundXY = new  int[2];

        this.getEntityCoord(enemyRoundXY);

        double deltaX = player_X - coordX;
        double deltaY = player_Y - coordY;

        //если враг живой
        if (hitPoints > 0)
        {
            if (playersVision(world_Map, map_Size_X, player_X, player_Y))
            {
                if (Math.abs(deltaX) > Math.abs(deltaY))
                {
                    if (!Utils.isWall(world_Map, map_Size_X, enemyRoundXY[0] + 1, enemyRoundXY[1]) && deltaX > 0)
                        this.entityStep(South);
                else
                    if (!Utils.isWall(world_Map, map_Size_X, enemyRoundXY[0] - 1, enemyRoundXY[1]) && deltaX < 0)
                        this.entityStep(North);

                }
                else
                {
                    if (!Utils.isWall(world_Map, map_Size_X, enemyRoundXY[0], enemyRoundXY[1] - 1) && deltaY < 0)
                        this.entityStep(West);
                else
                    if (!Utils.isWall(world_Map, map_Size_X, enemyRoundXY[0], enemyRoundXY[1] + 1) && deltaY > 0)
                        this.entityStep(East);
                }
            }
            else
            {
                switch (new java.util.Random().nextInt(10))
                {
                    case 0:
                        if (!Utils.isWall(world_Map, map_Size_X, enemyRoundXY[0] + 1, enemyRoundXY[1]))
                            this.entityStep(South); break;
                    case 1:
                        if (!Utils.isWall(world_Map, map_Size_X, enemyRoundXY[0] - 1, enemyRoundXY[1]))
                            this.entityStep(North); break;
                    case 2:
                        if (!Utils.isWall(world_Map, map_Size_X, enemyRoundXY[0], enemyRoundXY[1] - 1))
                            this.entityStep(West); break;
                    case 3:
                        if (!Utils.isWall(world_Map, map_Size_X, enemyRoundXY[0], enemyRoundXY[1] + 1))
                            this.entityStep(East); break;
                    default: break;
                }
            }
        }

        return 0;
    }

    public boolean playersVision(char[][] world_map, int map_Size_X, double player_X, double player_Y)
    {

        double[] enemyXY = new double[2];

        this.getEntityCoord(enemyXY);

        boolean fl = true;
        double distance = Utils.calcDistance(coordX, coordY, player_X, player_Y);

        //проверка: видит ли враг игрока
        while (distance > speed && fl)
        {


            enemyXY[0] = (0.2 * player_X + (distance - 0.2) * enemyXY[0]) / distance;
            enemyXY[1] = (0.2 * player_Y + (distance - 0.2) * enemyXY[1]) / distance;

            distance = Utils.calcDistance(enemyXY[0], enemyXY[1], player_X, player_Y);

            if ( Utils.isWall( world_map, map_Size_X, (int) enemyXY[0], (int) enemyXY[0] ) )
                fl = false;

        }

        return fl;
    }

}
