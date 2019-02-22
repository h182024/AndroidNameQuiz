package com.example.namequizapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.namequizapp.data.Constants;
import com.example.namequizapp.data.GlideApp;
import com.example.namequizapp.data.UploadList;
import com.example.namequizapp.data.Uploads;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.google.firebase.FirebaseApp.initializeApp;

public class startQuizActivity extends AppCompatActivity {

    private Uploads uploads;
    private ArrayList<Uploads> guessed;
    private List<Uploads> c;
    private UploadList uList;
    private Integer score = 0;
    private Integer attempts = 0;
    private Uploads currentPerson;
    private ImageView imageView;
    private TextView scoreCountView;
    private TextView attemptsView;
    private EditText guessText;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz);
        giveList(c);
        List<Uploads> cl = uList.getuList();
        if (cl.size() > 0) {
            startNewGame(cl);
        } else {
            Toast.makeText(getApplicationContext(), "List is empty", Toast.LENGTH_LONG).show();
        }
    }


    private void startNewGame(List<Uploads> c){

        currentPerson = giveRandom(c);
        score = 0;
        scoreCountView = findViewById(R.id.textView_score);
        scoreCountView.setText(score.toString());

        attempts = 0;
        attemptsView = findViewById(R.id.textView_attempts);
        attemptsView.setText(attempts.toString());

        imageView = findViewById(R.id.imageView);
        StorageReference gRef = FirebaseStorage.getInstance().getReference().child(uploads.getUrl());
        FirebaseApp.initializeApp(getApplicationContext());
        GlideApp.with(getApplicationContext()).load(gRef).into(imageView);
        guessed = new ArrayList<>();
        guessed.add(currentPerson);
    }

    public void guess(View view){
        giveList(c);
        final List<Uploads> list = uList.getuList();
        guessText = findViewById(R.id.editText_guess);
        String guess = guessText.getText().toString();

        attempts++;

     if (currentPerson != null) {
         if (guess.toLowerCase().equals(currentPerson.getName().toLowerCase())) { //if guessed correct
             score++;
             Toast.makeText(getApplicationContext(), R.string.correct, Toast.LENGTH_SHORT).show();
         } else {
             Toast.makeText(getApplicationContext(), "Wrong, that was " + currentPerson.getName() + "!", Toast.LENGTH_LONG).show();
         }

         if (guessed.size() >= list.size()) { //if game is over
             AlertDialog.Builder builder = new AlertDialog.Builder(this);
             builder.setTitle("Game Over")
                     .setMessage("You got " + score + "/" + list.size() + " points.")
                     .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             startNewGame(list);
                         }
                     })
                     .setNegativeButton("Return to menu", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             finish();
                         }
                     })
                     .setCancelable(false)
                     .show();
         } else {
             while (guessed.contains(currentPerson)) {
                 currentPerson = giveRandom(list);
             }

             //update
             guessed.add(currentPerson);
             GlideApp.with(getApplicationContext()).load(currentPerson.getUrl()).into(imageView);
             scoreCountView.setText(score.toString());
             attemptsView.setText(attempts.toString());
             guessText.setText("");

         }
     } else {
         Toast.makeText(getApplicationContext(), "nullPointerException", Toast.LENGTH_LONG);
     }
    }
    public Uploads giveRandom(List<Uploads> list){
        int s = 0;
        if (list != null){
            s = list.size();
            Random r = new Random();
            int res = r.nextInt(s);
            return list.get(res);
        } else {
            return null;
        }


    }
    public void giveList(final List<Uploads> cl) {
        FirebaseApp.initializeApp(this);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uList = new UploadList(cl);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Uploads uploads = snapshot.getValue(Uploads.class);
                    cl.add(uploads);
                    uList.setuList(cl);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
