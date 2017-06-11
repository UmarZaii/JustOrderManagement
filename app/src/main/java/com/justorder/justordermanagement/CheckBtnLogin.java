package com.justorder.justordermanagement;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CheckBtnLogin extends AsyncTask<Void,Void,Void> {

    private FirebaseAuth firebaseAuth;

    private Activity activity;
    private ProgressDialog progressDialog;
    private String strUserEmail, strUserPass;

    public CheckBtnLogin(Activity activity, String strUserEmail, String strUserPass) {
        this.activity = activity;
        this.strUserEmail = strUserEmail;
        this.strUserPass = strUserPass;

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("LogIn, Please Wait...");
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {

        if(TextUtils.isEmpty(strUserEmail) && TextUtils.isEmpty(strUserPass)) {
            Toast.makeText(activity, "Please input your email and password", Toast.LENGTH_LONG).show();
        } else if(TextUtils.isEmpty(strUserEmail)) {
            Toast.makeText(activity, "Please input your email", Toast.LENGTH_LONG).show();
        } else if(TextUtils.isEmpty(strUserPass)) {
            Toast.makeText(activity, "Please input your password", Toast.LENGTH_LONG).show();
        } else {
            firebaseAuth.signInWithEmailAndPassword(strUserEmail,strUserPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()) {
                        Log.d("Error", task.getException().toString());
                    }
                }
            });
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
    }

}
