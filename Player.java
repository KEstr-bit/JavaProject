package DOM;

import static DOM.TexturePack.TextureType.BULLET;
import static DOM.TexturePack.TextureType.NECROMANCER;

class Player extends Entity {
    public enum ActiveGun {
        FIRST, SECOND
    }

    public enum Directions {
        NORTH, EAST, SOUTH, WEST
    }


    private ActiveGun activeGun;             // активное оружие
    private final ShotGun firstGun;                    // оружие игрока1
    private final Rifle secondGun;                     // оружие игрока2
    private int percentHp = 100;
    private int score = 0;
    private int fov = 80;                        // ширина обзора

    public Player(double cordX, double cordY) {
        super(cordX, cordY, 0.03, 300, 50, 0.5, NECROMANCER, true, true);
        activeGun = ActiveGun.FIRST;
        firstGun = new ShotGun(10, 3, 0.01, 30, 0.035, 100, 50, BULLET, true);
        secondGun = new Rifle(10, 1, 0.005, 0.035, 70, 200, BULLET, true);
    }


    public Gun getActiveGun() {
        return switch (activeGun) {
            case FIRST -> firstGun;
            case SECOND -> secondGun;
        };
    }

    public void moveTo(double delta, Directions stepDirection) { // перемщение игрока
        if (timer.check()) {
            getActiveGun().startAnimation(Animations.ANIM_MOVE);
        }
        double oldAngle = viewAngle;

        // изменение угла в зависимости от направления движения
        switch (stepDirection) {
            case EAST: viewAngle -= 90; break;
            case SOUTH: viewAngle += 180; break;
            case WEST: viewAngle += 90; break;
            case NORTH: break;
        }

        super.move(delta);

        // возвращение исходного угла
        viewAngle = oldAngle;
    }

    public void changeActiveWeapon() {
        switch (activeGun) {
            case FIRST:
                activeGun = ActiveGun.SECOND;
                break;
            case SECOND:
                activeGun = ActiveGun.FIRST;
                break;
        }
        getActiveGun().startAnimation(Animations.ANIM_SPAWN);
    }

    public void shot() {
        getActiveGun().shot(cordX, cordY, viewAngle);
    }

    public void addScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return score;
    }

    public int getFov() {
        return fov;
    }

    public void changeVision(double angle) {
        viewAngle -= angle;

        if (viewAngle > 360) {
            viewAngle -= 360;
        }
        if (viewAngle < 0) {
            viewAngle += 360;
        }
    }

    public boolean update(double delta) {
        firstGun.update(delta);
        secondGun.update(delta);
        return false;
    }

    public void takeDamage(double damage) {
        super.takeDamage(damage);
        percentHp = (int) (hitPoints * 100 / getMaxHealthPoints());
    }

    public int getPercentHp() {
        return percentHp;
    }
}
