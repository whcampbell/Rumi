package com.example.rumi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private NavigationBarView bottomNavigationView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SharedPreferences sp;
    public static String houseNumber;
    public static String username;
    public static String houseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        sp = getSharedPreferences("com.example.rumi", Context.MODE_PRIVATE);
        Intent intent = getIntent();
        houseNumber = intent.getStringExtra(LoginActivity.houseNumKey);
        username = intent.getStringExtra(LoginActivity.usernameKey);
        // house name will be set either in "new account activity" or "dashboard activity."
        // it is possible that no house is set on login, so <username>'s house will be created
        // as a default.

        String houseName = sp.getString("houseName", "");
        if (houseName.equals("")) {
            // no house currently in shared preferences. need to go to default

            // HARD CODE FOR TESTING
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("houseName", "testHouse").apply();
            // HARD CODE FOR TESTING
            Log.i("info", "Setting houseName");

            //TODO database call create or load default house "<username>'s house"
        } else {
            Log.i("info", "House name in SP is " + houseName);
        }

        Log.i("info", "after set(?), housename is " + sp.getString("houseName", "err"));

        Context context = getApplicationContext();
        String welcome = String.format("Welcome home, %s!", username);
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, welcome, duration);
        toast.show();




       //Log.e(TAG, houseNumber);

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
                houseEdit();
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
                    fragment = new agreements(db);
                    break;
                case R.id.payments:
                    fragment = new payments();
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
    public void houseEdit(){
        HouseEditDialog houseEdit = new HouseEditDialog(sp);
        houseEdit.show(getSupportFragmentManager(), "events dialog");
    }

    public String getHouseName(){
        Map<String, Object> houseName = new HashMap<String, Object>();
        db.collection("Houses").document(houseNumber).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                   houseName.put("name", document.getData().get("houseName"));

                }
            }
        });
        return (String) houseName.get("name");
    }

}