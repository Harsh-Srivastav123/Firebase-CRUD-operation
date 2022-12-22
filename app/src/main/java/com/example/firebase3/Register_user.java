package com.example.firebase3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register_user extends AppCompatActivity {
    EditText new_email,new_password, confirm_password;
    Button signup;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_user);
        new_email=findViewById(R.id.textView5);
        new_password=findViewById(R.id.textView10);
        signup=findViewById(R.id.signup);
        confirm_password=findViewById(R.id.confirm_password);
        auth=FirebaseAuth.getInstance();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_email=new_email.getText().toString();
                String str_password=new_password.getText().toString();
                String str_confirmpassword=confirm_password.getText().toString();
                if(TextUtils.isEmpty(str_email)||TextUtils.isEmpty(str_password)||TextUtils.isEmpty(str_confirmpassword)){
                    Toast.makeText(Register_user.this, "Enter email, password", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (str_password.equals(str_confirmpassword)){
                        register(str_email,str_password);
                    }
                    else{
                        Toast.makeText(Register_user.this, "Password missmatched", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            private void register(String str_email, String str_password) {
                auth.createUserWithEmailAndPassword(str_email,str_password).addOnCompleteListener(Register_user.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {
                           Toast.makeText(Register_user.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                       }
                       else{
                           Toast.makeText(Register_user.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                       }
                    }
                });
            }
        });
    }


}