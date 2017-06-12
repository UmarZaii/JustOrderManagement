package com.justorder.justordermanagement;

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
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private DatabaseReference fDatabase;
    private Session session = null;

    private EditText edtUserEmailReg, edtUserPassReg;
    private Button btnRegisterAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance().getReference().child("tblUser");

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

        final String strUserEmailReg = edtUserEmailReg.getText().toString().trim();
        final String strUserPassReg = edtUserPassReg.getText().toString().trim();

        if(TextUtils.isEmpty(strUserEmailReg) && TextUtils.isEmpty(strUserPassReg)) {
            Toast.makeText(SignUpActivity.this, "Please input your email and password", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(strUserEmailReg)){
            Toast.makeText(SignUpActivity.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(strUserPassReg)){
            Toast.makeText(SignUpActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        fAuth.createUserWithEmailAndPassword(strUserEmailReg,strUserPassReg).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()){

                    Log.d("Unsuccessfull", task.getException().toString());
                    Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                } else {

                    StringGenerator stringGenerator = new StringGenerator();
                    final String strUserCode = stringGenerator.createStringID(10,4);
                    final String strUserIDReg = fAuth.getCurrentUser().getUid();
                    final String strUserMessage = "ActivationCode: " + strUserCode;

                    final HashMap<String, String> dataMap = new HashMap<String, String>();
                    dataMap.put("userEmail", strUserEmailReg);
                    dataMap.put("userPass", strUserPassReg);
                    dataMap.put("userID", strUserIDReg);
                    dataMap.put("userStatus", "Not Active");
                    dataMap.put("userCode", strUserCode);
                    fDatabase.child(strUserIDReg).setValue(dataMap);

                    Properties props = new Properties();
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.put("mail.smtp.socketFactory.port", "465");
                    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.port", "465");

                    session = Session.getDefaultInstance(props, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            //Turn on less secure app on GMAIL for sender
                            return new PasswordAuthentication("justorder2017@gmail.com", "99warriors");
                        }
                    });
                    new AccountActivation(session,strUserEmailReg,"Account Activation",strUserMessage).execute();

                    startActivity(new Intent(SignUpActivity.this, ActivationActivity.class));
                    finish();

                }
            }
        });

    }

}
