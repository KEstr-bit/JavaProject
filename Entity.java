package DOM;


abstract public class Entity implements AnimationControl {
    private final TexturePack.TextureType texture;
    private final double size;
    private final int maxHealthPoints;
    private final boolean isFriendly;
    private final boolean isPushable;

    protected Timer timer = new Timer();
    protected double cordX;
    protected double cordY;
    protected double hitPoints;
    protected double damage;
    protected double velocity;
    protected double viewAngle = 180;

    protected boolean isVisible = true;
    protected float frame = 0;
    protected Animations animation = Animations.ANIM_SPAWN;

    public Entity(double cordX, double cordY, double velocity, int maxHealthPoints,
                  double damage, double size, TexturePack.TextureType texture, boolean isFriendly, boolean isPushable) {
        this.texture = texture;
        this.size = size;
        this.maxHealthPoints = maxHealthPoints;
        this.hitPoints = maxHealthPoints;
        this.isFriendly = isFriendly;
        this.isPushable = isPushable;
        this.cordX = cordX;
        this.cordY = cordY;
        this.velocity = velocity;
        this.damage = damage;
        timer.start(0.5);
    }

    public boolean isAlive() {
        return hitPoints > 0.1;
    }

    public boolean isFriendly() {
        return isFriendly;
    }

    public boolean isPushable() {
        return isPushable;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void getCords(int[] coordinates) {
        coordinates[0] = (int) Math.round(this.cordX);
        coordinates[1] = (int) Math.round(this.cordY);
    }

    public void getCords(double[] coordinates) {
        coordinates[0] = this.cordX;
        coordinates[1] = this.cordY;
    }

    public double getDamage() {
        return damage;
    }

    public double getAngle() {
        return viewAngle;
    }

    public double getSize() {
        return size;
    }

    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }

    public TexturePack.TextureType getTexture() {
        return texture;
    }

    public void takeDamage(double damage) {
        if (isAlive()) {
            hitPoints -= damage;
            startAnimation(Animations.ANIM_TAKING_DAMAGE);

            if (!isAlive()) {
                startAnimation(Animations.ANIM_DIE);
                timer.start(0.5);
            }
        }
    }

    public void kill() {
        if (isAlive()) {
            hitPoints = 0;
            startAnimation(Animations.ANIM_DIE);
            timer.start(0.5);
        }
    }

    protected void baseStep(double delta) {
        cordX += Helper.projectToX(velocity * delta / TexturePack.FRAMES_COUNT, Helper.degToRad(viewAngle));
        cordY += Helper.projectToY(velocity * delta / TexturePack.FRAMES_COUNT, Helper.degToRad(viewAngle));
    }

    public boolean move(double delta) {
        double oldX, oldY;
        double[] oldCoordinates = new double[2];
        getCords(oldCoordinates);
        oldX = oldCoordinates[0];
        oldY = oldCoordinates[1];

        double sizeShiftX = size / 2;
        double sizeShiftY = sizeShiftX;

        baseStep(delta);

        double deltaX = this.cordX - oldX;
        if (deltaX < 0) {
            sizeShiftX *= -1;
        }

        double deltaY = this.cordY - oldY;
        if (deltaY < 0) {
            sizeShiftY *= -1;
        }

        if (GameMap.isWall(this.cordX + sizeShiftX, this.cordY)) {
            if (GameMap.isWall(this.cordX, this.cordY + sizeShiftY)) {
                this.cordX = oldX;
                this.cordY = oldY;
            } else {
                this.cordX = oldX;
                this.cordY = oldY + deltaY;
            }
            return true;
        }

        if (GameMap.isWall(this.cordX, this.cordY + sizeShiftY)) {
            this.cordX = oldX + deltaX;
            this.cordY = oldY;
            return true;
        }

        return false;
    }

    public boolean pushAt(double delta, double angle) {
        double oldAngle = viewAngle;
        viewAngle = angle;
        boolean didMove = move(delta);
        viewAngle = oldAngle;
        return didMove;
    }

    public boolean intersects(Entity other, float coefficient) {
        double distance = Helper.calcDistance(cordX, cordY, other.cordX, other.cordY);
        return distance < (size + other.size) * coefficient / 2;
    }

    @Override
    public void startAnimation(Animations animation) {
        frame = 0;
        this.animation = animation;
    }

    @Override
    public Animations getAnimation() {
        return animation;
    }

    @Override
    public int getFrame() {
        return (int) frame;
    }

    @Override
    public void updateAnimation(double delta) {
        frame += (float) (FRAME_SPEED * delta);
        if ((int) Math.round(frame) >= TexturePack.FRAMES_COUNT) {
            frame = 0;
            animation = Animations.ANIM_BASE;
        }
    }

    // Additional abstract method that must be implemented by subclasses
    public abstract boolean update(double delta);
}

