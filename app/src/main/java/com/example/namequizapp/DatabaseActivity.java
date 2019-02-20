package com.example.namequizapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.namequizapp.data.Constants;
import com.example.namequizapp.data.Uploads;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DatabaseActivity extends AppCompatActivity {


    private DatabaseReference mDatabase;

    private ArrayList<Uploads> uploads;

    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        FirebaseApp.initializeApp(getApplicationContext());

        uploads = new ArrayList<>();

        final ListView lv = findViewById(R.id.list_view);

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseApp.initializeApp(getApplicationContext());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Uploads upload = postSnapshot.getValue(Uploads.class);
                    uploads.add(upload);
                }
                adapter = new CustomAdapter(getApplicationContext(), uploads);
                lv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void addPerson(View view){
        Intent intent = new Intent(this, AddToDatabaseActivity.class);
        startActivity(intent);
    }
}
