package com.pet.att.pickapet.AppActivities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.pet.att.pickapet.HTTP.AddNewUserTask;
import com.pet.att.pickapet.R;

public class AddNewUserActivity extends AppCompatActivity {

    private static final String TAG = "AddNewUserActivity";
    private EditText mId;
    private EditText mFirstName;
    private EditText mLastName;
    private AutoCompleteTextView mEmail;
    private EditText mPhone;
    private EditText mAddress;
    private EditText mCity;
    private EditText mPassword;
    private EditText mPasswordConfirm;
    protected Context mContext;
    protected Spinner mAreaCodeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext =this;

        String[] arraySpinner = new String[] {" 05 ", " 02 "," 03 ", " 04 "," 08 ", " 09 "};
        mAreaCodeSpinner = (Spinner) findViewById(R.id.user_area_code);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAreaCodeSpinner.setAdapter(adapter);
        mAreaCodeSpinner.setSelection(0);

        mId = findViewById(R.id.user_text_id);
        mFirstName = findViewById(R.id.user_text_fname);
        mLastName = findViewById(R.id.user_text_lname);
        mEmail = findViewById(R.id.user_text_email);
        mPhone = findViewById(R.id.user_text_phone);
        mAddress = findViewById(R.id.user_text_address);
        mCity = findViewById(R.id.user_add_city);
        mPassword = findViewById(R.id.user_add_password);
        mPasswordConfirm = findViewById(R.id.user_add_password_confim);
        View focusView = mId;
        focusView.requestFocus();

        Button mRegistrationButton = (Button) findViewById(R.id.user_add_all_button);
        mRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mAreaCodeText= mAreaCodeSpinner.getSelectedItem().toString().trim();
                if(attemptLogin()) {
                    new AddNewUserTask(AddNewUserActivity.this, mContext).execute(
                            getString(R.string.user_request),
                            getString(R.string.user_request_login),
                            mId.getText().toString(),
                            mFirstName.getText().toString(),
                            mLastName.getText().toString(),
                            mEmail.getText().toString(),
                            mAreaCodeText + mPhone.getText().toString(),
                            mAddress.getText().toString(),
                            mCity.getText().toString(),
                            mPassword.getText().toString(),
                            mContext.getString(R.string.current_user_details_json),
                            mContext.getString(R.string.current_user_login_json)
                    );
                }
            }
        });
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
        }else if (!isValidId(id)){
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

        mEmail.setError(null);
        String email = mEmail.getText().toString();

        if (TextUtils.isEmpty(email)){
            mEmail.setError(getString(R.string.error_field_required));
            focusView = mEmail;
            focusView.requestFocus();
            return  false;
        }else if (!isEmailValid(email)){
            mEmail.setError(getString(R.string.error_invalid_email));
            focusView = mEmail;
            focusView.requestFocus();
            return  false;
        }

        mPassword.setError(null);
        String password = mPassword.getText().toString();

        if (TextUtils.isEmpty(password)){
            mPassword.setError(getString(R.string.error_field_required));
            focusView = mPassword;
            focusView.requestFocus();
            return  false;
        }else if (!isPasswordValid(password)){
            mPassword.setError(getString(R.string.error_incorrect_password));
            focusView = mPassword;
            focusView.requestFocus();
            return false;
        }

        mPasswordConfirm.setError(null);
        String passwordConfirm = mPasswordConfirm.getText().toString();

        if (TextUtils.isEmpty(passwordConfirm)){
            mPasswordConfirm.setError(getString(R.string.error_field_required));
            focusView = mPasswordConfirm;
            focusView.requestFocus();
            return false;
        }else if (!isPasswordsValid(password,passwordConfirm)){
            mPassword.setError(getString(R.string.error_incorrect_password_match));
            focusView = mPassword;
            focusView.requestFocus();
            return false;
        }

        return  true;
    }


    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidId(String id) {
        int sum, checkDigit;
        if(id.length()==9) {
            sum = idSummary(id);
            checkDigit = Character.getNumericValue(id.charAt(8));
            return ((sum+checkDigit)%10==0) ? true : false;
        }
        else{
            return false;
        }
    }

    public static int idSummary(String id) {
        String bigNumber;
        int sum=0, num;
        for(int i=0; i<8; i++){
            if(i%2==0){
                num = Character.getNumericValue(id.charAt(i));
                sum = sum+num;
            }
            else{
                num = Character.getNumericValue(id.charAt(i));
                if((num*2)>=10){
                    bigNumber = Integer.toString(num*2);
                    for(int j=0; j<2; j++){
                        num = Character.getNumericValue(bigNumber.charAt(j));
                        sum = sum+num;
                    }
                }else
                    sum = sum+(num*2);
            }
        }
        return sum;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private boolean isPasswordsValid(String password, String passwordConfirm) {
        return password.equals(passwordConfirm);
    }

    private boolean isPhoneValid(String phone) {
        if (mAreaCodeSpinner.getSelectedItem().toString().equals(" 05 ")) {
            return (phone.length() == 8);
        }else{
            return ( phone.length() == 7 &&  phone.charAt(0)!= '0' );
        }
    }
}
