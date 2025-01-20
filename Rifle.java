package DOM;
import java.util.Vector;

import static DOM.AnimationControl.Animations.ANIM_ATTACK1;
import static DOM.TexturePack.TextureType.RIFLE;

class Rifle extends Gun {
    public Rifle(int magazineCapacity, int ammoPerShot, double velocity, double bulletVelocity, double bulletHP,
                 double bulletDamage, TexturePack.TextureType bulletTexture, boolean friendly) {
        super(magazineCapacity, ammoPerShot, velocity, bulletVelocity, bulletHP, bulletDamage, friendly, RIFLE, bulletTexture);
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

        // Offset bullet from the shooting point
        double sideShift = SIDE_SHIFT;

        for (int i = 0; i < ammoPerShot; i++) {
            sideShift += bulletVelocity;

            double x = Helper.projectToX(sideShift, Helper.degToRad(shotAngle));
            double y = Helper.projectToY(sideShift, Helper.degToRad(shotAngle));

            x += cordX;
            y += cordY;

            // Initialize new bullet
            bullets.add(new Bullet(x, y, shotAngle, bulletVelocity, (int) bulletHP, bulletDamage, bulletTexture, isFriendly()));
            PhysicsEngine.addEntity(bullets.getLast());
            RenderEngine.addEntity(bullets.getLast());
        }
        return true;
    }
}
