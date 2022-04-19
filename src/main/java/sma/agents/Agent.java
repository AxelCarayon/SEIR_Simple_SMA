package sma.agents;

import sma.agents.states.State;

public interface Agent {

    void changeState(State state);
    boolean contact();
    boolean incubate();
    boolean recover();
    void move();
}
