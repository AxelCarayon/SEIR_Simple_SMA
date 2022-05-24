package agents;

import behaviors.Cyclic;

public sealed interface Agent
permits CyclicAgent, ThreePhasedAgent{

    String getId();
}
