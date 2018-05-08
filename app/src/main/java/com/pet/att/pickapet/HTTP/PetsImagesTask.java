package com.pet.att.pickapet.HTTP;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.pet.att.pickapet.AuxiliaryClasses.MainStringTask;
import com.pet.att.pickapet.AuxiliaryClasses.OnTaskCompleted;
import java.util.ArrayList;

public class PetsImagesTask extends MainStringTask {
    private static final String TAG = "PetsImagesCallRequest";
    private ArrayList<String> mActiveAnimalPicsBodyJson = null;
    private String mActiveAnimalPicsJsonResult;
    private String mPutExtraString;
    private String mGenderFilter;
    private String mKindFilter;
    private String mTypeFilter;
    private OnTaskCompleted listener;

    public PetsImagesTask(Activity activity, Context context ,OnTaskCompleted listener){
        super(context, (AppCompatActivity) activity);
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        boolean isCorrect = false;
        this.initStringsItems(strings);
        Log.d(TAG, "Fetching the all active animals pics from request "+ super.getFirstRequestName());
        String json = HttpRequestsURLConnection.SendHttpPost(baseURL + "/"+ super.getFirstRequestName(),
                                                                        this.getJsonBodyString(mActiveAnimalPicsBodyJson));
        if (super.isValidJsonResult(json)){
                this.setActiveAnimalPicsJsonResult(json);
                isCorrect=true;
        }

        return isCorrect;
    }

    @Override
    protected void onPostExecute(final Boolean result) {
        if (result) {
            mActivity.getIntent().putExtra(this.getFirstPutString(), this.getActiveAnimalPicsJsonResult());
        }
        listener.onTaskCompleted(result);
    }

    /*
     * The First arg is animal_pics Spacial request name
     * The Second arg is type id value
     * The Third arg is gid value
     * The Forth arg is kind_id value
     * The Fifth arg is putExtra String for json
     * */
    private void initStringsItems(String[] strings) {
        mActiveAnimalPicsBodyJson = new ArrayList<String>();
        super.setFirstRequestName(strings[0]);
        this.setTypeFilter(strings[1]);
        this.mActiveAnimalPicsBodyJson.add("typeid=" + this.getTypeFilter());

        this.setGenderFilter(strings[2]);
        this.mActiveAnimalPicsBodyJson.add("gid=" + this.getGenderFilter());

        this.setKindFilter(strings[3]);
        this.mActiveAnimalPicsBodyJson.add("kind_id=" + this.getKindFilter());

        super.setFirstPutString(strings[4]);
    }

    private String getGenderFilter() {
        return mGenderFilter;
    }

    private void setGenderFilter(String mGenderFilter) {
        this.mGenderFilter = mGenderFilter;
    }

    private String getKindFilter() {
        return mKindFilter;
    }

    private void setKindFilter(String mKindFilter) {
        this.mKindFilter = mKindFilter;
    }

    private String getTypeFilter() {
        return mTypeFilter;
    }

    private void setTypeFilter(String mTyperFilter) {
        this.mTypeFilter = mTyperFilter;
    }

    private String getActiveAnimalPicsJsonResult() {
        return mActiveAnimalPicsJsonResult;
    }

    private void setActiveAnimalPicsJsonResult(String activeAnimalPicsJsonResult) {
        mActiveAnimalPicsJsonResult = activeAnimalPicsJsonResult;
    }

}


