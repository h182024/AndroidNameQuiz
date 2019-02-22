package com.example.namequizapp.data;


import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


@IgnoreExtraProperties
public class Uploads {

        public DatabaseReference mDatabase;
        public String name;
        public String url;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Uploads() {

    }

    public Uploads(String name, String url) {
            this.name = name;
            this.url= url;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public List<Uploads> giveUList(List<Uploads> list){
        return list;
        }

}
