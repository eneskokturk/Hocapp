package com.example.hocapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hocapp.adapters.AdapterCommentList;
import com.example.hocapp.adapters.MessageAdapter;
import com.example.hocapp.models.ChatModel;
import com.example.hocapp.models.LessonModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {


    ImageView profile_image;
    TextView messageUsername;

    MessageAdapter messageAdapter;

    ImageView btn_send;
    EditText text_send;


    RecyclerView recyclerView;

    private DocumentReference ref;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firebaseFirestore.collection("Chats");
    private FirebaseAuth firebaseAuth;
    String userId;


    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        profile_image = findViewById(R.id.profile_image);
        messageUsername = findViewById(R.id.messageUsername);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.txt_send);


        recyclerView = findViewById(R.id.recycler_view);



        firebaseAuth = FirebaseAuth.getInstance();

        intent = getIntent();
        String lessonUsername = intent.getStringExtra("lessonUsername");
        final String lessonUserId = intent.getStringExtra("userid");


        messageUsername.setText(lessonUsername);

        userId = firebaseAuth.getCurrentUser().getUid();


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(text_send.getText().toString().equals("")){
                   Toast.makeText(MessageActivity.this,"Lütfen mesaj girin.",Toast.LENGTH_SHORT).show();
               }else{
                   sendMessage();
               }

                readMessage();
            }
        });


        readMessage();

    }

    private void sendMessage() {


        final HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("message",text_send.getText().toString());
        hashMap.put("sender",userId);

        firebaseFirestore.collection("Chats").add(hashMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT).show();
                text_send.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void readMessage() {

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<ChatModel> types = queryDocumentSnapshots.toObjects(ChatModel.class);

                ArrayList<ChatModel> chatModelArrayList = new ArrayList<ChatModel>();
                chatModelArrayList.addAll(types);

                MessageAdapter messageAdapter= new MessageAdapter(chatModelArrayList,getApplicationContext());

                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                layoutManager.setReverseLayout(true);
                layoutManager.setStackFromEnd(true);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);

                recyclerView.setAdapter(messageAdapter);
                for(int i=0; i<chatModelArrayList.size();i++){
                    Log.e("xxxx",chatModelArrayList.get(i).getMessage());
                }

            }
        });

    }




}

