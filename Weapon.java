package DOM;

public abstract class Weapon {

    protected int bulletCount;                    //количество пуль, выпускаемых за раз
    protected double bulletSpeed;                 //скорость полета пули
    protected int bulletDamage;                   //урон, наносимы пулей
    protected int countActiveBullets;

    public Bullet[] bullets = new Bullet[10];;

    public abstract boolean shot(double coord_X, double coord_Y, CardinalDirections shot_Direction);

    public Weapon(int bullet_Count, double bullet_Speed, int bullet_Damage){
        bulletDamage = bullet_Damage;
        bulletCount = bullet_Count;
        bulletSpeed = bullet_Speed;

        countActiveBullets = 0;
    }

    public Weapon() {
        bulletCount = 3;
        bulletSpeed = 0.5;
        bulletDamage = 50;

        countActiveBullets = 0;
    }

    //получит характеристики оружия
    public int getCountActiveBullets(){
        return countActiveBullets;
    }

    public int changeCountActiveBullets(int change){
        return countActiveBullets += change;
    }

    public int allBulletMovment(){
        for (int i = 0; i < bulletCount; i++)
        {
            if (bullets[i] != null) { // Check if bullet exists
                bullets[i].bulletMovment();
            }
        }
        return 0;
    }

}
