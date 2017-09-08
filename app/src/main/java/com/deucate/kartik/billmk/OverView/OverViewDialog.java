package com.deucate.kartik.billmk.OverView;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.deucate.kartik.billmk.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class OverViewDialog extends DialogFragment {

    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    TextView groceryRsTV, electronicRsTv, foodRsTv, clothRsTv,billRsTv,otherRsTv;

    int groceryValue = 0, electronicValue, foodValue = 0, clothValue = 0, categor = 0,billValue = 0,otherValue = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        mDatabase = FirebaseDatabase.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mReference = mDatabase.getReference().child("Spent").child(uid);

        View view = inflater.inflate(R.layout.overview_alert, null);

        groceryRsTV = view.findViewById(R.id.groceryOverviewRs);
        electronicRsTv = view.findViewById(R.id.electronicOverviewRs);
        foodRsTv = view.findViewById(R.id.foodOverRs);
        clothRsTv = view.findViewById(R.id.clothOverRs);
        billRsTv = view.findViewById(R.id.billOverRs);
        otherRsTv = view.findViewById(R.id.otherOverRs);


        mReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                categor = dataSnapshot.child("Category").getValue(int.class);


                if (categor == 0) {
                    electronicValue += dataSnapshot.child("Rs").getValue(int.class);
                    electronicRsTv.setText(electronicValue + "");
                } else if (categor == 1) {
                    foodValue += dataSnapshot.child("Rs").getValue(int.class);
                    foodRsTv.setText(foodValue + "");
                } else if (categor == 2) {
                    clothValue += dataSnapshot.child("Rs").getValue(int.class);
                    clothRsTv.setText(clothValue+"");
                } else if (categor == 3) {
                    groceryValue += dataSnapshot.child("Rs").getValue(int.class);
                    groceryRsTV.setText(groceryValue + "");
                }else if (categor==4){
                    billValue += dataSnapshot.child("Rs").getValue(int.class);
                    billRsTv.setText(billValue+"");
                }else if (categor==5){
                    otherValue = dataSnapshot.child("Rs").getValue(int.class);
                    otherRsTv.setText(otherValue+"");
                }else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        builder.setView(view).setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });

        return builder.create();
    }
}
