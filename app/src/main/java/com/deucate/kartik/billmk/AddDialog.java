package com.deucate.kartik.billmk;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class AddDialog extends DialogFragment {

    EditText mEditText;
    Spinner mSpinner;

    FirebaseFirestore mFirestore;
    DocumentReference mDocumentReference;


    int rs = 0;
    int category = 0;
    String date, time;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        mFirestore = FirebaseFirestore.getInstance();
        mDocumentReference = mFirestore.collection("UserData").document("Add");

        View view = inflater.inflate(R.layout.add_dialog_alert, null);

        mEditText = view.findViewById(R.id.alertAddET);
        mSpinner = view.findViewById(R.id.alertAddSpiner);

        builder.setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String rsT = mEditText.getText().toString();

                        if (rsT.equals("")) {
                            Toast.makeText(getActivity(), "Please enter value", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            rs = Integer.parseInt(rsT);
                        }

                        category = mSpinner.getSelectedItemPosition();

                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date dateFormt = new Date();
                        date = dateFormat.format(dateFormt);

                        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                        time = timeFormat.format(dateFormt);

                        pushDataOndatabase(rs, category, date, time);

                    }
                });

        return builder.create();
    }

    private void pushDataOndatabase(int rs, int category, String date, String Time) {

        Map<String, Object> data = new HashMap<>();

        data.put("Rs", rs);
        data.put("Category", category);
        data.put("Date", date);
        data.put("Time", Time);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDocumentReference.collection(uid).add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "onComplete: -----------> Task compllate");
                }else {
                    Log.d(TAG, "onComplete: -------------> Task failed"+task.getException().getLocalizedMessage());
                }
            }
        });

    }

}
