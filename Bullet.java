package DOM;

import java.util.Vector;

import static DOM.TextureType.BULLET;

class Bullet extends Entity {

    public Bullet(double cordX, double cordY, double flightAngle, double speed,
                  int maxHitPoints, double damage, TextureType texture, boolean friendly) {
        super(cordX, cordY, speed, maxHitPoints, damage, 0.5, texture, friendly, false);
        this.viewAngle = flightAngle;
        this.timer.start(0);
    }

    public boolean update(double delta) {
        updateAnimation(delta);

        if (!isAlive() && animation == Animations.ANIM_BASE) {
            isVisible = false;
            return true;
        }

        if (timer.check()) {
            double x = cordX;
            double y = cordY;
            baseStep(delta);
            takeDamage(velocity);

            if (GameMap.isWall(cordX, cordY)) {
                cordX = x;
                cordY = y;
                kill();
            }
        }

        return false;
    }

    @Override
    public void takeDamage(double damage) {
        if (isAlive()) {
            hitPoints -= damage;

            if (!isAlive()) {
                startAnimation(Animations.ANIM_DIE);
                timer.start(0.5);
            }
        }
    }
}


