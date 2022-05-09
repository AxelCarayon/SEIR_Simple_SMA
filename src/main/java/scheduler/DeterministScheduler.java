package scheduler;

import behaviors.Wakeable;
import utils.YamlReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Stack;

public class DeterministScheduler implements Scheduler {

    private Wakeable[] agents;
    private final Stack<String>  wakeUpOrder = new Stack<>();

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
    public void init(Wakeable[] agents) {
        this.agents = agents;
    }

    @Override
    public void doNextCycle() {
        for (int i = 0 ; i<agents.length-1; i++) {
            try {
                int next = Integer.parseInt(wakeUpOrder.pop());
                agents[next-(int)YamlReader.getParams().seed()].wakeUp();
            } catch (EmptyStackException e) {
                System.err.println("Last record entry was read, simulation cannot continue further.");
                System.exit(1);
            }
        }
    }
}
