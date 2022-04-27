package environment;

import agents.SEIRSAgent;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

public interface SEIRSEnvironment extends Environment2D {
    List<SEIRSAgent> getInfectedNeighbors(Point p);

    HashMap<String, Integer> getAgentsStatus();
}
