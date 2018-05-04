package com.pet.att.pickapet.AppActivities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.pet.att.pickapet.AuxiliaryClasses.OnTaskCompleted;
import com.pet.att.pickapet.HTTP.UpdateUserDetailsTask;
import com.pet.att.pickapet.R;

import org.json.JSONException;
import org.json.JSONObject;

public class EditUserDetailsActivity extends AppCompatActivity {

    private String mCurrentUserJson;
    private Context mContext;
    private Activity mParentActivity;
    private Spinner mAreaCodeSpinner;
    private TextView mId;
    private TextView mFirstName;
    private TextView mLastName;
    private TextView mPhone;
    private TextView mAddress;
    private TextView mCity;
    private Button mButton;
    private Intent mParentActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_edit_user_details);
        mCurrentUserJson = getIntent().getStringExtra(getString(R.string.current_user_details_json));
//        mParentActivityIntent = mContext.
        initViews();
        mButton = findViewById(R.id.user_update_all_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUserDetailsTask updateUserDetailsTask = new UpdateUserDetailsTask(EditUserDetailsActivity.this, mContext, new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted() {
                        String mUpdatedUserJson = getIntent().getStringExtra(getString(R.string.current_user_details_json));
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(getString(R.string.current_user_details_result_json),mUpdatedUserJson);
                        setResult(Activity.RESULT_OK,returnIntent);

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setMessage(mContext.getString(R.string.success_update_data))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        finish();
                    }

                    @Override
                    public void onTaskCompleted(String result) {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
                        builder.setMessage(result)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                        android.app.AlertDialog alert = builder.create();
                        alert.show();
                    }

                    @Override
                    public void onTaskCompleted(Boolean result) {
                        if(result){
                            onTaskCompleted();
                        }else{
                            onTaskCompleted(mContext.getString(R.string.prompt_invalid_data));
                        }
                    }
                });
                updateUserDetailsTask
                        .execute(mContext.getString(R.string.user_request_update),
                                mId.getText().toString(),
                                mFirstName.getText().toString(),
                                mLastName.getText().toString(),
                                mAreaCodeSpinner.getSelectedItem().toString() + mPhone.getText().toString(),
                                mAddress.getText().toString(),
                                mCity.getText().toString(),
                                getString(R.string.current_user_details_json));
            }
        });
    }

    private void initViews(){
        mContext =this;
        String[] arraySpinner = new String[] {"05", "02","03", "04","08", "09"};
        mAreaCodeSpinner = (Spinner) findViewById(R.id.user_area_code);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAreaCodeSpinner.setAdapter(adapter);
        try {
            JSONObject mCurrentUserDataJson = new JSONObject(mCurrentUserJson.trim());
            mId = findViewById(R.id.user_text_id);
            mId.setText(mCurrentUserDataJson.getString("id"));

            mFirstName = findViewById(R.id.user_text_fname);
            mFirstName.setText(mCurrentUserDataJson.getString("fname"));

            mLastName = findViewById(R.id.user_text_lname);
            mLastName.setText(mCurrentUserDataJson.getString("lname"));

            mPhone = findViewById(R.id.user_text_phone);
            String phone = mCurrentUserDataJson.getString("phone");
            mPhone.setText(phone.substring(2,phone.length()));
            mAreaCodeSpinner.setSelection(getIndexOfStringInArray(arraySpinner,phone.substring(0,2)));

            mAddress = findViewById(R.id.user_text_address);
            mAddress.setText(mCurrentUserDataJson.getString("address"));

            mCity = findViewById(R.id.user_add_city);
            mCity.setText(mCurrentUserDataJson.getString("city"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int getIndexOfStringInArray (String[] arraySpinner,String value){
        boolean isFound=false;
        int index=0;
        while (index<arraySpinner.length && !isFound ){
            if (arraySpinner[index].equals(value)){
                isFound=true;
            }else{
                index++;
            }
        }
        return index;
    }

    private boolean attemptLogin() {
        View focusView = null;
        mId.setError(null);
        String id = mId.getText().toString();

        if (TextUtils.isEmpty(id)){
            mId.setError(getString(R.string.error_field_required));
            focusView = mId;
            focusView.requestFocus();
            return false;
        }else if (!isIdValid(id)){
            mId.setError(getString(R.string.error_invalid_id));
            focusView = mId;
            focusView.requestFocus();
            return false;
        }

        mFirstName.setError(null);
        String firstName = mFirstName.getText().toString();

        if (TextUtils.isEmpty(firstName)){
            mFirstName.setError(getString(R.string.error_field_required));
            focusView = mFirstName;
            focusView.requestFocus();
            return false;
        }

        mLastName.setError(null);
        String lastName = mLastName.getText().toString();

        if (TextUtils.isEmpty(lastName)){
            mLastName.setError(getString(R.string.error_field_required));
            focusView = mLastName;
            focusView.requestFocus();
            return false;
        }

        mPhone.setError(null);
        String phone = mPhone.getText().toString();

        if (TextUtils.isEmpty(phone)){
            mPhone.setError(getString(R.string.error_field_required));
            focusView = mPhone;
            focusView.requestFocus();
            return  false;
        }else if (!isPhoneValid(phone)){
            mPhone.setError(getString(R.string.error_invalid_phone));
            focusView = mPhone;
            focusView.requestFocus();
            return false;
        }

        mAddress.setError(null);
        String address = mAddress.getText().toString();

        if (TextUtils.isEmpty(address)){
            mAddress.setError(getString(R.string.error_field_required));
            focusView = mAddress;
            focusView.requestFocus();
            return  false;
        }

        mCity.setError(null);
        String city = mCity.getText().toString();

        if (TextUtils.isEmpty(city)){
            mCity.setError(getString(R.string.error_field_required));
            focusView = mCity;
            focusView.requestFocus();
            return false;
        }

        return  true;
    }


    private boolean isIdValid(String id) {
        return id.length() == 9;
    }


    private boolean isPhoneValid(String phone) {
        if (mAreaCodeSpinner.getSelectedItem().toString().equals("05")) {
            if (phone.length() == 8 && phone.charAt(0) != '0')
                return true;
            else
                return false;
        }else{
            return ( phone.length() == 7 &&  phone.charAt(0)!= '0' );
        }
    }



}
