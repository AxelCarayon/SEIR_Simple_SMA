package agents.seirs;

import agents.CyclicAgent;
import agents.states.SEIRSState;
import behaviors.Infectious;
import behaviors.Positionable2D;

public interface CyclicSEIRSAgent extends CyclicAgent, SEIRSAgent {

    void wakeUp();
}
