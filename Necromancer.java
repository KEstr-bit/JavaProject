package DOM;

import java.util.Random;
import java.util.Vector;

import static DOM.Animations.*;
import static DOM.TextureType.BULLET;
import static DOM.TextureType.NECROMANCER;

public class Necromancer extends Enemy{
    private ShotGun shotGun;

    Necromancer(double cordX, double cordY, Entity target) {
        super(cordX, cordY, 0.02, 1000, 50, 2, NECROMANCER, target);
        shotGun = new ShotGun(10, 3, 0.05, 30, false , BULLET);
    }

    @Override
    public boolean update(GameMap map, Vector<Entity> entities) {
        Random random = new Random();
        double distance = updateAngle();
        switch (animation)
        {
            case ANIM_MOVE:

                if (distance > 5){
                    mapStep(map);
                }
                break;
            case ANIM_ATTACK1:
                if (frame == 7)
                    if (!shotGun.shot(cordX, cordY, viewAngle, entities))
                        shotGun.reloading();
                break;
            case ANIM_ATTACK2:
                if (frame == TexturePack.FRAMES_COUNT / 2.0f)
                    respawn(map, entities);
                break;
            default:
                break;
        }

        shotGun.updateAnimation();

        updateAnimation();

        if (eventFl)
            return false;

        if (!isAlive())
            return true;

//если враг не видит цель
        if (!isTargetSeen(map)) {
            return false;
        }

        if (distance > 5) {
            startAnimation(ANIM_MOVE);
        }

        if(random.nextInt()%50 == 0)
        {
            if(random.nextBoolean())
                startAnimation(ANIM_ATTACK1);
            else
                startAnimation(ANIM_ATTACK2);
        }

        return false;
    }

    private void respawn(GameMap map, Vector<Entity> entities)
    {
        Random random = new Random();

        double angle = viewAngle;
        angle -= 45;
        for(int i = 0; i < 4; i++)
        {
            angle += 90;
            double x, y;
            x = 2 * Math.cos(Utils.degToRad(angle)) + cordX;
            y = 2 * Math.sin(Utils.degToRad(angle)) + cordY;
            if (!map.isWall(x, y))
            {
                if(random.nextBoolean())
                {
                    entities.add(new Bomber(x, y, target));
                }
                else
                {
                    entities.add(new Archer(x, y, target));
                }
            }
        }
    }
}
