package com.pet.att.pickapet.AuxiliaryClasses;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.pet.att.pickapet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SpinnerDialog extends Dialog {
    private final String mKindJson;
    private final String mTypeJson;
    private Context mContext;
    private Spinner mGenderSpinner;
    private Spinner mKindSpinner;
    private Spinner mTypeSpinner;
    private ArrayList<String> mGenderArray=null;
    private String[][] mKindArray=null;
    private String[][] mTypeArray=null;

    public interface DialogListener {
        public void ready(String mGender,String mKind,String mType);
        public void cancelled();
    }

    private DialogListener mReadyListener;

    public SpinnerDialog(Context context,String mKindJson,String mTypeJson, DialogListener readyListener) {
        super(context);
        mReadyListener = readyListener;
        mContext = context;
        this.mKindJson = mKindJson;
        this.mTypeJson = mTypeJson;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.filter_dialog);
        mGenderSpinner = (Spinner) findViewById (R.id.dialog_spinner_gender);
        mGenderArray = new ArrayList<String>();
        mGenderArray.add("");
        mGenderArray.add("זכר");
        mGenderArray.add("נקבה");
        ArrayAdapter<String> mGenderAdapter = new ArrayAdapter<String> (mContext, android.R.layout.simple_spinner_dropdown_item,mGenderArray);
        mGenderSpinner.setAdapter(mGenderAdapter);


        mKindSpinner = (Spinner) findViewById (R.id.dialog_spinner_kind);
        mKindArray = this.getArrayFromJSON(this.mKindJson,"kind","kind_id");
        ArrayAdapter<String> mKindAdapter = new ArrayAdapter<String> (mContext, android.R.layout.simple_spinner_dropdown_item, this.getArrayListNameValueFromArray(mKindArray));
        mKindSpinner.setAdapter(mKindAdapter);



        mTypeSpinner = (Spinner) findViewById (R.id.dialog_spinner_type);
        mTypeArray = this.getArrayFromJSON(this.mTypeJson,"type_name","type_id");
        ArrayAdapter<String> mTypeAdapter = new ArrayAdapter<String> (mContext, android.R.layout.simple_spinner_dropdown_item, this.getArrayListNameValueFromArray(mTypeArray));
        mTypeSpinner.setAdapter(mTypeAdapter);

        Button buttonOK = (Button) findViewById(R.id.dialogOK);
        Button buttonCancel = (Button) findViewById(R.id.dialogCancel);
        buttonOK.setOnClickListener(new android.view.View.OnClickListener(){
            public void onClick(View v) {
                int mGenderPosition = mGenderSpinner.getSelectedItemPosition();
                int mKindPosition = mKindSpinner.getSelectedItemPosition();
                int mTypePosition = mTypeSpinner.getSelectedItemPosition();
                String mGender  = mGenderArray.get(mGenderPosition);
                String mKind  = mKindArray[mKindPosition][1];
                String mType  = mTypeArray[mTypePosition][1];
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
            array[0][0]="";
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

}