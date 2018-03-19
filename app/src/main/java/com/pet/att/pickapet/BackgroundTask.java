package com.pet.att.pickapet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class BackgroundTask  extends AsyncTask<Void, Void, Boolean> {
    private final Context context;
    private final String mEmail;
    private final String mPassword;
    private View mProgressView;
    private View mLoginFormView;

    BackgroundTask(Context context,String email, String password){
        this.context = context;
        this.mEmail = email;
        this.mPassword = password;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        // TODO: attempt authentication against a network service.

        try {
            // Simulate network access.
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            return false;
        }

//        for (String credential : DUMMY_CREDENTIALS) {
//            String[] pieces = credential.split(":");
//            if (pieces[0].equals(mEmail)) {
//                // Account exists, return true if the password matches.
//                return pieces[1].equals(mPassword);
//            }
//        }

        // TODO: register the new account here.
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
//        mAuthTask = null;
//        showProgress(false);
//
//        EditText mPasswordView = (AutoCompleteTextView) findViewById(R.id.email);
//
//        if (success) {
//            finish();
//        } else {
//            mPasswordView.setError(getString(R.string.error_incorrect_password));
//            mPasswordView.requestFocus();
//        }
    }

    @Override
    protected void onCancelled() {
//        mAuthTask = null;
//        showProgress(false);
    }



}


