package agents.seirs;

import environment.SEIRSEnvironment;
import utils.YamlReader;

import java.awt.*;

public class FairInfectionRWAgentCyclic extends FairInfectionRWAgent implements CyclicSEIRSAgent {

    private String id;
    public FairInfectionRWAgentCyclic(Point position, long seed, SEIRSEnvironment environment) {
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
