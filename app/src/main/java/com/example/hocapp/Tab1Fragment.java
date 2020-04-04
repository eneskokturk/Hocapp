
package com.example.hocapp;



import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */



public class Tab1Fragment extends Fragment {

    Spinner lessonSpinner;          //Dersler Spinner
    Spinner lessonFieldSpinner;     //Ders alanları Spinner
    EditText lessonPrice;           //Ders Ücreti

    public Tab1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        Spinner lessonSpinner = view.findViewById(R.id.lessonSpinner);
        Spinner lessonFieldSpinner = view.findViewById(R.id.lessonFieldSpinner);
        EditText lessonPrice=view.findViewById(R.id.lessonPrice);

        ArrayList<String> lessonList = new ArrayList<>();           //Dersler Listesi

        lessonList.add("Ders Seçiniz..");
        lessonList.add("Matematik");
        lessonList.add("Fizik");
        lessonList.add("Kimya");
        lessonList.add("Geometri");
        lessonList.add("Biyoloji");
        lessonList.add("Türkçe");
        lessonList.add("Edebiyat");
        lessonList.add("İngilizce");
        lessonList.add("Dil ve Anlatım");
        lessonList.add("Masa Tenisi");
        lessonList.add("Basketbol");

        lessonSpinner.setAdapter(new ArrayAdapter<>(this.getActivity(),android.R.layout.simple_spinner_dropdown_item,lessonList));

        lessonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    System.out.println("gg");           //Spinner 0 konumunda seçim yapılmadı
                }
                else
                {
                    System.out.println("aa");           //Spinner 0 konumunda değil. Seçim yapıldı.
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> lessonFieldList = new ArrayList<>();              //Ders Alanları

        lessonFieldList.add("Alan Seçiniz...");
        lessonFieldList.add("İlkokul");
        lessonFieldList.add("Ortaokul");
        lessonFieldList.add("Lise");
        lessonFieldList.add("Üniversite");
        lessonFieldList.add("YKS");

        lessonFieldSpinner.setAdapter(new ArrayAdapter<>(this.getActivity(),android.R.layout.simple_spinner_dropdown_item,lessonFieldList));

        lessonFieldSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position==0)
                {
                    System.out.println("gg");           //Spinner 0 konumunda seçim yapılmadı
                }
                else
                {
                    System.out.println("aa");           //Spinner 0 konumunda değil. Seçim yapıldı.
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }

}
