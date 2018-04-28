package com.pet.att.pickapet.AuxiliaryClasses;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.pet.att.pickapet.R;

import java.util.ArrayList;

public abstract class MainObjectTask extends AsyncTask<Object, Void, Boolean>  {

    private String mFirstRequestName;
    private String mSecondRequestName;
    private String mThirdRequestName;
    private String mFirstPutString;
    private String mSecondPutString;
    protected Context mContext;
    protected AppCompatActivity  mActivity;
    protected final String baseURL;

    public MainObjectTask(Context mContext, AppCompatActivity mActivity) {
        this.mContext = mContext;
        this.baseURL = mContext.getString(R.string.base_url);
        this.mActivity=mActivity;
    }


    protected String getSecondRequestName() {
        return mSecondRequestName;
    }

    protected void setSecondRequestName(String mSecondRequestName) {
        this.mSecondRequestName = mSecondRequestName;
    }

    protected String getFirstRequestName() {
        return mFirstRequestName;
    }

    protected void setFirstRequestName(String firstRequestName) {
        this.mFirstRequestName = firstRequestName;
    }

    protected String getThirdRequestName() {
        return mThirdRequestName;
    }

    protected void setThirdRequestName(String mThirdRequestName) {
        this.mThirdRequestName = mThirdRequestName;
    }


    protected String getJsonBodyString(ArrayList<String> jsonArray){
        String jsonString="";
        for (int i =0;i<jsonArray.size();i++){
            jsonString=jsonString +"&"+jsonArray.get(i);
        }
        return (jsonString.length()>0)? jsonString.substring(1,jsonString.length()):"";
    }

    protected String getStringJsonBody(ArrayList<String> jsonArray){
        String jsonString="";
        for (int i =0;i<jsonArray.size();i++){
            jsonString=jsonString +","+jsonArray.get(i);
        }
        return (jsonString.length()>0)? "{"+jsonString.substring(1,jsonString.length())+"}":"";
    }

    protected boolean isValidJsonResult(String jsonStr){
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

    protected String setStringToJsonFormat (String currentJsonString){
        return currentJsonString.substring(1,currentJsonString.length());
    }

    protected String getFirstPutString() {
        return mFirstPutString;
    }

    protected void setFirstPutString(String mFirstPutString) {
        this.mFirstPutString = mFirstPutString;
    }

    protected String getSecondPutString() {
        return mSecondPutString;
    }

    protected void setSecondPutString(String mSecondPutString) {
        this.mSecondPutString = mSecondPutString;
    }

}
