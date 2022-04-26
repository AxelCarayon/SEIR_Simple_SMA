package environment;

import agents.Agent;

import java.awt.*;

public interface Environment2D extends Environment {

    void notifyNewPosition(Point oldPosition, Point newPosition, Agent agent);
}
