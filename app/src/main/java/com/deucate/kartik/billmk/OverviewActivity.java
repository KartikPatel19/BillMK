package com.deucate.kartik.billmk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.intrusoft.scatter.ChartData;
import com.intrusoft.scatter.PieChart;
import com.intrusoft.scatter.SimpleChart;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OverviewActivity extends AppCompatActivity {

    List<ChartData> data;
    SimpleChart mPieChart;

    FirebaseDatabase mDatabase;
    DatabaseReference mReference, mReferenceOverAll;
    FirebaseAuth mAuth;

    TextView mElectronicTv, mFoodTv, mClothsTv, mGroceryTv, mBillsTv, mOthersTv;
    int mElectronic, mFood, mCloths, mGrocery, mBills, mOther,totalOfAll=0;
    float mElePer = 0, mFoodPer = 0, mClothPer = 0, mGroceryper = 0, mBillPer = 0, mOtherPer = 0;
    int mElectronicMonth, mFoodMonth, mClothsMonth, mGroceryMonth, mBillsMonth, mOtherMonth;

    String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        mPieChart = (SimpleChart) findViewById(R.id.pie_chart);
        data = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        mReference = mDatabase.getReference().child("Spent").child(uid);
        mReferenceOverAll = mDatabase.getReference().child("Spent").child(uid);

        mElectronicTv = (TextView) findViewById(R.id.electronicOverviewRs);
        mFoodTv = (TextView) findViewById(R.id.foodOverRs);
        mClothsTv = (TextView) findViewById(R.id.clothOverRs);
        mGroceryTv = (TextView) findViewById(R.id.groceryOverviewRs);
        mBillsTv = (TextView) findViewById(R.id.billOverRs);
        mOthersTv = (TextView) findViewById(R.id.otherOverRs);

        DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
        Date dateFormt = new Date();
        date = dateFormat.format(dateFormt);

        MnthlyRef();
        overAllRef();

        data.add(new ChartData("Electronic", 10));
        data.add(new ChartData("Food", 50));
        data.add(new ChartData("Cloths", 5));
        data.add(new ChartData("Grocery", 3));
        data.add(new ChartData("Bills", 7));
        data.add(new ChartData("Other", 25));

        mPieChart.setChartData(data);

    }

    private void MnthlyRef() {

        mElectronicMonth = 0;
        mFoodMonth = 0;
        mClothsMonth = 0;
        mGroceryMonth = 0;
        mBillsMonth = 0;
        mOtherMonth = 0;

        mReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String dateSt = dataSnapshot.child("Date").getValue(String.class);
                String dateTemp = dateSt.substring(3);

                if (dateTemp.equals(date)) {

                    int category = dataSnapshot.child("Category").getValue(int.class);

                    switch (category) {
                        case 0: {

                            try {
                                mElectronicMonth += dataSnapshot.child("Rs").getValue(int.class);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mElectronicTv.setText(mElectronicMonth + "");
                        }
                        break;
                        case 1: {
                            try {
                                mFoodMonth += dataSnapshot.child("Rs").getValue(int.class);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mFoodTv.setText(mFoodMonth + "");
                        }
                        break;
                        case 2: {
                            try {
                                mClothsMonth += dataSnapshot.child("Rs").getValue(int.class);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mClothsTv.setText(mClothsMonth + "");
                        }
                        break;
                        case 3: {
                            try {
                                mGroceryMonth = dataSnapshot.child("Rs").getValue(int.class);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mGroceryTv.setText(mGroceryMonth + "");
                        }
                        break;
                        case 4: {
                            try {
                                mBillsMonth = dataSnapshot.child("Rs").getValue(int.class);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mBillsTv.setText(mBillsMonth + "");
                        }
                        break;
                        case 5: {
                            try {
                                mOtherMonth = dataSnapshot.child("Rs").getValue(int.class);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mOthersTv.setText(mOtherMonth + "");
                        }
                        break;
                    }

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
    }

    private void overAllRef() {

        mElectronic = 0;
        mFood = 0;
        mCloths = 0;
        mGrocery = 0;
        mBills = 0;
        mOther = 0;

        mReferenceOverAll.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                int category = dataSnapshot.child("Category").getValue(int.class);

                switch (category) {
                    case 0: {

                        try {
                            mElectronic += dataSnapshot.child("Rs").getValue(int.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mElectronicTv.setText(mElectronic + "");
                    }
                    break;
                    case 1: {
                        try {
                            mFood += dataSnapshot.child("Rs").getValue(int.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mFoodTv.setText(mFood + "");
                    }
                    break;
                    case 2: {
                        try {
                            mCloths += dataSnapshot.child("Rs").getValue(int.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mClothsTv.setText(mCloths + "");
                    }
                    break;
                    case 3: {
                        try {
                            mGrocery = dataSnapshot.child("Rs").getValue(int.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mGroceryTv.setText(mGrocery + "");
                    }
                    break;
                    case 4: {
                        try {
                            mBillsMonth = dataSnapshot.child("Rs").getValue(int.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mBillsTv.setText(mBillsMonth + "");
                    }
                    break;
                    case 5: {
                        try {
                            mOther = dataSnapshot.child("Rs").getValue(int.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mOthersTv.setText(mOther + "");
                    }
                    break;
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
    }

    public void onMonthlyClick(View view) {

        totalOfAll = mElectronic + mFood + mCloths + mGrocery + mBills + mOther;

        mElePer = (totalOfAll * mElectronic) / 100;
        mFoodPer = (totalOfAll * mFood) / 100;
        mClothPer = (totalOfAll * mCloths) / 100;
        mGroceryper = (totalOfAll * mGrocery) / 100;
        mBillPer = (totalOfAll * mBills) / 100;
        mOtherPer = (totalOfAll * mOther) / 100;



        MnthlyRef();

    }

    public void onOverAllClick(View view) {
        overAllRef();
    }
}
