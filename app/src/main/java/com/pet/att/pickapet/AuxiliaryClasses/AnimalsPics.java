package com.pet.att.pickapet.AuxiliaryClasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class AnimalsPics {
    private static final String TAG = "AnimalsPicsFormator";

    private Bitmap myImage;
    private String imageJson;
    private String animalId;

    public AnimalsPics(String jsonString)  {
        try {
            this.setImageJson(jsonString);
            JSONObject jsonObject = new JSONObject(jsonString );

            this.setAnimalId(jsonObject.get("aid").toString());
            String blob =  jsonObject.get("pic").toString();
            JSONObject blobJson = new JSONObject(blob);

            byte[] tmp=new byte[blobJson.getJSONArray("data").length()];
            for(int i=0;i<blobJson.getJSONArray("data").length();i++){
                tmp[i]=(byte)(((int)blobJson.getJSONArray("data").get(i)) & 0xFF);
            }
            this.setMyImage( BitmapFactory.decodeByteArray(tmp, 0, tmp.length));
        } catch (JSONException e) {
            Log.d (TAG,e.toString());
            e.printStackTrace();
        }
    }

    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }

    public String getImageJson() {
        return imageJson;
    }

    public void setImageJson(String imageJson) {
        this.imageJson= imageJson;
    }

    public Bitmap getMyImage() {
        return myImage;
    }

    public void setMyImage(Bitmap myImage) {
        this.myImage = myImage;
    }

    @Override
    public String toString() {
        return imageJson.toString();
    }
}
