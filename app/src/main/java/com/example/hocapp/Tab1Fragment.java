
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


/**
 * A simple {@link Fragment} subclass.
 */



public class Tab1Fragment extends Fragment {

    private MapView mMapView;
    private GoogleMap googleMap;
   // Spinner spin;
   // Spinner spinAlan;
   // Spinner spinFiyat;
    public Tab1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

       /* mMapView= (MapView)view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMap.setMyLocationEnabled(true);
                //To add marker
                LatLng sydney = new LatLng(-34, 151);
               // googleMap.addMarker(new MarkerOptions().position(sydney).title("Title").snippet("Marker Description"));
                // For zooming functionality
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

*/

        final Spinner spin = (Spinner) view.findViewById(R.id.derslerSpinner);  //Spinner Dersler
        String[] dersler = getResources().getStringArray(R.array.dersler);
        ArrayAdapter<String> derslerAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.dersler_spinner_layout, R.id.textView, dersler);
        spin.setAdapter(derslerAdapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String code = String.valueOf(spin.getSelectedItem());
                if (position == 0) {
                    spin.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });


        final Spinner spinAlan = (Spinner) view.findViewById(R.id.alanSpinner);   //Spinner Alanlar
        final String[] alanlar = getResources().getStringArray(R.array.alanlar);
        ArrayAdapter<String> alanlarAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.alanlar_spinner_layout, R.id.textView, alanlar);
        spinAlan.setAdapter(alanlarAdapter);
        spinAlan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String code = String.valueOf(spinAlan.getSelectedItem());
                if (position == 0) {
                    spinAlan.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });


        final Spinner spinFiyat= (Spinner)view.findViewById(R.id.fiyatSpinner);
        final String[] fiyatlar = getResources().getStringArray(R.array.fiyatlar);
        ArrayAdapter<String> fiyatlarAdapter = new ArrayAdapter<String>(this.getActivity(),R.layout.fiyatlar_spinner_layout,R.id.textView,fiyatlar);
        spinFiyat.setAdapter(fiyatlarAdapter);
        spinFiyat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String code = String.valueOf(spinFiyat.getSelectedItem());
                if(position==0){
                    spinFiyat.setSelection(0)
                    ;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }

    /*
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
*/
}
