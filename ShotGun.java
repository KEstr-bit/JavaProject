package DOM;

import java.util.Map;

public class ShotGun extends Weapon{

    //угол разброса
    private static final int SPREAD_ANGLE = 30;

    public void shot(double coordX, double coordY, double shotAngle, Map<Integer, Entity> entities)
    {
        //смещение угла полета
        double sideShift = (double) SPREAD_ANGLE / (bulletCount - 1);
        shotAngle -= (double) SPREAD_ANGLE / 2;

        for (int i = 0; i < bulletCount; i++)
        {
            double x, y;
            x = Utils.projectionToX(bulletSpeed, Utils.degToRad(shotAngle));
            y = Utils.projectionToY(bulletSpeed, Utils.degToRad(shotAngle));

            x += coordX;
            y += coordY;

            //инициализация новой пули
            entities.put(Entity.lastID, new Bullet(x, y, shotAngle, this.bulletDamage, this.bulletSpeed));

            shotAngle += sideShift;
        }


    }
}
