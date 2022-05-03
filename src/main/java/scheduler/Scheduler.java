package scheduler;

import behaviors.Wakeable;

public interface Scheduler {

    void init(Wakeable[] agents);
    void doNextCycle();

}
