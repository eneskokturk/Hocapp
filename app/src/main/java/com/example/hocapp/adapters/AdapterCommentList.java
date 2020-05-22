package com.example.hocapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hocapp.CommentsActivity;
import com.example.hocapp.MainActivity;
import com.example.hocapp.R;
import com.example.hocapp.models.CommentsModel;
import com.example.hocapp.models.ForumModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class AdapterCommentList extends RecyclerView.Adapter {
    public ArrayList arrayList;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private FirebaseUser firebaseUser;

    // RecyclerView recyclerView;
    public AdapterCommentList(ArrayList listdata, Context context) {

        this.arrayList = listdata;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View list = MainActivity.inflater.inflate(R.layout.comments_card_item, parent, false);
        return new listItem(list);




    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        final listItem listItem = (AdapterCommentList.listItem) holder;
        final CommentsModel list = (CommentsModel) arrayList.get(position);

        listItem.comments.setText(list.getComment());

    }





    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class listItem extends RecyclerView.ViewHolder{

        public TextView comments;





        public listItem(@NonNull View itemView) {
            super(itemView);

            comments=itemView.findViewById(R.id.comment);






        }
    }

}
