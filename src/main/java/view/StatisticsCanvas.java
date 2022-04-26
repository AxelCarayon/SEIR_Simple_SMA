package view;

import agents.states.SEIRSState;
import utils.YamlReader;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class StatisticsCanvas extends JPanel {

    private final int canvasWidth;
    private final int canvasHeight;
    private HashMap<String,Integer> values;
    private final int total;

    public StatisticsCanvas(int width,int height) {
        this.setDoubleBuffered(false);
        canvasHeight = height;
        canvasWidth = width;
        values = new HashMap<>();
        total = YamlReader.getParams().getPopulation();
        setSize(width, height);
        setVisible(true);
    }

    private Color stringToColor(String str) {
        return switch (str){
            case SEIRSState.EXPOSED -> Color.YELLOW;
            case SEIRSState.SUCEPTIBLE -> Color.GRAY;
            case SEIRSState.INFECTED -> Color.RED;
            case SEIRSState.RECOVERED -> Color.GREEN;
            default -> throw new IllegalStateException("Illegal state : "+str);
        };
    }

    public void updateValues(HashMap<String,Integer> values ) {
        this.values = values;
    }

    @Override
    public void paint(Graphics g) {
        int start = 0;
        g.clearRect(0,0,getWidth(),getHeight());
        for (int i=0 ; i <values.keySet().size();i++) {
            String state = (String) values.keySet().toArray()[i];
            int value = values.get(state);
            g.setColor(stringToColor(state));
            float height = ((float)value/total)*canvasHeight;
            g.fillRect(10,start,canvasWidth/4,start+(int)height);
            start +=height;
            g.setColor(Color.BLACK);
            g.drawString(state + " : "+value,canvasWidth/2,canvasHeight/values.keySet().size()*(1+i)-100);
        }

    }
}
