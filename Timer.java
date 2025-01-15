package DOM;

import java.time.Duration;
import java.time.Instant;

public class Timer {
    private Instant startTime;
    private double duration;
    private boolean running;

    public Timer() {
        duration = 0;
        running = false;
    }

    public void start(double seconds) {
        duration = seconds;
        running = true;
        startTime = Instant.now();
    }

    public boolean check() {
        if (!running) return true;

        Instant now = Instant.now();
        double elapsed = Duration.between(startTime, now).getNano() / 1000000000.0;

        if (elapsed >= duration) {
            running = false; // Stop the timer
            return true; // Timer has expired
        }
        return false; // Timer has not expired yet
    }
}
