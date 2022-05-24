package agents;

import agents.seirs.RandomWakingAgent;
import agents.seirs.ThreePhasedSEIRSAgent;
import environment.SEIRSEnvironment;

import java.awt.*;

public class RandomWalkingAgent3P extends RandomWakingAgent implements ThreePhasedSEIRSAgent {

    private String id;

    public RandomWalkingAgent3P(Point position, long seed, SEIRSEnvironment environment) {
        super(position, seed, environment);
        this.id = String.valueOf(seed);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void perceive() {
        perceiveAuthorizedPositions();
    }

    @Override
    public void decide() {
        decideNextMove();
    }

    @Override
    public void act() {
        move();
    }
}
