package agents.seirs;

import agents.states.SEIRSState;
import agents.states.SuceptibleSEIRSState;
import behaviors.Randomized;
import environment.SEIRSEnvironment;
import utils.YamlReader;

import java.awt.Point;
import java.util.List;

public class RandomWalkingAgentCyclic extends RandomWakingAgent implements CyclicSEIRSAgent {

    private String id;

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
