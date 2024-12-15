package DOM;

import java.util.Vector;

import static DOM.Animations.ANIM_ATTACK1;
import static DOM.Animations.ANIM_MOVE;
import static DOM.TextureType.ENEMY;

public class Bomber extends Enemy{

    public Bomber(double cordX, double cordY, Entity target) {
        super(cordX, cordY, 0.06, 50, 100, 1, ENEMY, target);
    }

    @Override
    public boolean update(GameMap map, Vector<Entity> entities) {
        updateAngle();
        switch (animation)
        {
            case ANIM_MOVE:
                mapStep(map);
                break;
            case ANIM_ATTACK1:
                if(frame == 7.0f && intersects(target, 1))
            {
                target.dealDamage(damage);
                kill();
            }
            break;
            default:
                break;
        }

        updateAnimation();

        if (eventFl)
            return false;

        if (!isAlive())
            return true;

        //если враг не видит цель
        if (!isTargetSeen(map)){
            return false;
        }

        if (intersects(target, 0.7f)) {
            startAnimation(ANIM_ATTACK1);
            return false;
        }

        startAnimation(ANIM_MOVE);
        return false;
    }
}
