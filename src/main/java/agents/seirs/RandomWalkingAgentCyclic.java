package agents.seirs;

import environment.SEIRSEnvironment;

import java.awt.Point;

public class RandomWalkingAgentCyclic extends RandomWakingAgent implements CyclicSEIRSAgent {

    private final String id;

    public RandomWalkingAgentCyclic(Point position, long seed, SEIRSEnvironment environment) {
        super(position, seed, environment);
        this.id = String.valueOf(seed);
    }

    @Override
    public void wakeUp() {
        perceiveAuthorizedPositions();
        if (!authorizedPositions.isEmpty()) {
            decideNextMove();
            move();
        }
    }
    @Override
    public String getId() {
        return this.id;
    }

}
