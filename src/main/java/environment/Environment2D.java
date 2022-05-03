package environment;

import agents.Agent2D;

import java.awt.*;
import java.util.List;

public interface Environment2D extends Environment {

    int FORBIDDEN = -1;
    int LEFT = 0;
    int RIGHT = 1;
    int UP = 2;
    int DOWN = 3;
    int CENTER = 4;
    int MAX_MOVEMENT = CENTER;
    int UP_LEFT = 5;
    int UP_RIGHT = 6;
    int DOWN_LEFT = 7;
    int DOWN_RIGHT = 8;
    int MAX_CHUNK = 9;

    List<Point> perceiveAuthorizedPositions(Agent2D agent);
    void notifyNewPosition(Point newPosition, Agent2D agent);
    int getSize();
}
