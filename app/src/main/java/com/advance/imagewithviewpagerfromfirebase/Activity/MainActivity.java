package com.advance.imagewithviewpagerfromfirebase.Activity;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.advance.imagewithviewpagerfromfirebase.Adapter.MyAdapter;
import com.advance.imagewithviewpagerfromfirebase.Interface.IFirebaseLoadDone;
import com.advance.imagewithviewpagerfromfirebase.Model.Model;
import com.advance.imagewithviewpagerfromfirebase.R;
import com.advance.imagewithviewpagerfromfirebase.Transformer.DepthPageTransformer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity implements IFirebaseLoadDone {

    ViewPager viewPager;
    MyAdapter adapter;

    DatabaseReference bikes;

    IFirebaseLoadDone iFirebaseLoadDone;

    ProgressDialog progressDialog;

    Button preBtn, nxtBtn;
    List<Model> imgList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bikes = FirebaseDatabase.getInstance().getReference("Images");


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Images...");
        iFirebaseLoadDone = this;

        loadBikes();

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        //for transformation when image slided
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        viewPager.setCurrentItem(0);


        preBtn = findViewById(R.id.preBtn);
        nxtBtn = findViewById(R.id.nxtBtn);


        progressDialog.show();


    }

    private void loadBikes() {
        bikes.addListenerForSingleValueEvent(new ValueEventListener() {



            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot bikeSnapShot : dataSnapshot.getChildren())
                    imgList.add(bikeSnapShot.getValue(Model.class));
                iFirebaseLoadDone.onFirebaseLoadSuccess(imgList);
                Toast.makeText(MainActivity.this, "Totall number of image "+imgList.size(), Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(MainActivity.this, "Something wrong ....", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onFirebaseLoadSuccess(List<Model> imgList) {

        adapter = new MyAdapter(this, imgList);
        viewPager.setAdapter(adapter);

        //for circle indicator
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);


    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }

    //for next prev button
    public void pre(View view) {


        if (viewPager.getCurrentItem() == 1 ){
            preBtn.setVisibility(View.GONE);
            nxtBtn.setVisibility(View.VISIBLE);

        }else {
            preBtn.setVisibility(View.VISIBLE);
            nxtBtn.setVisibility(View.VISIBLE);


        }
        if (viewPager.getCurrentItem() != 0){
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }



    }

    public void nxt(View view) {

        if (viewPager.getCurrentItem() == imgList.size()-2){
            nxtBtn.setVisibility(View.GONE);
            preBtn.setVisibility(View.VISIBLE);
        }else {
            preBtn.setVisibility(View.VISIBLE);
            nxtBtn.setVisibility(View.VISIBLE);


        }
        if (viewPager.getCurrentItem() != imgList.size()-1){
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }


}
