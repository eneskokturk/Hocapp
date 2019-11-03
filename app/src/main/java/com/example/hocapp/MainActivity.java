package com.example.hocapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    TextView editTextMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextMain = findViewById(R.id.editMainText);
        firebaseAuth = FirebaseAuth.getInstance();  // initializing
    }

    public void signOut(View view)
    {
        firebaseAuth.signOut();

        Intent intentToSignIn = new Intent(MainActivity.this,SignInActivity.class);
        startActivity(intentToSignIn);
        finish();
    }
}
