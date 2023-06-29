package com.example.fall_single_edition;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    // Create a Cloud Storage reference from the app
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    int num = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a reference to a file from a Cloud Storage URI
//        StorageReference imageRef = storage.getReference().child("images/image.png");
//
////        StorageReference gsReference = storage.getReferenceFromUrl("gs://falldetection-386022.appspot.com/2ft.png");
//        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                // Create a Bitmap from the byte array
//                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//
//                // Display the image in an ImageView
//                ImageView imageView = findViewById(R.id.imageView);
//                imageView.setImageBitmap(bitmap);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//                Log.e("TAG", "Error downloading image: " + exception.getMessage());
//            }
//        gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

//            @Override
//            public void onSuccess(Uri uri) {
//                // Handle successful image download
//                String imageUrl = uri.toString();
//                // Now you can use the imageUrl to load the image into an ImageView
//                // (e.g., using an image loading library like Picasso or Glide)
//                // For simplicity, we'll use the Android built-in ImageView class directly in the example below.
//                ImageView imageView = findViewById(R.id.imageView);
//                Picasso.get().load(imageUrl).into(imageView); // Using Picasso library for image loading
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle failed image download
//                // Display an error message or take appropriate action
//                Log.e("Firebase", "Error: image not show");
//
//            }
//        });


        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("fall_detection");

        // Add a ValueEventListener to retrieve the latest data
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method will be called whenever the data at the specified location changes
                // Retrieve the latest data from the dataSnapshot
                // Assuming your data is a string, you can retrieve it like this
                Object latestData = dataSnapshot.getValue();
                Log.e("Firebase", "Realtime database");

                // Update your UI with the latest data

                updateUI(latestData);
            }

            private void updateUI(Object latestData) {
                // Update your UI with the latest data
                // For example, if you have a TextView with the id "textViewData"
                TextView textAlertMessage = findViewById(R.id.no_alert_text);
                TextView textAlertDetail = findViewById(R.id.no_risk_string);
                ImageView imageView = findViewById(R.id.imageView);
                RelativeLayout myRelativeLayout = findViewById(R.id.no_alert_layout);
                ImageView myImageView = findViewById(R.id.no_alert_icon);



                // Check if latestData is not null before setting the text
                num+=1;
                if (num == 1) {}
                else if (latestData!=null) {
                    // modify alert string

                    textAlertMessage.setText("1 Alert");
                    textAlertMessage.setTextColor(Color.RED);
                    textAlertDetail.setText("High Risk Detected\nBathroom\nMom 05-June-2023");
                    textAlertDetail.setTextColor(Color.RED);
                    myRelativeLayout.setBackgroundColor(getResources().getColor(R.color.red));
                    myImageView.setImageResource(R.drawable.alert);



                    // modify alert image
                    StorageReference imageRef = storageRef.child("images/image.png");
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Use an image loading library like Picasso or Glide to load the image into the ImageView
                            Picasso.get().load(uri).into(imageView); // Using Picasso library for image loading
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle failed image download
                            // Display an error message or take appropriate action
                            Log.e("Firebase", "Error downloading image: " + exception.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur
                Log.e("Firebase", "Error: " + databaseError.getMessage());
            }
        });

    }
}