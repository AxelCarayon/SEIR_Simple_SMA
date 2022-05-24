package scheduler;

import agents.Agent;
import agents.CyclicAgent;
import agents.ThreePhasedAgent;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FairAsynchronousScheduler implements Scheduler {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Queue<Agent> queue;

    private Boolean isThreePhased;

    @Override
    public void init(Agent[] agents) {
        this.queue = new ConcurrentLinkedQueue<>(Arrays.stream(agents).toList());
        isThreePhased = switch (agents[0]) {
            case ThreePhasedAgent ignored -> true;
            case CyclicAgent ignored -> false;
        };
    }

    private void wakeAll() {
        List<Future<Agent>> results = queue.parallelStream()
                .map(entity -> executor.submit(() -> {
                    ((CyclicAgent)entity).wakeUp(); return entity;})).toList();
        getFutureAgents(results);
    }

    private void perceiveAll() {
        List<Future<Agent>> results = queue.parallelStream()
                .map(entity -> executor.submit(() -> {
                    ((ThreePhasedAgent)entity).perceive(); return entity;})).toList();
        getFutureAgents(results);
    }

    private void getFutureAgents(List<Future<Agent>> results) {
        Function<Future<Agent>, Agent> futureTreatment = futureEntity -> {
            try {
                return futureEntity.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        };
        queue = results.parallelStream().map(futureTreatment).collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
    }

    private void decideAll() {
        List<Future<Agent>> results = queue.parallelStream()
                .map(entity -> executor.submit(() -> {
                    ((ThreePhasedAgent)entity).decide(); return entity;})).toList();
        getFutureAgents(results);
    }

    private void actAll() {
        List<Future<Agent>> results = queue.parallelStream()
                .map(entity -> executor.submit(() -> {
                    ((ThreePhasedAgent)entity).act(); return entity;})).toList();
        getFutureAgents(results);
    }

    @Override
    public void doNextCycle() {
        if (isThreePhased) {
            perceiveAll();
            decideAll();
            actAll();
        } else {
            wakeAll();
        }
    }
}
