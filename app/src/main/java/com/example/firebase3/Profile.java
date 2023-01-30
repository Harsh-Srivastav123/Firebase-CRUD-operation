package com.example.firebase3;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

public class Profile extends AppCompatActivity {
    private EditText name,phone_no,github,description;
    Button update;
    String key_pass;
    TextView delete;
    ImageView profile;
    Uri filepath;
    String email_pass="";
    String url;

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
        profile=findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    Dexter.withActivity(Profile.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            .withListener(new PermissionListener() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                    Intent intent=new Intent(Intent.ACTION_PICK);
                                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(intent,1);
                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                    Toast.makeText(Profile.this, "upload failed", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                    permissionToken.continuePermissionRequest();
                                }
                            }).check();
                }

            }
        });


        Intent intent = getIntent();
        key_pass=intent.getStringExtra("KEY");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        email_pass = user.getEmail();
        if(Objects.equals(key_pass, "")){
            update.setOnClickListener(v -> {
                HashMap<String,Object> m= new HashMap<>();
                m.put("Description",description.getText().toString());
                m.put("Name",name.getText().toString());
                m.put("Phone_no",phone_no.getText().toString());
                m.put("GitHub",github.getText().toString());
                m.put("Email",email_pass);
                m.put("Status","Created");
                m.put("imgUrl",url);
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
                m.put("imgUrl",url);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null){

            filepath=data.getData();
//            try{
//                InputStream inputStream=getContentResolver().openInputStream(filepath);
//                Bitmap bit= BitmapFactory.decodeStream(inputStream);
//                profile.setImageBitmap(bit);
//            }catch (Exception e){
//                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            }

            profile.setImageURI(filepath);
            uploadImage();
        }
    }

    private void uploadImage() {
        ProgressDialog progressDialog=new ProgressDialog(Profile.this);
        progressDialog.setTitle("Image uploading");
        progressDialog.show();
        FirebaseStorage.getInstance().getReference().child(email_pass)
                .putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        FirebaseStorage.getInstance().getReference().child(email_pass).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                url=uri.toString();
                            }
                        });

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float percentage=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        progressDialog.setMessage("uploaded "+(int) percentage+" %");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile.this, "DP upload failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void waytohomepage() {
        Intent intent = new Intent(this, Homepage.class);
        startActivity(intent);
        finish();
    }
}