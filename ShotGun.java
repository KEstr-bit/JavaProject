package DOM;

import java.util.Vector;

import static DOM.Animations.ANIM_ATTACK1;

public class ShotGun extends Weapon {

    static final int MAX_SPREAD_ANGLE = 30;
    static final double SIDE_SHIFT = 0.25;
    private int spreadAndle = MAX_SPREAD_ANGLE;

    ShotGun(int magazineCapacity, int bulletCount, double bulletSpeed,
            double bulletDamage, boolean friendly, TextureType bulletTexture) {
        super(magazineCapacity, bulletCount, bulletSpeed,
                bulletDamage, friendly, TextureType.SHOTGUN, bulletTexture);
    }

    public boolean shot(double cordX, double cordY, double shotAngle, Vector<Entity> entities) {
        //смещение угла полета
        if (ammunition < bulletCount) {
            return false;
        }

        if (eventFl) {
            return true;
        }

        ammunition -= bulletCount;
        startAnimation(ANIM_ATTACK1);


        //смещение угла полета
        double angleShift = 0;
        if (bulletCount > 1) {
            angleShift = (double) (spreadAndle) / (bulletCount - 1);
            shotAngle -= spreadAndle / 2.0;
        }

        for (int i = 0; i < bulletCount; i++) {
            double x = Utils.projectToX(SIDE_SHIFT, Utils.degToRad(shotAngle));
            double y = Utils.projectToY(SIDE_SHIFT, Utils.degToRad(shotAngle));

            x += cordX;
            y += cordY;

            //инициализация новой пули
            entities.add(new Bullet(x, y, shotAngle, bulletSpeed, 5, bulletDamage, bulletTexture, friendly));

            shotAngle += angleShift;
        }

        spreadAndle -= MAX_SPREAD_ANGLE/ (magazine_capacity / bulletCount);

        return true;

    }

    public void reloading()
    {
        super.reloading();
        spreadAndle = MAX_SPREAD_ANGLE;
    }
}
