package com.deucate.kartik.billmk;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.deucate.kartik.billmk.Add.AddList;
import com.deucate.kartik.billmk.Login.LoginActivity;
import com.deucate.kartik.billmk.OverView.OverViewDialog;
import com.deucate.kartik.billmk.Spent.SpentList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView mCurrentRS;

    FirebaseDatabase mDatabase;
    DatabaseReference mReferenceAdd;
    DatabaseReference mReferenceSpent;
    FirebaseAuth mAuth;

    Button mLogoutBtn,mOverViewBtn;

    int addedValue = 0, spentedValue = 0, currentValue = 0;
    String localSymbol = "₹";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        mReferenceAdd = mDatabase.getReference().child("Add").child(uid);
        mReferenceSpent = mDatabase.getReference().child("Spent").child(uid);

        mLogoutBtn = (Button) findViewById(R.id.mainLogout);
        mOverViewBtn = (Button) findViewById(R.id.mainOverViewBtn);

        mLogoutBtn.setBackgroundColor(Color.parseColor("#45FFAAAA"));
        mLogoutBtn.setTextColor(Color.parseColor("#FFFFFF"));
        mCurrentRS = (TextView) findViewById(R.id.mainCurrentText);

        syncData();

        mOverViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,OverviewActivity.class);
                startActivity(intent);

            }
        });


    }

    protected void syncData() throws NullPointerException{

        currentValue=0;
        spentedValue=0;
        addedValue=0;

        mReferenceAdd.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                addedValue += dataSnapshot.child("Rs").getValue(int.class);
                currentValue = addedValue - spentedValue;
                mCurrentRS.setText(currentValue+"");

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

        mReferenceSpent.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                try{
                    spentedValue += dataSnapshot.child("Rs").getValue(int.class);
                }catch (NullPointerException e){
                    Log.d("---->", "onChildAdded: "+e.getLocalizedMessage());
                }

                currentValue = addedValue - spentedValue;
                mCurrentRS.setText(localSymbol+currentValue);

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
    }

    public void onMoneyAdd(View view) {
        AddDialog addDialog = new AddDialog();
        addDialog.show(getFragmentManager(), "");
    }

    public void onMoneySpent(View view) {
        SpentDialog spentDialog = new SpentDialog();
        spentDialog.show(getFragmentManager(), "");
    }

    public void onClickAdded(View view) {
        Intent intent = new Intent(MainActivity.this, AddList.class);
        startActivity(intent);
    }

    public void onClickSpented(View view) {
        Intent intent = new Intent(MainActivity.this, SpentList.class);
        startActivity(intent);
    }

    public void onLayoutClick(View view) {
        syncData();
    }

    public void onlogout(View view) {

        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }
}
