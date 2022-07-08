package agents.seirs;

import environment.SEIRSEnvironment;

import java.awt.*;

public class FairInfectionRWAgent3P extends FairInfectionRWAgent implements ThreePhasedSEIRSAgent {

    private final String id;

    public FairInfectionRWAgent3P(Point position, long seed, SEIRSEnvironment environment) {
        super(position, seed, environment);
        this.id = String.valueOf(seed);
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

    @Override
    public String getId() {
        return this.id;
    }
}
