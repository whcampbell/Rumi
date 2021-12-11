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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NewAccountActivity extends AppCompatActivity {
    SharedPreferences sp;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sp = getSharedPreferences("com.example.rumi", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
    }

    public void onClickCreateNewAcct(View view) {

        EditText editUser = findViewById(R.id.editTextTextPersonName);
        EditText editPass = findViewById(R.id.editTextTextPersonName2);
        EditText editHouse = findViewById(R.id.editTextNumber);
        String user = editUser.getText().toString();
        String pass = editPass.getText().toString();
        String number = editHouse.getText().toString();
        String id;

        Context context = getApplicationContext();
        String badInput = "Please provide all correct information";
        String usernameTaken = "Username is taken, please try another";
        String badHouse = "This house does not exist";
        int duration = Toast.LENGTH_SHORT;

        Toast toastInfo = Toast.makeText(context, badInput, duration);
        Toast toastHouse = Toast.makeText(context, badHouse, duration);
        Toast toastUsername = Toast.makeText(context, usernameTaken, duration);

        if (user == null || user.length() == 0) {
            toastInfo.show();
            editUser.setText("");
            editPass.setText("");
            return;
        }else if (pass == null || pass.length() == 0) {
            toastInfo.show();
            editUser.setText("");
            editPass.setText("");
            return;
        }
        if (!number.equals("")) {
            id = number;

        }else {
            Random rand = new Random();
            id = String.format("%04d", rand.nextInt(10000));
        }

            Map<String, Object> houseMap = new HashMap<String, Object>();
            houseMap.put("houseName", user + "'s House");
            db.collection("Houses").document(id).set(houseMap);

            Map<String, Object> userMap = new HashMap<String, Object>();
            userMap.put(LoginActivity.usernameKey, user);
            userMap.put("pass", pass);
            userMap.put("housenum", id);
            sp.edit().putString(LoginActivity.usernameKey, user).apply();
            sp.edit().putString(LoginActivity.houseNumKey, id).apply();

            db.collection("Houses").document(id).collection("user").document(user).set(userMap);
            db.collection("user").document(user).set(userMap);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("housenum", id);
            startActivity(intent);


    }


}