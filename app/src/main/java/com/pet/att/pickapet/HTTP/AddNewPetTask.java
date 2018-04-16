package com.pet.att.pickapet.HTTP;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.pet.att.pickapet.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    private String mAnimalKind;
    private String mAnimalGender;
    private File mAnimalImage;
    private String mAnimalBirthDate;


    private ArrayList<String> jsonBodyArrayForAnimals;
    private ArrayList<String> jsonBodyArrayForAnimalsOwner;
    private ArrayList<String> jsonBodyArrayForAnimalsPic;
    private String mFirstPutString;
    private String mFirstRequestName;
    private String mSecondRequestName;
    private String mThirdRequestName;



    public AddNewPetTask(AppCompatActivity activity, Context context){
        this.mContext = context;
        this.baseURL =   mContext.getString(R.string.base_url);
        this.mActivity = activity;
        jsonBodyArrayForAnimals = new ArrayList<String>();
        jsonBodyArrayForAnimalsOwner = new ArrayList<String>();
        jsonBodyArrayForAnimalsPic = new ArrayList<String>();

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
        String  resultStr = HttpRequestsURLConnection.SendHttpPut(baseURL + "/"+ this.getFirstRequestName(), getJsonBodyString(jsonBodyArrayForAnimals));
        if (isValidJsonResult(resultStr) && (resultStr.equals("Empty")) ) {
            Log.d(TAG, "Result data for request "+ this.getFirstRequestName() + " is " + resultStr );
            resultStr = HttpRequestsURLConnection.SendHttpPut(baseURL + "/"+ this.getSecondRequestName(), getJsonBodyString(jsonBodyArrayForAnimalsOwner));
            if (isValidJsonResult(resultStr) && resultStr.equals("Empty")) {
                Log.d(TAG, "Result data for request "+ this.getSecondRequestName() + " is " + resultStr );
                resultStr = HttpRequestsURLConnection.SendObjectsHttpPut(baseURL + "/"+ this.getThirdRequestName(),
                                                                            getJsonBodyString(jsonBodyArrayForAnimalsPic),
                                                                                this.getAnimalImage());
                if (isValidJsonResult(resultStr) && resultStr.equals("Empty")){
                    Log.d(TAG, "Result data for request "+ this.getThirdRequestName() + " is " + resultStr );
                    isCorrect = true;
                }
            }
        }
        return isCorrect;
    }

    @Override
    protected void onPostExecute(final Boolean success) {

        if (success){

            mActivity.finish();
        }else{

        }

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

    /*
    * The First arg is animals request name
    * The Second arg is animal_pic request name
    * The Third arg is animal_Owner request name
    * The Forth arg is owner_id for json
    * The Fifth arg is animal_name for json
    * The Sixth arg is animal_type  for json
    * The Seventh arg is animal_birth_date name for json
    * The Eighth arg is animal_gender for json
    * The Ninth arg is animal_ind for json
    * The Tenth arg is animal_pic for json
    * */
    private void initStringsItems(Object[] objects) {

        this.setFirstRequestName( (String) objects[0]);
        this.setSecondRequestName((String) objects[1]);
        this.setThirdRequestName((String) objects[2]);

        this.setId((String) objects[3]);
        jsonBodyArrayForAnimalsOwner.add("id="+this.getId());

        long animalId = System.currentTimeMillis();
        this.setAnimalId(String.valueOf(animalId));
        jsonBodyArrayForAnimalsOwner.add("aid="+this.getAnimalId());
        jsonBodyArrayForAnimals.add("aid="+this.getAnimalId());
        jsonBodyArrayForAnimalsPic.add("aid="+this.getAnimalId());

        jsonBodyArrayForAnimalsOwner.add("checkout=1");

        SimpleDateFormat newString = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String strDate = newString.format(now);
        jsonBodyArrayForAnimalsOwner.add("start_date="+strDate);

        this.setAnimalName((String)objects[4]);
        jsonBodyArrayForAnimals.add("name="+this.getAnimalName());


        this.setAnimalType((String)objects[5]);
        jsonBodyArrayForAnimals.add("typeid="+this.getAnimalType());

        this.setAnimalBirthDate((String) objects[6]);
        jsonBodyArrayForAnimals.add("bday="+this.getAnimalBirthDate());

        String gender = (String)objects[7];
        this.setAnimalGender(gender.equals("זכר")? "1" : "2");
        jsonBodyArrayForAnimals.add("gid="+this.getAnimalGender());

        this.setAnimalKind((String)objects[8]);
        jsonBodyArrayForAnimals.add("kind_id="+this.getAnimalKind());

        this.setAnimalImage((File)objects[9]);
        Bitmap bm = BitmapFactory.decodeFile(objects[9].toString());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 1, output); //bm is the bitmap object
        byte[] bytes = output.toByteArray();
        String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

        jsonBodyArrayForAnimalsPic.add("pic="+base64Image);

    }

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

    public String getAnimalKind() {
        return mAnimalKind;
    }

    public void setAnimalKind(String mAnimalKind) {
        this.mAnimalKind = mAnimalKind;
    }
}
