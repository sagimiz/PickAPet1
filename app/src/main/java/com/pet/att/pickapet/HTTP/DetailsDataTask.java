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

public class DetailsDataTask extends AsyncTask <String, Void, String> {

    private static final String TAG = "DetailsDataRequest" ;
    private final Context mContext;
    private final String baseURL;
    private final int mMethod;
    private final AppCompatActivity  mActivity;
    private final Bundle mArgs;


    private String mAnimalDetailsJson;



    private String putText;


    public DetailsDataTask(AppCompatActivity activity, Context context, Bundle args , int method){
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

            this.setAnimalDetailsJson(jsonStr);
        }
        setPutText(strings[2]);
        return jsonStr;
    }


    @Override
    protected void onPostExecute(String jsonObject) {
        super.onPostExecute(jsonObject);

        String animalDetailsJson = this.getAnimalDetailsJson();
        this.setAnimalDetailsJson(animalDetailsJson.substring(1,animalDetailsJson.length()));
        mArgs.putString(getPutText(), this.getAnimalDetailsJson());

        try {
            JSONObject animalJson = new JSONObject(this.getAnimalDetailsJson());
            String animalIdJson = "aid=" + animalJson.getString("aid");
            new OwnerDataTask(mActivity, mContext,mArgs, POST).execute(mContext.getString(R.string.animals_owner_request_aid), animalIdJson, "animal_owner_json");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getAnimalDetailsJson() {
        return mAnimalDetailsJson;
    }
    private void setAnimalDetailsJson(String animalDetailsJson) {
        this.mAnimalDetailsJson = animalDetailsJson;
    }

    private String getPutText() {
        return putText;
    }

    private void setPutText(String putText) {
        this.putText = putText;
    }

}
