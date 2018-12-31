package com.codyjking.volleyballpassingstats;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    private ToggleButton toggle;
    private ConstraintLayout p2_layout;
    private StatsViewModel stats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        p2_layout = findViewById(R.id.p2_layout);
        toggle = findViewById(R.id.playerToggle);

        // create class extending ViewModel to retain data on configuration change (e.g. screen rot)
        stats = ViewModelProviders.of(this).get(StatsViewModel.class);
        updateText("p1");
        updateText("p2");

        // check for toggle switch and hide/show second player
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    p2_layout.setVisibility(View.GONE);
                }

                else {
                    p2_layout.setVisibility(View.VISIBLE);
                }

                //Toast.makeText(getApplicationContext(), String.valueOf(buttonView.isChecked()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Touch outside of edittext removes focus and closes keyboard
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if(v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if(!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    // Add options menu to actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // Actions for menu options
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void addZero(View view) {
        addToTotal(0, view.getTag().toString());
    }

    public void addOne(View view) {
        addToTotal(1, view.getTag().toString());
    }

    public void addTwo(View view) {
        addToTotal(2, view.getTag().toString());
    }

    public void addThree(View view) {
        addToTotal(3, view.getTag().toString());
    }

    // pre: none
    // post: all values are set to 0.
    public void reset(View view)
    {
        String p = view.getTag().toString();
        int index = 0;
        if(p.equals("p2"))
            index = 1;

        stats.reset(index);

        updateText(p);
    }

    // pre: none
    // post: Adds button value to total.
    public void addToTotal(int val, String p)
    {
        int index = 0;
        if(p.equals("p2"))
            index = 1;

        stats.addToTotal(index, val);

        updateText(p);
    }

    // pre: none
    // post: updates the average and individual values on screen.
    public void updateText(String p)
    {
        TextView displayText = findViewById(R.id.stats);
        int index = 0;
        if(p.equals("p2")) {
            index = 1;
            displayText = findViewById(R.id.stats2);
        }

        String display = stats.generateText(index);
        displayText.setText(display);
    }
}
