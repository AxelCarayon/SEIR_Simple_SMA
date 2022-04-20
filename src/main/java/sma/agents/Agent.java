package sma.agents;

import sma.agents.states.State;

import java.awt.*;

public interface Agent {

    void changeState(State state);
    State getState();
    boolean isExposed();
    boolean isInfected();
    boolean isRecovered();
    boolean hasLostImmunity();
    void move();
    Point getPosition();
}
