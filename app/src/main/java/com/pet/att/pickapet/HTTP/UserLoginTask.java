package com.pet.att.pickapet.HTTP;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.pet.att.pickapet.AuxiliaryClasses.MainStringTask;
import com.pet.att.pickapet.AuxiliaryClasses.OnTaskCompleted;
import com.pet.att.pickapet.R;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class UserLoginTask extends MainStringTask {
    private static final String TAG = "UserLoginTask";
    private String mPutText;
    private String mPassword;
    private String mUserDetailsJson;
    private String mEmail;
    private OnTaskCompleted listner;

    public UserLoginTask(AppCompatActivity mActivity, Context mContext,  OnTaskCompleted listner){
        super(mContext,mActivity);
        this.listner=listner;
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


//    @Override
//    protected void onPreExecute() {
//        mDialog = new ProgressDialog(mContext);
//        mDialog.setMessage("Please wait...");
//        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        mDialog.setIndeterminate(true);
//        mDialog.setCancelable(false);
//        mDialog.show();
//    }

    @Override
    protected Boolean doInBackground(String... strings) {

        initStringsItems(strings);
        boolean isCorrect = false;
        try {
            Log.d(TAG, "Fetching data for "+ super.getFirstRequestName());
            String  jsonStr = HttpRequestsURLConnection.SendHttpPost(baseURL + "/"+ super.getFirstRequestName(),"email="+this.getEmail());
            if (isValidJsonResult(jsonStr)) {
                Log.d(TAG, "JSON data for " + super.getFirstRequestName()  +" is "+ jsonStr);
                jsonStr = this.setStringToJsonFormat(jsonStr);

                JSONObject jsonObject = new JSONObject(jsonStr);

                if (jsonObject.getString("password").equals(this.getPassword())){
                    jsonStr = HttpRequestsURLConnection.SendHttpPost(baseURL + "/"+ mContext.getString(R.string.user_request),"email="+this.getEmail());
                    if (isValidJsonResult(jsonStr)) {
                        this.setUserDetailsJson(this.setStringToJsonFormat(jsonStr));
                        isCorrect =true;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return isCorrect;
    }

    private void initStringsItems(String[] strings) {
        super.setFirstRequestName(strings[0]);
        this.setEmail(strings[1]);
        this.setPassword(strings[2]);
    }

//    @Override
//    protected void onPostExecute(final Boolean success) {
////        mAuthTask = null;
////        showProgress(false);
//
//        if (success) {
//            if(mSpl==null) {
//                SharedPreferences sharedPreferences = mActivity.getSharedPreferences("Login", MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("UserEmail", this.getEmail());
//                editor.putString("Password", this.getPassword());
//                editor.commit();
//            }
//            Bundle bundle = mActivity.getIntent().getExtras();
//            Intent intent = new Intent(mContext, MainActivity.class);
//            intent.putExtra(getPutText(),this.getUserDetailsJson());
//            mActivity.startActivity(intent);
//            mActivity.finish();
//        } else {
//            Intent intent = new Intent(mContext, LoginActivity.class);
//            mActivity.startActivity(intent);
//            mActivity.finish();
//        }
////        mDialog.dismiss();
//    }

//    @Override
//    protected void onCancelled() {
////        mAuthTask = null;
////       showProgress(false);
//        mDialog.dismiss();
//    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if(success){
            listner.onTaskCompleted(this.getUserDetailsJson());
        }else {
            listner.onTaskCompleted();
        }
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

}