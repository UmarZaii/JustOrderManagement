package com.justorder.justordermanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivationActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private DatabaseReference fDatabase;

    private TextView txtUserEmail;
    private EditText edtUserCode;
    private Button btnVerifyCode, btnResendCode;
    private ProgressDialog progressDialog;

    private String strUserCode = "";
    private String strUserEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance().getReference().child("tblUser");

        txtUserEmail = (TextView)findViewById(R.id.txtUserEmail);
        edtUserCode = (EditText)findViewById(R.id.edtUserCode);
        btnVerifyCode = (Button)findViewById(R.id.btnVerifyCode);
        btnResendCode = (Button)findViewById(R.id.btnResendCode);

        final String strUserID= fAuth.getCurrentUser().getUid();
        fDatabase.child(strUserID).child("userEmail").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                strUserEmail = dataSnapshot.getValue().toString();
                txtUserEmail.setText(strUserEmail);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        fDatabase.child(strUserID).child("userCode").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                strUserCode = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String strUserSubj = "Account Activation";
                final String strUserMessage = "ActivationCode: " + strUserCode;

                progressDialog.setMessage("Sending Activation Code...");
                progressDialog.show();

                new AccountActivation(strUserEmail,strUserSubj,strUserMessage).execute();

                progressDialog.dismiss();
            }
        });

        btnVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String strUserCodeVerify = edtUserCode.getText().toString().trim();
                final String strUserID = fAuth.getCurrentUser().getUid();

                if (TextUtils.isEmpty(strUserCodeVerify)) {
                    Toast.makeText(ActivationActivity.this, "Please input the activation code", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage("Verifying, Please Wait...");
                progressDialog.show();

                if (strUserCode.equals(strUserCodeVerify)){
                    fDatabase.child(strUserID).child("userStatus").setValue("Active");
                    fDatabase.child(strUserID).child("userCode").removeValue();
                    Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(ActivationActivity.this, "Activation failed, please try again", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        fAuth.signOut();
        super.onBackPressed();
    }
}
