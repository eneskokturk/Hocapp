package com.example.hocapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class SignUpActivitySecondPage extends AppCompatActivity {


    ImageView profilePictureLogo,profilePictureAdd;
    TextView profilePictureText;
    Button createUserButton;
    EditText userBiographyText;
    Bitmap selectedImage;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    Uri userImageData;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    String userNameDatabase;
    String birthdayDatabase;
    String emailDatabase;
    String genderDatabase;
    String userTypeDatabase;
    String passwordDatabase;
    String userID;
    ProgressBar progressBarSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_second_page);


        Intent intent = getIntent();
        String userName = intent.getStringExtra("userNameInput");       //SignUpActivity'den bilgiler SignUpActivitye aktarildi.
        String password = intent.getStringExtra("passwordInput");
        String birthday = intent.getStringExtra("birthdayInput");
        String email = intent.getStringExtra("emailInput");
        String gender = intent.getStringExtra("genderInput");
        String userType = intent.getStringExtra("userTypeInput");

        profilePictureText = findViewById(R.id.profilePicture);
        //profilePictureAdd = findViewById(R.id.profilePictureAdd);
        profilePictureLogo = findViewById(R.id.profilePictureLogo);
        createUserButton = findViewById(R.id.createUserButton);
        userBiographyText = findViewById(R.id.userBiography);
        progressBarSignUp =findViewById(R.id.progressBarSignUp);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userNameDatabase=userName;
        birthdayDatabase=birthday;
        emailDatabase=email;
        genderDatabase=gender;
        userTypeDatabase=userType;
        passwordDatabase=password;

        progressBarSignUp.setVisibility(View.INVISIBLE);
    }

    public void createUserButtonClicked(View view)
    {


        if (userImageData != null) {
            progressBarSignUp.setVisibility(View.VISIBLE);

            UUID uuid = UUID.randomUUID();                      //universal unique id kullanici profil fotograflari icin id olusturma
            final String imageName = "images/" + uuid + ".jpg";

            storageReference.child(imageName).putFile(userImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName); //Download URL aliyoruz
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String downloadUrl = uri.toString();

                            String userBiography = userBiographyText.getText().toString();


                            HashMap<String, Object> userData = new HashMap<>();

                            userData.put("userName",userNameDatabase);
                            userData.put("email",emailDatabase);
                            userData.put("birthday",birthdayDatabase);
                            userData.put("gender",genderDatabase);
                            userData.put("userType",userTypeDatabase);
                            userData.put("downloadurl",downloadUrl);
                            userData.put("biography",userBiography);
                            userData.put("date", FieldValue.serverTimestamp());

                            firebaseAuth.createUserWithEmailAndPassword(emailDatabase,passwordDatabase).addOnSuccessListener(new OnSuccessListener<AuthResult>() //yeni üye olustur
                            {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    FirebaseUser user=firebaseAuth.getCurrentUser();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUpActivitySecondPage.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                                }
                            });



                            firebaseFirestore.collection("Users").add(userData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Intent intent = new Intent(SignUpActivitySecondPage.this,SignInActivity.class);
                                    Toast.makeText(getApplicationContext(),"Üye Kaydınız Başarılı Bir Şekilde Oluşturulmuştur. Hoşgeldiniz..",Toast.LENGTH_LONG).show();
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUpActivitySecondPage.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUpActivitySecondPage.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
            });
        }

        else{

            Toast.makeText(getApplicationContext(),"Lütfen Profil Fotoğrafı Seçiniz.",Toast.LENGTH_LONG).show();

        }

    }
    public void selectImage(View view) {                                                        //kullanicidan galeriye erisim izni

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
        } else {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery,2);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {           //galeri izin sdk 28 ve altı icin ayrildi

        if (requestCode == 2 && resultCode == RESULT_OK && data != null ) {

            userImageData = data.getData();

            try {

                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),userImageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    profilePictureLogo.setImageBitmap(selectedImage);
                } else {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),userImageData);
                    profilePictureLogo.setImageBitmap(selectedImage);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}





