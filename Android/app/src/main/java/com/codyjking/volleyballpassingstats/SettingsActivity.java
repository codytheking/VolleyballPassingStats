package com.codyjking.volleyballpassingstats;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private int numPlayersVisible;
    private EditText et_numPlayers;
    private SharedPreferences sharedPref;
    private TextWatcher textWatcher;

    private final String PREF_NAME = "prefs";
    private final int NUM_PLAYERS = 12;

    static boolean clearAll = false;

    public SettingsActivity() {
        numPlayersVisible = NUM_PLAYERS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        et_numPlayers = findViewById(R.id.et_numPlayers);
        et_numPlayers.addTextChangedListener(filterTextWatcher);

        // load preferences
        sharedPref = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        numPlayersVisible = sharedPref.getInt("numPlayersVisible", NUM_PLAYERS);

        // set previously selected number of players
        et_numPlayers.setText("" + (numPlayersVisible));
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int val = 12;

            try {
                val = Integer.parseInt(et_numPlayers.getText().toString());

                if(val < 1 || val > 12) {
                    et_numPlayers.getText().clear();
                    val = 12;
                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid value.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            catch (NumberFormatException e) {
                et_numPlayers.getText().clear();
                e.printStackTrace();
                return;
            }

            getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().putInt("numPlayersVisible", val).apply();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void clearAll(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Clear All")
                .setMessage("Do you really want to clear all stats?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        clearAll = true;
                    }})

                .setNegativeButton(android.R.string.no, null).show();
    }
}
