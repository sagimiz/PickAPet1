package com.pet.att.pickapet.HTTP;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pet.att.pickapet.AuxiliaryClasses.OnTaskCompleted;
import com.pet.att.pickapet.R;

public class GetUserActiveAnimalsTask extends AsyncTask<String, Void, Boolean> {
    private static final String TAG = "PetsImagesCallRequest";
    private String baseURL;
    private final Context mContext;
    private final AppCompatActivity mActivity;
    private final Bundle mArgs;
    private String mUserId;
    private String mUserActiveAnimalJsonResult = null;
    private String mFirstRequestName;
    private String mPutExtraString;
    private OnTaskCompleted listener;

    public GetUserActiveAnimalsTask(AppCompatActivity activity, Context context,Bundle mArgs,OnTaskCompleted listener) {
        this.mContext = context;
        this.baseURL = mContext.getString(R.string.base_url);
        mActivity = activity;
        this.listener=listener;
        this.mArgs = mArgs;
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
        Log.d(TAG, "Fetching the all active animals for user in the request " + this.getFirstRequestName());
        //Need to send request to get all active animals for user
        String json = HttpRequestsURLConnection.SendHttpPost(baseURL + "/" + this.getFirstRequestName(), "id="+this.getUserId());
        if (isValidJsonResult(json)) {
            Log.d(TAG, "JSON data for active animals for the user request" + this.getFirstRequestName() + " is " + json.toString());
            this.setUserActiveAnimalJsonResult(json);
            isCorrect = true;

        }
        return isCorrect;
    }

    @Override
    protected void onPostExecute(final Boolean result) {
        if (result) {
            mActivity.getIntent().putExtra(this.getPutExtraString(), this.getUserActiveAnimalJsonResult());
            listener.onTaskCompleted();
        }
    }

    /*
     * The First arg is animals_owner_active request name
     * The Second args is the User Id value
     * The Third arg is putExtra String for json
     */
    private void initStringsItems(String[] strings) {
        this.setFirstRequestName(strings[0]);
        this.setUserId(strings[1]);
        this.setPutExtraString(strings[2]);
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

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }
}