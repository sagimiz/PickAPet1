package com.pet.att.pickapet.HTTP;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.pet.att.pickapet.AuxiliaryClasses.MainObjectTask;
import com.pet.att.pickapet.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddNewPetTask extends MainObjectTask {
    private static final String TAG = "AddNewPetTask";
    private String mAnimalId;
    private String mOwnerId;
    private String mAnimalName;
    private String mAnimalType;
    private String mAnimalKind;
    private String mAnimalGender;
    private File mAnimalImage;
    private String mAnimalBirthDate;
    private String mAnimalDescription;
    private ArrayList<String> jsonBodyArrayForAnimals;
    private ArrayList<String> jsonBodyArrayForAnimalsOwner;
    private ArrayList<String> jsonBodyArrayForAnimalsPic;


    public AddNewPetTask(AppCompatActivity mActivity, Context mContext){
        super(mContext,mActivity);
        jsonBodyArrayForAnimals = new ArrayList<String>();
        jsonBodyArrayForAnimalsOwner = new ArrayList<String>();
        jsonBodyArrayForAnimalsPic = new ArrayList<String>();

    }
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
    @Override
    protected Boolean doInBackground(Object... objects) {
        this.initStringsItems(objects);

        boolean isCorrect = false;
        Log.d(TAG, "Sending Put data for "+ super.getFirstRequestName());
        String  resultStr = HttpRequestsURLConnection.SendHttpPut(baseURL + "/"+ super.getFirstRequestName(), super.getJsonBodyString(jsonBodyArrayForAnimals));
        if (super.isValidJsonResult(resultStr) && (resultStr.equals("Empty")) ) {
            Log.d(TAG, "Result data for request "+ super.getFirstRequestName() + " is " + resultStr );
            resultStr = HttpRequestsURLConnection.SendHttpPut(baseURL + "/"+ super.getSecondRequestName(), super.getJsonBodyString(jsonBodyArrayForAnimalsOwner));
            if (super.isValidJsonResult(resultStr) && resultStr.equals("Empty")) {
                Log.d(TAG, "Result data for request "+ super.getSecondRequestName() + " is " + resultStr );
                resultStr = HttpRequestsURLConnection.SendObjectsHttpPut(baseURL + "/"+ super.getThirdRequestName(),
                                                                            getJsonBodyString(jsonBodyArrayForAnimalsPic),
                                                                                this.getAnimalImage());
                if (super.isValidJsonResult(resultStr) && resultStr.equals("Empty")){
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
    * The Eleventh arg is animal Description for json
    * */
    private void initStringsItems(Object[] objects) {

        super.setFirstRequestName( (String) objects[0]);
        super.setSecondRequestName((String) objects[1]);
        super.setThirdRequestName((String) objects[2]);

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

        this.setAnimalDescription((String)objects[10]);
        jsonBodyArrayForAnimals.add("description="+this.getAnimalDescription());
    }

    private String getId() {
        return mOwnerId;
    }

    private void setId(String mId) {
        this.mOwnerId = mId;
    }

    private String getAnimalName() {
        return mAnimalName;
    }

    private void setAnimalName(String mFirstName) {
        this.mAnimalName = mFirstName;
    }

    private File getAnimalImage() {
        return mAnimalImage;
    }

    private void setAnimalImage(File mAnimalImage) {
        this.mAnimalImage = mAnimalImage;
    }

    private String getAnimalBirthDate() {
        return mAnimalBirthDate;
    }

    private void setAnimalBirthDate(String mAnimalBirthDate) {
        this.mAnimalBirthDate = mAnimalBirthDate;
    }

    private String getAnimalType() {
        return mAnimalType;
    }

    private void setAnimalType(String mAnimalType) {
        this.mAnimalType = mAnimalType;
    }

    private String getAnimalGender() {
        return mAnimalGender;
    }

    private void setAnimalGender(String mAnimalGender) {
        this.mAnimalGender = mAnimalGender;
    }

    private String getAnimalId() {
        return mAnimalId;
    }

    private void setAnimalId(String mAnimalId) {
        this.mAnimalId = mAnimalId;
    }

    private String getAnimalKind() {
        return mAnimalKind;
    }

    private void setAnimalKind(String mAnimalKind) {
        this.mAnimalKind = mAnimalKind;
    }

    public String getAnimalDescription() {
        return mAnimalDescription;
    }

    public void setAnimalDescription(String mAnimalDescription) {
        this.mAnimalDescription = mAnimalDescription;
    }
}
