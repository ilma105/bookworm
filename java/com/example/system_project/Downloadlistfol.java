package com.example.system_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.system_project.Adapters.UserAdapterfol;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Downloadlistfol extends AppCompatActivity {
    private ListView listView;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    private List<userbookclass> bookslists;
    private UserAdapterfol contributionAdapter;
    String profileid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dramabook);
        mAuth=FirebaseAuth.getInstance();
        firebaseUser= mAuth.getCurrentUser();
        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileid=sharedPreferences.getString("profileid","none");
        //profileid=firebaseUser.getUid();
        listView = findViewById(R.id.listview);
        Button up=findViewById(R.id.upload);
        // databaseReference = FirebaseDatabase.getInstance().getReference("contribution");
        bookslists = new ArrayList<>();
        contributionAdapter = new UserAdapterfol(Downloadlistfol.this, bookslists);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RetrievePdffol.class));
            }
        });
    }


    @Override
    protected void onStart() {
        // FirebaseUser firebaseUser=mAuth.getCurrentUser();

        String userid=firebaseUser.getUid();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("contribution").child(profileid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookslists.clear();

                for (DataSnapshot snapshot1:snapshot.getChildren()){

                    userbookclass users=snapshot1.getValue(userbookclass.class);
                    users.setKey(snapshot1.getKey());
                    bookslists.add(users);
                }

                listView.setAdapter(contributionAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        super.onStart();
    }
}