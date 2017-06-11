package com.justorder.justordermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    private Button btnVerifyCode;

    public static String strUserCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);

        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance().getReference().child("tblUser");

        txtUserEmail = (TextView)findViewById(R.id.txtUserEmail);
        edtUserCode = (EditText)findViewById(R.id.edtUserCode);
        btnVerifyCode = (Button)findViewById(R.id.btnVerifyCode);

        final String strUserID= fAuth.getCurrentUser().getUid();
        fDatabase.child(strUserID).child("userEmail").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String strUserEmail = dataSnapshot.getValue().toString();
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

        btnVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String strUserCodeVerify = edtUserCode.getText().toString().trim();
                final String strUserID = fAuth.getCurrentUser().getUid();
                if (strUserCode.equals(strUserCodeVerify)){
                    fDatabase.child(strUserID).child("userStatus").setValue("Active");
                    Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
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
