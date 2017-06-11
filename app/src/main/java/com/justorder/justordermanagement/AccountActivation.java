package com.justorder.justordermanagement;

import android.os.AsyncTask;
import android.util.Log;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AccountActivation extends AsyncTask <String, Void, String> {

    private Session session;
    private String strGmail, strSubj, strMessage;

    public AccountActivation(Session session, String strGmail, String strSubj, String strMessage) {
        this.session = session;
        this.strGmail = strGmail;
        this.strSubj = strSubj;
        this.strMessage = strMessage;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("justorder2017.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(strGmail));
            message.setSubject(strSubj);
            message.setContent(strMessage, "text/html; charset=utf-8");
            Transport.send(message);
            Log.d("Mail", "Success");
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

}
