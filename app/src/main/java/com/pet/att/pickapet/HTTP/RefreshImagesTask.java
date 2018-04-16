package com.pet.att.pickapet.HTTP;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pet.att.pickapet.AuxiliaryClasses.OnTaskCompleted;
import com.pet.att.pickapet.AuxiliaryClasses.RecyclerViewFragment;
import com.pet.att.pickapet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RefreshImagesTask extends AsyncTask<String, Void, Boolean> {
    private static final String TAG = "PetsImagesCallRequest";
    private String json = null;
    private String baseURL;
    private ProgressDialog mDialog;
    private final Context mContext;
    private final AppCompatActivity mActivity;
    private final  Bundle mArgs;
    private String mActiveAnimalPicsJsonResult = null;
    private String mCheckoutValue;
    private String mFirstRequestName;
    private String mSecondRequestName;
    private String mPutExtraString;
    private OnTaskCompleted listener;

    public RefreshImagesTask(AppCompatActivity activity, Context context,Bundle mArgs,OnTaskCompleted listener) {
        this.mContext = context;
        this.baseURL = mContext.getString(R.string.base_url);
        mActivity = activity;
        this.listener=listener;
        this.mArgs = mArgs;
    }

    /*
     * The First arg is animals_owner_active request name
     * The Second arg is animal_pic IN request name
     * The Third arg is putExtra String for json
     * */
    @Override
    protected Boolean doInBackground(String... strings) {
        boolean isCorrect = false;
        this.initStringsItems(strings);
        Log.d(TAG, "Fetching the all active animals for " + this.getFirstRequestName());
        //Need to send request to get all active animals
        String json = HttpRequestsURLConnection.SendHttpPost(baseURL + "/" + this.getFirstRequestName(), this.getCheckoutValue());
        if (isValidJsonResult(json)) {
            Log.d(TAG, "JSON data for active animals" + this.getFirstRequestName() + " is " + json.toString());
            String allAnimalsId = this.getAllAnimalsIdFromJson(json);
            //Need to send request to get all active animals pictures
            json = HttpRequestsURLConnection.SendHttpPost(baseURL + "/" + this.getSecondRequestName(), allAnimalsId);
            if (isValidJsonResult(json)) {
                Log.d(TAG, "JSON data Pics for active animals" + this.getSecondRequestName() + " is " + json.toString());
                this.setActiveAnimalPicsJsonResult(json);
                isCorrect = true;
            }
        }
        return isCorrect;
    }



    @Override
    protected void onPostExecute(final Boolean result) {
        if (result) {
            mArgs.putString(this.getPutExtraString(), this.getActiveAnimalPicsJsonResult());
            listener.onTaskCompleted();
        }
    }

    /*
     * The First arg is animals_owner_active request name
     * The Second arg is animal_pic IN request name
     * The Third arg is putExtra String for json
     * */
    private void initStringsItems(String[] strings) {
        this.setFirstRequestName(strings[0]);
        this.setSecondRequestName(strings[1]);
        this.setPutExtraString(strings[2]);
        this.setCheckoutValue("checkout=1");
    }

    private String getAllAnimalsIdFromJson(String json) {
        String allIdStringResult = "";
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                String animalStr = jsonArray.get(i).toString();
                JSONObject jsonObject = new JSONObject(animalStr);
                allIdStringResult = allIdStringResult + "," + jsonObject.getString("aid");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "aid=" + allIdStringResult.substring(1, allIdStringResult.length());
    }

    private boolean isValidJsonResult(String jsonStr) {
        if (jsonStr == null) {
            return false;
        }
        if (jsonStr.contains("Error")) {
            return false;
        }
        if (jsonStr.contains("something")) {
            return false;
        }

        return true;
    }

    private String getFirstRequestName() {
        return mFirstRequestName;
    }

    private void setFirstRequestName(String mFirstRequestName) {
        this.mFirstRequestName = mFirstRequestName;
    }

    private String getSecondRequestName() {
        return mSecondRequestName;
    }

    private void setSecondRequestName(String mSecondRequestName) {
        this.mSecondRequestName = mSecondRequestName;
    }

    private String getPutExtraString() {
        return mPutExtraString;
    }

    private void setPutExtraString(String mPutExtraString) {
        this.mPutExtraString = mPutExtraString;
    }

    private String getCheckoutValue() {
        return mCheckoutValue;
    }

    private void setCheckoutValue(String mCheckoutValue) {
        this.mCheckoutValue = mCheckoutValue;
    }

    private String getActiveAnimalPicsJsonResult() {
        return mActiveAnimalPicsJsonResult;
    }

    private void setActiveAnimalPicsJsonResult(String mActiveAnimalPicsJsonResult) {
        this.mActiveAnimalPicsJsonResult = mActiveAnimalPicsJsonResult;
    }
}
