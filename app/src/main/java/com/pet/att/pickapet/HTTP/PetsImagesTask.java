package com.pet.att.pickapet.HTTP;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.pet.att.pickapet.R;
import com.pet.att.pickapet.AuxiliaryClasses.RecyclerViewFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PetsImagesTask extends AsyncTask<String, Void, Boolean> {
    private static final String TAG = "PetsImagesCallRequest";
    private String json= null;
    private String  baseURL;
    private ProgressDialog mDialog;
    private final Context mContext;
    private final AppCompatActivity  mActivity;
    private  String  mActiveAnimalJsonResult=null;
    private String mCheckoutValue;
    private String mFirstRequestName;
    private String mSecondRequestName;
    private String mPutExtraString;


    public PetsImagesTask(AppCompatActivity activity, Context context){
        this.mContext = context;
        this.baseURL =   mContext.getString(R.string.base_url);
        mActivity = activity;
    }

    @Override
    protected void onPreExecute() {
        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage("Please wait...");
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setIndeterminate(true);
        mDialog.setCancelable(false);
        mDialog.show();
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
        Log.d(TAG, "Fetching the all active animals for "+ this.getFirstRequestName());
        //Need to send request to get all active animals
        String json = HttpRequestsURLConnection.SendHttpPost(baseURL + "/"+ this.getFirstRequestName(),this.getCheckoutValue());
        if (isValidJsonResult(json)){
            Log.d(TAG, "JSON data for active animals" + this.getFirstRequestName()+ " is " + json.toString());
            String allAnimalsId = this.getAllAnimalsIdFromJson(json);
            //Need to send request to get all active animals pictures
            json = HttpRequestsURLConnection.SendHttpPost(baseURL + "/"+ this.getSecondRequestName(),allAnimalsId);
            if (isValidJsonResult(json)){
                Log.d(TAG, "JSON data Pics for active animals" + this.getSecondRequestName()+ " is " + json.toString());
                this.setActiveAnimalJsonResult(json);
                isCorrect=true;
            }
        }
        return isCorrect;
    }

//    @Override
//    protected String doInBackground(String... strings) {
//        String requestName = strings[0];
//        Log.d(TAG, "Fetching data for "+ requestName);
//        String json = null;
//        json = HttpRequestsURLConnection.SendHttpGet(baseURL + "/"+ requestName);
//        if (json != null) {
//
//            this.setJson(json);
//        }
//        return json;
//    }
//
    @Override
    protected void onPostExecute(final Boolean result) {
        if (result) {
            Bundle args = new Bundle();
//            args.putString("animal_pic_json", json.toString());
            args.putString(this.getPutExtraString(), this.getActiveAnimalJsonResult());

            FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
            RecyclerViewFragment fragment = new RecyclerViewFragment();
            fragment.setArguments(args);

            transaction.replace(R.id.pets_content_fragment, fragment);
            transaction.commit();
        }
        mDialog.dismiss();
    }


    private void initStringsItems(String[] strings) {
        this.setFirstRequestName(strings[0]);
        this.setSecondRequestName(strings[1]);
        this.setPutExtraString(strings[2]);
        this.setCheckoutValue("checkout=1");
    }

    private String getAllAnimalsIdFromJson(String json) {
        String allIdStringResult ="";
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i=0; i<jsonArray.length(); i++){
                String animalStr = jsonArray.get(i).toString();
                JSONObject jsonObject = new JSONObject(animalStr);
                allIdStringResult = allIdStringResult + "," + jsonObject.getString("aid");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "aid="+allIdStringResult.substring(1,allIdStringResult.length());
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

    private String getActiveAnimalJsonResult() {
        return mActiveAnimalJsonResult;
    }

    private void setActiveAnimalJsonResult(String mActiveAnimalJsonResult) {
        this.mActiveAnimalJsonResult = mActiveAnimalJsonResult;
    }
}


