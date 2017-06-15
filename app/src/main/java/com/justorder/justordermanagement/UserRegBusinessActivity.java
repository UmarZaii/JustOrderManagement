package com.justorder.justordermanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class UserRegBusinessActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private DatabaseReference fDatabaseBusiness, fDatabaseUser;
    private StorageReference fStorage;

    private EditText edtBusinessNameReg, edtBusinessPhoneNoReg;
    private Button btnBusinessReg;
    private ImageButton imgBusinessReg;
    private Uri imageUrl;
    private ProgressDialog progressDialog;

    private String strUserID = "";
    private String strUserFirstName = "";
    private String strUserLastName = "";

    private static final int GALLERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userregbusiness);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        fAuth = FirebaseAuth.getInstance();
        fDatabaseBusiness = FirebaseDatabase.getInstance().getReference().child("tblBusiness");
        fDatabaseUser = FirebaseDatabase.getInstance().getReference().child("tblUser");
        fStorage = FirebaseStorage.getInstance().getReference();

        edtBusinessNameReg = (EditText)findViewById(R.id.edtBusinnessNameReg);
        edtBusinessPhoneNoReg = (EditText)findViewById(R.id.edtBusinessPhoneNoReg);

        strUserID= fAuth.getCurrentUser().getUid();

        fDatabaseUser.child(strUserID).child("userFirstName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                strUserFirstName = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        fDatabaseUser.child(strUserID).child("userLastName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                strUserLastName = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        imgBusinessReg = (ImageButton)findViewById(R.id.imgBusinessReg);
        imgBusinessReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        btnBusinessReg = (Button)findViewById(R.id.btnBusinessReg);
        btnBusinessReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerBusiness();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            imageUrl = data.getData();
            imgBusinessReg.setImageURI(imageUrl);
        }
    }

    private void registerBusiness() {

        StringGenerator stringGenerator = new StringGenerator();

        final String strBusinessNameReg = edtBusinessNameReg.getText().toString().trim();
        final String strBusinessPhoneNoReg = edtBusinessPhoneNoReg.getText().toString().trim();
        final String strBusinessIDReg = stringGenerator.createPassayRNG(28);
        final String strUserFullName = strUserFirstName + " " + strUserLastName;

        if (fStorage == null) {
            Toast.makeText(this, "Please insert profile picture..", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(strBusinessNameReg)) {
            Toast.makeText(this, "Please insert restaurant name..", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(strBusinessPhoneNoReg)) {
            Toast.makeText(this, "Please insert contact number..", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering, Please Wait...");
        progressDialog.show();

        StorageReference filePath = fStorage.child(strBusinessIDReg).child("Profile_Pic").child(imageUrl.getLastPathSegment());
        filePath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();

                final HashMap<String, String> tblBusiness = new HashMap<String, String>();
                tblBusiness.put("businessName", strBusinessNameReg);
                tblBusiness.put("businessID", strBusinessIDReg);
                tblBusiness.put("businessContact", strBusinessPhoneNoReg);
                tblBusiness.put("businessPic", downloadUrl.toString());
                final HashMap<String, String> tblBusinessManager = new HashMap<String, String>();
                tblBusinessManager.put("managerID", strUserID);
                tblBusinessManager.put("managerName", strUserFullName);

                fDatabaseBusiness.child(strBusinessIDReg).setValue(tblBusiness);
                fDatabaseBusiness.child(strBusinessIDReg).child("businessManager").child("Manager").child(strUserID).setValue(tblBusinessManager);
                fDatabaseUser.child(strUserID).child("userType").child("Manager").child(strBusinessIDReg).child("businessID").setValue(strBusinessIDReg);

                progressDialog.dismiss();
            }
        });

    }

}
