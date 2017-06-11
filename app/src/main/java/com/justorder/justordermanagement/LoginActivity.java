package com.justorder.justordermanagement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fAuthListener;

    private EditText edtUserEmailLogin, edtUserPassLogin;
    private Button btnLogin, btnGoToRegisterAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();

        edtUserEmailLogin = (EditText)findViewById(R.id.edtUserEmailLogin);
        edtUserPassLogin = (EditText)findViewById(R.id.edtUserPassLogin);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnGoToRegisterAcc = (Button)findViewById(R.id.btnGoToRegisterAcc);

        Log.d("test", "a");

        fAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    Log.d("uid", fAuth.getCurrentUser().getUid());
                    new CheckUserLogin(LoginActivity.this).execute();
                }
            }
        };

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String strUserEmail = edtUserEmailLogin.getText().toString().trim();
                final String strUserPass = edtUserPassLogin.getText().toString().trim();

                new CheckBtnLogin(LoginActivity.this, strUserEmail, strUserPass).execute();
            }
        });

        btnGoToRegisterAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
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
}
