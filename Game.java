package DOM;

public class Game {
    public Player you; // Игрок
    public Enemy monster; // Враг

    public Game() {
        // Инициализация игрока и врага
        you = new Player();
        monster = new Enemy();
    }

    // Взаимодействие объектов
    public int interaction(char[][] world_Map) {

        this.you.firstGun.allBulletMovment();                    //движение пули
        this.you.secondGun.allBulletMovment();                    //движение пули

        boolean monsterIsAlive = false;
        int[] monsterCoordXY = new int[2];
        if (!monster.getEntityCoord(monsterCoordXY))
        {
            monsterIsAlive = true;
        }

        int[] playerCoordXY= new int[2];
        if (!you.getEntityCoord(playerCoordXY) && monsterIsAlive)
        {
            this.monster.enemyMovment(world_Map, 10, playerCoordXY[0], playerCoordXY[1]);     //движение врага
            //если враг достиг игрока
            if (monsterCoordXY[0] == playerCoordXY[0] && monsterCoordXY[1] == playerCoordXY[1])
            {
                you.attackEntity(monster.getEntityDamage());
            }
        }


//если на карте есть пули первого оружия
        if (you.firstGun.getCountActiveBullets() > 0)
        {
            for (int i = 0; i < you.firstGun.bulletCount; i++) {
                //если пуля существует
                if (you.firstGun.bullets[i].active) {

                    int[] bulletCoordXY = new int[2];
                    int[] bulletFinalCoordXY = new int[2];

                    you.firstGun.bullets[i].getBulletCoords(bulletFinalCoordXY);
                    you.firstGun.bullets[i].getEntityCoord(bulletCoordXY);


                    //если пуля столкнулась со стеной
                    if (Utils.isWall(world_Map, 10, bulletCoordXY[0], bulletCoordXY[1])) {
                        you.firstGun.bullets[i].active = false;
                        you.firstGun.changeCountActiveBullets(-1);
                    } else {

                        //если пуля попала во врага

                        if (bulletCoordXY[0] == monsterCoordXY[0] && bulletCoordXY[1] == monsterCoordXY[1] && monsterIsAlive) {

                            monster.attackEntity(you.firstGun.bullets[i].getEntityDamage());
                            you.firstGun.bullets[i].active = false;
                            you.firstGun.changeCountActiveBullets(-1);

                        } else {
                            //если пуля достигла своей конечной точки
                            if (bulletCoordXY[0] == bulletFinalCoordXY[0] && bulletCoordXY[1] == bulletFinalCoordXY[1]) {
                                you.firstGun.bullets[i].active = false;
                                you.firstGun.changeCountActiveBullets(-1);
                            }
                        }

                    }
                }
            }
        }

        if (you.secondGun.getCountActiveBullets() > 0) {
            for (int i = 0; i < 10; i++) {
                //если пуля существует
                if (you.secondGun.bullets[i].active) {

                    int[] bulletCoordXY = new int[2];
                    int[] bulletFinalCoordXY = new int[2];

                    you.secondGun.bullets[i].getBulletCoords(bulletFinalCoordXY);
                    you.secondGun.bullets[i].getEntityCoord(bulletCoordXY);


                    //если пуля столкнулась со стеной
                    if (Utils.isWall(world_Map, 10, bulletCoordXY[0], bulletCoordXY[1])) {
                        you.secondGun.bullets[i].active = false;
                        you.secondGun.changeCountActiveBullets(-1);
                    } else {

                        //если пуля попала во врага

                        if (bulletCoordXY[0] == monsterCoordXY[0] && bulletCoordXY[1] == monsterCoordXY[1] && monsterIsAlive) {

                            monster.attackEntity(you.firstGun.bullets[i].getEntityDamage());
                            you.secondGun.bullets[i].active = false;
                            you.secondGun.changeCountActiveBullets(-1);

                        } else {
                            //если пуля достигла своей конечной точки
                            if (bulletCoordXY[0] == bulletFinalCoordXY[0] && bulletCoordXY[1] == bulletFinalCoordXY[1]) {
                                you.secondGun.bullets[i].active = false;
                                you.secondGun.changeCountActiveBullets(-1);
                            }
                        }
                    }
                }
            }
        }

        return 0;
    }
}