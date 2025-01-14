package DOM;

import java.util.Vector;

import static DOM.Animations.ANIM_ATTACK1;
import static DOM.TextureType.SHOTGUN;

public class ShotGun extends Gun {
    private final int maxSpreadAngle;
    private int spreadAngle;

    public ShotGun(int magazineCapacity, int ammoPerShot, double velocity, int maxSpreadAngle, double bulletVelocity,
                   double bulletHP, double bulletDamage, TextureType bulletTexture, boolean isFriendly) {
        super(magazineCapacity, ammoPerShot, velocity, bulletVelocity, bulletHP, bulletDamage, isFriendly, SHOTGUN, bulletTexture);
        this.maxSpreadAngle = maxSpreadAngle;
        this.spreadAngle = maxSpreadAngle;
    }

    @Override
    public boolean shot(double cordX, double cordY, double shotAngle) {
        if (ammunition < ammoPerShot) {
            return false;
        }

        if (!timer.check()) {
            return true;
        }

        ammunition -= ammoPerShot;
        startAnimation(ANIM_ATTACK1);
        timer.start(0.3);

        double angleShift = 0;
        if (ammoPerShot > 1) {
            angleShift = (double) spreadAngle / (ammoPerShot - 1);
            shotAngle -= spreadAngle / 2.0;
        }

        for (int i = 0; i < ammoPerShot; i++) {
            double x = Helper.projectToX(SIDE_SHIFT, Helper.degToRad(shotAngle));
            double y = Helper.projectToY(SIDE_SHIFT, Helper.degToRad(shotAngle));

            x += cordX;
            y += cordY;

            // Initialization of a new bullet
            bullets.add(new Bullet(x, y, shotAngle, bulletVelocity, (int) bulletHP, bulletDamage, bulletTexture, isFriendly()));
            PhysicsEngine.addEntity(bullets.get(bullets.size() - 1));
            RenderEngine.addEntity(bullets.get(bullets.size() - 1));

            shotAngle += angleShift;
        }

        spreadAngle -= maxSpreadAngle / (getMagazineCapacity() / ammoPerShot);

        return true;
    }

    @Override
    public void reload() {
        super.reload();
        spreadAngle = maxSpreadAngle;
    }
}
