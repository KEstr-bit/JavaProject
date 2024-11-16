package DOM;

public class Enemy extends Entity{

    public static final double VISSION_STEP = 0.2; //шаг луча взгляда

    public Enemy(double coordX, double coordY, double speed, int hitPoints, int damage)
    {
        this.damage = damage;
        this.hitPoints = hitPoints;
        this.coordX = coordX;
        this.coordY = coordY;
        this.speed = speed;
    }

    Enemy()
    {
        damage = 50;
        hitPoints = 100;
        coordX = 1;
        coordY = 8;
        speed = 0.02;
    }

    @Override
    public boolean entityMovement(GameMap map, double playerX, double playerY)
    {
        if (hitPoints <= 0)
            return true;

        double deltaX = playerX - this.coordX;
        double deltaY = playerY - this.coordY;

        //если враг видит игрока
        if (playersVision(map, playerX, playerY))
        {
            double distance = Utils.calcDistance(playerX, playerY, this.coordX, this.coordY);
            double angleCos = deltaX / distance;
            double angleSin = deltaY / distance;
            this.viewAngle = Utils.radToDeg(Math.atan2(angleSin, angleCos));
            this.entityMapStep(map);
        }

        return false;
    }

    public boolean playersVision(GameMap map, double playerX, double playerY)
    {
        double[] enemyXY = new double[2];
        this.getEntityCoord(enemyXY);

        boolean flVission = true;
        double distance = Utils.calcDistance(enemyXY[0], enemyXY[1], playerX, playerY);

        //движение луча взгляда к игроку
        while (distance > VISSION_STEP && flVission)
        {
            enemyXY[0] = Utils.interpolateCoord(enemyXY[0], playerX, distance);
            enemyXY[1] = Utils.interpolateCoord(enemyXY[1], playerY, distance);

            distance = Utils.calcDistance(enemyXY[0], enemyXY[1], playerX, playerY);

            //если луч столкнулся со стеной
            if (map.isWall(enemyXY[0], enemyXY[1]))
                flVission = false;
        }

        return flVission;
    }

}
