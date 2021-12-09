package com.example.rumi;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences sp;
    public static String usernameKey = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getSharedPreferences("com.uw.rumi", Context.MODE_PRIVATE);

        if (sp.getString(usernameKey, "").equals("")) {
            setContentView(R.layout.activity_login);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
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


        sp.edit().putString(usernameKey, user).apply();

        // TODO pull database info from matching username
        // check that passwords match
        // set intent to load correct household

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    public void onClickNewAcct(View view) {
        Intent intent = new Intent(this, NewAccountActivity.class);
        startActivity(intent);
    }

}