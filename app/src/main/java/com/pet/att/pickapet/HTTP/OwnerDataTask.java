package com.pet.att.pickapet.HTTP;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pet.att.pickapet.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

public class OwnerDataTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "OwnerDataCallRequest" ;
    private final Context mContext;
    private final String baseURL;
    private final int mMethod;
    private final AppCompatActivity mActivity;
    private final Bundle mArgs;
    private String mOwnerDetailsJson;
    private String putText;


    public OwnerDataTask(AppCompatActivity activity, Context context,Bundle args, int method){
        this.mContext = context;
        this.baseURL =   mContext.getString(R.string.base_url);
        this.mMethod =  method;
        this.mActivity = activity;
        this.mArgs=args;
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
            this.setOwnerDetailsJson(jsonStr);
        }
        setPutText(strings[2]);
        return jsonStr;
    }

    @Override
    protected void onPostExecute(String jsonObject) {
        super.onPostExecute(jsonObject);

        String OwnerDetailsJson = this.getOwnerDetailsJson();
        this.setOwnerDetailsJson(OwnerDetailsJson.substring(1,OwnerDetailsJson.length()));
        mArgs.putString(getPutText(), this.getOwnerDetailsJson());

        try {
            JSONObject ownerJson = new JSONObject(this.getOwnerDetailsJson());
            String animalIdJson = "id=" + ownerJson.getString("id");
            new UserDataTask(mActivity, mContext, mArgs, POST).execute(mContext.getString(R.string.user_request_id), animalIdJson, "user_json");

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private String getOwnerDetailsJson() {
        return mOwnerDetailsJson;
    }

    private void setOwnerDetailsJson(String ownerDetailsJson) {
        this.mOwnerDetailsJson = ownerDetailsJson;
    }

    private String getPutText() {
        return putText;
    }

    private void setPutText(String putText) {
        this.putText = putText;
    }

}
