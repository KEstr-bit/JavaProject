package DOM;

import java.util.Vector;

import static DOM.Animations.*;
import static DOM.TextureType.ENEMY;

class Bomber extends Enemy {
    private boolean exploded = false;

    public Bomber(double coordinateX, double coordinateY, Entity target) {
        super(coordinateX, coordinateY, 0.03, 50, 100, 1, ENEMY, target);
    }

    @Override
    public boolean update(double delta) {
        updateAnimation(delta);

        if (isAlive()) {
            lookAtTarget();

            switch (animation) {
                case ANIM_MOVE:
                    move(delta);
                    return false;

                case ANIM_ATTACK1:
                    if (timer.check()) {
                        if (intersects(target, 1.3f)) {
                            target.takeDamage(damage);
                        }

                        exploded = true;
                        timer.start(0.5);
                    }
                    return false;

                default:
                    break;
            }

            if (exploded) {
                kill();
            }

            if (timer.check() && isTargetSeen()) {
                if (intersects(target, 1)) {
                    startAnimation(ANIM_ATTACK1);
                    timer.start(0.5);
                } else {
                    startAnimation(ANIM_MOVE);
                }
            }
        } else if (animation == ANIM_BASE) {
            isVisible = false;
            return true;
        }

        return false;
    }
}
