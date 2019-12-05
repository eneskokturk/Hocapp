package com.example.hocapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    EditText emailText, passwordText;            // Kullanici adi ve sifreyi tutan degiskenler tanimlandi.
    TextView signUp;
    ProgressBar loadingProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        firebaseAuth = FirebaseAuth.getInstance();  // initializing
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordAgainText);
        signUp= findViewById(R.id.signUp);
        loadingProgress = findViewById(R.id.loadingProgress);

        loadingProgress.setVisibility(View.INVISIBLE);

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();    // kullanici giris yapmis ise deger döndürür ,kimse yok ise null dondurur

        if (firebaseUser != null) {                                    //eger null degil ise giris ekrani yerine mainactivity cagirilir.

            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }

    }

    public void signInClicked(View view)      //Giris Yap
    {


        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        if(emailText.getText().toString().trim().equals("")||passwordText.getText().toString().equals("")) //Eğer e-mail veya sifre kısımınlarından birtanesi bos ise
        {                                                                                                  //ekrana toast mesaji bastir.
            Toast.makeText(getApplicationContext(),"Lüften E-mail ve Şifre Bilgilerinizi Giriniz..",Toast.LENGTH_LONG).show();
        }
        else
        {
            loadingProgress.setVisibility(View.VISIBLE);
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {          //giris basarili ise mainactivitye yönlen

                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {           //giris basarisiz ise hata mesaji göster
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignInActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    public void signUpClicked(View view) {     //Üye Ol

        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);

    }
}