package agents;

import agents.states.SEIRSState;
import behaviors.Positionable2D;

public interface SEIRSAgent extends Agent, Positionable2D {

    void changeState(SEIRSState SEIRSState);
    SEIRSState getState();
    boolean isExposed();
    boolean isInfected();
    boolean isRecovered();
    boolean hasLostImmunity();
}
