package DOM;

import java.util.Vector;

import static DOM.Animations.ANIM_MOVE;
import static DOM.Animations.ANIM_SPAWN;
import static DOM.TextureType.BULLET;
import static DOM.TextureType.NECROMANCER;
import static DOM.WeaponOption.GUN_RIFLE;
import static DOM.WeaponOption.GUN_SHOTGUN;

public class Player extends Entity{

    private WeaponOption activeWeapon;
    private ShotGun firstGun;                    //оружие игрока1
    private Rifle secondGun;                 //оружие игрока2

    public static final int VISION_SPEED = 1;  //скорость изменения угла обзора
    public static final int FOV = 60;          //ширина обзора

    Player(double cordX, double cordY)
    {
        super(cordX, cordY, 0.05, 100, 50, 0.5, NECROMANCER, true);
        activeWeapon = GUN_SHOTGUN;
        firstGun = new ShotGun(10, 3, 0.05, 50, true, BULLET);
        secondGun = new Rifle(10, 1, 0.3, 10, true, BULLET);
    }

    public void playerMapStep(CardinalDirections stepDirection, GameMap map)
    {
        getActiveWeapon().startAnimation(ANIM_MOVE, 1);
	    double oldAngle = viewAngle;

        //изменение угла в зависимости от направления движения
        switch (stepDirection)
        {
            case EAST: viewAngle -= 90; break;
            case SOUTH: viewAngle += 180; break;
            case WEST: viewAngle += 90; break;
            case NORTH:break;
        }

        this.mapStep(map);
        //возвращение исходного угла
        viewAngle = oldAngle;
    }


    public void changeActiveWeapon()
    {
        switch (activeWeapon)
        {
            case GUN_SHOTGUN:
                activeWeapon = GUN_RIFLE;
                break;
            case GUN_RIFLE:
                activeWeapon = GUN_SHOTGUN;
                break;
        }
        getActiveWeapon().startAnimation(ANIM_SPAWN);
    };

    public void shot(Vector<Entity> entities)
    {

        switch (activeWeapon)
        {
            case GUN_SHOTGUN:
                firstGun.shot(cordX, cordY, viewAngle, entities);
                break;
            case GUN_RIFLE:
                secondGun.shot(cordX, cordY, viewAngle, entities);
                break;
            case null:
                break;
        }
    }

    public Weapon getActiveWeapon()
    {
        return switch (activeWeapon) {
            case GUN_SHOTGUN -> firstGun;
            case GUN_RIFLE -> secondGun;
        };
    };

    public void changeVision(double angle)
    {
        viewAngle -= angle;

        if (viewAngle > 360)
            viewAngle -= 360;

        if (viewAngle < 0)
            viewAngle += 360;
    };

    public boolean update(GameMap map, Vector<Entity> entities)
    {
        firstGun.updateAnimation();
        secondGun.updateAnimation();
        return false;
    }

    public double getDamage()
    {
        return getActiveWeapon().getDamage();
    }
}
