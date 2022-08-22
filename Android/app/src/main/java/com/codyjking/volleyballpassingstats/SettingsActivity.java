package com.codyjking.volleyballpassingstats;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    private StatsViewModel stats;
    private int numPlayersVisible;
    private EditText et_numPlayers;

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
        et_numPlayers.setText("" + numPlayersVisible);

        Gson gson = new Gson();
        String json = sharedPref.getString("statsObj", "");
        stats = gson.fromJson(json, StatsViewModel.class);

        // Clear All Button
        final Button clearAllBtn = findViewById(R.id.clear_all_button);
        clearAllBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle("Clear All")
                        .setMessage("Do you really want to clear all stats?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> clearAll = true)
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        // Export as TXT Button
        final Button exportTxtBtn = findViewById(R.id.export_txt_button);
        exportTxtBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // if no stats show alert

                // else, share popup
                String timeStamp = new SimpleDateFormat("E LL/dd", Locale.forLanguageTag("en_us")).format(Calendar.getInstance().getTime());
                String shortTimeStamp = new SimpleDateFormat("yyyy-MM-dd--HH-mm-ss", Locale.forLanguageTag("en_us")).format(Calendar.getInstance().getTime());

                StringBuilder statsText = new StringBuilder();
                statsText.append("Group Average: ").append(stats.getGroupAvg(numPlayersVisible)).append("\n\n");

                for (int i = 0; i < numPlayersVisible; i++) {
                    String name = stats.getName(i + 1);

                    if (name.length() == 0) {
                        name = "No Name (P" + (i + 1) + ")";
                    }

                    statsText.append(name).append("\n").append(stats.generateExportText(i)).append("\n\n");
                }

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
                i.putExtra(Intent.EXTRA_SUBJECT, "Passing Stats Export (" + shortTimeStamp + ")");
                i.putExtra(Intent.EXTRA_TEXT, "Here are your stats for " + timeStamp + "\n\n" + statsText);
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(SettingsActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Export as CSV Button
        final Button exportCsvBtn = findViewById(R.id.export_csv_button);
        exportCsvBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // if no stats show alert

                // else, share popup
                String shortTimeStamp = new SimpleDateFormat("yyyy-MM-dd--HH-mm-ss", Locale.forLanguageTag("en_us")).format(Calendar.getInstance().getTime());

                // Create string to hold csv data
                StringBuilder statsText = new StringBuilder();
                statsText.append("\"Name\",\"Average\",\"No. of Passes\",\"Zeros\",\"Ones\",\"Twos\",\"Threes\"\n");
                for (int i = 0; i < numPlayersVisible; i++) {
                    statsText.append(stats.generateCsvExportText(i));
                }

                // Write data to file
                String filename = "stats--" + shortTimeStamp;

                /*FilenameDialogFragment setFilenameDialog = new FilenameDialogFragment();
                setFilenameDialog.show(getFragmentManager(), FilenameDialogFragment.TAG);*/

                // Show dialog to set filename
                onCreateDialog(filename, statsText.toString()).show();
            }
        });
    }

    private Dialog onCreateDialog(String filenamePlaceholder, String statsText) {
        // Use the Builder class for convenient dialog construction
        final EditText dialogEditText = new EditText(this);
        dialogEditText.setText(filenamePlaceholder);
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setMessage("Rename file")
                .setView(dialogEditText)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String filename = dialogEditText.getText().toString() + ".csv";
                        if(filename.length() == 4) {
                            filename = filenamePlaceholder + ".csv";
                        }

                        try (FileOutputStream fos = getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE)) {
                            fos.write(statsText.getBytes(StandardCharsets.UTF_8));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // Share file
                        File csvFile = new File(getFilesDir(), filename);
                        Uri uri = FileProvider.getUriForFile(SettingsActivity.this, getApplicationContext().getPackageName() + ".fileprovider", csvFile);
                        if (uri != null) {
                            Intent i = new Intent(Intent.ACTION_SEND);

                            // Grant temporary read permission to the content URI
                            i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                            i.setDataAndType(uri, "text/csv");
                            i.putExtra(Intent.EXTRA_STREAM, uri);
                            startActivity(Intent.createChooser(i, "Share file"));
                        }

                        // Delete any old csv files
                        for(String s : fileList()) {
                            System.out.println(s);
                            if(!s.equals(filename) && s.endsWith(".csv")) {
                                File f = new File(getFilesDir(), s);
                                f.delete();
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private final TextWatcher filterTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int val;

            try {
                val = Integer.parseInt(et_numPlayers.getText().toString());
                numPlayersVisible = val;

                if (val < 1 || val > 12) {
                    et_numPlayers.getText().clear();
                    val = 12;
                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid value.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (NumberFormatException e) {
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




}
