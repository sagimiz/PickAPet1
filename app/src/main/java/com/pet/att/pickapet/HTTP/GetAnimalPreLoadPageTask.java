package com.pet.att.pickapet.HTTP;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pet.att.pickapet.AppActivities.AddNewPetActivity;
import com.pet.att.pickapet.R;

public class GetAnimalPreLoadPageTask extends AsyncTask<String, Void, Boolean> {

    private static final String TAG = "GetAnimalPreLoadPage" ;
    private final Context mContext;
    private final String baseURL;
    private final AppCompatActivity mActivity;


    private String mFirstRequestName;
    private String mSecondPutExtraText;
    private String mFirstPutExtraText;
    private String mSecondRequestName;
    private String mFirstJSONResult;
    private String mSecondJSONResult;


    public GetAnimalPreLoadPageTask(AppCompatActivity activity, Context context){
        this.mContext = context;
        this.baseURL =   mContext.getString(R.string.base_url);
        this.mActivity = activity;
    }



    @Override
    protected Boolean doInBackground(String... strings) {
        boolean isCorrect= false;
        this.initStringsItems(strings);
        Log.d(TAG, "Fetching data for "+ this.getFirstRequestName());
        String jsonStr  = HttpRequestsURLConnection.SendHttpGet(baseURL + "/"+ this.getFirstRequestName());
        Log.d(TAG, "Result data for "+ this.getFirstRequestName() +" is " +jsonStr);
        if (isValidJsonResult(jsonStr)){
            this.setFirstJSONResult(jsonStr);
            Log.d(TAG, "Fetching data for "+ this.getSecondRequestName());
            jsonStr  = HttpRequestsURLConnection.SendHttpGet(baseURL + "/"+ this.getSecondRequestName() );
            Log.d(TAG, "Result data for "+ this.getSecondRequestName() +" is " +jsonStr);
            if (isValidJsonResult(jsonStr)){
                this.setSecondJSONResult(jsonStr);
                isCorrect= true;
            }
        }
        return isCorrect;
    }


    /**
     * First Value is animal_type request name
     * Second Value is animal_kind request name
     * Third Value is PutExtraValue for type
     * Forth Value is PutExtraValue for kind
     */
    private void initStringsItems(String[] strings) {
        this.setFirstRequestName(strings[0]);
        this.setSecondRequestName(strings[1]);
        this.setFirsPutExtraText(strings[2]);
        this.setSecondPutExtraText(strings[3]);
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        Bundle bundle = mActivity.getIntent().getExtras();
        Intent intent = new Intent (mActivity, AddNewPetActivity.class);

        if (success && (bundle!=null) ) {
            intent.putExtra(this.getFirsPutExtraText(), this.getFirstJSONResult());
            intent.putExtra(this.getSecondPutExtraText(), this.getSecondJSONResult());
            String userDetailsString =bundle.getString(mContext.getString(R.string.current_user_details_json));
            intent.putExtra(mContext.getString(R.string.current_user_details_json),userDetailsString.trim() );
        }
        mActivity.startActivity(intent);
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

    private String getFirsPutExtraText() {
        return mFirstPutExtraText;
    }

    private void setFirsPutExtraText(String putText) {
        this.mFirstPutExtraText = putText;
    }

    private String setStringToJsonFormat (String currentJsonString){
        return currentJsonString.substring(1,currentJsonString.length()-2);
    }


    public String getSecondPutExtraText() {
        return mSecondPutExtraText;
    }

    public void setSecondPutExtraText(String mSecondPutExtraText) {
        this.mSecondPutExtraText = mSecondPutExtraText;
    }

    public String getFirstRequestName() {
        return mFirstRequestName;
    }

    public void setFirstRequestName(String mFirstRequestName) {
        this.mFirstRequestName = mFirstRequestName;
    }

    public String getSecondRequestName() {
        return mSecondRequestName;
    }

    public void setSecondRequestName(String mSecondRequestName) {
        this.mSecondRequestName = mSecondRequestName;
    }

    public String getFirstJSONResult() {
        return mFirstJSONResult;
    }

    public void setFirstJSONResult(String mFirstJSONResult) {
        this.mFirstJSONResult = mFirstJSONResult;
    }

    public String getSecondJSONResult() {
        return mSecondJSONResult;
    }

    public void setSecondJSONResult(String mSecondJSONResult) {
        this.mSecondJSONResult = mSecondJSONResult;
    }



}
