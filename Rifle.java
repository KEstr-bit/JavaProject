package DOM;
import java.util.Map;
import java.util.Vector;

import static DOM.Animations.ANIM_ATTACK1;

public class Rifle extends Weapon{

    static final double SIDE_SHIFT = 0.25;

    Rifle(int magazineCapacity, int bulletCount, double bulletSpeed, double bulletDamage, boolean friendly, TextureType bulletTexture) {
        super(magazineCapacity, bulletCount, bulletSpeed, bulletDamage, friendly, TextureType.RIFLE, bulletTexture);
    }

    @Override
    public boolean shot(double cordX, double cordY, double shotAngle, Vector<Entity> entities) {

        if (ammunition < bulletCount){
            return false;
        }

        if (eventFl) {
            return true;
        }

        ammunition -= bulletCount;
        startAnimation(ANIM_ATTACK1);

        //смещение пули от точки выстрела
        double sideShift = SIDE_SHIFT;

        for (int i = 0; i < bulletCount; i++)
        {
            sideShift += bulletSpeed;

            double x = Utils.projectToX(sideShift, Utils.degToRad(shotAngle));
            double y = Utils.projectToY(sideShift, Utils.degToRad(shotAngle));

            x += cordX;
            y += cordY;

            //инициализация новой пули
            entities.add(new Bullet(x, y, shotAngle, bulletSpeed, 10, bulletDamage, bulletTexture, friendly));

        }
        return true;
    }
}
