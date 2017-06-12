package com.justorder.justordermanagement;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AccountActivation extends AsyncTask <String, Void, String> {

    private Session session = null;
    private String strGmail, strSubj, strMessage;

    public AccountActivation(String strGmail, String strSubj, String strMessage) {
        this.strGmail = strGmail;
        this.strSubj = strSubj;
        this.strMessage = strMessage;

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
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        //Error if trying to send email on UI thread, need AsyncTask
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
