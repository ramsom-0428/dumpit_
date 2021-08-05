package com.dumpit.ffff;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BuyList extends AppCompatActivity{


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private ListView listViews;
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_list);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        listViews = (ListView)findViewById(R.id.purchase_lists);

        String email = user.getEmail();
        int index = email.indexOf("@");
        String id = email.substring(0, index);
        String web = email.substring(index+1);
        int webidx = web.indexOf(".");
        String website = web.substring(0, webidx);


        DatabaseReference buy = databaseReference.child(id+"_"+website).child("marketHistory");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        listViews.setAdapter(adapter);


        buy.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){


                for(DataSnapshot snaps : snapshot.getChildren()) {
                    String date = snaps.getKey().toString();
                    String point = snaps.getValue().toString();

                    Array.add(date + " " + point);
                    adapter.add(date + " " +point);
                }


                adapter.notifyDataSetChanged();
                listViews.setSelection(adapter.getCount()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }



}




