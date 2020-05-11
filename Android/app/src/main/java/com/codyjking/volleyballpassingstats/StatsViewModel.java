package com.codyjking.volleyballpassingstats;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class StatsViewModel extends ViewModel {
    private int[] totals;
    private int[] numVals;
    private double[] average;
    private int[][] values;
    private String[] names;
    private List<List<Integer>> lastPasses;  // history for undo

    private final int NUM_PLAYERS = 12;

    public StatsViewModel() {
        totals = new int[NUM_PLAYERS];
        numVals = new int[NUM_PLAYERS];
        average = new double[NUM_PLAYERS];
        values = new int[NUM_PLAYERS][4];
        names = new String[NUM_PLAYERS];
        lastPasses = new ArrayList<>(NUM_PLAYERS);

        for(int i = 0; i < NUM_PLAYERS; i++) {
            reset(i);
            names[i] = "Player " + (i+1);
        }
    }

    public int getNumPlayers() {
        return NUM_PLAYERS;
    }

    public void reset(int index) {
        totals[index] = 0;
        numVals[index] = 0;
        average[index] = 0.0;

        for(int i = 0; i < values[index].length; i++) {
            values[index][i] = 0;
        }

        if(lastPasses.size() > 0) {
            lastPasses.set(index, new ArrayList<Integer>());
        }
    }

    public void clearAll() {
        // check if history is already there
        if(lastPasses.size() == 0) {
            lastPasses = new ArrayList<>(NUM_PLAYERS);
            for (int i = 0; i < NUM_PLAYERS; i++) {
                lastPasses.add(new ArrayList<Integer>());
            }
        }

        for(int i = 0; i < NUM_PLAYERS; i++) {
            reset(i);
            names[i] = "Player " + (i+1);

            lastPasses.get(i).clear();
        }
    }

    public void undo(int i) {
        int len = lastPasses.get(i).size();
        if(len != 0) {
            int pass = lastPasses.get(i).remove(len - 1);
            numVals[i]--;
            totals[i] -= pass;
            values[i][pass]--;
            calcAvg(i);
        }
    }

    public double getGroupAvg(int numVisible) {
        int sum = 0;
        int passes = 0;

        for(int i = 0; i < numVisible; i++) {
            sum += totals[i];
            passes += numVals[i];
        }

        if(passes == 0) {
            return 0.0;
        }

        return Double.parseDouble(String.format(Locale.ROOT, "%,.2f", (double) sum / passes));
    }

    public void setName(int player, String name) {
        names[player - 1] = name;
    }

    // pre: i is the player number
    // post: name for player is returned
    public String getName(int i) { return names[i-1]; }

    public void addToTotal(int index, int val) {
        numVals[index]++;
        totals[index] += val;
        values[index][val]++;
        if(lastPasses.size() == 0) {
            lastPasses = new ArrayList<>(NUM_PLAYERS);
            for (int i = 0; i < NUM_PLAYERS; i++) {
                lastPasses.add(new ArrayList<Integer>());
            }
        }
        lastPasses.get(index).add(val);
        calcAvg(index);
    }

    private void calcAvg(int index) {
        if(numVals[index] == 0) {
            average[index] = 0.0;
        }
        else {
            average[index] = totals[index] / (double) numVals[index];
            average[index] = Double.parseDouble(String.format(Locale.ROOT, "%,.2f", average[index]));
        }
    }

    public String generateText(int index) {
        String last = "";
        if(Locale.getDefault().getLanguage().equalsIgnoreCase("es")) {
            if(lastPasses.get(index).size() > 0) {
                last = "Pase anterior: " + lastPasses.get(index).get(lastPasses.get(index).size() - 1);
            }
            return ("Promedio: " + average[index] + " (" + numVals[index] + " pasos)\nCero: " + values[index][0] +
                    " Uno: " + values[index][1] + " Dos: " + values[index][2] + " Tres: " + values[index][3] +
                    "\n" + last);
        }

        if(lastPasses.get(index).size() > 0) {
            last = "Last pass: " + lastPasses.get(index).get(lastPasses.get(index).size() - 1);
        }
        return ("Average: " + average[index] + " (" + numVals[index] + " passes)\nZeros: " + values[index][0] +
                " Ones: " + values[index][1] + " Twos: " + values[index][2] + " Threes: " + values[index][3] +
                "\n" + last);
    }

    public String generateExportText(int index) {
        if(Locale.getDefault().getLanguage().equalsIgnoreCase("es")) {
            return ("Promedio: " + average[index] + " (" + numVals[index] + " pasos)\nCero: " + values[index][0] +
                    " Uno: " + values[index][1] + " Dos: " + values[index][2] + " Tres: " + values[index][3]);
        }

        return ("Average: " + average[index] + " (" + numVals[index] + " passes)\nZeros: " + values[index][0] +
                " Ones: " + values[index][1] + " Twos: " + values[index][2] + " Threes: " + values[index][3]);
    }
}
