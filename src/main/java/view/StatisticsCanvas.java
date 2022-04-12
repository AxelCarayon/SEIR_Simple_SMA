package view;

import sma.Agent;
import utils.Pair;
import utils.YamlReader;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatisticsCanvas extends Canvas {

    private int canvasWidth;
    private int canvasHeight;
    private HashMap<Agent.State,Pair<Integer,Color>> values;
    private int total;

    public StatisticsCanvas(int width,int height) {
        canvasHeight = height;
        canvasWidth = width;
        values = new HashMap<>();
        total = YamlReader.getParams().getPopulation();
        setSize(width, height);
        setVisible(true);
    }

    public void updateValues(HashMap<Agent.State,Pair<Integer,Color>> values ) {
        this.values = values;
    }

    @Override
    public void paint(Graphics g) {
        int start = 0;

        for (int i=0 ; i <values.keySet().size();i++) {
            Agent.State state = values.keySet().stream().toList().get(i);
            Pair<Integer,Color> subpopulation = values.get(state);
            g.setColor(subpopulation.getSecond());
            float height = ((float)subpopulation.getFirst()/total)*canvasHeight;
            g.fillRect(10,start,canvasWidth/4,start+(int)height);
            start +=height;

            g.setColor(Color.BLACK);
            switch (state) {
                case SUSCEPTIBLE -> g.drawString("SUSCEPTIBLE : " + subpopulation.getFirst(),canvasWidth/2,canvasHeight/values.keySet().size()*(1+i)-100);
                case EXPOSED -> g.drawString("EXPOSED : " + subpopulation.getFirst(),canvasWidth/2,canvasHeight/values.keySet().size()*(1+i)-100);
                case INFECTED -> g.drawString("INFECTED : " + subpopulation.getFirst(),canvasWidth/2,canvasHeight/values.keySet().size()*(1+i)-100);
                case RECOVERED -> g.drawString("RECOVERED : " + subpopulation.getFirst(),canvasWidth/2,canvasHeight/values.keySet().size()*(1+i)-100);
            }
        }

    }
}
