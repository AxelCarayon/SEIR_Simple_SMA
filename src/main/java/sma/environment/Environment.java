package sma.environment;

import sma.agents.RandomWalkingAgent;

import java.awt.Point;
import java.util.List;

public interface Environment {
    int LEFT = 0;
    int RIGHT = 1;
    int UP = 2;
    int DOWN = 3;
    int CENTER = 4;
    int UP_LEFT = 5;
    int UP_RIGHT = 6;
    int DOWN_LEFT = 7;
    int DOWN_RIGHT = 8;
    int MAX_CHUNK = 9;

    List<RandomWalkingAgent> getNeighbors(Point position);
}
