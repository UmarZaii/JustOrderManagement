package com.justorder.justordermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class UserMainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    Button btnGoToRegBusiness, btnLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermain);

        firebaseAuth = FirebaseAuth.getInstance();

        btnGoToRegBusiness = (Button)findViewById(R.id.btnGoToRegBusiness);
        btnGoToRegBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserMainActivity.this, UserRegBusinessActivity.class));
            }
        });

        btnLogOut = (Button)findViewById(R.id.btnLogOut);
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
