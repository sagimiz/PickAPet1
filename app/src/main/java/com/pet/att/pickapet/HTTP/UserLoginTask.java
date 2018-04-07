package com.pet.att.pickapet.HTTP;

//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//
//import com.pet.att.pickapet.AppActivities.LoginActivity;
//import com.pet.att.pickapet.AppActivities.MainActivity;
//import com.pet.att.pickapet.R;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
///**
// * Created by mizrahi on 04/04/2018.
// */
//
//public class LoginUserTask extends AsyncTask<String, Void, String> {
//
//    private static final String TAG = "OwnerDataCallRequest" ;
//    private final Context mContext;
//    private final String baseURL;
//    private final AppCompatActivity mActivity;
//    private final SharedPreferences mSpl;
//    private String mUserDetailsJson;
//    private String mPassword;
//    private String putText;
//
//
//    public LoginUserTask(AppCompatActivity activity, Context context, SharedPreferences sp1){
//        this.mContext = context;
//        this.baseURL =   mContext.getString(R.string.base_url);
//        this.mActivity = activity;
//        this.mSpl = sp1;
//    }
//
//    /*The First arg is  requestName
//    /*The Second arg is  emailJson
//     The Third arg is the putStringValue
//     The Forth arg is the password*/
//    @Override
//    protected String doInBackground(String... strings) {
//        String requestName = strings[0];
//        Log.d(TAG, "Fetching data for "+ requestName);
//        String  jsonStr = HttpRequestsURLConnection.SendHttpPost(baseURL + "/"+ requestName,strings[1]);
//        if (jsonStr != null) {
//            Log.d(TAG, "JSON data for " + requestName + jsonStr);
//            this.setUserDetailsJson(jsonStr);
//        }
//        setPutText(strings[2]);
//        setPassword(strings[3]);
//        return jsonStr;
//    }
//
//
//    @Override
//    protected void onPostExecute(String jsonObject) {
//        super.onPostExecute(jsonObject);
//        this.setStringToJsonFormat(this.getUserDetailsJson());
//
//        try {
//            JSONObject userJson = new JSONObject(this.getUserDetailsJson());
//            Intent intent;
//            if (userJson.getString("password").equals(this.getPassword())){
//                 intent = new Intent(mContext, MainActivity.class);
//                 intent.putExtra(getPutText(),this.getUserDetailsJson());
//            }else{
//                 intent = new Intent(mContext, LoginActivity.class);
//            }
//            mActivity.startActivity(intent);
//            mActivity.finish();
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private String getPassword() {
//        return mPassword;
//    }
//
//    private void setPassword(String mPassword) {
//        this.mPassword = mPassword;
//    }
//
//    private String setStringToJsonFormat (String currentJsonString){
//        return currentJsonString.substring(1,currentJsonString.length());
//    }
//
//    private String getUserDetailsJson() {
//        return mUserDetailsJson;
//    }
//
//    private void setUserDetailsJson(String ownerDetailsJson) {
//        this.mUserDetailsJson = ownerDetailsJson;
//    }
//
//    private String getPutText() {
//        return putText;
//    }
//
//    private void setPutText(String putText) {
//        this.putText = putText;
//    }
//
//}

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pet.att.pickapet.AppActivities.LoginActivity;
import com.pet.att.pickapet.AppActivities.MainActivity;
import com.pet.att.pickapet.R;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class UserLoginTask extends AsyncTask<String, Void, Boolean> {
    private static final String TAG = "UserLoginTask";
    private final AppCompatActivity mActivity;
    private final SharedPreferences mSpl;
    private final String baseURL;
    private Context mContext;
    private String mPutText;
    private String mPassword;
    private String mUserDetailsJson;
    private String mEmail;
    private ProgressDialog mDialog;

    public UserLoginTask(AppCompatActivity activity, Context context, SharedPreferences sp1){
        this.mContext = context;
        this.baseURL =   mContext.getString(R.string.base_url);
        this.mActivity = activity;
        this.mSpl = sp1;
    }

//        @Override
//        protected Boolean doInBackground(Void... params) {
//            // TODO: attempt authentication against a network service.
//
//            try {
//                // Simulate network access.
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                return false;
//            }
//
//
//            boolean isCorrect = false;
////            for (String credential : DUMMY_CREDENTIALS) {
////                String[] pieces = credential.split(":");
////                if (pieces[0].equalsIgnoreCase(mEmail)&& pieces[1].equalsIgnoreCase(mPassword)) {
////                    SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
////                    SharedPreferences.Editor editor=sharedPreferences.edit();
////                    editor.putString("UserName",mEmail );
////                    editor.putString("Password",mPassword);
////                    editor.commit();
////                    isCorrect=true;
////                    break;
////                }
////            }
////            // TODO: register the new account here.
//            return isCorrect;
//
//        }

                                                                                      /*The First arg is  request name
                                                                                       The Second arg is  email for json
                                                                                       The Third arg is entered password for json
                                                                                       The Forth arg is the putString value
                                                                                      */


    @Override
    protected void onPreExecute() {
        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage("Please wait...");
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setIndeterminate(true);
        mDialog.setCancelable(false);
        mDialog.show();
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        String requestName = strings[0];
        this.setEmail(strings[1]);
        this.setPassword(strings[2]);

        boolean isCorrect = false;
        try {
            Log.d(TAG, "Fetching data for "+ requestName);
            String  jsonStr = HttpRequestsURLConnection.SendHttpPost(baseURL + "/"+ requestName,"email="+mEmail);
            if (isValidJsonResult(jsonStr)) {
                Log.d(TAG, "JSON data for " + requestName  +" is "+ jsonStr);
                jsonStr = this.setStringToJsonFormat(jsonStr);

                JSONObject jsonObject = new JSONObject(jsonStr);

                if (jsonObject.getString("password").equals(this.getPassword())){
                    jsonStr = HttpRequestsURLConnection.SendHttpPost(baseURL + "/"+ mContext.getString(R.string.user_request),"email="+mEmail);
                    if (isValidJsonResult(jsonStr)) {
                        this.setUserDetailsJson(this.setStringToJsonFormat(jsonStr));
                        setPutText(strings[3]);
                        isCorrect =true;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return isCorrect;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
//        mAuthTask = null;
//        showProgress(false);

        if (success) {
            if(mSpl==null) {
                SharedPreferences sharedPreferences = mActivity.getSharedPreferences("Login", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("UserEmail", this.getEmail());
                editor.putString("Password", this.getPassword());
                editor.commit();
            }

            Intent intent = new Intent(mContext, MainActivity.class);
            intent.putExtra(getPutText(),this.getUserDetailsJson());
            mActivity.startActivity(intent);
            mActivity.finish();
        } else {
            Intent intent = new Intent(mContext, LoginActivity.class);
            mActivity.startActivity(intent);
            mActivity.finish();
        }
        mDialog.dismiss();
    }

    @Override
    protected void onCancelled() {
//        mAuthTask = null;
//       showProgress(false);
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

    private String setStringToJsonFormat (String currentJsonString){
        return currentJsonString.substring(1,currentJsonString.length());
    }

    private String getUserDetailsJson() {
        return mUserDetailsJson;
    }

    private void setUserDetailsJson(String ownerDetailsJson) {
        this.mUserDetailsJson = ownerDetailsJson;
    }

    private String getPutText() {
        return mPutText;
    }

    private void setPutText(String putText) {
        this.mPutText = putText;
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
}