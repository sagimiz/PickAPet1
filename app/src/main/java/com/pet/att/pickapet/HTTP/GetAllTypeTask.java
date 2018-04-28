package com.pet.att.pickapet.HTTP;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pet.att.pickapet.AuxiliaryClasses.MainStringTask;
import com.pet.att.pickapet.AuxiliaryClasses.OnTaskCompleted;

public class GetAllTypeTask extends MainStringTask {

    private static final String TAG = "GetAllKindsTask" ;
    private String mKind;
    OnTaskCompleted listener;
    public GetAllTypeTask(AppCompatActivity mActivity, Context mContext, OnTaskCompleted listener) {
        super(mContext, mActivity);
        this.listener =listener;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        boolean isCorrect= false;
        this.initStringsItems(strings);
        Log.d(TAG, "Fetching data for "+ this.getFirstRequestName());
        String jsonStr  = HttpRequestsURLConnection.SendHttpPost(baseURL + "/"+ super.getFirstRequestName(),"kind_id="+this.getKind());
        if (super.isValidJsonResult(jsonStr)){
            this.setFirstJSONResult(jsonStr);
            isCorrect =true;
        }
        return isCorrect;
    }

    private void initStringsItems(String[] strings) {
        super.setFirstRequestName(strings[0]);
        this.setKind(strings[1]);
        super.setFirstPutString(strings[2]);
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if(success) {
            mActivity.getIntent().putExtra(this.getFirstPutString(), this.getFirstJSONResult());
        }
        listener.onTaskCompleted(success);
    }


    public String getKind() {
        return mKind;
    }

    public void setKind(String mKind) {
        this.mKind = mKind;
    }
}
