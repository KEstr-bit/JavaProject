package DOM;

import static DOM.WeaponOption.AUTOMATE;
import static DOM.WeaponOption.SHOTGUN;
import java.util.Map;

public class Player extends Entity{

    private WeaponOption activeWeapon;

    public static final int VISION_SPEED = 1;  //скорость изменения угла обзора
    public static final int FOV = 60;          //ширина обзора
    public ShotGun firstGun;                    //оружие игрока1
    public Automate secondGun;                 //оружие игрока2

    public Player(double coordX, double coordY, double speed, int hitPoints, int damage)
    {
        this.damage = damage;
        this.hitPoints = hitPoints;
        this.coordX = coordX;
        this.coordY = coordY;
        this.speed = speed;

        activeWeapon = SHOTGUN;
        firstGun = new ShotGun();
        secondGun = new Automate();

        viewAngle = 0;
    }

    public Player(){
        coordX = 7;
        coordY = 2;
        hitPoints = 100;
        speed = 0.05;
        damage = 50;

        activeWeapon = SHOTGUN;
        firstGun = new ShotGun();
        secondGun = new Automate();

        viewAngle = 180;
    }

    //перемщение игрока
    public void playerMapStep(CardinalDirections stepDirection, GameMap map)
    {
        if (hitPoints <= 0)
            return;

        double oldAngle = viewAngle;

        //изменение угла в зависимости от направления движения
        switch (stepDirection)
        {
            case East: viewAngle -= 90; break;
            case South: viewAngle += 180; break;
            case West: viewAngle += 90; break;
            default: break;
        }

        this.entityMapStep(map);
        //возвращение исходного угла
        viewAngle = oldAngle;


    }

    public void changeActiveWeapon(){
        switch (activeWeapon)
        {
            case SHOTGUN:
                activeWeapon = AUTOMATE;
                break;
            case AUTOMATE:
                activeWeapon = SHOTGUN;
                break;
        }
    }

    public void shot(Map<Integer, Entity> entiyes)
    {
        switch (activeWeapon)
        {
            case SHOTGUN:
                this.firstGun.shot(coordX, coordY, viewAngle, entiyes);
                break;
            case AUTOMATE:
                this.secondGun.shot(coordX, coordY, viewAngle, entiyes);
                break;
        }

    }

    public void changeVision(CardinalDirections direct)
    {
        switch (direct)
        {
            case East:
                viewAngle -= VISION_SPEED;
                break;
            case West:
                viewAngle += VISION_SPEED;
                break;
            default:
                break;
        }

        if (viewAngle > 360)
        {
            viewAngle -= 360;
        }

        if (viewAngle < 0)
        {
            viewAngle += 360;
        }

    }

    @Override
    public boolean entityMovement(GameMap map, double playerX, double playerY)
    {
        return false;
    }
}
