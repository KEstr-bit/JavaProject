package DOM;

public class Automat extends Weapon{

    public boolean shot(double coord_X, double coord_Y, CardinalDirections shot_Direction)
    {
        double final_coord_X = 0, final_coord_Y = 0;

        int sideShift = -bulletCount / 2;
        boolean success = true;
        //если оружие выстреливает больше 0 пуль и на карте ни одной пули
        if (bulletCount > 0 && countActiveBullets == 0)
        {
            for (int i = 0; i < bulletCount; i++)
            {
                double x, y;
                x = coord_X;
                y = coord_Y;
                //выбор координа в зависимости от направления
                switch (shot_Direction)
                {
                    case North:
                        final_coord_X = coord_X - 10;
                        final_coord_Y = coord_Y;
                        x -= sideShift;
                        break;
                    case East:
                        final_coord_X = coord_X;
                        final_coord_Y = coord_Y + 10;
                        y += sideShift;
                        break;
                    case South:
                        final_coord_X = coord_X + 10;
                        final_coord_Y = coord_Y;
                        x += sideShift;
                        break;
                    case West:
                        final_coord_X = coord_X;
                        final_coord_Y = coord_Y - 10;
                        y -= sideShift;
                        break;
                }
                sideShift += 1;
                // инициализация пуль
                bullets[i] = new Bullet(x, y, final_coord_X, final_coord_Y, bulletDamage, bulletSpeed);
                bullets[i].active = true;
                this.changeCountActiveBullets(1);


            }
        }
        else
            success = false;
        return success;
    }
}
