package scheduler;

import behaviors.Wakeable;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FairAsynchronousScheduler implements Scheduler {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Queue<Wakeable> queue;

    @Override
    public void init(Wakeable[] agents) {
        this.queue = new ConcurrentLinkedQueue<>(Arrays.stream(agents).toList());
    }

    @Override
    public void doNextCycle() {
        List<Future<Wakeable>> results = queue.parallelStream().map(entity -> executor.submit(() -> {entity.wakeUp(); return entity;})).toList();
        Function<Future<Wakeable>, Wakeable> futureTreatment = futureEntity -> {
            try {
                return futureEntity.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        };
        queue = results.parallelStream().map(futureTreatment).collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
    }
}
