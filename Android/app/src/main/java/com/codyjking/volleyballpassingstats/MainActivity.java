package com.codyjking.volleyballpassingstats;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private StatsViewModel stats;
    private SharedPreferences sharedPref;
    private Gson gson;
    private int numPlayersVisible;

    private final String PREF_NAME = "prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        String json = sharedPref.getString("statsObj", "");

        if(!json.equals("")) {
            stats = gson.fromJson(json, StatsViewModel.class);
        }
        else {
            // create class extending ViewModel to retain data on configuration change (e.g. screen rot)
            stats = ViewModelProviders.of(this).get(StatsViewModel.class);
        }

        numPlayersVisible = sharedPref.getInt("numPlayersVisible", stats.getNumPlayers());

        for (int i = 1; i <= stats.getNumPlayers(); i++) {
            updateText("p" + i);
        }

        // Set text for EditText objects
        for(int i = 0; i < stats.getNumPlayers(); i++) {
            int id = getResources().getIdentifier("p" + (i+1) + "_name", "id", getPackageName());
            EditText text = findViewById(id);
            text.setText(stats.getName(i + 1));
            text.addTextChangedListener(filterTextWatcher);
        }

        // show/hide players
        if (numPlayersVisible > 0) {
            for (int j = 0; j < stats.getNumPlayers(); j++) {
                int id = getResources().getIdentifier("p" + (j+1) + "_layout", "id", getPackageName());

                if (j+1 <= numPlayersVisible) {
                    findViewById(id).setVisibility(View.VISIBLE);
                } else {
                    findViewById(id).setVisibility(View.GONE);
                }
            }
        }
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }

    // Returning to this Activity
    @Override
    protected void onResume() {
        super.onResume();

        if(SettingsActivity.clearAll) {
            clearAll();
            SettingsActivity.clearAll = false;
        }
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
        Intent intent = null;

        switch (item.getItemId()) {
            case R.id.action_about:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_settings:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
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

    public void clearText(View view) {
        ((TextView) view).setText("");
    }

    // pre: none
    // post: all values are set to 0.
    public void reset(View view) {
        String p = view.getTag().toString();
        int index = Integer.parseInt(p.substring(1)) - 1;

        stats.reset(index);

        updateText(p);
    }

    // pre: none
    // post: last pass for the player is removed.
    public void undo(View view) {
        String p = view.getTag().toString();
        int index = Integer.parseInt(p.substring(1)) - 1;

        stats.undo(index);

        updateText(p);
    }

    public void clearAll() {
        stats.clearAll();

        for(int i = 0; i < stats.getNumPlayers(); i++) {
            String name = "Player " + (i+1);
            int tvId = getResources().getIdentifier("p" + (i+1) + "_name", "id", getPackageName());
            TextView nameText = findViewById(tvId);
            nameText.setText(name);
        }

        for(int i = 0; i < stats.getNumPlayers(); i++) {
            updateText("p" + (i+1));
        }
    }

    // pre: none
    // post: Adds button value to total.
    public void addToTotal(int val, String p) {
        int index = Integer.parseInt(p.substring(1)) - 1;

        stats.addToTotal(index, val);

        updateText(p);
    }

    // pre: none
    // post: updates the average and individual values on screen.
    public void updateText(String p) {
        int index = Integer.parseInt(p.substring(1)) - 1;
        int id = getResources().getIdentifier("stats" + (index + 1), "id", getPackageName());
        TextView displayText = findViewById(id);

        // Set stats text (bold except for last pass).
        String display = stats.generateText(index);
        int lastIndex = display.indexOf("Last");
        final SpannableStringBuilder sb = new SpannableStringBuilder(display);
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD); // Span to make text bold
        if(lastIndex != -1) {
            sb.setSpan(bss, 0, lastIndex, Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make characters Bold (except last pass)
        }
        else {
            sb.setSpan(bss, 0, display.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make characters Bold (except last pass)
        }
        displayText.setText(sb);

        // Set group average at top
        id = getResources().getIdentifier("group_avg", "id", getPackageName());
        TextView groupAvgText = findViewById(id);
        String groupAvg = "Group Average: " + stats.getGroupAvg(numPlayersVisible);
        groupAvgText.setText(groupAvg);

        // Save data
        String json = gson.toJson(stats);
        getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().putString("statsObj", json).apply();
    }

    // pre: none
    // post: updates sharedprefs when text is changed
    private TextWatcher filterTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            for(int i = 0; i < stats.getNumPlayers(); i++) {
                int id = getResources().getIdentifier("p" + (i+1) + "_name", "id", getPackageName());
                EditText text = findViewById(id);
                stats.setName(i + 1, text.getText().toString());
            }

            String json = gson.toJson(stats);
            getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().putString("statsObj", json).apply();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
