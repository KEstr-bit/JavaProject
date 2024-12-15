package DOM;

import java.util.Vector;

import static DOM.TextureType.BULLET;

public class Bullet extends Entity {


    public Bullet(double cordX, double cordY, double flightAngle, double speed, double hitPoints, double damage, TextureType texture, boolean friendly)
    {
        super(cordX, cordY, speed, hitPoints, damage, 0.5 ,texture, friendly);
        this.viewAngle = flightAngle;
    }

    @Override
    public boolean update(GameMap map, Vector<Entity> entities) {
        updateAnimation();

        if (eventFl)
            return false;

        if(!isAlive())
            return true;

        baseStep();
        hitPoints -= speed;

//если пуля врезалась в стену
        if (map.isWall(cordX, cordY))
            kill();

        return false;
    }

}


