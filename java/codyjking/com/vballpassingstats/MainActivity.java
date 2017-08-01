package codyjking.com.vballpassingstats;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // logic variables
    private int total = 0;
    private int numVals = 0;
    private double average = 0;
    private int[] values = new int[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addZero(View view) {
        addToTotal(0);
    }

    public void addOne(View view) {
        addToTotal(1);
    }

    public void addTwo(View view) {
        addToTotal(2);
    }

    public void addThree(View view) {
        addToTotal(3);
    }

    public void reset(View view)
    {
        total = 0;
        numVals = 0;
        average = 0;

        for(int i = 0; i < values.length; i++)
            values[i] = 0;

        updateText();
    }

    // pre: none
    // post: Adds button value to total.
    public void addToTotal(int val)
    {
        numVals++;
        total += val;
        values[val]++;
        average = total / (double) numVals;
        average = Double.parseDouble(String.format("%,.2f", average));

        updateText();
    }

    public void updateText()
    {
        TextView displayText = (TextView) findViewById(R.id.stats);
        displayText.setText("Average (" + numVals + "): " + average + "\nZeros: " + values[0] +
                "\nOnes: " + values[1] + "\nTwos: " + values[2] + "\nThrees: " + values[3]);
    }
}
