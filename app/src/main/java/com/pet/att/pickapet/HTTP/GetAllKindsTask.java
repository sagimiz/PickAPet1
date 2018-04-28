package com.pet.att.pickapet.HTTP;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.pet.att.pickapet.AuxiliaryClasses.MainStringTask;
import com.pet.att.pickapet.AuxiliaryClasses.OnTaskCompleted;

public class GetAllKindsTask extends MainStringTask {

    private static final String TAG = "GetAllKindsTask" ;
    OnTaskCompleted listener;
    public GetAllKindsTask(AppCompatActivity mActivity,Context mContext, OnTaskCompleted listener) {
        super(mContext, mActivity);
        this.listener =listener;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        boolean isCorrect= false;
        this.initStringsItems(strings);
        Log.d(TAG, "Fetching data for "+ this.getFirstRequestName());
        String jsonStr  = HttpRequestsURLConnection.SendHttpGet(baseURL + "/"+ super.getFirstRequestName());
        if (super.isValidJsonResult(jsonStr)){
            this.setFirstJSONResult(jsonStr);
            isCorrect =true;
        }
        return isCorrect;
    }

    private void initStringsItems(String[] strings) {
        super.setFirstRequestName(strings[0]);
        super.setFirstPutString(strings[1]);
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if(success) {
            mActivity.getIntent().putExtra(this.getFirstPutString(), this.getFirstJSONResult());
        }
        listener.onTaskCompleted(success);
    }

}
