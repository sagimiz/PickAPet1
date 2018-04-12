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
import com.pet.att.pickapet.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class AddNewUserTask extends AsyncTask<String, Void, Boolean> {
    private static final String TAG = "UserLoginTask";
    private final AppCompatActivity mActivity;
    private final String baseURL;
    private Context mContext;
    private String mUserDetailsJson;
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
    private String mFirstPutString;
    private String mSecondPutString;
    private String mFirstRequestName;
    private String mSecondRequestName;



    public AddNewUserTask(AppCompatActivity activity, Context context){
        this.mContext = context;
        this.baseURL =   mContext.getString(R.string.base_url);
        this.mActivity = activity;
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

        Log.d(TAG, "Sending Put data for "+ this.getFirstRequestName());
        String  resultStr = HttpRequestsURLConnection.SendHttpPut(baseURL + "/"+ this.getFirstRequestName(), getJsonBodyString(jsonBodyArrayForUser));
        if (isValidJsonResult(resultStr) && (resultStr.equals("Empty")) ) {
           resultStr = HttpRequestsURLConnection.SendHttpPut(baseURL + "/"+ this.getSecondRequestName(), getJsonBodyString(jsonBodyArrayForUserLogin));
           if (isValidJsonResult(resultStr) && resultStr.equals("Empty")) {
               isCorrect = true;
           }
        }
        return isCorrect;
    }

    private String getJsonBodyString(ArrayList<String> jsonArray){
        String jsonString="";
        for (int i =0;i<jsonArray.size();i++){
            jsonString=jsonString +"&"+jsonArray.get(i);
        }
        return (jsonString.length()>0)? jsonString.substring(1,jsonString.length()):"";
    }

    private String getStringJsonBody(ArrayList<String> jsonArray){
        String jsonString="";
        for (int i =0;i<jsonArray.size();i++){
            jsonString=jsonString +","+jsonArray.get(i);
        }
        return (jsonString.length()>0)? "{"+jsonString.substring(1,jsonString.length())+"}":"";
    }

    private void initStringsItems(String[] strings) {

        this.setFirstRequestName( strings[0]);
        this.setSecondRequestName(strings[1]);

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
            intent.putExtra(this.getFirstPutString(),this.getStringJsonBody(this.jsonBodyArrayForUser));
            intent.putExtra(this.getSecondRequestName(),this.getStringJsonBody(this.jsonBodyArrayForUserLogin));
            mDialog.dismiss();
            mActivity.startActivity(intent);
            mActivity.finish();

        }else{

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(mContext.getString(R.string.error_put_data))
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            mDialog.dismiss();
        }

    }
    public String getSecondRequestName() {
        return mSecondRequestName;
    }

    public void setSecondRequestName(String mSecondRequestName) {
        this.mSecondRequestName = mSecondRequestName;
    }


    public String getFirstRequestName() {
        return mFirstRequestName;
    }

    public void setFirstRequestName(String firstRequestName) {
        this.mFirstRequestName = firstRequestName;
    }

    @Override
    protected void onCancelled() {
        mDialog.dismiss();
    }

    public String getFirstPutString() {
        return mFirstPutString;
    }

    public void setFirstPutString(String mFirstPutString) {
        this.mFirstPutString = mFirstPutString;
    }

    public String getSecondPutString() {
        return mSecondPutString;
    }

    public void setSecondPutString(String mSecondPutString) {
        this.mSecondPutString = mSecondPutString;
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

    private String setStringToJsonFormat (String currentJsonString){
        return currentJsonString.substring(1,currentJsonString.length());
    }

    private String getUserDetailsJson() {
        return mUserDetailsJson;
    }

    private void setUserDetailsJson(String ownerDetailsJson) {
        this.mUserDetailsJson = ownerDetailsJson;
    }


    private boolean isValidJsonResult(String jsonStr){
        if (jsonStr == null){
            return false;
        }
        if (jsonStr.contains("Error")){
            return false;
        }
        if (jsonStr.contains("something")){
            return false;
        }

        return true;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String mCity) {
        this.mCity = mCity;
    }


}
