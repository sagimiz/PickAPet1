package com.pet.att.pickapet.AuxiliaryClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.pet.att.pickapet.HTTP.GetAllTypeTask;
import com.pet.att.pickapet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class SpinnerDialog extends Dialog {
    private static final String TAG = "SpinnerDialog";
    private final String mKindJson;
    private Context mContext;
    private Spinner mGenderSpinner;
    private Spinner mKindSpinner;
    private Activity mActivity;
    private Spinner mTypeSpinner;
    private ArrayList<String> mGenderArray=null;
    private String[][] mKindArray=null;
    private String[][] mTypeArray=null;


    public interface DialogListener {
        void ready(String mGender,String mKind,String mType);
        void cancelled();
    }

    private DialogListener mReadyListener;

    public SpinnerDialog(Context context, Activity mActivity, String mKindJson, DialogListener readyListener) {
        super(context);
        mReadyListener = readyListener;
        this.mContext = context;
        this.mActivity=mActivity;
        this.mKindJson = mKindJson;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_dialog);
        this.initSpinners();
        try {
            mKindSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (!mKindSpinner.getSelectedItem().toString().equals("הכל")) {
                        GetAllTypeTask getAllTypeTask = new GetAllTypeTask((AppCompatActivity) mActivity, mContext, new OnTaskCompleted() {
                            @Override
                            public void onTaskCompleted() {
                                String mTypeJson = mActivity.getIntent().getStringExtra(mContext.getString(R.string.all_type_json));
                                mTypeArray = getArrayFromJSON(mTypeJson, "type_name", "type_id");
                                ArrayAdapter<String> mTypeAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, getArrayListNameValueFromArray(mTypeArray));
                                mTypeSpinner.setAdapter(mTypeAdapter);
                                mTypeSpinner.setEnabled(true);
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
                        getAllTypeTask.execute(mContext.getString(R.string.animal_type_request), mKindArray[position][1], mContext.getString(R.string.all_type_json));
                    } else {
                        mTypeSpinner.setEnabled(false);
                        mTypeSpinner.setSelection(0);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {  }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG,e.toString());
        }

        Button buttonOK = findViewById(R.id.dialogOK);
        Button buttonCancel = findViewById(R.id.dialogCancel);
        buttonOK.setOnClickListener(new android.view.View.OnClickListener(){
            public void onClick(View v) {
                int mGenderPosition = mGenderSpinner.getSelectedItemPosition();
                int mKindPosition = mKindSpinner.getSelectedItemPosition();
                int mTypePosition = mTypeSpinner.getSelectedItemPosition();
                String mGender  = (mGenderArray.get(mGenderPosition).equals("הכל")) ? "" : mGenderArray.get(mGenderPosition);
                String mKind  = (mKindArray[mKindPosition][1].equals("הכל")) ? "": mKindArray[mKindPosition][1];
                String mType;
                if (mTypeArray != null)
                    mType = (mTypeArray[mTypePosition][1].equals("הכל")) ? "" : mTypeArray[mTypePosition][1];
                else
                    mType ="";
                mReadyListener.ready(mGender,mKind,mType);
                SpinnerDialog.this.dismiss();
            }
        });
        buttonCancel.setOnClickListener(new android.view.View.OnClickListener(){
            public void onClick(View v) {
                mReadyListener.cancelled();
                SpinnerDialog.this.dismiss();
            }
        });
    }

    private String[][] getArrayFromJSON (String JSONString,String nameValue,String keyValue){
        JSONArray jsonArray = null;
        String[][] array=null;
        try {
            jsonArray = new JSONArray(JSONString);
            int size = jsonArray.length();
            array = new String[size+1][size+1];
            array[0][0]="הכל";
            array[0][1]="";
            for(int i=1; i<size+1;i++){
                String jsonStr = jsonArray.get(i-1).toString();
                JSONObject jsonObject = new JSONObject(jsonStr );
                array[i][0]=(jsonObject.getString(nameValue));
                array[i][1]=(jsonObject.getString(keyValue));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }

    private ArrayList<String> getArrayListNameValueFromArray (String[][] array) {
        ArrayList<String>  arrayList= new ArrayList<String>();
        for(int i=0; i<array.length;i++){
            arrayList.add(array[i][0]);
        }
        return arrayList;
    }

    private void initSpinners(){

        mGenderSpinner = (Spinner) findViewById (R.id.dialog_spinner_gender);
        mGenderArray = new ArrayList<String>();
        mGenderArray.add("הכל");
        mGenderArray.add("זכר");
        mGenderArray.add("נקבה");
        ArrayAdapter<String> mGenderAdapter = new ArrayAdapter<String> (mContext, android.R.layout.simple_spinner_dropdown_item,mGenderArray);
        mGenderSpinner.setAdapter(mGenderAdapter);


        mKindSpinner = findViewById (R.id.dialog_spinner_kind);
        mKindArray = this.getArrayFromJSON(this.mKindJson,"kind","kind_id");
        ArrayAdapter<String> mKindAdapter = new ArrayAdapter<String> (mContext, android.R.layout.simple_spinner_dropdown_item, this.getArrayListNameValueFromArray(mKindArray));
        mKindSpinner.setAdapter(mKindAdapter);

        mTypeSpinner = findViewById(R.id.dialog_spinner_type);
        ArrayList<String> mTypeAll = new ArrayList<String>();
        mTypeAll.add("הכל");
        ArrayAdapter<String> mTypeAdapter = new ArrayAdapter<String> (mContext, android.R.layout.simple_spinner_dropdown_item,mTypeAll);
        mTypeSpinner.setAdapter(mTypeAdapter);
    }
}