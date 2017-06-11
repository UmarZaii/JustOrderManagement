package com.justorder.justordermanagement;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckUserLogin extends AsyncTask<Void,Void,Void> {

    private FirebaseAuth fAuth;
    private DatabaseReference fDatabase;

    private Activity activity;
    private ProgressDialog progressDialog;

    public CheckUserLogin(Activity activity) {
        this.activity = activity;
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCanceledOnTouchOutside(false);

        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance().getReference().child("tblUser");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("LogIn, Please Wait...");
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {

        final String strUserID = fAuth.getCurrentUser().getUid();

        fDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(strUserID)) {
                    fDatabase.child(strUserID).child("userStatus").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String strUserStatus = dataSnapshot.getValue().toString();
                            if (strUserStatus.equals("Not Active")) {
                                activity.startActivity(new Intent(activity, ActivationActivity.class));
                            } else if (strUserStatus.equals("Active")) {
                                activity.startActivity(new Intent(activity, UserMainActivity.class));
                                activity.finish();
                            }
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    //Prevent deleted account make changes to database
                    fAuth.signOut();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return null;
    }

    @Override
    protected void onPostExecute(Void a) {
        super.onPostExecute(a);
    }
}
