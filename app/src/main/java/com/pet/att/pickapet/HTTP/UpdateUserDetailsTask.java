package com.pet.att.pickapet.HTTP;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.pet.att.pickapet.AuxiliaryClasses.MainStringTask;
import com.pet.att.pickapet.AuxiliaryClasses.OnTaskCompleted;
import com.pet.att.pickapet.R;
import java.util.ArrayList;


public class UpdateUserDetailsTask extends MainStringTask {
    private static final String TAG = "UpdateUserDetailsTask";
    private final OnTaskCompleted listener;
    private String mId;
    private String mFirstName;
    private String mLastName;
    private String mPhone;
    private String mAddress;
    private String mCity;
    private ArrayList<String> jsonBodyArrayForUser;

    public UpdateUserDetailsTask(AppCompatActivity mActivity, Context mContext,OnTaskCompleted listener){
        super(mContext,mActivity);
        this.listener=listener;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        this.initStringsItems(strings);
        boolean isCorrect = false;
        Log.d(TAG, "Sending Put data for "+ super.getFirstRequestName());
        String  resultStr = HttpRequestsURLConnection.SendHttpPut(baseURL + "/"+ super.getFirstRequestName(), super.getJsonBodyString(jsonBodyArrayForUser));
        if (super.isValidJsonResult(resultStr) && (resultStr.equals("Empty"))) {
            resultStr = HttpRequestsURLConnection.SendHttpPost(baseURL + "/"+ mContext.getString(R.string.user_request_id), "id="+this.getId());
            if (super.isValidJsonResult(resultStr)){
                super.setFirstJSONResult(resultStr);
                isCorrect = true;
            }
        }
        return isCorrect;
    }


     /*
         The First arg is user update request name
         The Second arg is entered id for json
         The Third arg is entered first name for json
         The Forth arg is entered last name for json
         The Fifth arg is entered phone for json
         The Sixth arg is entered address for json
         The Seventh arg is the putString first value
    */
    private void initStringsItems(String[] strings) {
        jsonBodyArrayForUser = new ArrayList<String>();
        super.setFirstRequestName( strings[0]);

        this.setId(strings[1]);
        this.jsonBodyArrayForUser.add("id=" +this.getId());

        this.setFirstName(strings[2]);
        this.jsonBodyArrayForUser.add("fname=" + this.getFirstName());

        this.setLastName(strings[3]);
        this.jsonBodyArrayForUser.add("lname=" + this.getLastName());

        this.setPhone(strings[4]);
        this.jsonBodyArrayForUser.add("phone=" + this.getPhone());

        this.setAddress(strings[5]);
        this.jsonBodyArrayForUser.add("address=" + this.getAddress());

        this.setCity(strings[6]);
        this.jsonBodyArrayForUser.add("city=" + this.getCity());

        this.setFirstPutString(strings[7]);
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            mActivity.getIntent().putExtra(getFirstPutString(),super.setStringToJsonFormat(getFirstJSONResult()));
            listener.onTaskCompleted(success);
        }else {
            listener.onTaskCompleted(success);
        }
    }

    private String getId() {
        return mId;
    }

    private void setId(String mId) {
        this.mId = mId;
    }

    private String getFirstName() {
        return mFirstName;
    }

    private void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    private String getLastName() {
        return mLastName;
    }

    private void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    private String getPhone() {
        return mPhone;
    }

    private void setPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    private String getAddress() {
        return mAddress;
    }

    private void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    private String getCity() {
        return mCity;
    }

    private void setCity(String mCity) {
        this.mCity = mCity;
    }
}