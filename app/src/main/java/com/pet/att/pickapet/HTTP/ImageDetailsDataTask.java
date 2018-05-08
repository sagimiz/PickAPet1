package com.pet.att.pickapet.HTTP;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.pet.att.pickapet.AuxiliaryClasses.MainStringTask;
import com.pet.att.pickapet.AuxiliaryClasses.OnTaskCompleted;

public class ImageDetailsDataTask extends MainStringTask {

    private static final String TAG = "PictureDetailsData" ;
    private String mAnimalId;
    private OnTaskCompleted listener;
    private String mImageDetailsJsonResult;

    public ImageDetailsDataTask(AppCompatActivity mActivity, Context mContext, OnTaskCompleted listener){
        super(mContext,mActivity);
        this.listener=listener;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        boolean isCorrect=false;
        initStringsItems(strings);
        Log.d(TAG, "Fetching data for "+ super.getFirstRequestName());
        String jsonStr = HttpRequestsURLConnection.SendHttpPost(baseURL + "/"+ super.getFirstRequestName(),this.getAnimalId());
        if(super.isValidJsonResult(jsonStr)){
            this.setImageDetailsJsonResult(jsonStr);
            isCorrect=true;
        }
        return isCorrect;
    }

    /*
    /* The First arg is image_all_details request name
    /* The Second arg is animal_id request name
    /* The Third arg is PutString value for json
     */

    private void initStringsItems(String[] strings) {
        super.setFirstRequestName(strings[0]);
        this.setAnimalId("aid="+strings[1]);
        super.setFirstPutString(strings[2]);

    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if(success){
            mActivity.getIntent().putExtra(super.getFirstPutString(),super.setStringToJsonFormat(this.getImageDetailsJsonResult()));
        }
        listener.onTaskCompleted(success);
    }

    private String getAnimalId() {
        return mAnimalId;
    }

    private void setAnimalId(String mAnimalId) {
        this.mAnimalId = mAnimalId;
    }

    private String getImageDetailsJsonResult() {
        return mImageDetailsJsonResult;
    }

    private void setImageDetailsJsonResult(String mImageDetailsJsonResult) {
        this.mImageDetailsJsonResult = mImageDetailsJsonResult;
    }

}
