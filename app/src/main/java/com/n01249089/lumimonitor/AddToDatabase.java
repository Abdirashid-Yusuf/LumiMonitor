package com.n01249089.lumimonitor;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddToDatabase extends AppCompatActivity {

    private Button save,back;
    private EditText temperature;
    private EditText humidity;
    private EditText micIn;
    private EditText micOut;
    private EditText lightLevel;
    private EditText rgb;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    DataStructure mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_database_layout);
        this.setTitle("Write to Database");

        findAllViews();
        getDatabase();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeData(temperature.getText(), humidity.getText(), lightLevel.getText(),micIn.getText(),micOut.getText(),rgb.getText());
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getDatabase(){
        // TODO: Find the reference form the database.
        database = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        String path = "userdata/" + mAuth.getUid();  // Write to the user account.
        myRef = database.getReference(path);

    }

    private DataStructure createData(Editable temperature, Editable humidity, Editable lightLevel, Editable micIn, Editable micOut, Editable rgb){
        // TODO: Get the timestamp
        Long time = System.currentTimeMillis()/1000;
        String timestamp = time.toString();
        return new DataStructure(String.valueOf(temperature),
                String.valueOf(humidity),
                String.valueOf(lightLevel),
                String.valueOf(micIn),
                String.valueOf(micOut),
                String.valueOf(rgb),
                timestamp);
    }

    private void writeData(Editable temperature, Editable humidity, Editable lightLevel, Editable micIn, Editable micOut, Editable rgb) {

        DataStructure mData = createData(temperature, humidity, lightLevel,micIn,micOut,rgb);
        // Select one of the following methods to update the data.
        // 1. To set the value of data
        //myRef.setValue(mData);
        // 2. To create a new node on database.
       // myRef.push().setValue(mData);
       // myRef.push().setValue(rgb);
        // TODO: Write the data to the database.
        // 3. To create a new node on database and detect if the writing is successful.
        myRef.push().setValue(mData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Value was set. ", Toast.LENGTH_LONG).show();
               // gotoRead();  // after write the data, read it from another screen.
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Writing failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Find all the views for this activity.
    private void findAllViews(){
        save = findViewById(R.id.save);
        temperature = findViewById(R.id.temperature);
        humidity = findViewById(R.id.humidity);
        lightLevel = findViewById(R.id.lightLevel);
        micIn = findViewById(R.id.micIn);
        micOut = findViewById(R.id.micOut);
        rgb = findViewById(R.id.rgb);
        back = findViewById(R.id.button7);

    }

}
