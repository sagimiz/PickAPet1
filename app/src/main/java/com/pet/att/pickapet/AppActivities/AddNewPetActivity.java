package com.pet.att.pickapet.AppActivities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pet.att.pickapet.AuxiliaryClasses.FileUtil;
import com.pet.att.pickapet.AuxiliaryClasses.OnTaskCompleted;
import com.pet.att.pickapet.HTTP.AddNewPetTask;
import com.pet.att.pickapet.HTTP.GetAllKindsTask;
import com.pet.att.pickapet.HTTP.GetAllTypeTask;
import com.pet.att.pickapet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import id.zelory.compressor.Compressor;

public class AddNewPetActivity extends AppCompatActivity{
    private static final int PICK_IMAGE=100;
    private static final String TAG ="AddNewPetActivity" ;
    private static final int QUALITY =35 ;
    private static final int MAX_WIDTH = 640;
    private static final int MAX_HEIGHT = 480;
    private ImageView actualImageView=null;
    private boolean mImageSelected=false;
    private File actualImage;
    private File compressedImage;
    private String mTypeStringJson;
    private String mKindStringJson;
    private Context mContext;
    private String mUserJsonString;
    private String mOwnerId;
    private EditText mAnimalName;
    private Spinner mAnimalTypeSpinner;
    private Spinner mAnimalKindSpinner;
    private int mTypeSpinnerPosition =0;
    private int mKindSpinnerPosition =0;
    private Spinner mGenderSpinner;
    private TextView mBirthDateText;
    private TextView mDescriptionText;
    private Button imgButton;
    private Calendar myCalendar;
    private String [] mTypeNameArray;
    private String [] mTypeIdArray;
    private String [] mKindNameArray;
    private String [] mKindIdArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_pet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.initItems();
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStoragePermissionGranted()){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICK_IMAGE);
                }
            }
        });

        mAnimalKindSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                mKindSpinnerPosition =position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {   }
        });

        String[] arraySpinner = new String[] {"זכר", "נקבה"};
        mGenderSpinner = (Spinner) findViewById(R.id.animal_gender_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGenderSpinner.setAdapter(adapter);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "yyyy/MM/dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                mBirthDateText.setText(sdf.format(myCalendar.getTime()));
            }
        };

        mBirthDateText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(mContext, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mBirthDateText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    new DatePickerDialog(mContext, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });

        Button addButton =  findViewById(R.id.animal_add_all);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attemptPutData()){
                    String mFirstRequest = mContext.getString(R.string.animals_request);
                    String mSecondRequest = mContext.getString(R.string.animals_owner_request_id);
                    String mThirdRequest = mContext.getString(R.string.animals_pic_request);
                    String mAnimalGender = mGenderSpinner.getSelectedItem().toString();
                    String mAnimalBirthDate =mBirthDateText.getText().toString();
                    String mAnimalDescription =mDescriptionText.getText().toString();
                    new AddNewPetTask(AddNewPetActivity.this, mContext).execute(
                            mFirstRequest,
                            mSecondRequest,
                            mThirdRequest,
                            mOwnerId,
                            mAnimalName.getText().toString(),
                            mTypeIdArray[mTypeSpinnerPosition],
                            mAnimalBirthDate,
                            mAnimalGender,
                            mKindIdArray[mKindSpinnerPosition],
                            compressedImage,
                            mAnimalDescription
                    );
                }
            }
        });
    }

    public void initItems(){
        mContext = this;
        mAnimalTypeSpinner = findViewById(R.id.animal_type_spinner);
        mAnimalKindSpinner = findViewById(R.id.animal_kind_spinner);
        mKindStringJson = getIntent().getStringExtra(getString(R.string.all_kind_json));
        if (mKindStringJson!=null){
            GetAllKindsTask getAllKindsTask = new GetAllKindsTask(AddNewPetActivity.this,mContext, new OnTaskCompleted() {
                @Override
                public void onTaskCompleted() {
                    mKindStringJson = getIntent().getStringExtra(getString(R.string.all_kind_json));
                    setSpinnerWithAnimalsKinds();
                }

                @Override
                public void onTaskCompleted(String result) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage(result)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

                @Override
                public void onTaskCompleted(Boolean result) {
                    if(result){
                        onTaskCompleted();
                    }else{
                        String resultStr = getString(R.string.dialog_error_text);
                        onTaskCompleted(resultStr);
                    }
                }
            });
            getAllKindsTask.execute(getString(R.string.animal_kind_request),getString(R.string.all_kind_json));
        }else {
            this.setSpinnerWithAnimalsKinds();
        }

        imgButton = findViewById(R.id.add_image_button);
        actualImageView = findViewById(R.id.add_pet_image);
        mAnimalName = findViewById(R.id.animal_text_name);

        View focusView;
        focusView = mAnimalName;
        focusView.requestFocus();

        mDescriptionText = findViewById(R.id.animal_text_description);
        myCalendar = Calendar.getInstance();
        mBirthDateText = findViewById(R.id.animal_text_birth_date);
        Bundle bundle = getIntent().getExtras();
        mUserJsonString = bundle.getString(mContext.getString(R.string.current_user_details_json));

        Log.d(TAG,"The user json is " + mUserJsonString );
        try {
            JSONObject jsonObject = new JSONObject(mUserJsonString);
            mOwnerId = jsonObject.getString("id");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean attemptPutData() {
        View focusView;
        mAnimalName.setError(null);
        String name = mAnimalName.getText().toString();

        if (TextUtils.isEmpty(name)) {
            mAnimalName.setError(getString(R.string.error_field_required));
            focusView = mAnimalName;
            focusView.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(name.trim())) {
            mAnimalName.setError(getString(R.string.error_invalid_name));
            focusView = mAnimalName;
            focusView.requestFocus();
            return false;
        }

        mDescriptionText.setError(null);
        String description = mDescriptionText.getText().toString();

        if (TextUtils.isEmpty(description)) {
            mDescriptionText.setError(getString(R.string.error_field_required));
            focusView = mDescriptionText;
            focusView.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(description.trim())) {
            mDescriptionText.setError(getString(R.string.error_invalid_description1));
            focusView = mDescriptionText;
            focusView.requestFocus();
            return false;
        }
        if (description.length()>70) {
            mDescriptionText.setError(getString(R.string.error_invalid_description2));
            focusView = mDescriptionText;
            focusView.requestFocus();
            return false;
        }

        mBirthDateText.setError(null);
        String birthDate = mBirthDateText.getText().toString();

        if (TextUtils.isEmpty(birthDate)) {
            mBirthDateText.setError(getString(R.string.error_field_required));
            focusView = mBirthDateText;
            focusView.requestFocus();
            return false;
        }
        if (!isDateValid(birthDate)) {
            mBirthDateText.setError(getString(R.string.error_invalid_birth_date));
            focusView = mBirthDateText;
            focusView.requestFocus();
            return false;
        }

        if (!mImageSelected){
            TextView mNoImage = findViewById(R.id.no_image_error);
            mNoImage.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

    private void setSpinnerWithAnimalsTypes() {
        mTypeStringJson = getIntent().getStringExtra(getString(R.string.all_type_json));
        try {
            JSONArray jsonArray = new JSONArray(mTypeStringJson);
            mTypeNameArray = new String[jsonArray.length()];
            mTypeIdArray = new String[jsonArray.length()];
            for (int i=0; i<jsonArray.length();i++){
                String typeStr = jsonArray.get(i).toString();
                JSONObject jsonObject = new JSONObject(typeStr );
                mTypeNameArray[i]= jsonObject.getString("type_name");
                mTypeIdArray[i] = jsonObject.getString("type_id");
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, mTypeNameArray);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mAnimalTypeSpinner.setAdapter(arrayAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setSpinnerWithAnimalsKinds() {
        try {
            JSONArray jsonArray = new JSONArray(mKindStringJson);
            mKindNameArray = new String[jsonArray.length()];
            mKindIdArray = new String[jsonArray.length()];
            for (int i=0; i<jsonArray.length();i++){
                String kindStr = jsonArray.get(i).toString();
                JSONObject jsonObject = new JSONObject(kindStr );
                mKindNameArray[i]= jsonObject.getString("kind");
                mKindIdArray[i] = jsonObject.getString("kind_id");
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, mKindNameArray);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mAnimalKindSpinner.setAdapter(arrayAdapter);
            mAnimalKindSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!mAnimalKindSpinner.getSelectedItem().toString().equals("")) {
                        GetAllTypeTask getAllTypeTask = new GetAllTypeTask(AddNewPetActivity.this, mContext, new OnTaskCompleted() {

                            @Override
                            public void onTaskCompleted() {
                                mTypeStringJson = getIntent().getStringExtra(mContext.getString(R.string.all_type_json));
                                setSpinnerWithAnimalsTypes();
                            }

                            @Override
                            public void onTaskCompleted(String result) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setMessage(result)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }

                            @Override
                            public void onTaskCompleted(Boolean result) {
                                if (result) {
                                    onTaskCompleted();
                                } else {
                                    String resultStr = mContext.getString(R.string.dialog_error_text);
                                    onTaskCompleted(resultStr);
                                }
                            }
                        });
                        getAllTypeTask.execute(mContext.getString(R.string.animal_type_request), mKindIdArray[position], mContext.getString(R.string.all_type_json));
                    }else{
                        mAnimalTypeSpinner.setClickable(false);
                        mAnimalTypeSpinner.setVerticalScrollbarPosition(0);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_CANCELED) return;
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            try {
                actualImage = FileUtil.from(this, data.getData());
                customCompressImage(MAX_WIDTH, MAX_HEIGHT,QUALITY);
                mImageSelected=true;
            } catch (IOException e) {
                e.printStackTrace();
                showError("fail to read picture data");
            }
        }
    }

    public void customCompressImage(int maxWidth,int maxHeight,int quality) {
        if (actualImage == null) {
            showError("Please choose an image!");
        } else {
            // Compress image in main thread using custom Compressor
            try {
                compressedImage = new Compressor(this)
                        .setMaxWidth(maxWidth)
                        .setMaxHeight(maxHeight)
                        .setQuality(quality)
                        .setCompressFormat(Bitmap.CompressFormat.WEBP)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES).getAbsolutePath())
                        .compressToFile(actualImage);

                setCompressedImage();
            } catch (IOException e) {
                e.printStackTrace();
                showError(e.getMessage());
            }
        }
    }

    private void setCompressedImage() {
        Bitmap finalImage =BitmapFactory.decodeFile(compressedImage.getAbsolutePath());
        actualImageView.setImageBitmap(finalImage);
        Log.d("Compressor", "Compressed image save in " + compressedImage.getPath());
        Log.d("Compressor", "image size is " +finalImage.getByteCount());
    }

    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private boolean isDateValid(String date) {
        try {
            Date current = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
            Date givenDate = df.parse(date);
            if(givenDate.after(current))
                return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
        }
    }


}


