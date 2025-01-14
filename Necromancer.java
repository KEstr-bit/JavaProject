package DOM;

import java.util.ArrayList;
import java.util.List;

import static DOM.Animations.*;
import static DOM.TextureType.NECROMANCER;
import static DOM.TextureType.SKULL;

class Necromancer extends Enemy {

    private ShotGun shotGun;
    private List<Entity> entities;

    public Necromancer(double coordinateX, double coordinateY, Entity target) {
        super(coordinateX, coordinateY, 0.01, 2000, 50, 2, NECROMANCER, target);
        shotGun = new ShotGun(10, 3, 0.01, 30, 0.025, 5, 100, SKULL, false);
        entities = new ArrayList<>();
    }


    public void updateAll(double delta) {
        entities.removeIf(entity -> entity.update(delta));
    }

    @Override
    public boolean update(double delta) {
        updateAnimation(delta);
        shotGun.update(delta);
        updateAll(delta);

        if (isAlive()) {
            double distance = lookAtTarget();

            switch (animation) {
                case ANIM_MOVE:
                    if (distance > 7) {
                        move(delta);
                    }
                    return false;

                case ANIM_ATTACK1:
                    if (timer.check()) {
                        if (!shotGun.shot(cordX, cordY, viewAngle)) {
                            shotGun.reload();
                        }
                        timer.start(0.5);
                    }
                    break;

                case ANIM_ATTACK2:
                    if (timer.check()) {
                        respawn();
                        timer.start(0.5);
                    }
                    break;

                default:
                    break;
            }

            if (timer.check() && isTargetSeen()) {
                if (distance > 7) {
                    startAnimation(ANIM_MOVE);
                } else {
                    startAnimation(ANIM_ATTACK1);
                    timer.start(0.6);
                }

                if (entities.size() < 5) {
                    startAnimation(ANIM_ATTACK2);
                    timer.start(0.5);
                }
            }
        } else if (animation == ANIM_BASE) {
            isVisible = false;

            if (shotGun.hasNotBullets() && entities.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public void respawn() {
        double angle = viewAngle;
        angle -= 45;
        for (int i = 0; i < 4; i++) {
            angle += 90;
            double x = 2 * Math.cos(Helper.degToRad(angle)) + cordX;
            double y = 2 * Math.sin(Helper.degToRad(angle)) + cordY;

            if (!GameMap.isWall(x, y)) {
                if (Math.random() < 0.5) {
                    entities.add(new Bomber(x, y, target));
                } else {
                    entities.add(new Archer(x, y, target));
                }
                PhysicsEngine.addEntity(entities.get(entities.size() - 1));
                RenderEngine.addEntity(entities.get(entities.size() - 1));
            }
        }
    }
}