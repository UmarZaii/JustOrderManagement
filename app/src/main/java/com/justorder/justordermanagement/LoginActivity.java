package com.justorder.justordermanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference fDatabase;

    private EditText edtUserEmailLogin, edtUserPassLogin;
    private Button btnLogin, btnGoToRegisterAcc;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance().getReference().child("tblUser");

        edtUserEmailLogin = (EditText)findViewById(R.id.edtUserEmailLogin);
        edtUserPassLogin = (EditText)findViewById(R.id.edtUserPassLogin);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnGoToRegisterAcc = (Button)findViewById(R.id.btnGoToRegisterAcc);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUserLogin();
            }
        });

        btnGoToRegisterAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

    private void checkUserLogin() {

        final String strUserEmail = edtUserEmailLogin.getText().toString().trim();
        final String strUserPass = edtUserPassLogin.getText().toString().trim();

        if(TextUtils.isEmpty(strUserEmail) && TextUtils.isEmpty(strUserPass)) {
            Toast.makeText(this, "Please input your email and password", Toast.LENGTH_LONG).show();
            return;
        } else if(TextUtils.isEmpty(strUserEmail)) {
            Toast.makeText(this, "Please input your email", Toast.LENGTH_LONG).show();
            return;
        } else if(TextUtils.isEmpty(strUserPass)) {
            Toast.makeText(this, "Please input your password", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("LogIn, Please Wait...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(strUserEmail,strUserPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    Log.d("Error", task.getException().toString());
                } else {
                    final String strUserID = firebaseAuth.getCurrentUser().getUid();
                    fDatabase.child(strUserID).child("userStatus").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String strUserStatus = dataSnapshot.getValue().toString();
                            if (strUserStatus.equals("Not Active")) {
                                startActivity(new Intent(LoginActivity.this, ActivationActivity.class));
                                progressDialog.dismiss();
                            } else if (strUserStatus.equals("Active")) {
                                startActivity(new Intent(LoginActivity.this, UserMainActivity.class));
                                finish();
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

    }

}
