package com.example.hocapp;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


<<<<<<< Updated upstream
=======
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;


>>>>>>> Stashed changes
/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1Fragment extends Fragment {

<<<<<<< Updated upstream
=======

    Spinner derslerSpinner;
    Spinner alanSpinner;
    TextView textViewDers;

    public Tab1Fragment() {
        // Required empty public constructor
    }
>>>>>>> Stashed changes

    private static final String TAG="İlanEkle";

<<<<<<< Updated upstream
    private PageViewModel pageViewModel;

   public Tab1Fragment(){

   }

    public static Tab1Fragment newInstance(){
       return new Tab1Fragment();
    }
=======
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        final Spinner derslerSpinner = (Spinner) view.findViewById(R.id.derslerSpinner);        //assing variable
        final TextView textViewDers = view.findViewById(R.id.textViewDers);
        final Spinner alanSpinner = (Spinner) view.findViewById(R.id.alanSpinner);

        ArrayList<String> derslerList= new ArrayList<>();

        derslerList.add("ders sec");
        derslerList.add("mat");
        derslerList.add("fen");
        derslerList.add("kimya");
        derslerList.add("geo");
        derslerList.add("mat");
        derslerList.add("fen");
        derslerList.add("kimya");
        derslerList.add("geo");

        derslerSpinner.setAdapter(new ArrayAdapter<>(this.getActivity(),android.R.layout.simple_spinner_dropdown_item,derslerList));

        derslerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    textViewDers.setText("");
                }
                else{
                    String selectedDers= parent.getItemAtPosition(position).toString();
                    textViewDers.setText(selectedDers);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
>>>>>>> Stashed changes

    public void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);

       pageViewModel= ViewModelProviders.of(this).get(PageViewModel.class);
       pageViewModel.setIndex(TAG);
    }


<<<<<<< Updated upstream
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_home,container,false);

       /*final TextView textView = root.findViewById(R.id.section_label);
        pageViewModel.getText().observe(this,new Observer<String>(){

            public void onChanged(String s){
                textView.setText(s);
            }
        }); */
        return root;
=======



        return view;
>>>>>>> Stashed changes
    }

}
