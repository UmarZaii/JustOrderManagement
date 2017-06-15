package com.justorder.justordermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.SecureRandom;
import java.util.Random;
import java.util.StringTokenizer;

public class UserMainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    Button btnTest;
    Button btnViewRestaurantList, btnGoToRegBusiness, btnLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermain);

        firebaseAuth = FirebaseAuth.getInstance();

        btnViewRestaurantList = (Button)findViewById(R.id.btnViewRestaurantList);
        btnGoToRegBusiness = (Button)findViewById(R.id.btnGoToRegBusiness);
        btnLogOut = (Button)findViewById(R.id.btnLogOut);

        btnTest = (Button)findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        btnViewRestaurantList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserMainActivity.this, BusinessListActivity.class));
            }
        });

        btnGoToRegBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserMainActivity.this, UserRegBusinessActivity.class));
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(UserMainActivity.this, LoginActivity.class));
            }
        });
    }

}
