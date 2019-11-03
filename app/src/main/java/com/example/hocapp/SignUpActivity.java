package com.example.hocapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SignUpActivity extends AppCompatActivity {

    EditText nameText;
    EditText emailText;
    EditText passwordText;
    EditText passwordAgainText;
    EditText birthdayText;
    RadioGroup studentOrTeacher;
    RadioGroup maleOrFemale;
    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton radioButton3;
    RadioButton radioButton4;
    Button signUpNextButton;
    String stuorteach; // ogrenci ogretmen degiskeni


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        studentOrTeacher =findViewById(R.id.studentOrTeacher);       // Ogrenci ve Ogretmen Radio Group
        maleOrFemale = findViewById(R.id.maleOrFemale);              // Cinsiyet Radio Group
        radioButton1= findViewById(R.id.radioButton);               //Ogrenci
        radioButton2= findViewById(R.id.radioButton2);              //Ogretmen
        radioButton3= findViewById(R.id.radioButton3);              //Kadın
        radioButton4= findViewById(R.id.radioButton4);              //Erkek

        nameText = findViewById(R.id.nameText);
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        passwordAgainText = findViewById(R.id.passwordAgainText);
        birthdayText = findViewById(R.id.birthdayText);
        signUpNextButton =findViewById(R.id.signUpNextButton);

        signUpNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioButton1.isChecked())
                {
                    stuorteach="Öğrenci";
                    System.out.println(stuorteach);
                }else if (radioButton2.isChecked())
                {
                    stuorteach="Öğretmen";
                    System.out.println(stuorteach);
                }


            }
        });


    }



    public void studentOrTeacherClick(View view)
    {


    }

    public void maleOrFemaleClick (View view)
    {


    }




    public void signUpNextClicked(View view)
    {


    }
}
