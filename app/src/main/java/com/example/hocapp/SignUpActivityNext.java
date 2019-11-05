package com.example.hocapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivityNext extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_next);


        Intent intent = getIntent();
        String userName = intent.getStringExtra("userNameInput");
        String password = intent.getStringExtra("passwordInput");
        String birthday = intent.getStringExtra("birthdayInput");
        String email = intent.getStringExtra("emailInput");
        String gender = intent.getStringExtra("genderInput");
        String userType = intent.getStringExtra("userTypeInput");

    }
}
