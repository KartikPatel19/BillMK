package com.deucate.kartik.billmk;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class SpentDialog extends DialogFragment {

    EditText mReasonET, rsET;
    Spinner mSpinner;

    String mReason, date,time;
    int category, rs;

    DocumentReference mDocumentReference;
//    FirebaseDatabase mDatabase;
//    DatabaseReference mReference;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.spent_dialog, null);

//        mDatabase = FirebaseDatabase.getInstance();
//        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        mReference = mDatabase.getReference().child("Spent").child(uid).push();
        mDocumentReference = FirebaseFirestore.getInstance().collection("UserData").document("Spent");

        mReasonET = view.findViewById(R.id.alertSpentReason);
        rsET = view.findViewById(R.id.alertSpentRs);
        mSpinner = view.findViewById(R.id.alertSpentSpiner);

        builder
                .setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        mReason = mReasonET.getText().toString();
                        category = mSpinner.getSelectedItemPosition();
                        String rsT = rsET.getText().toString();

                        if (TextUtils.isEmpty(mReason)){
                            mReasonET.setError("Please enter reason");
                            Toast.makeText(getActivity(), "Please enter reason", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (rsT.equals("")){
                            Toast.makeText(getActivity(), "Please enter value", Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            rs = Integer.parseInt(rsT);
                        }

                        @SuppressLint("SimpleDateFormat")
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        java.util.Date dateFormt = new Date();
                        date = dateFormat.format(dateFormt);

                        @SuppressLint("SimpleDateFormat")
                        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                        time = timeFormat.format(dateFormt);

                        pushDataOnDatabase(mReason, category, date, rs,time);

                    }
                });

        return builder.create();
    }

    private void pushDataOnDatabase(String reason, int category, String date, int rs,String time) {

        Map<String, Object> data = new HashMap<>();

        data.put("Reason",reason);
        data.put("Rs", rs);
        data.put("Category", category);
        data.put("Date", date);
        data.put("Time", time);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDocumentReference.collection(uid).add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "onComplete: -----------> Task complete");
                }else {
                    Log.d(TAG, "onComplete: -------------> Task failed"+task.getException().getLocalizedMessage());
                }
            }
        });

    }


}
