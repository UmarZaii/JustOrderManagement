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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private DatabaseReference fDatabase;

    private EditText edtUserEmailReg, edtUserPassReg, edtUserFirstNameReg, edtUserLastNameReg;
    private Button btnRegisterAcc;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance().getReference().child("tblUser");

        edtUserFirstNameReg = (EditText)findViewById(R.id.edtUserFirstNameReg);
        edtUserLastNameReg = (EditText)findViewById(R.id.edtUserLastNameReg);
        edtUserEmailReg = (EditText)findViewById(R.id.edtUserEmailReg);
        edtUserPassReg = (EditText)findViewById(R.id.edtUserPassReg);
        btnRegisterAcc = (Button)findViewById(R.id.btnRegisterAcc);

        btnRegisterAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

    }

    private void signUp() {

        final String strUserFirstNameReg = edtUserFirstNameReg.getText().toString().trim();
        final String strUserLastNameReg = edtUserLastNameReg.getText().toString().trim();
        final String strUserEmailReg = edtUserEmailReg.getText().toString().trim();
        final String strUserPassReg = edtUserPassReg.getText().toString().trim();

        if(TextUtils.isEmpty(strUserFirstNameReg)) {
            Toast.makeText(SignUpActivity.this, "Please input your first name", Toast.LENGTH_LONG).show();
            return;
        } else if(TextUtils.isEmpty(strUserLastNameReg)) {
            Toast.makeText(SignUpActivity.this, "Please input your last name", Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(strUserEmailReg)){
            Toast.makeText(SignUpActivity.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(strUserPassReg)){
            Toast.makeText(SignUpActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Signing Up, Please Wait...");
        progressDialog.show();

        fAuth.createUserWithEmailAndPassword(strUserEmailReg,strUserPassReg).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()){

                    progressDialog.dismiss();
                    Log.d("Unsuccessfull", task.getException().toString());
                    Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                } else {

                    StringGenerator stringGenerator = new StringGenerator();
                    final String strUserCode = stringGenerator.createStringID(10,4);
                    final String strUserIDReg = fAuth.getCurrentUser().getUid();
                    final String strUserSubj = "Account Activation";
                    final String strUserMessage = "ActivationCode: " + strUserCode;

                    final HashMap<String, String> dataMap = new HashMap<String, String>();
                    dataMap.put("userFirstName", strUserFirstNameReg);
                    dataMap.put("userLastName", strUserLastNameReg);
                    dataMap.put("userEmail", strUserEmailReg);
                    dataMap.put("userID", strUserIDReg);
                    dataMap.put("userStatus", "Not Active");
                    dataMap.put("userCode", strUserCode);
                    fDatabase.child(strUserIDReg).setValue(dataMap);

                    new AccountActivation(strUserEmailReg,strUserSubj,strUserMessage).execute();

                    startActivity(new Intent(SignUpActivity.this, ActivationActivity.class));
                    finish();
                    progressDialog.dismiss();

                }
            }
        });

    }

}
