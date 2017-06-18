package com.justorder.justordermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.SpriteFactory;
import com.github.ybq.android.spinkit.Style;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LaunchScreenActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fAuthListener;
    private DatabaseReference fDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_launchscreen);

        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance().getReference().child("tblUser");

        SpinKitView spinKit = (SpinKitView) findViewById(R.id.spin_kit);
        Style style = Style.values()[2];
        Sprite drawable = SpriteFactory.create(style);
        spinKit.setIndeterminateDrawable(drawable);

        fAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    checkUserLogin();
                } else {
                    startActivity(new Intent(LaunchScreenActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        fAuth.addAuthStateListener(fAuthListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fAuth.removeAuthStateListener(fAuthListener);
    }

    private void checkUserLogin() {

        final String strUserID = fAuth.getCurrentUser().getUid();

        fDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(strUserID)) {
                    fDatabase.child(strUserID).child("userStatus").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String strUserStatus = dataSnapshot.getValue().toString();
                            if (strUserStatus.equals("Active")) {
                                //Check if user activates the account
                                startActivity(new Intent(LaunchScreenActivity.this, UserMainActivity.class));
                                finish();
                            } else if (strUserStatus.equals("Not Active")) {
                                fAuth.signOut();
                                startActivity(new Intent(LaunchScreenActivity.this, LoginActivity.class));
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
//                } else {
//                    //Prevent deleted account make changes to database
//                    //SERIOUS BUG IF DELETE DIRECT FROM DB FIREBASE
//                    fAuth.signOut();
//                    startActivity(new Intent(LaunchScreenActivity.this, LoginActivity.class));
//                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
