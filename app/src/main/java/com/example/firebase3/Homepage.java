package com.example.firebase3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class Homepage extends AppCompatActivity {
    private ProgressBar pb2;
    TextView email_status;
    ArrayList<Model> arrcontact=new ArrayList<>();
    String email_check="",key="";
    FloatingActionButton float_create,float_update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_homepage);
        RecyclerView recview = findViewById(R.id.recycle);
        pb2 = findViewById(R.id.progressBar2);
        float_create=findViewById(R.id.floatingActionButton2);
        float_update=findViewById(R.id.floatingActionButton3);
        recview.setLayoutManager(new LinearLayoutManager(this));
        email_status=findViewById(R.id.textView8);
        Myadapter myadapter=new Myadapter(this,arrcontact);
        recview.setAdapter(myadapter);
        pb2.setVisibility(View.VISIBLE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase.getInstance().getReference("Profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Model mdl = dataSnapshot.getValue(Model.class);
                    email_check=dataSnapshot.child("Email").getValue(String.class);
                    assert user != null;
                    if(Objects.equals(email_check, user.getEmail())){
                        key=dataSnapshot.getKey();
                        float_create.setVisibility(View.GONE);
                    }
                    arrcontact.add(mdl);
                }
                pb2.setVisibility(View.GONE);
                myadapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        recview.setAdapter(myadapter);
    }
    public void profile(View view){
        Intent intent = new Intent(this, Profile.class);
        intent.putExtra("KEY",key);
        startActivity(intent);
    }
    public void signOut(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}