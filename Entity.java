package DOM;



public class Entity {
    protected double coordX;             //координата по X
    protected double coordY;             //координата по Y
    protected int hitPoints;             //очки здоровья
    protected int damage;                 //урон наносимый
    protected double speed;               //скорость

    public Entity(double coord_X, double coord_Y, double entity_Speed, int hit_Points, int entity_Damage) {
        this.coordX = coord_X;
        this.coordY = coord_Y;
        this.speed = entity_Speed;
        this.hitPoints = hit_Points;
        this.damage = entity_Damage;
    }

    public Entity() {
        this.coordX = 8;
        this.coordY = 1;
        this.hitPoints = 100;
        this.speed = 1;
        this.damage = 50;
    }

    public int getEntityCoord(double[] coord) {
        if (hitPoints > 0) {
            coord[0] = coordX;
            coord[1] = coordY;
            return 0;
        }
        return 1;
    }

    public boolean getEntityCoord(int[] coord) {
        if (hitPoints > 0) {
            coord[0] = (int) coordX;
            coord[1] = (int) coordY;
            return false;
        }
        return  true;
    }

    public int getEntityDamage() {
        return damage;
    }

    public int getEntityHitPoints() {
        return hitPoints;
    }

    // Метод атаки сущности
    public int attackEntity(int entity_Damage) {
        if (hitPoints > 0) {
            hitPoints -= entity_Damage;
            return 0; // Успешная атака
        } else {
            return 1; // Сущность не может быть атакована, так как здоровье равно или меньше 0
        }
    }

    public int entityStep(CardinalDirections step_Direction) {
        // Здесь вы можете определить логику перемещения в зависимости от направления
        if (hitPoints > 0)
        {
            int i = 0;
            switch (step_Direction)
            {
                case North: coordX -= speed; break;
                case East: coordY += speed; break;
                case South: coordX += speed; break;
                case West: coordY -= speed; break;
                default: i = 1;
            }

            return i;
        }
        return 2;
    }

};

