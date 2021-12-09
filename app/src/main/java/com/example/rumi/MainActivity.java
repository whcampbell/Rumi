package com.example.rumi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private NavigationBarView bottomNavigationView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        sp = getSharedPreferences("com.uw.rumi", Context.MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new dashboard()).commit();

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnItemSelectedListener(bottomnavFunction);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.Logout:
                logout();
                break;
            case R.id.SwitchHouse:

                break;
        }

        return true;

    }

    private NavigationBarView.OnItemSelectedListener bottomnavFunction = new NavigationBarView.OnItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()){
                case R.id.dashboard:
                    fragment = new dashboard();
                    break;
                case R.id.chores:
                    fragment = new chores();
                    break;
                case R.id.calendar:
                    fragment = new calendar();
                    break;
                case R.id.agreements:
                    fragment = new agreements();
                    break;
                case R.id.payments:
                    fragment = new payments(db);
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            return true;
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.house_login, menu);
        return true;
    }
    public void logout(){
        sp.edit().remove(LoginActivity.usernameKey).apply();
        startActivity(new Intent(this, LoginActivity.class));

    }
}