package agents;

import agents.states.SEIRSState;

public interface SEIRSAgent extends Agent2D {

    void changeState(SEIRSState SEIRSState);
    SEIRSState getState();
    boolean isExposed();
    boolean isInfected();
    boolean isRecovered();
    boolean hasLostImmunity();
}
