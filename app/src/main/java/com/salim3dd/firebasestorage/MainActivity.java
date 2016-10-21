package com.salim3dd.firebasestorage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ImageView imageView, imageView2;
    private StorageReference mStorage;
    private Uri img_uri;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView2 = (ImageView) findViewById(R.id.imageView2);

        progressDialog = new ProgressDialog(this);

        mStorage = FirebaseStorage.getInstance().getReference();

    }

    public void imageView_click(View view) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 200 && data != null) {

            Uri imagerUri = data.getData();
            imageView.setImageURI(imagerUri);
            img_uri = imagerUri;
            Toast.makeText(getApplicationContext(), imagerUri.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void btn_Upload(View view) {

        Calendar calendar = Calendar.getInstance();

        progressDialog.setMessage("انتظر رفع الصورة .....");
        progressDialog.show();

        StorageReference filepath = mStorage.child("Salim3DD").child("img_" + calendar.getTimeInMillis());

        filepath.putFile(img_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "تم رفع الصورة", Toast.LENGTH_SHORT).show();

                Picasso.with(getApplicationContext()).load(taskSnapshot.getDownloadUrl()).into(imageView2);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "يوجد خطأ لم يتم رفع الصورة", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
