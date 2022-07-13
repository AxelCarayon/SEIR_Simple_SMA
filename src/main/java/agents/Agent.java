package agents;

public sealed interface Agent
permits CyclicAgent, ThreePhasedAgent{

    String getId();
}
