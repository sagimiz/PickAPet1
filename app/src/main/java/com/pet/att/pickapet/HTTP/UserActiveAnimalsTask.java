package com.pet.att.pickapet.HTTP;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pet.att.pickapet.AuxiliaryClasses.MainStringTask;
import com.pet.att.pickapet.AuxiliaryClasses.OnTaskCompleted;
import com.pet.att.pickapet.R;

import java.util.ArrayList;

import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;

public class UserActiveAnimalsTask extends MainStringTask {
    private static final String TAG = "PetsImagesCallRequest";
    private int method;
    private String mUserId;
    private String mAnimalId;
    private ArrayList<String> jsonBodyArrayForAnimalsCheckout;

    private String mUserActiveAnimalJsonResult = null;
    private String mFirstRequestName;
    private String mPutExtraString;
    private OnTaskCompleted listener;

    public UserActiveAnimalsTask(AppCompatActivity activity, Context context,int method, OnTaskCompleted listener) {
        super(context,activity);
        this.listener=listener;
        this.method=method;
    }

    /*
     * The First arg is animals_owner_active request name
     * The Second args is the User Id value
     * The Third arg is putExtra String for json
     */
    @Override
    protected Boolean doInBackground(String... strings) {
        boolean isCorrect = false;
        this.initStringsItems(strings);
        if (method == POST) {
            Log.d(TAG, "Fetching the all active animals for user in the request " + this.getFirstRequestName());
            //Need to send request to get all active animals for user
            String json = HttpRequestsURLConnection.SendHttpPost(baseURL + "/" + this.getFirstRequestName(), "id=" + this.getUserId());
            if (isValidJsonResult(json)) {
                Log.d(TAG, "JSON data for active animals for the user request" + this.getFirstRequestName() + " is " + json);
                this.setUserActiveAnimalJsonResult(json);
                isCorrect = true;

            }
        }else if (method == PUT){
            Log.d(TAG, "Checking out animal for user in the request " + this.getFirstRequestName());
            //Need to send request to get all active animals for user
            String json = HttpRequestsURLConnection.SendObjectsHttpPut(baseURL + "/" + this.getFirstRequestName(), this.getJsonBodyString(this.jsonBodyArrayForAnimalsCheckout));
            if (isValidJsonResult(json)) {
                Log.d(TAG, "JSON data for Checking out animal for user in the request" + this.getFirstRequestName() + " is " + json);
                this.setUserActiveAnimalJsonResult(json);
                isCorrect = true;

            }
        }
        return isCorrect;
    }

    @Override
    protected void onPostExecute(final Boolean result) {
        if (result && method==POST) {
            mActivity.getIntent().putExtra(this.getPutExtraString(), this.getUserActiveAnimalJsonResult());
            listener.onTaskCompleted();
        }else if (result && method==PUT){
            listener.onTaskCompleted(true);
        }else{
            listener.onTaskCompleted(result);
        }
    }

    /*
     * For (POST)
     * The First arg is animals_owner_active request name
     * The Second args is the User Id value
     * The Third arg is putExtra String for json
     *
     * For (PUT)
     * The First arg is animals_owner_active request name
     * The Second args is the User Id value
     * The Third args is the Animal Id value
     * The Forth arg is putExtra String for json
     */
    private void initStringsItems(String[] strings) {
        if( method==POST){
            this.setFirstRequestName(strings[0]);
            this.setUserId(strings[1]);
            this.setPutExtraString(strings[2]);
        }else if  (method==PUT){
            jsonBodyArrayForAnimalsCheckout = new ArrayList<String>();
            this.setFirstRequestName(strings[0]);
            this.setUserId(strings[1]);
            jsonBodyArrayForAnimalsCheckout.add("id="+this.getUserId());
            this.setAnimalId(strings[2]);
            jsonBodyArrayForAnimalsCheckout.add("aid="+this.getAnimalId());
            this.setPutExtraString(strings[3]);
        }

    }

    private String getPutExtraString() {
        return mPutExtraString;
    }

    private void setPutExtraString(String mPutExtraString) {
        this.mPutExtraString = mPutExtraString;
    }

    private String getUserActiveAnimalJsonResult() {
        return mUserActiveAnimalJsonResult;
    }

    private void setUserActiveAnimalJsonResult(String mActiveAnimalPicsJsonResult) {
        this.mUserActiveAnimalJsonResult = mActiveAnimalPicsJsonResult;
    }

    private String getUserId() {
        return mUserId;
    }

    private void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    private String getAnimalId() {
        return mAnimalId;
    }

    private void setAnimalId(String mAnimalId) {
        this.mAnimalId = mAnimalId;
    }
}