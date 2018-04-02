package com.pet.att.pickapet.HTTP;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pet.att.pickapet.AuxiliaryClasses.DetailsViewFragment;
import com.pet.att.pickapet.R;

import static com.android.volley.Request.Method.GET;

/**
 * Created by mizrahi on 02/04/2018.
 */

public class UserDataTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "UserDataCallRequest" ;
    private final Context mContext;
    private final String baseURL;
    private final int mMethod;
    private final AppCompatActivity mActivity;
    private final Bundle mArgs;
    private String putText;



    private String mUserDetailsJson;

    public UserDataTask(AppCompatActivity activity, Context context, Bundle args, int method){
        this.mContext = context;
        this.baseURL =   mContext.getString(R.string.base_url);
        this.mMethod =  method;
        this.mActivity = activity;
        this.mArgs =args;
    }


    @Override
    protected String doInBackground(String... strings) {
        String requestName = strings[0];
        String jsonStr = null;
        Log.d(TAG, "Fetching data for "+ requestName);
        if (mMethod == GET){
            jsonStr = HttpRequestsURLConnection.SendHttpGet(baseURL + "/"+ requestName);
        }else{
            jsonStr = HttpRequestsURLConnection.SendHttpPost(baseURL + "/"+ requestName,strings[1]);
        }
        if (jsonStr != null) {
            Log.d(TAG, "JSON data for " + requestName + jsonStr);
            this.setUserDetailsJson(jsonStr);
        }
        setPutText(strings[2]);
        return jsonStr;
    }


    @Override
    protected void onPostExecute(String jsonObject) {
        super.onPostExecute(jsonObject);

        String userDetailsJson = this.getUserDetailsJson();
        this.setUserDetailsJson(userDetailsJson.substring(1,userDetailsJson.length()));
        mArgs.putString(getPutText(), this.getUserDetailsJson());


        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        DetailsViewFragment fragment = new DetailsViewFragment();
        fragment.setArguments(mArgs);
        transaction.replace(R.id.details_content_fragment, fragment);
        transaction.commit();

    }

    private String getUserDetailsJson() {
        return mUserDetailsJson;
    }

    private void setUserDetailsJson(String userDetailsJson) {
        this.mUserDetailsJson = userDetailsJson;
    }

    private String getPutText() {
        return putText;
    }

    private void setPutText(String putText) {
        this.putText = putText;
    }

}
