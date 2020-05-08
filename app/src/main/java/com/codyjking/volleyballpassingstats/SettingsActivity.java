package com.codyjking.volleyballpassingstats;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private int selectedIndex = 5;
    private RadioGroup group;
    private SharedPreferences sharedPref;
    private final String PREF_NAME = "prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        group = findViewById(R.id.settings_radioGroup);

        // load preferences
        sharedPref = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int selectedIndex = sharedPref.getInt("selectedIndex", 5);

        // set previously selected radio button
        int id = getResources().getIdentifier("radioButton" + (selectedIndex + 1), "id", getPackageName());
        RadioButton rb = findViewById(id);
        rb.toggle();
    }

    public void onRadioButtonClicked(View view) {
        int radioButtonID = group.getCheckedRadioButtonId();
        View radioButton = group.findViewById(radioButtonID);
        int selectedIndex = group.indexOfChild(radioButton);
        getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().putInt("selectedIndex", selectedIndex).apply();
    }
}
