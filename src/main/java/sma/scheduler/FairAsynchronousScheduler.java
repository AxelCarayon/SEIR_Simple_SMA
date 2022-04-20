package sma.scheduler;

import sma.agents.Agent;
import sma.agents.RandomWalkingAgent;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FairAsynchronousScheduler implements Scheduler{

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Queue<Agent> queue;

    public FairAsynchronousScheduler(Agent[] agents) {
        this.queue = new ConcurrentLinkedQueue<>(Arrays.stream(agents).toList());
    }

    public void nextCycle() {
        List<Future<Agent>> results = queue.parallelStream().map(agent -> executor.submit(() -> {agent.move(); return agent;})).toList();
        Function<Future<Agent>, Agent> futureTreatment = futureAgent -> {
            try {
                return futureAgent.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        };
        queue = results.parallelStream().map(futureTreatment).collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
    }
}
