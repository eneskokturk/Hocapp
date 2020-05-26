package com.example.hocapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hocapp.MainActivity;
import com.example.hocapp.MessageActivity;
import com.example.hocapp.R;
import com.example.hocapp.models.ChatModel;
import com.example.hocapp.models.LessonModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;
import com.google.gson.internal.$Gson$Preconditions;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {



    public ArrayList arrayList;
    public Context context;



    private FirebaseAuth firebaseAuth;
    public FirebaseUser firebaseUser;





    // RecyclerView recyclerView;
    public MessageAdapter(ArrayList listdata, Context context) {

        this.arrayList = listdata;
        this.context = context;

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();




    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View list = MainActivity.inflater.inflate(R.layout.chat_item_right, parent, false);
            return new listItem(list);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        final listItem listItem = (MessageAdapter.listItem) holder;
        final ChatModel list = (ChatModel) arrayList.get(position);

        listItem.show_message.setText(list.getMessage());






    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class listItem extends RecyclerView.ViewHolder{

        public TextView show_message;



        public listItem(@NonNull View itemView) {
            super(itemView);

            show_message=itemView.findViewById(R.id.show_message);



        }
    }


}
