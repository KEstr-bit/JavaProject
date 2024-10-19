package DOM;

import static DOM.CardinalDirections.*;
import static DOM.WeaponOption.AUTOMAT;
import static DOM.WeaponOption.SHOTGUN;

public class Player extends Entity{

    private CardinalDirections playerDirection;
    private WeaponOption activeWeapon;

    public ShotGun firstGun;
    public Automat secondGun;

    public Player(double coord_X, double coord_Y, double entity_Speed, int hit_Points, int entity_Damage, CardinalDirections direction){
        damage = entity_Damage;
        hitPoints = hit_Points;
        coordX = coord_X;
        coordY = coord_Y;
        speed = entity_Speed;

        playerDirection = direction;
        activeWeapon = SHOTGUN;
        firstGun = new ShotGun();
        secondGun = new Automat();
    }

    public Player(){
        coordX = 8;
        coordY = 1;
        hitPoints = 100;
        speed = 1;
        damage = 50;

        playerDirection = North;
        activeWeapon = SHOTGUN;
        firstGun = new ShotGun();
        secondGun = new Automat();
    }

    //перемщение игрока
    public int playerStep(CardinalDirections step_Direction){

        int i = 0;
        playerDirection = step_Direction;

        i = this.entityStep(step_Direction);
        return i;
    }
    //получение координат игрока
    public CardinalDirections getPlayerDirection(){
        return playerDirection;
    }

    public int gamePlayerStep(char[][] world_Map, int map_Size_X, CardinalDirections step_Direction){
        int fl = 0;
        if (hitPoints > 0)
        {
            int[] roundXY = new int[2];
            this.getEntityCoord(roundXY);


            //изменение координат игрока в зависимости от направления
            switch (step_Direction)
            {
                case North:
                    if (!Utils.isWall(world_Map, map_Size_X, roundXY[0] - 1, roundXY[1])) {
                        this.playerStep(North);
                    }
                    else {
                        fl = 2;
                    }
                    break;
                case East:
                    if (!Utils.isWall(world_Map, map_Size_X, roundXY[0], roundXY[1] + 1)){
                        this.playerStep(East);
                    }
                    else {
                        fl = 2;
                    }
                    break;
                case South:
                    if (!Utils.isWall(world_Map, map_Size_X, roundXY[0] + 1, roundXY[1])) {
                        this.playerStep(South);
                    }
                    else {
                        fl = 2;
                    }
                    break;
                case West:
                    if (!Utils.isWall(world_Map, map_Size_X, roundXY[0], roundXY[1] - 1)) {
                        this.playerStep(West);
                    }
                    else {
                        fl = 2;
                    }
                    break;
                default:
                    fl = 1;
            }
        }
        return fl;
    }

    public WeaponOption changeActiveWeapon(){
        switch (activeWeapon)
        {
            case SHOTGUN:
                activeWeapon = AUTOMAT;
                break;
            case AUTOMAT:
                activeWeapon = SHOTGUN;
                break;
        }
        return activeWeapon;
    }

    public WeaponOption shot(){
        switch (activeWeapon)
        {
            case SHOTGUN:
                this.firstGun.shot(coordX, coordY, playerDirection);
                break;
            case AUTOMAT:
                this.secondGun.shot(coordX, coordY, playerDirection);
                break;
        }

        return activeWeapon;
    }
}
