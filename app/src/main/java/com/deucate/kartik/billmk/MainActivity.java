package com.deucate.kartik.billmk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    TextView mCurrentRS;

//    FirebaseDatabase mDatabase;
//    DatabaseReference mReferenceAdd;
//    DatabaseReference mReferenceSpent;
    FirebaseFirestore mFirestore;
    DocumentReference mDocumentReferenceAdd,mDocumentReferenceSpent;


    FirebaseAuth mAuth;

    Button mOverViewBtn;
    LineChart mLineChart;

    List<Integer> mChartSpentData = new ArrayList<>();

    long addedValue = 0, spentedValue = 0, currentValue = 0;
    String localSymbol = "₹";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mDocumentReferenceAdd = mFirestore.collection("UserData").document("Add");
        mDocumentReferenceSpent = mFirestore.collection("UserData").document("Spent");

        mOverViewBtn = findViewById(R.id.mainOverViewBtn);
        mLineChart = findViewById(R.id.mainLineChart);
        mCurrentRS = findViewById(R.id.mainCurrentText);

        syncData();

        mOverViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, OverviewActivity.class);
                startActivity(intent);

            }
        });


    }

    protected void syncData() throws NullPointerException {

        String uid = mAuth.getCurrentUser().getUid();

        currentValue = 0;
        spentedValue = 0;
        addedValue = 0;

        mDocumentReferenceAdd.collection(uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot : task.getResult()){
                        Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                        addedValue += (long) documentSnapshot.get("Rs");
                    }
                    currentValue=0;
                    currentValue = addedValue-spentedValue;
                    mCurrentRS.setText(localSymbol+currentValue);
                }else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        mDocumentReferenceSpent.collection(uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                        long currentSpent =(long) documentSnapshot.get("Rs");
                        spentedValue += currentSpent;
                        mChartSpentData.add((int)currentSpent);

                    }
                    currentValue=0;
                    currentValue = addedValue-spentedValue;
                    mCurrentRS.setText(localSymbol+currentValue);
                }else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        List<Entry> entries = new ArrayList<>();

        int i = 0;
        for(int currentRS : mChartSpentData){
            i++;
            entries.add(new Entry(i,(float) currentRS));
        }
        LineDataSet dataSet = new LineDataSet(entries,"Usage");

    }

    public void onMoneyAdd(View view) {
        AddDialog addDialog = new AddDialog();
        addDialog.show(getFragmentManager(), "");
    }

    public void onMoneySpent(View view) {
        SpentDialog spentDialog = new SpentDialog();
        spentDialog.show(getFragmentManager(), "");
    }

}
