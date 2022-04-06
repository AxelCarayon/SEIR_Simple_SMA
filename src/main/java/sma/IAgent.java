package sma;

public interface IAgent {

    public enum State {
        SUSCEPTIBLE,
        EXPOSED,
        INFECTED,
        RECOVERED
    }

    void move();
    void contact();
    void getStatus();
}
