package com.example.hocapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.security.Key;
import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.storage.FirebaseStorage.getInstance;


public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
        TextView text;


    }

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth.AuthStateListener authStateListener;
    public FirebaseUser firebaseUser;
    private CollectionReference collectionReference;
    private DatabaseReference databaseReference;



    //storage
    StorageReference storageReference;


    TextView email;
    TextView signOut;
    Bitmap selectedImage;
    TextView text;
    TextView userBio;
    ImageView profilePicture;
    String userType;
    String userName;
    String userGender;
    String userEmail;
    String userBiography;
    String userBirthday;
    public String userPictureUrl;
    CardView fab;
    Uri userImageData;

    //progress dialog
    ProgressDialog pd;

    //permissions constants
    private static final int CAMERA_REQUEST_CODE=100;
    private static final int STORAGE_REQUEST_CODE=200;
    private static final int IMAGE_PICK_GALLERY_CODE=300;
    private static final int IMAGE_PICK_CAMERA_CODE=400;
    //arrays of permissions to be requested
    String cameraPermissions[];
    String storagePermissions[];


    //uri of picked image
    Uri image_uri;

    String profilePhoto;
    String userId;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);


        //init arrays of permissions
        cameraPermissions= new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};


        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore =FirebaseFirestore.getInstance();




        storageReference =getInstance().getReference(); //firebase storage reference

        userId = firebaseAuth.getCurrentUser().getUid();





        email=view.findViewById(R.id.email);
        profilePicture=view.findViewById(R.id.profilePicture);
        signOut=view.findViewById(R.id.signOut);
        text=view.findViewById(R.id.text);
        userBio=view.findViewById(R.id.userBio);
        fab=view.findViewById(R.id.fab);
        TextView textView1 = view.findViewById(R.id.text);

        //init progress dialog
        pd = new ProgressDialog(getActivity());


        firebaseUser = firebaseAuth.getCurrentUser();    // kullanici giris yapmis ise deger döndürür ,kimse yok ise null dondurur
        //getDataUserFromFirestore();

        DocumentReference documentReference =firebaseFirestore.collection("Users").document(userId);
        documentReference.addSnapshotListener( new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                    email.setText(documentSnapshot.getString("email"));
                    text.setText(documentSnapshot.getString("userName"));
                    userBio.setText(documentSnapshot.getString("biography"));
                    userPictureUrl= (documentSnapshot.getString("downloadUrl"));

                    Picasso.get().load(userPictureUrl).into(profilePicture);
            }
        });





       //fab button click

      fab.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              showEditProfileDialog();
          }
      });

      signOut.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              FirebaseAuth.getInstance().signOut();
              Intent intent = new Intent(getActivity(), SignInActivity.class);
              startActivity(intent);
          }
      });







        return view;
    }



    private void showEditProfileDialog(){

        /* Show dialog containing options
        1) Edit Profile Picture
        2) Edit Name
        3) Edit Biography*/

        //options to show in dialog

        String options[]={"Edit Profile Picture","Edit Name","Edit Biography"};

        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //set title
        builder.setTitle("Choose Action");
        //set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //handle dialog item clicks
                if (which == 0) {
                    //Edit Profile Clicked
                    pd.setMessage("Updating Profile Picture");
                    profilePhoto="image";
                    showImagePicDialog();

                }

                else if(which==1){
                    //Edit Name Clicked
                    pd.setMessage("Updating Username");
                    //calling method and pass key "userName" as parameter to update it's value in database
                    showNameBioDialog("userName");
                }

                else if(which==2){
                    //Edit Biography Clicked
                    pd.setMessage("Updating Biography");
                    showNameBioDialog("biography");
                }
            }
        });
        //create and show dialog
        builder.create().show();

    }

    private void showNameBioDialog(final String key) {
        /*parameter "key" will contain value:
           either "userName" which is key in user's database which is used to update user's name
           or "biography" which is key in user's database which is used to update user's bio
         */

        //custom dialog
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        builder.setTitle("Update"+key); //Update name or Update biography

        //set layout of dialog
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        //add edit text
        final EditText editText= new EditText(getActivity());
        editText.setHint("Enter"+key); //Enter name
        linearLayout.addView(editText);


        builder.setView(linearLayout);

        //add buttons in dialog to update
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //input text from edit text
                final String value= editText.getText().toString().trim();
                //validate if user has entered something or not
                if(!TextUtils.isEmpty(value)){
                    pd.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key,value);




                    firebaseFirestore.collection("Users").document(userId).update(result).addOnSuccessListener(new OnSuccessListener<Void>() {

                        @Override
                        public void onSuccess(Void aVoid) {

                            pd.dismiss();
                            if(key=="userName"){
                                text.setText(value);
                            }
                            else {
                                userBio.setText(value);
                            }

                            Toast.makeText(getActivity(),"Updated...",Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });


                }
                else{
                    Toast.makeText(getActivity(),"Please enter"+key,Toast.LENGTH_SHORT).show();

                }
            }
        });
        //add buttons in dialog to cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        //create and show dialog
        builder.create().show();

    }

    private void showImagePicDialog() {
        //show dialog containing options Camera and Gallery to pick the image
        String options[] = {"Gallery"};

        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //set title
        builder.setTitle("Pick Image From");
        //set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //handle dialog item clicks

                        pickFromGallery();



            }
        });
        //create and show dialog
        builder.create().show();
    }

    private void pickFromGallery() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {           //galeri izin sdk 28 ve altı icin ayrildi

        //This method will be called after picking image from gallery
        if (requestCode == 2 && resultCode == RESULT_OK && data != null ) {

            userImageData = data.getData();

            try {

                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(),userImageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    profilePicture.setImageBitmap(selectedImage);
                    uploadProfilePicture(userImageData);
                } else {
                    selectedImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),userImageData);
                    profilePicture.setImageBitmap(selectedImage);
                    uploadProfilePicture(userImageData);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfilePicture(Uri uri) {
        //show progress
        pd.show();

        if (userImageData != null) {

            final UUID uuid = UUID.randomUUID();                      //universal unique id kullanici profil fotograflari icin id olusturma
            final String imageName = "images/" + uuid + ".jpg";

            StorageReference storageReference2nd = storageReference.child(imageName);
            storageReference2nd.putFile(userImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    Uri downloadUrl= uriTask.getResult();

                    if(uriTask.isSuccessful()){
                        HashMap<String, Object> results = new HashMap<>();

                      results.put("downloadUrl",downloadUrl.toString());

                        firebaseFirestore.collection("Users").document(userId).update(results).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                pd.dismiss();
                                Toast.makeText(getActivity(),"Image Updated...",Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(getActivity(),"Error Updating Image...",Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                    else{
                        //error
                        pd.dismiss();
                        Toast.makeText(getActivity(),"Some error occured",Toast.LENGTH_SHORT).show();
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });


        }
    }


  /*  public void getDataUserFromFirestore()              //Giris yapan kullanici bilgilerini çeker.
    {
        CollectionReference collectionReference =firebaseFirestore.collection("Users");

        collectionReference.whereEqualTo("email",firebaseUser.getEmail().toString()).addSnapshotListener(new EventListener<QuerySnapshot>() {   //firebase den gelen datayi giris yaparken kullanilan e maile göre filtreliyoruz.
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e !=null){
                    Toast.makeText(getContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();    //eger firebasefirestoredan data okunanamazsa exception e yi göster
                }

                if(queryDocumentSnapshots!=null)
                {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments())
                    {
                        Map<String,Object> data= snapshot.getData();   //map olarak datayi geri cekiyoruz

                        userType =(String) data.get("userType");  //gelecek verinin string oldugundan emin oldugumuz icin casting islemi yapabiliyoruz
                        userName= (String) data.get("userName");
                        userGender =(String) data.get("gender");
                        userEmail=(String) data.get("email");
                        userBiography=(String) data.get("biography");
                        userBirthday=(String) data.get("birthday");
                        userPictureUrl=(String) data.get("downloadurl");

                        Picasso.get().load(userPictureUrl).into(profilePicture);

                        userBio.setText(userBiography);
                        text.setText(userName);
                    }
                }



            }
        });
    }



   */




    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
