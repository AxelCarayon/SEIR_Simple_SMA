package utils;

import sma.Agent;

import java.awt.*;
import java.util.HashMap;

public class DataAdapter {

    public static HashMap<String,String> adaptData(HashMap<Agent.State,Pair<Integer, Color>> data) {
        var map = new HashMap<String,String>();

        map.put("SUCEPTIBLE",data.get(Agent.State.SUSCEPTIBLE).getFirst().toString());
        map.put("EXPOSED",data.get(Agent.State.EXPOSED).getFirst().toString());
        map.put("INFECTED",data.get(Agent.State.INFECTED).getFirst().toString());
        map.put("RECOVERED",data.get(Agent.State.RECOVERED).getFirst().toString());

        return map;
    }
}
