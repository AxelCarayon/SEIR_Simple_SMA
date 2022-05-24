package agents.states;

import agents.seirs.CyclicSEIRSAgent;
import agents.seirs.SEIRSAgent;

public abstract class SEIRSState {

    public final static String EXPOSED = "EXPOSED";
    public final static String INFECTED = "INFECTED";
    public final static String SUCEPTIBLE = "SUCEPTIBLE";
    public final static String RECOVERED = "RECOVERED";

    protected final SEIRSAgent agent;

    SEIRSState(SEIRSAgent agent) {
        this.agent = agent;
    }

    public abstract void onMovement();

}
