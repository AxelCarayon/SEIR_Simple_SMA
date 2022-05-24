package scheduler;

import agents.Agent;
import agents.CyclicAgent;
import agents.ThreePhasedAgent;
import utils.YamlReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("ConstantConditions")
public class DeterministScheduler implements Scheduler {

    private Agent[] agents;
    private final Queue<String> wakeUpOrder = new LinkedList<>();

    private Boolean isThreePhased;

    public DeterministScheduler(String csvFile) {
        readCSV(csvFile);
    }

    private void readCSV(String file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            String[] values = line.split(",");
            wakeUpOrder.addAll(Arrays.asList(values));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(Agent[] agents) {
        isThreePhased = switch (agents[0]) {
            case ThreePhasedAgent ignored -> true;
            case CyclicAgent ignored -> false;
        };
        this.agents = agents;
    }

    private void wakeUpNextCycle(CyclicAgent[] agents) {
        for (int i = 0 ; i<agents.length; i++) {
            try {
                int next = Integer.parseInt(wakeUpOrder.poll());
                agents[next-(int)YamlReader.getParams().seed()].wakeUp();
            } catch (EmptyStackException e) {
                System.err.println("Last record entry was read, simulation cannot continue further.");
                System.exit(1);
            }
        }
    }

    private void perceiveNextCycle(ThreePhasedAgent[] agents, List<Integer> nextCycle) {
        for (Integer i : nextCycle) {
            agents[i].perceive();
        }
    }

    private void decideNextCycle(ThreePhasedAgent[] agents, List<Integer> nextCycle) {
        for (Integer i : nextCycle) {
            agents[i].decide();
        }
    }

    private void actNextCycle(ThreePhasedAgent[] agents, List<Integer> nextCycle) {
        for (Integer i : nextCycle) {
            agents[i].act();
        }
    }

    private List<Integer> getNextCycle() {
        List<Integer> nextCycle = new ArrayList<>();
        for (int i = 0 ; i<agents.length; i++ ) {
            try {
                nextCycle.add(Integer.parseInt(wakeUpOrder.poll()));
            } catch (EmptyStackException e) {
                System.err.println("Last record entry was read, simulation cannot continue further.");
                System.exit(1);
            }
        }
        return nextCycle;
    }

    @Override
    public void doNextCycle() {
        if (isThreePhased) {
            List<Integer> nextCycle = getNextCycle();
            ThreePhasedAgent[] agents = (ThreePhasedAgent[])this.agents;
            perceiveNextCycle(agents,nextCycle);
            decideNextCycle(agents,nextCycle);
            actNextCycle(agents,nextCycle);
        } else {
            CyclicAgent[] agents = (CyclicAgent[]) this.agents;
            wakeUpNextCycle(agents);
        }
    }
}
