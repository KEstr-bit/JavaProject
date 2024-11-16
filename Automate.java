package DOM;
import java.util.Map;

public class Automate extends Weapon{

    public void shot(double coordX, double coordY, double shotAngle, Map<Integer, Entity> entities)
    {
        //смещение пули от точки выстрела
        double sideShift = 0;

        for (int i = 0; i < bulletCount; i++)
        {
            sideShift += 10*bulletSpeed;

            double x, y;
            x = Utils.projectionToX(sideShift, Utils.degToRad(shotAngle));
            y = Utils.projectionToY(sideShift, Utils.degToRad(shotAngle));

            x += coordX;
            y += coordY;

            //инициализация новой пули
            entities.put(Entity.lastID, new Bullet(x, y, shotAngle, this.bulletDamage, this.bulletSpeed));

        }


    }
}
