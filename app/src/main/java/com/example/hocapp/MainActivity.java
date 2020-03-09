package com.example.hocapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


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

        toolbar.setTitle("Home");
        loadFragment(new HomeFragment());




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
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle("Home");
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_video:
                    toolbar.setTitle("Canlı Yayın");
                    fragment = new VideoFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_forum:
                    toolbar.setTitle("Forum");
                    fragment = new ForumFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    toolbar.setTitle("Profile");
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }

            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        //load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void signOut(View view)
    {
        firebaseAuth.signOut();

        Intent intentToSignIn = new Intent(MainActivity.this,SignInActivity.class);
        startActivity(intentToSignIn);
        finish();
    }
}
