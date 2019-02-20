package com.example.namequizapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.namequizapp.data.Constants;
import com.example.namequizapp.data.Uploads;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddToDatabaseActivity extends AppCompatActivity {

    static final int REQUEST_CAMERA = 1;
    static final int REQUEST_GALLERY = 2;

    private ImageView iv;
    private TextView tv;
    private Uri file;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_database);

        tv = findViewById(R.id.inputName);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
    }

    public void selectImage(View view){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*"); //to specify type of data to return
        startActivityForResult(Intent.createChooser(intent, "Select file"), REQUEST_GALLERY);
    }

    public void captureImage(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            file = Uri.fromFile(createImageFile());
        } catch (Exception ex){
            //
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        iv = findViewById(R.id.addedImageView);
        if(data != null && resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_GALLERY){
                file = data.getData();
                iv.setImageURI(file);
            } else if(requestCode == REQUEST_CAMERA) {
                iv.setImageURI(file);
            }
        }
    }

    public File createImageFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                Log.d("CameraDemo", "failed to create directory");
                return null;
            }
        }


        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }
    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        //cheks if file is available
        if (file != null){

            final StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(file));

            sRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_LONG).show();



                            Uploads uploads = new Uploads(tv.getText().toString().trim(), sRef.getPath());

                            String uploadId = databaseReference.push().getKey();
                            databaseReference.child(uploadId).setValue(uploads);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    public void addPerson(View view){
        EditText inputName = findViewById(R.id.inputName);
        String name = inputName.getText().toString();

        if(name.isEmpty()){
            Toast.makeText(getApplicationContext(), "Name can't be empty!", Toast.LENGTH_SHORT).show();
        }else if(file == null){
            Toast.makeText(getApplicationContext(), "Please select an image.", Toast.LENGTH_SHORT).show();
        }else{
            uploadFile();
            Toast.makeText(getApplicationContext(), "Person added.", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

}
