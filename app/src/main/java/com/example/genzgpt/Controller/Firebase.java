package com.example.genzgpt.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class Firebase {

    private String email;
    //Handle Firebase interactions

    public static void uploadImageToFirebaseStorage(Uri imageUri, StorageReference storageReference, ProgressDialog progressDialog, Context context) {

        final String randomKey = UUID.randomUUID().toString();
        StorageReference imageRef = storageReference.child("images/" + randomKey);

        // Upload the image to Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Image successfully uploaded
                        progressDialog.dismiss();
                        showToast(context, "Profile picture uploaded to Firebase Storage");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        showToast(context, "Failed to get download URL: " + e.getMessage());
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        progressDialog.setMessage("Percentage: " + (int) progressPercent + "%");
                    }
                });
    }

    private static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private static void getUserData(){

    }

    private void getUserData(String email){
        //search the firebase database, user, and returns email, name, and profile picture

    }

    private void setEmail(String email){
        this.email = email;
    }

    public Firebase() {
        //constructor

    }
}
