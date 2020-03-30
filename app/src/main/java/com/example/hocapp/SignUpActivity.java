package com.example.hocapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    Button DatePickerButton;                    //Tarih ayarlama butonu
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
        final Context context = this;
        nameText = findViewById(R.id.nameText);
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        passwordAgainText = findViewById(R.id.passwordAgainText);
        birthdayText = findViewById(R.id.birthdayText);
        signUpNextButton = findViewById(R.id.signUpNextButton);
        DatePickerButton =findViewById(R.id.DatePickerButton);


        DatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // Şimdiki zaman bilgilerini alıyoruz. güncel yıl, güncel ay, güncel gün.
                final Calendar takvim = Calendar.getInstance();
                int yil = takvim.get(Calendar.YEAR);
                int ay = takvim.get(Calendar.MONTH);
                int gun = takvim.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // ay değeri 0 dan başladığı için (Ocak=0, Şubat=1,..,Aralık=11)
                                // değeri 1 artırarak gösteriyoruz.
                                month += 1;
                                // year, month ve dayOfMonth değerleri seçilen tarihin değerleridir.
                                // Edittextte bu değerleri gösteriyoruz.
                                birthdayText.setText(dayOfMonth + "/" + month + "/" + year);
                            }
                        }, yil, ay, gun);
                // datepicker açıldığında set edilecek değerleri buraya yazıyoruz.
                // şimdiki zamanı göstermesi için yukarda tanmladığımz değşkenleri kullanyoruz.

                // dialog penceresinin button bilgilerini ayarlıyoruz ve ekranda gösteriyoruz.
                dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", dpd);
                dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", dpd);
                dpd.show();
            }
        });



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
                if(isEmailValid(eMail)==true)                                   //E-mail kurallara uygun yazildiysa devam et yoksa toast message yayinla
                {
                    if(passwordText.getText().toString().equals(passwordAgainText.getText().toString()))   //Girilen Sifre ve Sifre tekrar alanlarını karşılaştır
                    {

                        if(gender ==null &&userType==null)
                        {
                            Toast.makeText(getApplicationContext(),"Lütfen Cinsiyet ve Eğitmen Bilgi Seçimini Boş Bırakmayınız..",Toast.LENGTH_LONG).show();

                        }else
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



