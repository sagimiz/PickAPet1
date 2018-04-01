package com.pet.att.pickapet.recyclerview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class AnimalsPics {
    private static final String TAG = "AnimalsPicsFormator";

    private Bitmap myImage;
    private JSONObject imageJsonObj;
    private String animalId;

    public AnimalsPics(String jsonString, String aid)  {
        try {
//            JSONArray jsonArray = new JSONArray(jsonString);
//            int i = 0;
//            boolean found=false;
//            while ( i<jsonArray.length() && !found){
//                String imageStr = jsonArray.get(i++).toString();
//                JSONObject jsonObject = new JSONObject(imageStr );
//                String animalId = jsonObject.get("picid").toString();
//                if(animalId.equals(String.valueOf(aid))) {
//                    byte[] imageBytes = Base64.decode(jsonObject.get("pic").toString(), Base64.DEFAULT);
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0 ,imageBytes.length);
//                    this.setImageJsonObj(jsonObject);
//                    this.setMyImage(bitmap);
//                    found=true;
//                }
//            }
            JSONObject jsonObject = new JSONObject(jsonString );
            this.animalId = jsonObject.get("aid").toString();
            String blob =  jsonObject.get("pic").toString();
            JSONObject blobJson = new JSONObject(blob);
            byte[] tmp=new byte[blobJson.getJSONArray("data").length()];
            for(int i=0;i<blobJson.getJSONArray("data").length();i++){
                tmp[i]=(byte)(((int)blobJson.getJSONArray("data").get(i)) & 0xFF);
            }
            myImage= BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
//            byte[] imageBytes = Base64.decode(jsonObject.get("pic").toString(), Base64.DEFAULT);
//            myImage = BitmapFactory.decodeByteArray(imageBytes, 0 ,imageBytes.length);
        } catch (JSONException e) {
            Log.d (TAG,e.toString());
            e.printStackTrace();
        }
    }


//    JSONObject obj=new JSONObject("{\"type\":\"Buffer\",\"data\":[137,80,78,71,13,10,26,10,0,0,0,13,73,72,68,82,0,0,1,140,0,0,1,34,4,3,0,0,0,24,1,248,231,0,0,0,21,80,76,84,69,255,255,255,0,0,0,220,217,207,0,100,0,176,196,222,0,153,204,204,255,255,227,179,150,92,0,0,1,29,73,68,65,84,120,156,237,207,1,13,128,64,12,4,193,211,130,5,44,96,1,11,248,151,192,91,104,66,66,191,153,85,176,147,99,68,249,123,224,155,48,58,181,24,217,62,140,78,205,98,156,165,146,171,84,114,151,74,158,82,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,24,157,25,187,135,209,169,73,140,9,97,116,106,8,227,5,178,0,69,109,104,207,36,38,0,0,0,0,73,69,78,68,174,66,96,130]} ");
//    Bitmap bitmap=null;
//    byte[] tmp=new byte[obj.getJSONArray("data").length()];
//for(int i=0;i<obj.getJSONArray("data").length();i++){
//        tmp[i]=(byte)(((int)obj.getJSONArray("data").get(i)) & 0xFF);
//    }
//    bitmap= BitmapFactory.decodeByteArray(tmp, 0, tmp.length);





    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }

    public JSONObject getImageJsonObj() {
        return imageJsonObj;
    }

    public void setImageJsonObj(JSONObject imageJsonObj) {
        this.imageJsonObj = imageJsonObj;
    }

    public Bitmap getMyImage() {
        return myImage;
    }

    public void setMyImage(Bitmap myImage) {
        this.myImage = myImage;
    }
}
