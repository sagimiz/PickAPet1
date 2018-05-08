package com.pet.att.pickapet.HTTP;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pet.att.pickapet.AppActivities.MainActivity;
import com.pet.att.pickapet.AuxiliaryClasses.MainStringTask;
import com.pet.att.pickapet.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class AddNewUserTask extends MainStringTask {
    private static final String TAG = "AddNewUserTask";
    private ProgressDialog mDialog;
    private String mId;
    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mPhone;
    private String mAddress;
    private String mCity;
    private String mPassword;
    private ArrayList<String> jsonBodyArrayForUser;
    private ArrayList<String> jsonBodyArrayForUserLogin;

    public AddNewUserTask(AppCompatActivity mActivity, Context mContext){
        super(mContext,mActivity);
    }



    @Override
    protected void onPreExecute() {
        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage("Please wait...");
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setIndeterminate(true);
        mDialog.setCancelable(false);
        mDialog.show();
    }

  /*
   The First arg is  user request name
   The Second arg is user_login request name
   The Third arg is entered id for json
   The Forth arg is entered first name for json
   The Fifth arg is entered last name for json
   The Sixth arg is entered email for json
   The Seventh arg is entered phone for json
   The Eighth arg is entered address for json
   The Ninth arg is entered city for json
   The Tenth arg is the password first value
   The Eleventh arg is the putString first value
   The Twelfth arg is the putString second value
  */

    @Override
    protected Boolean doInBackground(String... strings) {

        jsonBodyArrayForUser = new ArrayList<String>();
        jsonBodyArrayForUserLogin = new ArrayList<String>();
        this.initStringsItems(strings);
        boolean isCorrect = false;

        Log.d(TAG, "Sending Put data for "+ super.getFirstRequestName());
        String  resultStr = HttpRequestsURLConnection.SendHttpPut(baseURL + "/"+ super.getFirstRequestName(), getJsonBodyString(jsonBodyArrayForUser));
        if (super.isValidJsonResult(resultStr) && (resultStr.equals("Empty")) ) {
           resultStr = HttpRequestsURLConnection.SendHttpPut(baseURL + "/"+ super.getSecondRequestName(), getJsonBodyString(jsonBodyArrayForUserLogin));
           if (super.isValidJsonResult(resultStr) && resultStr.equals("Empty")) {
               isCorrect = true;
           }
        }
        return isCorrect;
    }


    private void initStringsItems(String[] strings) {

        super.setFirstRequestName( strings[0]);
        super.setSecondRequestName(strings[1]);

        this.setId(strings[2]);
        this.jsonBodyArrayForUser.add("id=" +this.getId());

        this.setFirstName(strings[3]);
        this.jsonBodyArrayForUser.add("fname=" + this.getFirstName());

        this.setLastName(strings[4]);
        this.jsonBodyArrayForUser.add("lname=" + this.getLastName());

        this.setEmail(strings[5]);
        this.jsonBodyArrayForUser.add("email=" + this.getEmail());
        this.jsonBodyArrayForUserLogin.add("email=" + this.getEmail());

        this.setPhone(strings[6]);
        this.jsonBodyArrayForUser.add("phone=" + this.getPhone());

        this.setAddress(strings[7]);
        this.jsonBodyArrayForUser.add("address=" + this.getAddress());

        this.setCity(strings[8]);
        this.jsonBodyArrayForUser.add("city=" + this.getCity());

        this.setPassword(strings[9]);
        this.jsonBodyArrayForUserLogin.add("password=" + this.getPassword());

        this.setFirstPutString(strings[10]);
        this.setSecondPutString(strings[11]);

    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            SharedPreferences sharedPreferences = mActivity.getSharedPreferences("Login", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("UserEmail", this.getEmail());
            editor.putString("Password", this.getPassword());
            editor.commit();

            Intent intent = new Intent(mContext, MainActivity.class);
            intent.putExtra(super.getFirstPutString(),super.getStringJsonBody(this.jsonBodyArrayForUser));
            intent.putExtra(super.getSecondRequestName(),super.getStringJsonBody(this.jsonBodyArrayForUserLogin));
            mDialog.dismiss();
            mActivity.startActivity(intent);
            mActivity.finish();

        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(mContext.getString(R.string.error_put_data))
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            mDialog.dismiss();
        }

    }

    @Override
    protected void onCancelled() {
        mDialog.dismiss();
    }

    private String getEmail() {
        return mEmail;
    }

    private void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    private String getPassword() {
        return mPassword;
    }

    private void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    private String getId() {
        return mId;
    }

    private void setId(String mId) {
        this.mId = mId;
    }

    private String getFirstName() {
        return mFirstName;
    }

    private void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    private String getLastName() {
        return mLastName;
    }

    private void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    private String getPhone() {
        return mPhone;
    }

    private void setPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    private String getAddress() {
        return mAddress;
    }

    private void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    private String getCity() {
        return mCity;
    }

    private void setCity(String mCity) {
        this.mCity = mCity;
    }


}
