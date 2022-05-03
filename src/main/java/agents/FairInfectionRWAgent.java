package agents;

import environment.SEIRSEnvironment;
import utils.YamlReader;

import java.awt.*;

public class FairInfectionRWAgent extends RandomWalkingAgent implements SEIRSAgent {

    public FairInfectionRWAgent(Point position, long seed, SEIRSEnvironment environment) {
        super(position, seed, environment);
    }

    @Override
    public boolean isExposed() {
        boolean isExposed = false;
        int roll = r.nextInt(10000)+1;
        if (this.environment.getInfectedNeighbors(position).size() != 0) {
            if (roll <= YamlReader.getParams().infectionRate()*10000) {
                isExposed = true;
            }
        }
        return isExposed;
    }
}
