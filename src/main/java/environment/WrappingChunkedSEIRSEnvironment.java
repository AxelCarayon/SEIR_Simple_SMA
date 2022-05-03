package environment;

import agents.SEIRSAgent;
import behaviors.Positionable2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WrappingChunkedSEIRSEnvironment extends ChunkedSEIRSEnvironment implements SEIRSEnvironment{

    public WrappingChunkedSEIRSEnvironment(int size, SEIRSAgent[] agents) {
        super(size, agents);
    }

    private void wrapPosition(Point newPosition) {
        if (newPosition.x >= size) {
            newPosition.x -= size-1;
        }
        if (newPosition.x < 0) {
            newPosition.x += size-1;
        }
        if (newPosition.y >= size) {
            newPosition.y -= size-1;
        }
        if (newPosition.y < 0) {
            newPosition.y +=size-1;
        }
    }

    @Override
    public List<Point> perceiveAuthorizedPositions(Positionable2D agent) {
        List<Point> authorisedPositions = new ArrayList<>();
        for (int move = 0; move < MAX_MOVEMENT; move++) {
            Point position = getNextPosition(move,agent.getPosition());
            wrapPosition(position);
            authorisedPositions.add(position);
        }
        return authorisedPositions;
    }
}
