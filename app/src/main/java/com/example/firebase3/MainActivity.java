package com.example.firebase3;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
   private  EditText email,password;
    private FirebaseAuth auth_login;
    private ProgressBar pb;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth_login.getCurrentUser();
        if(currentUser!=null){
            Intent intent = new Intent(this, Homepage.class);
            startActivity(intent);
            finish();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.textView3);
        password = findViewById(R.id.textView2);
        Button login = findViewById(R.id.submit);
        ImageView google_signup = findViewById(R.id.googlesignin);
        auth_login = FirebaseAuth.getInstance();
        pb=findViewById(R.id.progressBar);
        Intent intent = new Intent(this, Homepage.class);
        login.setOnClickListener(v -> {
            String email_login = email.getText().toString();
            String password_login = password.getText().toString();
            if (TextUtils.isEmpty(email_login) || TextUtils.isEmpty(password_login)) {
                Toast.makeText(MainActivity.this, "Enter valid EmailId & Password", Toast.LENGTH_SHORT).show();
            } else {
                pb.setVisibility(View.VISIBLE);

                auth_login.signInWithEmailAndPassword(email_login, password_login).addOnCompleteListener(MainActivity.this, task -> {
                    if (task.isSuccessful()) {
                        startActivity(intent);
                        pb.setVisibility(View.GONE);

                    } else {
                        pb.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }

                });

            }
        });
        //Google Signin.
        processRequest();
        google_signup.setOnClickListener(v -> processLogin());
        
    }
    private void processRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    private void processLogin() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 150);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 150) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();

            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth_login.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
//                        FirebaseUser user =auth_login.getCurrentUser();
                         waytoHomepage();

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void waytoHomepage() {
        Intent intent = new Intent(this, Homepage.class);
        startActivity(intent);
        finish();
    }

    public void signup(View v){
        Intent intent = new Intent(this, Register_user.class);
        startActivity(intent);
        finish();
    }
}