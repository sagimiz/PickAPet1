package com.pet.att.pickapet.AppActivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.pet.att.pickapet.AuxiliaryClasses.FileUtil;
import com.pet.att.pickapet.HTTP.AddNewPetTask;
import com.pet.att.pickapet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import id.zelory.compressor.Compressor;

public class AddNewPetActivity extends AppCompatActivity {
    private static final int PICK_IMAGE=100;
    private static final String TAG ="AddNewPetActivity" ;
    private static final int QUALITY =35 ;
    private static final int MAX_WIDTH = 640;
    private static final int MAX_HEIGHT = 480;
    private static final int MAX_YEAR = 16;
    private static final int MAX_MONTH = 12;
    private static final int MAX_DAY = 31;
    private ImageView actualImageView;
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
    private int mYearSpinnerPosition=0;
    private int mMonthSpinnerPosition=0;
    private int mDaySpinnerPosition=0;
    private Spinner mGenderSpiner;
    private Spinner mAnimalBirthDateYear;
    private Spinner mAnimalBirthDateDay;
    private Spinner mAnimalBirthDateMonth;

    Button imgButton;

    String [] mTypeNameArray;
    String [] mTypeIdArray;
    String [] mKindNameArray;
    String [] mKindIdArray;
    String[] arrayYearSpinner;
    String[] arrayMonthSpinner;
    String[] arrayDaySpinner;

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
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });



        mAnimalTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(getBaseContext(), mTypeNameArray[position], Toast.LENGTH_SHORT).show();
                mTypeSpinnerPosition =position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


        mAnimalKindSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(getBaseContext(), mKindNameArray[position], Toast.LENGTH_SHORT).show();
                mKindSpinnerPosition =position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        String[] arraySpinner = new String[] {"זכר", "נקבה"};
        mGenderSpiner = (Spinner) findViewById(R.id.animal_gender_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGenderSpiner.setAdapter(adapter);


        mAnimalBirthDateYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
                mYearSpinnerPosition =position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


        mAnimalBirthDateMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
                mMonthSpinnerPosition =position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        mAnimalBirthDateDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
                mDaySpinnerPosition =position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });





        Button addButton = (Button) findViewById(R.id.animal_add_all);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attemptPutData()){
                    String mFirstRequest = mContext.getString(R.string.animals_request);
                    String mSecondRequest = mContext.getString(R.string.animals_owner_request_id);
                    String mThirdRequest = mContext.getString(R.string.animals_pic_request);

                    String animalGender = mGenderSpiner.getSelectedItem().toString();
                    String animalBirthDate = arrayYearSpinner[mYearSpinnerPosition] +"-" +
                                                arrayMonthSpinner[mMonthSpinnerPosition] +"-" +
                                                    arrayDaySpinner[mDaySpinnerPosition] ;

                    new AddNewPetTask(AddNewPetActivity.this, mContext).execute(
                            mFirstRequest,
                            mSecondRequest,
                            mThirdRequest,
                            mOwnerId,
                            mAnimalName.getText().toString(),
                            mTypeIdArray[mTypeSpinnerPosition],
                            animalBirthDate,
                            animalGender,
                            mKindIdArray[mKindSpinnerPosition],
                            compressedImage
                    );
                }
            }
        });

    }

    public void initItems(){
        mContext = this;
        mAnimalTypeSpinner = (Spinner) (findViewById(R.id.animal_type_spinner));
        mAnimalKindSpinner = (Spinner) (findViewById(R.id.animal_kind_spinner));
        this.setSpinnerWithAnimalsTypes();
        this.setSpinnerWithAnimalsKinds();

        imgButton = (Button) findViewById(R.id.add_image_button);
        actualImageView = (ImageView) findViewById(R.id.add_pet_image);
        mAnimalName = (EditText)  findViewById(R.id.animal_text_name);
        mAnimalBirthDateYear = (Spinner) findViewById(R.id.animal_birth_date_year);

        arrayYearSpinner = new String[MAX_YEAR];
        for(int i = 0; i<arrayYearSpinner.length;i++){
            Calendar prevYear = Calendar.getInstance();
            prevYear.add(Calendar.YEAR, -i);
            arrayYearSpinner[i] = String.valueOf(prevYear.get(Calendar.YEAR));
        }
        mAnimalBirthDateYear = (Spinner) findViewById(R.id.animal_birth_date_year);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayYearSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAnimalBirthDateYear.setAdapter(adapter);


        arrayMonthSpinner = new String[MAX_MONTH];
        for(int i = 0; i<arrayMonthSpinner.length;i++){
            arrayMonthSpinner[i] = String.valueOf(i+1);
        }
        mAnimalBirthDateMonth = (Spinner) findViewById(R.id.animal_birth_date_month);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayMonthSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAnimalBirthDateMonth.setAdapter(adapter);


        arrayDaySpinner = new String[MAX_DAY];
        for(int i = 0; i<arrayDaySpinner.length;i++){
            arrayDaySpinner[i] = String.valueOf(i+1);
        }
        mAnimalBirthDateDay = (Spinner) findViewById(R.id.animal_birth_date_day);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayDaySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAnimalBirthDateDay.setAdapter(adapter);

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
        // Reset errors.
        View focusView = null;
        mAnimalName.setError(null);
        String name = mAnimalName.getText().toString();

        if (TextUtils.isEmpty(name)) {
            mAnimalName.setError(getString(R.string.error_field_required));
            focusView = mAnimalName;
            focusView.requestFocus();
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
        mKindStringJson = getIntent().getStringExtra(getString(R.string.all_kind_json));
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
            } catch (IOException e) {
                e.printStackTrace();
                showError("fail to read picture data");
            }
        }
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
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
        finalImage = getResizedBitmap(finalImage,actualImageView.getHeight(),actualImageView.getWidth());
        actualImageView.setImageBitmap(finalImage);
        Log.d("Compressor", "Compressed image save in " + compressedImage.getPath());
        Log.d("Compressor", "image size is " +finalImage.getByteCount());
    }

    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}


