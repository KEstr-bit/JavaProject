package DOM;
import java.util.ArrayList;
import java.util.List;

abstract class Gun implements AnimationControl {
    public static final double SIDE_SHIFT = 0.5;

    private TextureType texture;
    private int magazineCapacity;
    private boolean friendly;

    protected List<Entity> bullets;
    protected TextureType bulletTexture;
    protected double bulletVelocity;
    protected double bulletDamage;
    protected double bulletHP;

    protected double velocity;
    protected int ammunition;
    protected int ammoPerShot;
    protected Timer timer;

    private float frame = 0;
    private Animations animation = Animations.ANIM_SPAWN;

    public Gun(int magazineCapacity, int ammoPerShot, double velocity, double bulletVelocity, double bulletHP,
               double bulletDamage, boolean friendly, TextureType texture, TextureType bulletTexture) {
        this.bulletHP = bulletHP;
        this.magazineCapacity = magazineCapacity;
        this.ammoPerShot = ammoPerShot;
        this.velocity = velocity;
        this.bulletVelocity = bulletVelocity;
        this.bulletDamage = bulletDamage;
        this.friendly = friendly;
        this.texture = texture;
        this.bulletTexture = bulletTexture;
        this.ammunition = magazineCapacity;
        this.bullets = new ArrayList<>();
    }

    public boolean isFriendly() {
        return friendly;
    }

    public boolean hasNotBullets() {
        return bullets.isEmpty();
    }

    public TextureType getTexture() {
        return texture;
    }

    public int getMagazineCapacity() {
        return magazineCapacity;
    }

    public int getAmmo() {
        return ammunition;
    }

    public void startAnimation(Animations animation) {
        if (animation == this.animation && animation == Animations.ANIM_MOVE) {
            return;
        }

        if (timer.check()) {
            if (this.animation == Animations.ANIM_BASE || this.animation == Animations.ANIM_MOVE ||
                    (animation != Animations.ANIM_BASE && animation != Animations.ANIM_MOVE)) {
                frame = 0;
                this.animation = animation;
            }
        }
    }

    public void getAnimationState(Animations animation, int[] frame) {
        frame[0] = Math.round(this.frame);
        animation = this.animation;
    }

    @Override
    public Animations getAnimation() {
        return animation;
    }

    @Override
    public int getFrame() {
        return (int) frame;
    }

    public void updateAnimation(double delta) {
        frame += (float) (FRAME_SPEED * delta);
        if (Math.round(frame) >= TexturePack.FRAMES_COUNT) {
            frame = 0;
            animation = Animations.ANIM_BASE;
        }
    }

    public void reload() {
        if (timer.check()) {
            startAnimation(Animations.ANIM_ATTACK2);
            timer.start(0.5);
            ammunition = magazineCapacity;
        }
    }

    public void update(double delta) {
        updateAnimation(delta);

        for (int i = 0; i < bullets.size(); ) {
            if (bullets.get(i).update(delta)) {
                bullets.remove(i);
            } else {
                i++;
            }
        }
    }

    public abstract boolean shot(double cordX, double cordY, double shotAngle);
}
