package sma.agents.states;

import sma.agents.Agent;

public abstract class State {

    public final static String EXPOSED = "EXPOSED";
    public final static String INFECTED = "INFECTED";
    public final static String SUCEPTIBLE = "SUCEPTIBLE";
    public final static String RECOVERED = "RECOVERED";

    protected Agent agent;

    State(Agent agent) {
        this.agent = agent;
    }

    public abstract void onMovement();

}
