package com.pet.att.pickapet.HTTP;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pet.att.pickapet.AuxiliaryClasses.OnTaskCompleted;
import com.pet.att.pickapet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PetsImagesTask extends AsyncTask<String, Void, Boolean> {
    private static final String TAG = "PetsImagesCallRequest";
    private String json= null;
    private String  baseURL;
    private final Context mContext;
    private final Activity mActivity;
    private ArrayList<String> mActiveAnimalPicsBodyJson = null;
    private String mActiveAnimalPicsJsonResult;
    private String mFirstRequestName;
    private String mPutExtraString;
    private String mGenderFilter;
    private String mKindFilter;
    private String mTypeFilter;
    private OnTaskCompleted listener;
    private String[] mCurrentStrings;

    public PetsImagesTask(Activity activity, Context context ,OnTaskCompleted listener){
        this.mContext = context;
        this.baseURL =   mContext.getString(R.string.base_url);
        mActivity = activity;
        this.listener = listener;
        mActiveAnimalPicsBodyJson = new ArrayList<String>();
    }

//    @Override
//    protected void onPreExecute() {
//        mDialog = new ProgressDialog(mContext);
//        mDialog.setMessage("Please wait...");
//        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        mDialog.setIndeterminate(true);
//        mDialog.setCancelable(false);
//        mDialog.show();
//    }


    /*
     * The First arg is animals_owner_active request name
     * The Second arg is animal_pic IN request name
     * The Third arg is putExtra String for json
     * */
//    @Override
//    protected Boolean doInBackground(String... strings) {
//        boolean isCorrect = false;
//        this.initStringsItems(strings);
//        Log.d(TAG, "Fetching the all active animals for "+ this.getFirstRequestName());
//        //Need to send request to get all active animals
//        String json = HttpRequestsURLConnection.SendHttpPost(baseURL + "/"+ this.getFirstRequestName(),this.getCheckoutValue());
//        if (isValidJsonResult(json)){
//            Log.d(TAG, "JSON data for active animals" + this.getFirstRequestName()+ " is " + json.toString());
//            String allAnimalsId = this.getAllAnimalsIdFromJson(json);
//            //Need to send request to get all active animals pictures
//            json = HttpRequestsURLConnection.SendHttpPost(baseURL + "/"+ this.getSecondRequestName(),allAnimalsId);
//            if (isValidJsonResult(json)){
//                Log.d(TAG, "JSON data Pics for active animals" + this.getSecondRequestName()+ " is " + json.toString());
//                this.setActiveAnimalPicsJsonResult(json);
//                isCorrect=true;
//            }
//        }
//        return isCorrect;
//    }


    @Override
    protected Boolean doInBackground(String... strings) {
        boolean isCorrect = false;
        this.initStringsItems(strings);
        Log.d(TAG, "Fetching the all active animals pics from request "+ this.getFirstRequestName());
        //Need to send request to get all active animals
        String json = HttpRequestsURLConnection.SendHttpPost(baseURL + "/"+ this.getFirstRequestName(),
                                                                        this.getJsonBodyString(mActiveAnimalPicsBodyJson));
        if (isValidJsonResult(json)){
                this.setActiveAnimalPicsJsonResult(json);
                isCorrect=true;
        }

        return isCorrect;
    }

//    @Override
//    protected void onPostExecute(final Boolean result) {
//        if (result) {
//            Bundle args = new Bundle();
////            args.putString("animal_pic_json", json.toString());
//            args.putString(this.getPutExtraString(), this.getActiveAnimalPicsJsonResult());
//
//            FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
//            RecyclerViewFragment fragment = new RecyclerViewFragment();
//            fragment.setArguments(args);
//
//            transaction.replace(R.id.pets_content_fragment, fragment);
//            transaction.commit();
//        }
//        mDialog.dismiss();
//    }

        @Override
    protected void onPostExecute(final Boolean result) {
        if (result) {
            mActivity.getIntent().putExtra(this.getPutExtraString(), this.getActiveAnimalPicsJsonResult());
        }
//        }else{
////            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
////            builder.setMessage(mContext.getString(R.string.error_put_data))
////                    .setCancelable(false)
////                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
////                        public void onClick(DialogInterface dialog, int id) {
////
////                        }
////                    });
////            AlertDialog alert = builder.create();
////            alert.show();
//        }
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
        this.mCurrentStrings = strings;
        this.setFirstRequestName(strings[0]);
        this.setTypeFilter(strings[1]);
        this.mActiveAnimalPicsBodyJson.add("typeid=" + this.getTypeFilter());

        this.setGenderFilter(strings[2]);
        this.mActiveAnimalPicsBodyJson.add("gid=" + this.getGenderFilter());

        this.setKindFilter(strings[3]);
        this.mActiveAnimalPicsBodyJson.add("kind_id=" + this.getKindFilter());

        this.setPutExtraString(strings[4]);
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

    private String getPutExtraString() {
        return mPutExtraString;
    }

    private void setPutExtraString(String mPutExtraString) {
        this.mPutExtraString = mPutExtraString;
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

    private String getJsonBodyString(ArrayList<String> jsonArray){
        String jsonString="";
        for (int i =0;i<jsonArray.size();i++){
            jsonString=jsonString +"&"+jsonArray.get(i);
        }
        return (jsonString.length()>0)? jsonString.substring(1,jsonString.length()):"";
    }

    private String getActiveAnimalPicsJsonResult() {
        return mActiveAnimalPicsJsonResult;
    }

    private void setActiveAnimalPicsJsonResult(String activeAnimalPicsJsonResult) {
        mActiveAnimalPicsJsonResult = activeAnimalPicsJsonResult;
    }

}


