package sma;

import java.awt.Point;
import java.util.List;

public interface IEnvironment {
    Boolean isCaseEmpty(Point p);
    void fillCase(Agent a,Point p);
    void emptyCase(Point p);
    List<Agent> getNeighbors(Point position);
}
