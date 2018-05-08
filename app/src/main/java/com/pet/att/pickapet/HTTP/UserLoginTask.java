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

}