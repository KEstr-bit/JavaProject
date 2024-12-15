package DOM;
import java.util.Map;
import java.util.Vector;

import static DOM.Animations.*;

public abstract class Weapon implements AnimationControl {

    public final int magazine_capacity;
    public final TextureType texture;
    public final TextureType bulletTexture;

    protected int bulletCount;                    //количество пуль, выпускаемых за раз
    protected double bulletSpeed;                 //скорость полета пули
    protected double  bulletDamage;                   //урон, наносимы пулей
    protected int ammunition;
    protected boolean friendly;
    protected boolean eventFl = true;
    protected float frame = 0;
    protected Animations animation = ANIM_SPAWN;

    public abstract boolean shot(double cordX, double cordY, double shotAngle, Vector<Entity> entities);

    Weapon(int magazineCapacity, int bulletCount, double bulletSpeed, double bulletDamage, boolean friendly, TextureType texture, TextureType bulletTexture)
    {
        this.magazine_capacity = magazineCapacity;
        this.texture = texture;
        this.bulletTexture = bulletTexture;
        this.bulletDamage = bulletDamage;
        this.bulletCount = bulletCount;
        this.bulletSpeed = bulletSpeed;
        this.friendly = friendly;
        this.ammunition = magazineCapacity;
    }

    public void reloading()
    {
        startAnimation(ANIM_ATTACK2);
        ammunition = magazine_capacity;
    }

    public void startAnimation(Animations animation)
    {
        frame = 0;
        this.animation = animation;
        eventFl = true;
    }

    public void startAnimation(Animations animation, int a)
    {
        if(!eventFl)
            this.animation = animation;
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

    public double getDamage()
    {
        return bulletDamage;
    }

}
