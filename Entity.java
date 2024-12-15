package DOM;


import java.util.Vector;

import static DOM.Animations.*;

public abstract class Entity implements AnimationControl {
    public final TextureType texture;
    public final double size;
    protected double cordX;
    protected double cordY;
    protected double hitPoints;
    protected double damage;
    protected double speed;
    protected double viewAngle;
    protected boolean friendly;
    protected boolean eventFl = false;
    protected float frame = 0;
    protected Animations animation = ANIM_SPAWN;

    public void startAnimation(Animations animation)
    {
        frame = 0;
        this.animation = animation;
        eventFl = true;
    }

    public Animations getAnimation()
    {
        return this.animation;
    }

    public int getFrame()
    {
        return (int)(Math.round(this.frame));
    }

    public void updateAnimation()
    {
        frame += FRAME_SPEED;
        if (Math.round(frame) >= TexturePack.FRAMES_COUNT)
        {
            frame = 0;
            animation = ANIM_BASE;
            eventFl = false;
        }
    }

    protected void baseStep() {
        cordX += Utils.projectToX(speed, Utils.degToRad(viewAngle));
        cordY += Utils.projectToY(speed, Utils.degToRad(viewAngle));
    }

    public Entity(double cordX, double cordY, double speed, double hitPoints,
                  double damage, double size, TextureType texture, boolean friendly)
    {
        this.cordX = cordX;
        this.cordY = cordY;
        this.speed = speed;
        this.damage = damage;
        this.hitPoints = hitPoints;
        this.friendly = friendly;
        this.texture = texture;
        this.size = size;
    }

    boolean isFriendly()
    {
        return friendly;
    }

    boolean isAlive()
    {
        return hitPoints > 0.1;
    }

    public Cords getCords() {
        return new Cords(cordX, cordY);
    }

    public Cords getCords(int a) {
        return new Cords(Math.round(cordX), Math.round(cordY));
    }

    public double getDamage() {
        return damage;
    }

    public double getHitPoints() {
        return hitPoints;
    }

    public double getAngle()
    {
        return viewAngle;
    }

    // Метод атаки сущности
    public void dealDamage(double damage) {
        hitPoints -= damage;
        if (isAlive())
            startAnimation(ANIM_TAKING_DAMAGE, 1);
        else if(animation != ANIM_DIE)
            startAnimation(ANIM_DIE);
    }

    public void kill()
    {
        hitPoints = 0;
        startAnimation(ANIM_DIE);
    }

    public boolean mapStep(GameMap map)
    {
        Cords oldCords;
        oldCords = this.getCords();

        double sizeShiftX = size / 2;
        double sizeShiftY = sizeShiftX;

        this.baseStep();

        double deltaX = this.cordX - oldCords.getX();
        if (deltaX < 0)
            sizeShiftX *= -1;

        double deltaY = this.cordY - oldCords.getY();
        if (deltaY < 0)
            sizeShiftY *= -1;


//если объект шагнул в стену
        if (map.isWall(this.cordX + sizeShiftX, this.cordY))
        {
            if (map.isWall(this.cordX, this.cordY + sizeShiftY))
            {
                this.cordX = oldCords.getX();
                this.cordY = oldCords.getY();
            }
            else
            {
                this.cordX = oldCords.getX();
                this.cordY = oldCords.getY() + deltaY;
            }
            return true;
        }
        else
        {
            if (map.isWall(this.cordX, this.cordY + sizeShiftY))
            {
                this.cordX = oldCords.getX() + deltaX;
                this.cordY = oldCords.getY();
                return true;
            }

        }

        return false;
    }

    public boolean directionStep(GameMap map, double angle)
    {
	    double oldAngle = viewAngle;
        viewAngle = angle;
	    boolean fl = mapStep(map);
        viewAngle = oldAngle;
        return fl;
    }

    public boolean intersects(Entity other, float coefficient)
    {
	    double distance = Utils.calcDistance(cordX, cordY, other.cordX, other.cordY);
        return distance < (size + other.size) * coefficient / 2;
    }

    public void startAnimation(Animations animation, int a)
    {
        frame = 0;
        this.animation = animation;
    }

    abstract public boolean update(GameMap map, Vector<Entity> entities);


}

