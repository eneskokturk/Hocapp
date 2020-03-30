package com.example.hocapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class UserForgotPassword extends AppCompatActivity {

    EditText ForgotPasswordEmailText;                                   //Kullanici e-mail adresi
    Button ForgotPasswordEmailClicked;                                    //Parola sifirlama baglantisi gönder butonu

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_forgot_password);

        final EditText ForgotPasswordEmailText =findViewById(R.id.ForgotPasswordEmailText);
        Button ForgotPasswordEmailClicked = findViewById(R.id.ForgotPasswordEmailClicked);

        firebaseAuth = FirebaseAuth.getInstance();

        ForgotPasswordEmailClicked.setOnClickListener(new View.OnClickListener() {               //butona tiklanildi
            @Override
            public void onClick(View v) {
                firebaseAuth.sendPasswordResetEmail(ForgotPasswordEmailText.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {                 //firebase sifre sifirlama fonksiyonu
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){                                                  //baglanti gönderildi
                            Toast.makeText(UserForgotPassword.this,
                                    "E-mailinizi kontrol ediniz. Şifre Sıfırlama bağlantınız gönderilmiştir...",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(UserForgotPassword.this,                      //hata olusursa hatayi ekrana bas
                                    task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });



    }
}
