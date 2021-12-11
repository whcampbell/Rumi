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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NewAccountActivity extends AppCompatActivity {
    SharedPreferences sp;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sp = getSharedPreferences("com.uw.rumi", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
    }

    public void onClickCreateNewAcct(View view) {

        EditText editUser = findViewById(R.id.editTextTextPersonName);
        EditText editPass = findViewById(R.id.editTextTextPersonName2);
        String user = editUser.getText().toString();
        String pass = editPass.getText().toString();


        Context context = getApplicationContext();
        String badInput = "Please provide all information";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, badInput, duration);

        if (user == null || user.length() == 0) {
            toast.show();
            editUser.setText("");
            editPass.setText("");
            return;
        }else if (pass == null || pass.length() == 0) {
            toast.show();
            editUser.setText("");
            editPass.setText("");
            return;
        }else {
            Map<String, Object> userMap = new HashMap<String, Object>();
            Random rand = new Random();
            String id = String.format("%04d", rand.nextInt(10000));
            userMap.put(LoginActivity.usernameKey, user);
            userMap.put("pass", pass);
            userMap.put("housenum", id);
            sp.edit().putString(LoginActivity.usernameKey, user).apply();

            db.collection("house").document(id).collection("user").document("user")
                    .set(userMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.e(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "Error writing document", e);
                                }
                            });
            db.collection("user").document(user).set(userMap);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("housenum", id);
            startActivity(intent);
        }

    }

}