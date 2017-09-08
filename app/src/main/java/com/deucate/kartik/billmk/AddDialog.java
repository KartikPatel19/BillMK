package com.deucate.kartik.billmk;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddDialog extends DialogFragment {

    EditText mEditText;
    Spinner mSpinner;

    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    int rs = 0;
    int category = 0;
    String date, time;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        mDatabase = FirebaseDatabase.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mReference = mDatabase.getReference().child("Add").child(uid).push();

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

        mReference.child("Rs").setValue(rs);
        mReference.child("Category").setValue(category);
        mReference.child("Date").setValue(date);
        mReference.child("Time").setValue(Time);

    }

}
