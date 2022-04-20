package sma.agents;

import sma.agents.states.State;

public interface Agent {

    void changeState(State state);
    boolean isExposed();
    boolean isInfected();
    boolean isRecovered();
    boolean hasLostImmunity();
    void move();
}
