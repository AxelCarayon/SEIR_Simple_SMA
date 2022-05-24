package behaviors;

import agents.states.SEIRSState;

public interface Infectious {
    void changeState(SEIRSState SEIRSState);
    SEIRSState getState();
    boolean isExposed();
    boolean isInfected();
    boolean isRecovered();
    boolean hasLostImmunity();
}
