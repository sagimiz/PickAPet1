package com.pet.att.pickapet.HTTP;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pet.att.pickapet.AppActivities.AddNewPetActivity;
import com.pet.att.pickapet.R;

public class GetAnimalTypeTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "GetAnimalTypeTask" ;
    private final Context mContext;
    private final String baseURL;
    private final AppCompatActivity mActivity;

    private String putText;


    public GetAnimalTypeTask(AppCompatActivity activity, Context context){
        this.mContext = context;
        this.baseURL =   mContext.getString(R.string.base_url);
        this.mActivity = activity;
    }

    /**
     * First Value is request name
     * Second value putString Value
     */

    @Override
    protected String doInBackground(String... strings) {
        String requestName = strings[0];
        String jsonStr = null;
        Log.d(TAG, "Fetching data for "+ requestName);
        jsonStr = HttpRequestsURLConnection.SendHttpGet(baseURL + "/"+ requestName);
        Log.d(TAG, "JSON data for " + requestName + ", is: " + jsonStr);
        this.setPutExtraValue(strings[1]);
        return jsonStr;
    }

    @Override
    protected void onPostExecute(String jsonObject) {

            Intent intent = new Intent (mActivity, AddNewPetActivity.class);
            if (jsonObject !=null)
                intent.putExtra(this.getPutExtraValue(),jsonObject);
            mActivity.startActivity(intent);
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

    private String getPutExtraValue() {
        return putText;
    }

    private void setPutExtraValue(String putText) {
        this.putText = putText;
    }

    private String setStringToJsonFormat (String currentJsonString){
        return currentJsonString.substring(1,currentJsonString.length()-2);
    }
}
