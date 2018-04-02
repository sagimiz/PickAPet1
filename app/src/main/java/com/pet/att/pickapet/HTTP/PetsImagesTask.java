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


public class PetsImagesTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "PetsImagesCallRequest";
    private final int mMethod;
    private String json= null;

    private String  baseURL;
    private ProgressDialog mDialog;
    private final Context mContext;
    private final AppCompatActivity  mActivity;

    public PetsImagesTask(AppCompatActivity activity, Context context, int method){
        this.mContext = context;
        this.baseURL =   mContext.getString(R.string.base_url);
        this.mMethod =  method;
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

    @Override
    protected String doInBackground(String... strings) {
        String requestName = strings[0];
        Log.d(TAG, "Fetching data for "+ requestName);
        String json = null;
        json = HttpRequestsURLConnection.SendHttpGet(baseURL + "/"+ requestName);
        if (json != null) {
            Log.d(TAG, "JSON data for " + requestName + json.toString());
            this.setJson(json);
        }
        return json;
    }

    @Override
    protected void onPostExecute(String jsonObject) {
        super.onPostExecute(jsonObject);
        Bundle args = new Bundle();
        args.putString("animal_pic_json", json.toString());

        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        fragment.setArguments(args);

        transaction.replace(R.id.pets_content_fragment, fragment);
        transaction.commit();
        mDialog.dismiss();
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}


