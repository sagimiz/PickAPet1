package com.pet.att.pickapet.HTTP;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pet.att.pickapet.R;

import java.io.File;
import java.util.ArrayList;

public class AddNewPetTask extends AsyncTask<Object, Void, Boolean> {
    private static final String TAG = "AddNewPetTask";
    private final AppCompatActivity mActivity;
    private final String baseURL;
    private Context mContext;
    private String mUserDetailsJson;
    private ProgressDialog mDialog;
    private String mAnimalId;
    private String mOwnerId;
    private String mAnimalName;
    private String mAnimalType;
    private String mAnimalGender;
    private File mAnimalImage;
    private String mAnimalBirthDate;


    private ArrayList<String> jsonBodyArrayForAnimals;
    private ArrayList<String> jsonBodyArrayForAnimalsOwner;
    private String mFirstPutString;
    private String mFirstRequestName;
    private String mSecondRequestName;
    private String mThirdRequestName;




    public AddNewPetTask(AppCompatActivity activity, Context context){
        this.mContext = context;
        this.baseURL =   mContext.getString(R.string.base_url);
        this.mActivity = activity;
    }


    /*
       The First arg is animals request name
       The Second arg is animal_pic request name
       The Second arg is animal_Owner request name
       The Third arg is animal_name for json
       The Third arg is animal_type for json
       The Forth arg is image_file name for json
       The Fifth arg is animal_birth_date name for json
       The Sixth arg is owner_id for json
    */

    @Override
    protected Boolean doInBackground(Object... objects) {
        this.initStringsItems(objects);

        boolean isCorrect = false;

        Log.d(TAG, "Sending Put data for "+ this.getFirstRequestName());
        String  resultStr = HttpRequestsURLConnection.SendObjectsHttpPut(baseURL + "/"+ this.getFirstRequestName(), getJsonBodyString(jsonBodyArrayForAnimals));
        if (isValidJsonResult(resultStr) && (resultStr.equals("Empty")) ) {
            resultStr = HttpRequestsURLConnection.SendHttpPut(baseURL + "/"+ this.getSecondRequestName(), getJsonBodyString(jsonBodyArrayForAnimalsOwner));
            if (isValidJsonResult(resultStr) && resultStr.equals("Empty")) {
                isCorrect = true;
            }
        }

        return isCorrect;
    }



//
//    @Override
//    protected Boolean doInBackground(String... strings) {
//
//        jsonBodyArrayForUser = new ArrayList<String>();
//        this.initStringsItems(strings);
//        boolean isCorrect = false;
//
//        Log.d(TAG, "Sending Put data for "+ this.getFirstRequestName());
//        String  resultStr = HttpRequestsURLConnection.SendHttpPut(baseURL + "/"+ this.getFirstRequestName(), getJsonBodyString(jsonBodyArrayForUser));
//        if (isValidJsonResult(resultStr) && (resultStr.equals("Empty")) ) {
//            resultStr = HttpRequestsURLConnection.SendHttpPut(baseURL + "/"+ this.getSecondRequestName(), getJsonBodyString(jsonBodyArrayForUserLogin));
//            if (isValidJsonResult(resultStr) && resultStr.equals("Empty")) {
//                isCorrect = true;
//            }
//        }
//        return isCorrect;
//    }

    private String getJsonBodyString(ArrayList<String> jsonArray){
        String jsonString="";
        for (int i =0;i<jsonArray.size();i++){
            jsonString=jsonString +"&"+jsonArray.get(i);
        }
        return (jsonString.length()>0)? jsonString.substring(1,jsonString.length()):"";
    }

    private String getStringJsonBody(ArrayList<String> jsonArray){
        String jsonString="";
        for (int i =0;i<jsonArray.size();i++){
            jsonString=jsonString +","+jsonArray.get(i);
        }
        return (jsonString.length()>0)? "{"+jsonString.substring(1,jsonString.length())+"}":"";
    }




    /*
   The First arg is animals request name
   The Second arg is animal_pic request name
   The Second arg is animal_Owner request name
   The Third arg is animal_name for json
   The Third arg is animal_type for json
   The Forth arg is image_file name for json
   The Fifth arg is animal_birth_date name for json
   The Sixth arg is owner_id for json
*/
    private void initStringsItems(Object[] objects) {

        this.setFirstRequestName( (String) objects[0]);
        this.setSecondRequestName((String) objects[1]);
        this.setSecondRequestName((String) objects[2]);

        this.setId((String) objects[3]);
        jsonBodyArrayForAnimalsOwner.add("id="+this.getId());

        long animalId = System.currentTimeMillis();
        this.setAnimalId(String.valueOf(animalId));
        jsonBodyArrayForAnimalsOwner.add("aid="+this.getAnimalId());


    }

    @Override
    protected void onPostExecute(final Boolean success) {

        //TODO

    }




    public String getSecondRequestName() {
        return mSecondRequestName;
    }

    public void setSecondRequestName(String mSecondRequestName) {
        this.mSecondRequestName = mSecondRequestName;
    }


    public String getFirstRequestName() {
        return mFirstRequestName;
    }

    public void setFirstRequestName(String firstRequestName) {
        this.mFirstRequestName = firstRequestName;
    }

    @Override
    protected void onCancelled() {
        mDialog.dismiss();
    }

    public String getFirstPutString() {
        return mFirstPutString;
    }

    public void setFirstPutString(String mFirstPutString) {
        this.mFirstPutString = mFirstPutString;
    }


    private String setStringToJsonFormat (String currentJsonString){
        return currentJsonString.substring(1,currentJsonString.length());
    }

    private String getUserDetailsJson() {
        return mUserDetailsJson;
    }

    private void setUserDetailsJson(String ownerDetailsJson) {
        this.mUserDetailsJson = ownerDetailsJson;
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

    public String getId() {
        return mOwnerId;
    }

    public void setId(String mId) {
        this.mOwnerId = mId;
    }

    public String getAnimalName() {
        return mAnimalName;
    }

    public void setAnimalName(String mFirstName) {
        this.mAnimalName = mFirstName;
    }


    public File getAnimalImage() {
        return mAnimalImage;
    }

    public void setAnimalImage(File mAnimalImage) {
        this.mAnimalImage = mAnimalImage;
    }

    public String getAnimalBirthDate() {
        return mAnimalBirthDate;
    }

    public void setAnimalBirthDate(String mAnimalBirthDate) {
        this.mAnimalBirthDate = mAnimalBirthDate;
    }

    public String getThirdRequestName() {
        return mThirdRequestName;
    }

    public void setThirdRequestName(String mThirdRequestName) {
        this.mThirdRequestName = mThirdRequestName;
    }


    public String getAnimalType() {
        return mAnimalType;
    }

    public void setAnimalType(String mAnimalType) {
        this.mAnimalType = mAnimalType;
    }

    public String getAnimalGender() {
        return mAnimalGender;
    }

    public void setAnimalGender(String mAnimalGender) {
        this.mAnimalGender = mAnimalGender;
    }


    public String getAnimalId() {
        return mAnimalId;
    }

    public void setAnimalId(String mAnimalId) {
        this.mAnimalId = mAnimalId;
    }
}
