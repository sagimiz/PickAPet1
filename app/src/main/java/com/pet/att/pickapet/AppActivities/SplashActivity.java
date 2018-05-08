package com.pet.att.pickapet.AppActivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import com.pet.att.pickapet.AuxiliaryClasses.OnTaskCompleted;
import com.pet.att.pickapet.HTTP.UserLoginTask;
import com.pet.att.pickapet.R;

public class SplashActivity extends AppCompatActivity {
    private String mUserEmail;
    private String mPassword;
    private Context mContext;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        final SharedPreferences sharedPreferences = this.getSharedPreferences("Login", MODE_PRIVATE);
        mUserEmail =sharedPreferences.getString("UserEmail", null);
        mPassword = sharedPreferences.getString("Password", null);
         if (mUserEmail ==null || mPassword == null){
             Intent intent = new Intent(this, LoginActivity.class);
             startActivity(intent);
             finish();
         }else{
             new UserLoginTask(SplashActivity.this, this, new OnTaskCompleted() {
                 @Override
                 public void onTaskCompleted() {
                     DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             switch (which){
                                 case DialogInterface.BUTTON_POSITIVE:
                                     Intent intent = new Intent(mContext, LoginActivity.class);
                                     startActivity(intent);
                                     finish();
                                     break;

                                 case DialogInterface.BUTTON_NEGATIVE:
                                     finish();
                                     break;
                             }
                         }
                     };

                     AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                     builder.setMessage(R.string.dialog_error_text).setPositiveButton("המשך", dialogClickListener)
                             .setNegativeButton("ביטול", dialogClickListener).show();
                 }

                 @Override
                 public void onTaskCompleted(String result) {
                     if(result != null){
                         Intent intent = new Intent(mContext, MainActivity.class);
                         intent.putExtra(mContext.getString(R.string.current_user_details_json),result);
                         startActivity(intent);
                         finish();
                     }else{
                         onTaskCompleted();
                     }
                 }
                 @Override
                 public void onTaskCompleted(Boolean result) { }
             }).execute(this.getString(R.string.user_request_login), mUserEmail, mPassword);
         }
    }
}


