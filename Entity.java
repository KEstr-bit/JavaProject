package DOM;


import static DOM.TextureType.Enemy1;

public abstract class Entity {
    protected double coordX;             //координата по X
    protected double coordY;             //координата по Y
    protected int hitPoints;             //очки здоровья
    protected int damage;                 //урон наносимый
    protected double speed;               //скорость
    protected double viewAngle;          //угол обзора
    protected  double size;               //размер
    protected TextureType texture;       //текстура

    public static int lastID = 0;             //последний записанный id

    public Entity(double coordX, double coordY, double speed, int hitPoints, int damage, TextureType texture) {
        this.damage = damage;
        this.hitPoints = hitPoints;
        this.coordX = coordX;
        this.coordY = coordY;
        this.speed = speed;
        this.texture = texture;
        viewAngle = 0;
        lastID++;
        size = 1;
    }

    public Entity() {
        coordX = 8;
        coordY = 1;
        hitPoints = 100;
        speed = 1;
        damage = 50;
        viewAngle = 0;
        lastID++;
        size = 1;
        texture = Enemy1;
    }

    public boolean getEntityCoord(double[] coord) {
        coord[0] = coordX;
        coord[1] = coordY;
        return hitPoints <= 0;
    }

    public boolean getEntityCoord(int[] coord) {
        coord[0] = (int) Math.round(coordX);
        coord[1] = (int) Math.round(coordY);
        return hitPoints <= 0;
    }

    public int getEntityDamage() {
        return damage;
    }

    public int getEntityHitPoints() {
        return hitPoints;
    }

    public double getEntityAngle()
    {
        return viewAngle;
    }

    // Метод атаки сущности
    public boolean attackEntity(int entity_Damage) {
        if (hitPoints > 0) {
            hitPoints -= entity_Damage;
            return false; // Успешная атака
        } else {
            return true; // Сущность не может быть атакована, так как здоровье равно или меньше 0
        }
    }

    public boolean entityStep() {

        if (hitPoints > 0)
        {
            coordX += Utils.projectionToX(speed, Utils.degToRad(viewAngle));
            coordY += Utils.projectionToY(speed, Utils.degToRad(viewAngle));
            return false;
        }
        return true;
    }

    public void entityMapStep(GameMap map)
    {
        double[] oldXY = new double[2];
        this.getEntityCoord(oldXY);

        this.entityStep();

        //если объект шагнул в стену
        if (map.isWall(this.coordX, this.coordY))
        {
            double deltaX = this.coordX - oldXY[0];
            double deltaY = this.coordY - oldXY[1];

            //если можно продолжить движение по оси X
            if (!map.isWall(oldXY[0] + deltaX, oldXY[1]))
            {
                this.coordX = oldXY[0] + deltaX;
                this.coordY = oldXY[1];
            }
            //если можно продолжить движение по оси Y
            else if (!map.isWall(oldXY[0], oldXY[1] + deltaY))
            {
                this.coordX = oldXY[0];
                this.coordY = oldXY[1] + deltaY;
            }
            else
            {
                this.coordX = oldXY[0];
                this.coordY = oldXY[1];
            }
        }
    }

    public double getSize()
    {
        return size;
    }

    public TextureType getTextureType()
    {
        return texture;
    }

    abstract public boolean entityMovement(GameMap map, double playerX, double playerY);
}

