package com.example.hocapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Spinner spin;
    Spinner spinAlan;

    private FirebaseAuth firebaseAuth;
    private ActionBar toolbar;  //Navigation Bar




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar= getSupportActionBar();

        BottomNavigationView navigation= (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        toolbar.setTitle("Home");


        spin= findViewById(R.id.derslerSpinner);  //Spinner Dersler
        final String[] dersler= getResources().getStringArray(R.array.dersler);
        ArrayAdapter<String> derslerAdapter = new ArrayAdapter<String >(this, R.layout.dersler_spinner_layout,R.id.textView,dersler);
        spin.setAdapter(derslerAdapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String code= String.valueOf(spin.getSelectedItem());
                if(position==0){
                    spin.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }



        });



        spinAlan=findViewById(R.id.alanSpinner);   //Spinner Alanlar
        final String[] alanlar= getResources().getStringArray(R.array.alanlar);
        ArrayAdapter<String> alanlarAdapter = new ArrayAdapter<String >(this, R.layout.alanlar_spinner_layout,R.id.textView,alanlar);
        spinAlan.setAdapter(alanlarAdapter);
        spinAlan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String code= String.valueOf(spinAlan.getSelectedItem());
                if(position==0){
                    spinAlan.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }



        });



        firebaseAuth = FirebaseAuth.getInstance();  // initializing



    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment;
            switch (menuItem.getItemId()){
                case R.id.navigation_home:
                    toolbar.setTitle("Home");
                    return true;
                case R.id.navigation_video:
                    toolbar.setTitle("Canlı Yayın");
                    return true;
                case R.id.navigation_forum:
                    toolbar.setTitle("Forum");
                    return true;
                case R.id.navigation_profile:
                    toolbar.setTitle("Profil");
                    return true;

            }
            return false;
        }
    };


    public void signOut(View view)
    {
        firebaseAuth.signOut();

        Intent intentToSignIn = new Intent(MainActivity.this,SignInActivity.class);
        startActivity(intentToSignIn);
        finish();
    }
}
