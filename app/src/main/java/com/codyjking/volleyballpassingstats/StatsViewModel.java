package com.codyjking.volleyballpassingstats;

import android.arch.lifecycle.ViewModel;
import java.util.Locale;


public class StatsViewModel extends ViewModel {
    private int[] total = {0, 0};
    private int[] numVals = {0, 0};
    private double[] average = {0.0, 0.0};
    private int[][] values = new int[4][4];

    public StatsViewModel() {}

    public void reset(int index) {
        total[index] = 0;
        numVals[index] = 0;
        average[index] = 0.0;

        for(int i = 0; i < values[index].length; i++)
            values[index][i] = 0;
    }

    public void addToTotal(int index, int val) {
        numVals[index]++;
        total[index] += val;
        values[index][val]++;
        average[index] = total[index] / (double) numVals[index];
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
