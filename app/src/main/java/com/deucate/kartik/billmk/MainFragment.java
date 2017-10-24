package com.deucate.kartik.billmk;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
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
import java.util.Timer;
import java.util.TimerTask;


public class MainFragment extends Fragment {

    public MainFragment() {
    }

    private static final String TAG = "MainFragment";
    TextView mCurrentRS;

    FirebaseFirestore mFirestore;
    DocumentReference mDocumentReferenceAdd, mDocumentReferenceSpent;

    FirebaseAuth mAuth;

    Button mOverViewBtn;
    LineChart mLineChart;

    List<Integer> mChartSpentData;

    long addedValue = 0, spentedValue = 0, currentValue = 0;
    String localSymbol = "₹";
    int j = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mChartSpentData = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mDocumentReferenceAdd = mFirestore.collection("UserData").document("Add");
        mDocumentReferenceSpent = mFirestore.collection("UserData").document("Spent");

        mOverViewBtn = view.findViewById(R.id.mainOverViewBtn);
        mLineChart = view.findViewById(R.id.mainLineChart);
        mCurrentRS = view.findViewById(R.id.mainCurrentText);

        syncData();

        mOverViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), OverviewActivity.class);
                startActivity(intent);

            }
        });

        return view;
    }


    protected void syncData() throws NullPointerException {

        String uid = mAuth.getCurrentUser().getUid();

        currentValue = 0;
        spentedValue = 0;
        addedValue = 0;

        mDocumentReferenceAdd.collection(uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                        addedValue += (long) documentSnapshot.get("Rs");
                    }
                    currentValue = 0;
                    currentValue = addedValue - spentedValue;
                    mCurrentRS.setText(localSymbol + currentValue);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        mDocumentReferenceSpent.collection(uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                        long currentSpent = (long) documentSnapshot.get("Rs");
                        spentedValue += currentSpent;
                        mChartSpentData.add((int) currentSpent);

                    }
                    currentValue = 0;
                    currentValue = addedValue - spentedValue;
                    mCurrentRS.setText(localSymbol + currentValue);

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }

                Timer timer = new Timer();
                timer.schedule(new keepInBack(), 0, 5000);

            }
        });
    }

    private class keepInBack extends TimerTask {

        @Override
        public void run() {

            List<Entry> entries = new ArrayList<>();

            int i = 0;
            for (Integer currentRS : mChartSpentData) {
                i++;
                entries.add(new Entry(i, (float) currentRS));
            }
            LineDataSet dataSet = new LineDataSet(entries, "Usage");
            LineData lineData = new LineData(dataSet);
            mLineChart.setData(lineData);

            mLineChart.invalidate();
            if (j == 0) {
                mLineChart.animateX(2000, Easing.EasingOption.EaseInOutCirc);
                j++;
            }

        }
    }

}
