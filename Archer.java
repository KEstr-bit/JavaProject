package DOM;

import java.util.Random;
import java.util.Vector;

import static DOM.Animations.ANIM_ATTACK1;
import static DOM.Animations.ANIM_MOVE;
import static DOM.TextureType.BULLET;
import static DOM.TextureType.ENEMY;

public class Archer extends Enemy implements Cloneable{
    private Rifle rifle;
    private int shift = 0;

    public Archer(double cordX, double cordY,  Entity target) {
        super(cordX, cordY, 0.02, 100, 50, 1, ENEMY, target);
        rifle = new Rifle(10, 1, 0.1,
                50,false,  BULLET);
    }

    @Override
    public boolean update(GameMap map, Vector<Entity> entities) {
        Random random = new Random();
        double distance = updateAngle();
        switch (animation)
        {
            case ANIM_MOVE:
                if (distance > 5)
                    mapStep(map);
                else if (distance < 3)
                    directionStep(map,viewAngle - 180);
                else
                {
                    if (frame == 0.0f)
                    {
                        if (random.nextBoolean())
                            shift = -90;
                        else
                            shift = 90;
                    }

                    if (directionStep(map, viewAngle - shift))
                        shift *= -1;
                }
                break;
            case ANIM_ATTACK1:
                if (frame == TexturePack.FRAMES_COUNT / 2.0f)
                    if (!rifle.shot(cordX, cordY, viewAngle, entities))
                        rifle.reloading();
                break;
            default:
                break;
        }

        rifle.updateAnimation();

        updateAnimation();

        if (eventFl)
            return false;

        if(!isAlive())
            return true;

//если враг не видит цель
        if (!isTargetSeen(map)) {
            return false;
        }

        if (random.nextBoolean())
        {
            startAnimation(ANIM_ATTACK1);
            return false;
        }

        startAnimation(ANIM_MOVE);
        return false;
    }

    @Override
    public Archer clone() throws CloneNotSupportedException {
        return (Archer) super.clone();
    }

    public Archer deepClone() throws CloneNotSupportedException {
        Archer cloned = (Archer) super.clone(); // Клонируем самого себя
        cloned.rifle = new Rifle(this.rifle.magazine_capacity, this.rifle.bulletCount,
                this.rifle.bulletSpeed, this.rifle.bulletDamage, this.rifle.friendly,
                this.rifle.bulletTexture); // Клонируем rifle
        return cloned; // Возвращаем глубокую копию
    }
}
