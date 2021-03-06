package agents.seirs;

import agents.states.SEIRSState;
import agents.states.SuceptibleSEIRSState;
import behaviors.Randomized;
import environment.SEIRSEnvironment;
import utils.YamlReader;

import java.awt.*;
import java.util.List;

import static agents.seirs.FairInfectionRWAgent.rollExposition;

public abstract class RandomWakingAgent extends Randomized implements SEIRSAgent {

    protected Point position;
    protected final SEIRSEnvironment environment;
    protected SEIRSState state;

    protected List<Point> authorizedPositions;
    protected Point nextPosition;

    public RandomWakingAgent(Point position, long seed, SEIRSEnvironment environment) {
        super(seed);
        this.position = position;
        this.state = new SuceptibleSEIRSState(this);
        this.environment = environment;
        r.setSeed(seed);
    }

    protected void move() {
        state.onMovement();
        environment.notifyNewPosition(nextPosition,this);
        position = nextPosition;
    }

    protected void perceiveAuthorizedPositions() {
        authorizedPositions =  environment.perceiveAuthorizedPositions(this);
    }

    protected void decideNextMove() {
        int next = r.nextInt(authorizedPositions.size());
        nextPosition = authorizedPositions.get(next);
    }

    @Override
    public void changeState(SEIRSState SEIRSState) { this.state = SEIRSState; }

    @Override
    public boolean isExposed() {
        return rollExposition(environment, position, r.nextInt(10000));
    }

    @Override
    public boolean isInfected() {
        boolean isSick = false;
        int roll = r.nextInt(10000)+1;
        if (roll <= YamlReader.getParams().incubationRate()*10000) {
            isSick = true;
        }
        return isSick;
    }

    @Override
    public boolean isRecovered() {
        boolean isHealed = false;
        int roll = r.nextInt(10000)+1;
        if (roll <= YamlReader.getParams().recoveryRate()*10000) {
            isHealed = true;
        }
        return isHealed;
    }

    @Override
    public boolean hasLostImmunity() {
        boolean hasLostImmunity = false;
        int roll = r.nextInt(10000)+1;
        if (roll <= YamlReader.getParams().looseImmunityRate()*10000) {
            hasLostImmunity = true;
        }
        return hasLostImmunity;
    }

    @Override
    public SEIRSState getState() { return this.state; }

    @Override
    public Point getPosition() { return position; }

}
