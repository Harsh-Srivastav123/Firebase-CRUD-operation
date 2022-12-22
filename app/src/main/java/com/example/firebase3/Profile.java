package com.example.firebase3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class Profile extends AppCompatActivity {
    private EditText name,phone_no,github,description;
    Button update;
    String key_pass;
    TextView delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name=findViewById(R.id.details_name);
        phone_no=findViewById(R.id.details_phone);
        github=findViewById(R.id.details_github);
        description=findViewById(R.id.details_description);
        delete=findViewById(R.id.delete);
        delete.setVisibility(View.GONE);
        update=findViewById(R.id.update);
        Intent intent = getIntent();
        key_pass=intent.getStringExtra("KEY");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String email_pass = user.getEmail();
        if(Objects.equals(key_pass, "")){
            update.setOnClickListener(v -> {
                HashMap<String,Object> m= new HashMap<>();
                m.put("Description",description.getText().toString());
                m.put("Name",name.getText().toString());
                m.put("Phone_no",phone_no.getText().toString());
                m.put("GitHub",github.getText().toString());
                m.put("Email",email_pass);
                m.put("Status","Created");
                FirebaseDatabase.getInstance().getReference().child("Profile").child(name.getText().toString()+" profile").setValue(m).addOnSuccessListener(unused -> {
                    Toast.makeText(Profile.this, "Created Successfully", Toast.LENGTH_SHORT).show();
                    waytohomepage();
                });
            });
        }
        else{
            update.setText(R.string.upd);
            delete.setVisibility(View.VISIBLE);
            update.setOnClickListener(v -> {
                HashMap<String,Object> m= new HashMap<>();
                m.put("Description",description.getText().toString());
                m.put("Name",name.getText().toString());
                m.put("Phone_no",phone_no.getText().toString());
                m.put("GitHub",github.getText().toString());
                m.put("Email",email_pass);
                m.put("Status","Created");
                FirebaseDatabase.getInstance().getReference().child("Profile").child(key_pass).setValue(m).addOnSuccessListener(unused -> {
                    Toast.makeText(Profile.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    waytohomepage();
                });
            });
        }
        delete.setOnClickListener(v -> {
           DatabaseReference current_user= FirebaseDatabase.getInstance().getReference().child("Profile").child(key_pass);
           current_user.removeValue();
            Toast.makeText(Profile.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
           waytohomepage();
        });

    }

    private void waytohomepage() {
        Intent intent = new Intent(this, Homepage.class);
        startActivity(intent);
        finish();
    }
}