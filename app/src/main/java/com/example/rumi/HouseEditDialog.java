package com.example.rumi;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class HouseEditDialog extends AppCompatDialogFragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences sp;
    public HouseEditDialog(){

    }
    public HouseEditDialog(SharedPreferences sp){
        this.sp = sp;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.house_edit_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Switch houses")
                .setView(view)
                .setPositiveButton("Switch House", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText editHouseNumber = view.findViewById(R.id.houseNumber);
                        String houseNumber = editHouseNumber.getText().toString();
                        editHouse(houseNumber);
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });



        return builder.create();
    }

    public void editHouse(String houseNumber){
        DocumentReference documentRef = db.collection("user").document(MainActivity.username);
        String oldHouseNumber = MainActivity.houseNumber;
        MainActivity.houseNumber = houseNumber;
        Log.e(TAG, MainActivity.houseNumber);
        Map<String, Object> userUpdate = new HashMap<String, Object>();
        userUpdate.put("housenum", houseNumber);
        Map<String, Object> userDoc = new HashMap<String, Object>();

        documentRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    userDoc.put("pass", document.getData().get("pass"));

                }
            }
        });
        userDoc.put(LoginActivity.usernameKey, MainActivity.username);
        userDoc.put(LoginActivity.houseNumKey, houseNumber);

        Log.e(TAG, oldHouseNumber);
        Log.e(TAG, MainActivity.username);

        db.collection("Houses").document(oldHouseNumber).collection("user").document(MainActivity.username).delete();
        db.collection("user").document(MainActivity.username).update(userUpdate);
        db.collection("Houses").document(houseNumber).collection("user").document(MainActivity.username).set(userDoc);
        sp.edit().remove(LoginActivity.houseNumKey).apply();
        sp.edit().putString(LoginActivity.houseNumKey, houseNumber).apply();
    }
}
