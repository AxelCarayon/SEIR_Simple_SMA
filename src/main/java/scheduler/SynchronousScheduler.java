package scheduler;

import java.util.Random;

public abstract class SynchronousScheduler implements Scheduler {

    protected final Random r;

    public SynchronousScheduler(int seed) {
        r = new Random(seed);
    }
}
