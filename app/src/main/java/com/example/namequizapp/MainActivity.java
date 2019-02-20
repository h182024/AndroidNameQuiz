package com.example.namequizapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.Menu;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String name = prefs.getString("owner", "");

        if(name.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText input = new EditText(this);
            builder.setTitle("Name of owner")
                    .setMessage("Input your name.")
                    .setView(input)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String ownerName = input.getText().toString();
                            prefs.edit().putString("owner", ownerName).apply();
                            Toast.makeText(getApplicationContext(), "Welcome, " + ownerName + "!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
        }else{
            Toast.makeText(getApplicationContext(), "Welcome back, " + name + "!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.settings){
            Intent intent = new Intent(this, MyPreferencesActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToDatabase(View view){
        Intent intent = new Intent(this, DatabaseActivity.class);
        startActivity(intent);
    }

    public void startQuiz(View view){
        Intent intent = new Intent(this, startQuizActivity.class);
        startActivity(intent);
    }

    public void addToDatabase(View view){
        Intent intent = new Intent(this, AddToDatabaseActivity.class);
        startActivity(intent);
    }
}