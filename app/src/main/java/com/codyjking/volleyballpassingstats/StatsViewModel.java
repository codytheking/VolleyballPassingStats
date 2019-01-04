package com.codyjking.volleyballpassingstats;

import android.arch.lifecycle.ViewModel;
import java.util.Locale;


public class StatsViewModel extends ViewModel {
    private int[] totals;
    private int[] numVals;
    private double[] average;
    private int[][] values;

    private final int NUM_PLAYERS = 6;

    public StatsViewModel() {
        totals = new int[NUM_PLAYERS];
        numVals = new int[NUM_PLAYERS];
        average = new double[NUM_PLAYERS];
        values = new int[NUM_PLAYERS][4];

        for(int i = 0; i < NUM_PLAYERS; i++) {
            reset(i);
        }
    }

    public int getNumPlayers() {
        return NUM_PLAYERS;
    }

    public void reset(int index) {
        totals[index] = 0;
        numVals[index] = 0;
        average[index] = 0.0;

        for(int i = 0; i < values[index].length; i++)
            values[index][i] = 0;
    }

    public void addToTotal(int index, int val) {
        numVals[index]++;
        totals[index] += val;
        values[index][val]++;
        average[index] = totals[index] / (double) numVals[index];
        average[index] = Double.parseDouble(String.format(Locale.ROOT,"%,.2f", average[index]));
    }

    public String generateText(int index) {
        if(Locale.getDefault().getLanguage().equalsIgnoreCase("es")) {
            return ("Promedio (" + numVals[index] + "): " + average[index] + "\nCero: " + values[index][0] +
                    " Uno: " + values[index][1] + " Dos: " + values[index][2] + " Tres: " + values[index][3]);
        }

        return ("Average (" + numVals[index] + "): " + average[index] + "\nZeros: " + values[index][0] +
                " Ones: " + values[index][1] + " Twos: " + values[index][2] + " Threes: " + values[index][3]);
    }
}
