package com.example.rumi;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences sp;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String usernameKey = "username";
    public final String houseNumKey = "housenum";
    Map<String, String> userPass = new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getSharedPreferences("com.uw.rumi", Context.MODE_PRIVATE);
        userPass = getUserPass();

        if (sp.getString(usernameKey, "").equals("")) {
            setContentView(R.layout.activity_login);
        } else {
            Map<String, String> houseNumberMap = getHouseNum(sp.getString(usernameKey, ""));
            String houseNumber = houseNumberMap.get(houseNumKey);

            Intent intent = new Intent(this, MainActivity.class);
            //intent.putExtra("housenum", houseNumber);
            startActivity(intent);
            finish();
        }

    }

    public void onClickLogin(View view) {
        EditText editUser = findViewById(R.id.editTextUser);
        EditText editPass = findViewById(R.id.editTextPass);
        String user = editUser.getText().toString();
        String pass = editPass.getText().toString();


        Context context = getApplicationContext();
        String badInput = "Username or Password Incorrect";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, badInput, duration);

        if(!userPass.containsKey(user) || !userPass.get(user).equals(pass)){
            toast.show();
            editUser.setText("");
            editPass.setText("");
            return;
        }

        if (user == null || user.length() == 0) {
            toast.show();
            editUser.setText("");
            editPass.setText("");
            return;
        }

        if (pass == null || pass.length() == 0) {
            toast.show();
            editUser.setText("");
            editPass.setText("");
            return;
        }

        Map house = getHouseNum(user);
        String houseNumber = (String) house.get("housenum");

        sp.edit().putString(usernameKey, user).apply();

        // TODO pull database info from matching username
        // check that passwords match
        // set intent to load correct household

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("housenum", houseNumber);
        startActivity(intent);
        finish();

    }

    public void onClickNewAcct(View view) {
        Intent intent = new Intent(this, NewAccountActivity.class);
        startActivity(intent);
    }
    public Map<String, String> getUserPass(){
        Map<String, String> returnval = new HashMap<String, String>();
        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> unpaid = new ArrayList<String>();
                            ArrayList<QueryDocumentSnapshot> unpaidId = new ArrayList<QueryDocumentSnapshot>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String username = (String) document.getData().get("username");
                                String pass = (String) document.getData().get("pass");
                                returnval.put(username, pass);

                            }


                        } else {
                            Log.e(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        Log.e(TAG, returnval.toString());
        return returnval;
    }
    public Map<String, String> getHouseNum (String user){
        Map<String, String> returnval = new HashMap<String, String>();
        DocumentReference docRef = db.collection("user").document(user);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();



                    if (document.exists()) {
                        String houseNumber = (String) document.getData().get(houseNumKey);
                        returnval.put(houseNumKey, houseNumber);
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }

        });
        return returnval;
    }

}