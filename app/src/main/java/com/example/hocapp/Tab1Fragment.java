package com.example.hocapp;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1Fragment extends Fragment {


    private static final String TAG="İlanEkle";

    private PageViewModel pageViewModel;

   public Tab1Fragment(){

   }

    public static Tab1Fragment newInstance(){
       return new Tab1Fragment();
    }

    public void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);

       pageViewModel= ViewModelProviders.of(this).get(PageViewModel.class);
       pageViewModel.setIndex(TAG);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_home,container,false);

        final TextView textView = root.findViewById(R.id.section_label);
        pageViewModel.getText().observe(this,new Observer<String>(){

            public void onChanged(String s){
                textView.setText(s);
            }
        });
        return root;
    }

}
