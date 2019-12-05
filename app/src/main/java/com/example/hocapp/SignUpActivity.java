package com.example.hocapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    EditText nameText;
    EditText emailText;
    EditText passwordText;
    EditText passwordAgainText;
    EditText birthdayText;
    RadioGroup studentOrTeacher;
    RadioGroup maleOrFemale;
    RadioButton radioStudent;
    RadioButton radioTeacher;
    RadioButton radioMale;
    RadioButton radioFemale;
    Button signUpNextButton;
    String gender;                              //Cinsiyet degiskeni
    String userType;                            //Kullanici tipi degiskeni , ogretmen veya ogrenci
    String userName;
    String password;
    String birthday;
    String eMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        studentOrTeacher = findViewById(R.id.studentOrTeacher);         // Ogrenci ve Ogretmen Radio Group
        maleOrFemale = findViewById(R.id.maleOrFemale);                // Cinsiyet Radio Group
        radioStudent = findViewById(R.id.radioStudent);                //Ogrenci
        radioTeacher = findViewById(R.id.radioTeacher);                //Ogretmen
        radioMale = findViewById(R.id.radioFemale);                    //Kadın
        radioFemale = findViewById(R.id.radioMale);                    //Erkek

        nameText = findViewById(R.id.nameText);
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        passwordAgainText = findViewById(R.id.passwordAgainText);
        birthdayText = findViewById(R.id.birthdayText);
        signUpNextButton = findViewById(R.id.signUpNextButton);



        signUpNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = nameText.getText().toString();
                password = passwordText.getText().toString();
                eMail = emailText.getText().toString();
                birthday = birthdayText.getText().toString();



                int genderId = maleOrFemale.getCheckedRadioButtonId();
                switch (genderId)                                                   //Kullanicinin Cinsiyetini belirlemek icin kullanılan switch case yapisi
                {
                    case R.id.radioMale: {
                        gender = "Erkek";
                        break;
                    }
                    case R.id.radioFemale: {
                        gender = "Kadın";
                        break;
                    }
                }
                int userTypeId = studentOrTeacher.getCheckedRadioButtonId();      //Kullanicinin ogretmen veya ogrenci oldugunu belirlemek icin kullanilan switch-case
                switch (userTypeId) {
                    case R.id.radioStudent: {
                        userType = "Öğrenci";
                        break;
                    }
                    case R.id.radioTeacher: {
                        userType = "Öğretmen";
                        break;
                    }
                }
                if(isEmailValid(eMail)&& isValidDate(birthday)==true)                                   //E-mail kurallara uygun yazildiysa devam et yoksa toast message yayinla
                {
                    if(passwordText.getText().toString().equals(passwordAgainText.getText().toString()))   //Girilen Sifre ve Sifre tekrar alanlarını karşılaştır
                    {
                        Intent intent = new Intent(SignUpActivity.this, SignUpActivitySecondPage.class);
                        intent.putExtra("userNameInput",userName);                  //SignUpActivitySecondPage e data yollama
                        intent.putExtra("passwordInput",password);
                        intent.putExtra("genderInput",gender);
                        intent.putExtra("userTypeInput",userType);
                        intent.putExtra("birthdayInput",birthday);
                        intent.putExtra("emailInput",eMail);

                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Girdiğiniz Şifreler Eşleşmiyor.Lütfen Kontrol Edip Tekrar Deneyiniz...",Toast.LENGTH_LONG).show();
                    }

                }else
                {
                    Toast.makeText(getApplicationContext(),"Lüften Geçerli E-mail Adresi Giriniz...",Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    public boolean isEmailValid(String eMail)                               //E-mail adresinin gecerli olup olmadigini regex kullanarak kontrol et
    {                                                                        //Uygun sartlar olusturulursa true , degilse false dondur.
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = eMail;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    boolean isValidDate(String birthday) {
        try {
            format.parse(birthday);
            return true;
        }
        catch(ParseException e){
            return false;
        }
    }
}



