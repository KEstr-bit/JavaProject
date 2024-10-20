package DOM;

import static DOM.CardinalDirections.*;

public class Bullet extends Entity{

    public boolean active;
    private final double finalCoordX; //конечная координата пули по X
    private final double finalCoordY; //конечная координата пул по Y


    public Bullet(double coord_X, double coord_Y, double final_coord_X, double final_coord_Y, int entity_Damage, double entity_Speed){
        this.coordX = coord_X;
        this.coordY = coord_Y;
        this.finalCoordX = final_coord_X;
        this.finalCoordY = final_coord_Y;
        this.speed = entity_Speed;
        this.damage = entity_Damage;
        this.active = true;
    }

    public Bullet(){
        this.coordX = 0;
        this.coordY = 0;
        this.finalCoordX = 0;
        this.finalCoordY = 0;
        this.speed = 0;
        this.damage = 0;
        this.active = false;
    }

    //получение координат точки назначения пули
    public int getBulletCoords(double[] final_coord){
        final_coord[0] = finalCoordX;
        final_coord[1] = finalCoordY;
        return 0;
    }

    public int getBulletCoords(int[] final_coord){
        final_coord[0] = (int) Math.round(finalCoordX);
        final_coord[1] = (int) Math.round(finalCoordY);
        return 0;
    }

    //движение пули
    public int bulletMovment(){
        if(active) {
            double deltaX = finalCoordX - coordX;
            double deltaY = finalCoordY - coordY;

            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                // Движение по оси X
                if (deltaX < 0) {
                    this.entityStep(North);
                } else {
                    this.entityStep(South);
                }
            } else {
                // Движение по оси Y
                if (deltaY < 0) {
                    this.entityStep(West);
                } else {
                    this.entityStep(East);
                }
            }
            return 0;
        }
        return 1;
    }


}
