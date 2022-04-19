package sma.scheduler;

import sma.agents.RandomWalkingAgent;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FairAsynchronousScheduler implements Scheduler{

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Queue<RandomWalkingAgent> queue;

    public FairAsynchronousScheduler(RandomWalkingAgent[] agents) {
        this.queue = new ConcurrentLinkedQueue<>(Arrays.stream(agents).toList());
    }

    public void nextCycle() {

        List<Future<RandomWalkingAgent>> results = queue.parallelStream().map(agent -> executor.submit(() -> {agent.move(); return agent;})).collect(Collectors.toList());
        Function<Future<RandomWalkingAgent>, RandomWalkingAgent> futureTreatment = futureAgent -> {
            try {
                return futureAgent.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        };

        List<RandomWalkingAgent> nextQueue = results.parallelStream().map(futureTreatment).collect(Collectors.toList());
        queue = new ConcurrentLinkedQueue<>(nextQueue);
    }

}
