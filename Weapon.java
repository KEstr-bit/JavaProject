package DOM;
import java.util.Map;

public abstract class Weapon {

    protected int bulletCount;                    //количество пуль, выпускаемых за раз
    protected double bulletSpeed;                 //скорость полета пули
    protected int bulletDamage;                   //урон, наносимы пулей


    public abstract void shot(double coordX, double coordY, double shotAngle, Map<Integer, Entity> entities);

    public Weapon(int bulletCount, double bulletSpeed, int bulletDamage)
    {
        this.bulletDamage = bulletDamage;
        this.bulletCount = bulletCount;
        this.bulletSpeed = bulletSpeed;

    }

    public Weapon() {
        bulletCount = 3;
        bulletSpeed = 0.02;
        bulletDamage = 50;
    }

}
