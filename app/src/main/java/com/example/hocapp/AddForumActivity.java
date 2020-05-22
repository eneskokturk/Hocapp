package com.example.hocapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.UUID;

public class AddForumActivity extends AppCompatActivity {

    ImageView close;
    TextView save;
    EditText forumTitle;
    EditText forumContent;


    String userId;
    String userFirebaseEmail;

    private FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private CollectionReference collectionReference=firebaseFirestore.collection("Forums");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_forum);

        firebaseAuth = FirebaseAuth.getInstance();              // initializing
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        final String currentEmail= FirebaseAuth.getInstance().getCurrentUser().getEmail();

        forumContent=findViewById(R.id.forumContent);
        forumTitle=findViewById(R.id.forumTitle);
        save=findViewById(R.id.addForum);
        close=findViewById(R.id.close);




        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userId = firebaseAuth.getCurrentUser().getUid();


                String forumContentDatabase = forumContent.getText().toString();
                String forumTitleDatabase= forumTitle.getText().toString();


                 DocumentReference ref= firebaseFirestore.collection("Forums").document();

                 final String postid= ref.getId();


                final HashMap<String,Object> forumData = new HashMap<>();


                forumData.put("postid",postid);
                forumData.put("date", FieldValue.serverTimestamp());
                forumData.put("forumTitle",forumTitleDatabase);
                forumData.put("forumContent",forumContentDatabase);
                forumData.put("publisherId",userId);



                firebaseFirestore.collection("Forums").add(forumData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){

                            DocumentReference docRef= task.getResult();
                            String key=docRef.getId();
                            Log.v("KEY",key);

                            final HashMap<String,Object> forumData = new HashMap<>();
                            forumData.put("postid",key);

                            firebaseFirestore.collection("Forums").document(key).update(forumData);




                        }

                    }
                });




                finish();

            }
        });
    }
}
