package DOM;

import static DOM.Animations.*;
import static DOM.TextureType.ARCHER;
import static DOM.TextureType.FGHYH;

class Archer extends Enemy {
    private Rifle rifle;
    int shift = 90;

    public Archer(double coordinateX, double coordinateY, Entity target) {
        super(coordinateX, coordinateY, 0.01, 100, 50, 1, ARCHER, target);
        rifle = new Rifle(10, 1, 0.01, 0.035, 10, 50, FGHYH, false);
    }

    @Override
    public boolean update(double delta) {

        updateAnimation(delta);
        rifle.update(delta);

        if (isAlive()) {
            double distance = lookAtTarget();

            switch (animation) {
                case ANIM_MOVE:
                    if (distance > 5) {
                        move(delta);
                    } else if (distance < 3) {
                        pushAt(delta, viewAngle - 180);
                    } else {
                        if (timer.check()) {
                            timer.start(0.2);
                            if (Math.random() < 0.5) // simulating random with 50% chance
                                shift *= -1;
                        }
                        if (pushAt(delta, viewAngle + shift))
                            shift *= -1;
                    }
                    return false;

                case ANIM_ATTACK1:
                    if (timer.check()) {
                        if (!rifle.shot(cordX, cordY, viewAngle))
                            rifle.reload();
                        timer.start(0.5);
                    }
                    break;

                default:
                    break;
            }

            if (timer.check() && isTargetSeen()) {
                if (Math.random() < 0.5) {
                    startAnimation(ANIM_ATTACK1);
                    timer.start(0.3);
                } else {
                    startAnimation(ANIM_MOVE);
                }
            }

        } else if (animation == ANIM_BASE) {
            isVisible = false;
            return rifle.hasNotBullets();
        }

        return false;
    }
}
