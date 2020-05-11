package com.example.hocapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.security.Key;
import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Map;

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
    private DocumentReference documentReference;


    //storage
    StorageReference storageReference;
    //path where image of user profile will be stored
    String storePath = "User_Profile_Imgs/";

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
    FloatingActionButton fab;

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

        databaseReference =firebaseDatabase.getReference("Users");

        storageReference =getInstance().getReference(); //firebase storage reference







        profilePicture=view.findViewById(R.id.profilePicture);
        text=view.findViewById(R.id.text);
        userBio=view.findViewById(R.id.userBio);
        fab=view.findViewById(R.id.fab);
        TextView textView1 = view.findViewById(R.id.text);

        //init progress dialog
        pd = new ProgressDialog(getActivity());


        firebaseUser = firebaseAuth.getCurrentUser();    // kullanici giris yapmis ise deger döndürür ,kimse yok ise null dondurur
        getDataUserFromFirestore();





       //fab button click

      fab.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              showEditProfileDialog();
          }
      });




        return view;
    }

    private boolean checkStoragePermission(){
        //check if storage permission is enabled or not
        //return true if enabled
        //return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(getActivity(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestStoragePermission(){
        //request runtime storage permission
        requestPermissions(storagePermissions,STORAGE_REQUEST_CODE);

    }

    private boolean checkCameraPermission(){
        //check if storage permission is enabled or not
        //return true if enabled
        //return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(getActivity(),android.Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(getActivity(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermission(){
        //request runtime storage permission
        requestPermissions(cameraPermissions,CAMERA_REQUEST_CODE);

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
                String value= editText.getText().toString().trim();
                //validate if user has entered something or not
                if(!TextUtils.isEmpty(value)){
                    pd.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key,value);


                    databaseReference.child(firebaseUser.getUid()).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {

                   // firebaseFirestore.collection("Users").document(currentDocumentId[0]).update(result)
                        @Override
                        public void onSuccess(Void aVoid) {
                            pd.dismiss();
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
        String options[]={"Camera","Gallery"};

        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //set title
        builder.setTitle("Pick Image From");
        //set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //handle dialog item clicks
                if (which == 0) {
                    //Camera Clicked

                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else {
                        pickFromCamera();
                    }
                }

                else if(which==1){
                    //Gallery Clicked
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else{
                        pickFromGallery();
                    }
                }


            }
        });
        //create and show dialog
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        /*This method called when user press Allow or Deny from permission request dialog
         here we will handle permission cases (allowed & denied)
         */

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                //picking from camera, first check if camera and storage permissions allowed or not

                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1]== PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && writeStorageAccepted){
                        //permissions enabled
                        pickFromCamera();
                    }
                    else {
                        //permission denied
                        Toast.makeText(getActivity(),"Please enable camera & storage permission ",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                //picking from gallery, first check if storage permissions allowed or not

                if(grantResults.length>0){
                    boolean writeStorageAccepted = grantResults[1]== PackageManager.PERMISSION_GRANTED;
                    if(writeStorageAccepted){
                        //permissions enabled
                        pickFromGallery();
                    }
                    else {
                        //permission denied
                        Toast.makeText(getActivity(),"Please enable storage permission ",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //This method will be called after picking image from Camera or Gallery
        if(resultCode ==RESULT_OK){
            if(requestCode==IMAGE_PICK_GALLERY_CODE){
                //image is picked from gallery, get uri of image
                image_uri= data.getData();

                uploadProfilePhoto(image_uri);
            }
            if(requestCode==IMAGE_PICK_CAMERA_CODE){
                //image is picked from camera, get uri of image
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfilePhoto(Uri uri) {
        //show progress
        pd.show();
        /* To add check ill add a string variable and assign it value "image" when user clicks
         "Edit Profile Pic"
         Here: image is the key in each user containing url of user's profile picture
         */

        /*The parameter "image_uri" contains the uri of image picked either from camera or gallery
        We will use UID of the currently signed in user as name of the image so there will be only one image
        profile
         */

        //path and name of image to be stored in firebase storage
        String filePathAndName = storePath+""+profilePhoto+"_"+userPictureUrl;

        StorageReference storageReference2nd= storageReference.child(filePathAndName);
        storageReference2nd.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask= taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        Uri dowloadUri = uriTask.getResult();

                        //check if image is uploaded or not and url is received
                        if(uriTask.isSuccessful()){
                            //image uploaded
                            //add/update url in user's database
                            HashMap<String, Object> results= new HashMap<>();
                            /*First parameter is profilePhoto that has value "image"
                            which are keys in user's database where url of image will be saved in one of them
                            Second parameter contains the url of the image stored in firebase storage, this url will be saved as value against key "image"
                             */
                            results.put(profilePhoto,dowloadUri.toString());

                            databaseReference.child(userPictureUrl).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //url in database of user is added succesfully
                                            //dismiss progress bar
                                            pd.dismiss();
                                            Toast.makeText(getActivity(),"Image Updated...",Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //error adding url in database of user
                                            //dismiss progress bar
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        pd.dismiss();
                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });


    }

    private void pickFromCamera() {
        //Intent of picking image from device camera
        ContentValues values= new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");
        // put image uri
        image_uri= getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        //intent to start camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromGallery() {
        //pick from gallery
        Intent galleryIntent= new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE);

    }

    public void getDataUserFromFirestore()              //Giris yapan kullanici bilgilerini çeker.
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






    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
