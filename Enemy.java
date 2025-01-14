package DOM;

abstract class Enemy extends Entity {
    protected Entity target; // Using Optional to handle nullability
    private static final double VISION_STEP = 0.2;

    public Enemy(double cordX, double cordY, double velocity, int maxHitPoints, double damage,
                 double size, TextureType texture, Entity target) {
        super(cordX, cordY, velocity, maxHitPoints, damage, size, texture, false, true);
        this.target = target;
    }

    protected boolean isTargetSeen() {
        double[] targetCords = new double[2];
        target.getCords(targetCords); // Safely access target

        double[] enemyCords = new double[2];
        this.getCords(enemyCords);

        boolean notHit = true;
        double distance = Helper.calcDistance(enemyCords[0], enemyCords[1], targetCords[0],targetCords[1]);

        viewAngle += Math.atan2((targetCords[0] - enemyCords[0]) / distance, (targetCords[1] - enemyCords[1]) / distance);

        while (distance > VISION_STEP && notHit) {
            enemyCords[0] = Helper.interpolateCords(enemyCords[0], targetCords[0], VISION_STEP, distance);
            enemyCords[1] = Helper.interpolateCords(enemyCords[1], targetCords[1], VISION_STEP, distance);

            distance = Helper.calcDistance(enemyCords[0], enemyCords[1], targetCords[0], targetCords[1]);

            if (GameMap.isWall(enemyCords[0], enemyCords[1])) {
                notHit = false;
            }
        }

        return notHit;
    }

    public double lookAtTarget() {
        double distance = 10;

        double[] targetCords = new double[2];
        target.getCords(targetCords); // Safely access target

        double deltaX = targetCords[0] - cordX;
        double deltaY = targetCords[1] - cordY;

        distance = Helper.calcDistance(targetCords[0], targetCords[1], cordX, cordY);
        double angleCos = deltaX / distance;
        double angleSin = deltaY / distance;
        viewAngle = Helper.radToDeg(Math.atan2(angleSin, angleCos));

        return distance;
    }
}
