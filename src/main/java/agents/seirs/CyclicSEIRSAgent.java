package agents.seirs;

import agents.CyclicAgent;

public interface CyclicSEIRSAgent extends CyclicAgent, SEIRSAgent {

    void wakeUp();
}
