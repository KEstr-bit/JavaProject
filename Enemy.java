package DOM;

public abstract class Enemy extends Entity{

    protected Entity target;
    public static final double VISION_STEP = 0.2; //шаг луча взгляда

    Enemy(double cordX, double cordY, double speed, double hitPoints, double damage, double size, TextureType texture, Entity target)
    {
        super(cordX, cordY, speed, hitPoints, damage, size, texture, false);
        this.target = target;
    }

    protected boolean isTargetSeen(GameMap map)
    {
        Cords targetCords;
        targetCords = target.getCords();

        Cords enemyCords;
        enemyCords = this.getCords();

        boolean visionFl = true;
        double distance = Utils.calcDistance(enemyCords.getX(), enemyCords.getY(), targetCords.getX(), targetCords.getY());

        viewAngle += Math.atan2((targetCords.getX() - enemyCords.getX()) / distance, ( targetCords.getY() - enemyCords.getY()) / distance);

        //движение луча взгляда к игроку
        while (distance > VISION_STEP && visionFl)
        {
            enemyCords.setCords(Utils.interpolateCords(enemyCords.getX(), targetCords.getX(), VISION_STEP, distance), Utils.interpolateCords( enemyCords.getY(), targetCords.getY(), VISION_STEP, distance));

            distance = Utils.calcDistance(enemyCords.getX(), enemyCords.getY(), targetCords.getX(), targetCords.getY());

            //если луч столкнулся со стеной
            if (map.isWall(enemyCords.getX(), enemyCords.getY()))
                visionFl = false;
        }

        return visionFl;
    }

    protected double updateAngle()
    {
        Cords targetCords;
        targetCords = target.getCords();

        double deltaX = targetCords.getX() - cordX;
        double deltaY = targetCords.getY() - cordY;

        double distance = Utils.calcDistance(targetCords.getX(), targetCords.getY(), cordX, cordY);
        double angleCos = deltaX / distance;
        double angleSin = deltaY / distance;
        viewAngle = Utils.radToDeg(Math.atan2(angleSin, angleCos));
        return distance;
    }

}
