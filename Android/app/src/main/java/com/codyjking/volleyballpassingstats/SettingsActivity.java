package com.codyjking.volleyballpassingstats;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    private StatsViewModel stats;
    private int numPlayersVisible;
    private EditText et_numPlayers;
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
        SharedPreferences sharedPref = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        numPlayersVisible = sharedPref.getInt("numPlayersVisible", NUM_PLAYERS);

        // set previously selected number of players
        et_numPlayers.setText("" + (numPlayersVisible));

        Gson gson = new Gson();
        String json = sharedPref.getString("statsObj", "");
        stats = gson.fromJson(json, StatsViewModel.class);
    }

    private final TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int val = 12;

            try {
                val = Integer.parseInt(et_numPlayers.getText().toString());
                numPlayersVisible = val;

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

    public void exportData(View view) {
        String timeStamp = new SimpleDateFormat("E MM-dd-yyyy HH:mm", Locale.forLanguageTag("en_us")).format(Calendar.getInstance().getTime());
        String shortTimeStamp = new SimpleDateFormat("MM-dd-yyyy_HH-mm", Locale.forLanguageTag("en_us")).format(Calendar.getInstance().getTime());

        String statsText = "Group Average: " + stats.getGroupAvg(numPlayersVisible) + "\n\n";

        for (int i = 0; i < numPlayersVisible; i++) {
            String name = stats.getName(i + 1);

            if(name.length() == 0) {
                name = "No Name (P" + (i+1) + ")";
            }

            statsText += name + "\n" + stats.generateExportText(i) + "\n\n";
        }

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{""});
        i.putExtra(Intent.EXTRA_SUBJECT, "Passing Stats Data Export (" + shortTimeStamp + ")");
        i.putExtra(Intent.EXTRA_TEXT   , "Here are your stats for " + timeStamp + "\n\n" + statsText);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SettingsActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
