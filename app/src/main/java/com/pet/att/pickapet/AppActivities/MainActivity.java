package com.pet.att.pickapet.AppActivities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pet.att.pickapet.AuxiliaryClasses.AnimalsPics;
import com.pet.att.pickapet.AuxiliaryClasses.CustomAdapter;
import com.pet.att.pickapet.AuxiliaryClasses.OnTaskCompleted;
import com.pet.att.pickapet.AuxiliaryClasses.RecyclerViewFragment;
import com.pet.att.pickapet.AuxiliaryClasses.SpinnerDialog;
import com.pet.att.pickapet.HTTP.GetAnimalPreLoadPageTask;
import com.pet.att.pickapet.HTTP.GetPetsDetailsTask;
import com.pet.att.pickapet.HTTP.UserActiveAnimalsTask;
import com.pet.att.pickapet.HTTP.PetsImagesTask;
import com.pet.att.pickapet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private String mCurrentUserJsonData;
    private Context mContext;
    private String [] mAllUserAnimalsName;
    protected String currentUserId;
    private String [] mAllUserAnimalsId;
    protected  ProgressDialog mDialog;
    protected FragmentTransaction transaction;
    protected RecyclerViewFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext=this;
        mCurrentUserJsonData =this.getIntent().getStringExtra(getString(R.string.current_user_details_json));

        if (savedInstanceState == null) {
            getPetsImageTask(false,"","","");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetPetsDetailsTask getPetsDetailsTask = new GetPetsDetailsTask(MainActivity.this, mContext, new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted() {
                        String mKindJson = getIntent().getStringExtra(getString(R.string.all_kind_json));
                        String mTypeJson = getIntent().getStringExtra(getString(R.string.all_type_json));
                        showSpinnerDialog( mKindJson, mTypeJson);
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
                        if(result){
                            onTaskCompleted();
                        }else{
                            String resultStr = getString(R.string.dialog_error_text);
                            onTaskCompleted(resultStr );
                        }
                    }
                });
                getPetsDetailsTask.execute(getString(R.string.animal_type_request),
                                            getString(R.string.animal_kind_request),
                                                getString(R.string.all_type_json),
                                                    getString(R.string.all_kind_json));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_add_pet) {
//            Intent intent = new Intent(this, AddNewPetActivity.class);
//            startActivity(intent);
            new GetAnimalPreLoadPageTask(MainActivity.this,this)
                    .execute(getString(R.string.animal_type_request),
                            getString(R.string.animal_kind_request),
                            getString(R.string.all_type_json),
                            getString(R.string.all_kind_json));
            return true;
        }

        if (id == R.id.action_remove_pet) {
            try {
                JSONObject jsonObject = new JSONObject(mCurrentUserJsonData);
                currentUserId= jsonObject.getString("id");
            } catch (JSONException e) { e.printStackTrace(); }

            UserActiveAnimalsTask getUserActiveAnimalsTask = new UserActiveAnimalsTask(MainActivity.this, mContext, POST, new OnTaskCompleted() {
                @Override
                public void onTaskCompleted() {
                    try {
                        String allAnimalsJson = getIntent().getStringExtra(mContext.getString(R.string.current_user_active_animals_json));
                        JSONArray jsonArray = new JSONArray(allAnimalsJson);
                        int size = jsonArray.length();
                        mAllUserAnimalsName = new String[size];
                        mAllUserAnimalsId = new String[size];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String animalJsonStr = jsonArray.get(i).toString();
                            JSONObject jsonObject = new JSONObject(animalJsonStr);
                            mAllUserAnimalsId[i] = jsonObject.getString("aid");
                            mAllUserAnimalsName[i] = jsonObject.getString("name");
                        }
                    } catch(JSONException e){
                        e.printStackTrace();
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle(getString(R.string.dialog_remove_animals_title_text));
                    builder.setItems(mAllUserAnimalsName, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, final int position) {

                            dialog.dismiss();
                            UserActiveAnimalsTask setUserActiveAnimalsTask = new UserActiveAnimalsTask(MainActivity.this, mContext, PUT, new OnTaskCompleted() {
                                @Override
                                public void onTaskCompleted() {

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
                                    if (result){
                                        String resultStr = getString(R.string.dialog_remove_animals_success_text2)+ " " +  mAllUserAnimalsName[position]+ " " +getString(R.string.dialog_remove_animals_success_text1);
                                        onTaskCompleted(resultStr );
                                    }else{
                                        String resultStr = getString(R.string.dialog_error_text);
                                        onTaskCompleted(resultStr);
                                    }

                                }
                            });

                            setUserActiveAnimalsTask
                                    .execute(mContext.getString(R.string.animals_owner_deactivate_animals_request),
                                            currentUserId,
                                            mAllUserAnimalsId[position],
                                            mContext.getString(R.string.current_user_active_animals_json));

                        }

                    });

                    builder.show();
                }
                @Override
                public void onTaskCompleted(String result) {

                }
                @Override
                public void onTaskCompleted(Boolean result) {

                }
            });
            getUserActiveAnimalsTask
                    .execute(this.getString(R.string.animals_owner_all_active_animals_request),
                                currentUserId,
                                    this.getString(R.string.current_user_active_animals_json));
        }
        return super.onOptionsItemSelected(item);
    }

    public void getPetsImageTask(final boolean isReload, String mGender, String mKind, String mType){
        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage("Please wait...");
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setIndeterminate(true);
        mDialog.setCancelable(false);
        mDialog.show();
        new PetsImagesTask(MainActivity.this, this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted() {

            }

            @Override
            public void onTaskCompleted(String result) {
                Bundle args = new Bundle();
                args.putString(getString(R.string.all_active_animal_pic_json), result);
                if(!isReload){

                    transaction = getSupportFragmentManager().beginTransaction();
                    fragment = new RecyclerViewFragment();
                    fragment.setArguments(args);
                    transaction.replace(R.id.pets_content_fragment, fragment);
                    transaction.commit();
                    fragment.setListener(new OnTaskCompleted() {
                        @Override
                        public void onTaskCompleted() {
                            mDialog.dismiss();
                        }
                        @Override
                        public void onTaskCompleted(String result) {

                        }

                        @Override
                        public void onTaskCompleted(Boolean result) {

                        }
                    });
                }else{
                    CustomAdapter customAdapter = fragment.getAdapter();
                    AnimalsPics[] animalsPics = fragment.getAnimalsPicsDataset(result);
                    customAdapter.refreshPics(animalsPics);
                    mDialog.dismiss();
                }
            }

            @Override
            public void onTaskCompleted(Boolean result) {
                if (result){
                    String mAllPicsResult = getIntent().getStringExtra(getString(R.string.all_active_animal_pic_json));
                    onTaskCompleted(mAllPicsResult);
                }else{
                    mDialog.dismiss();
                }
            }
        }).execute(this.getString(R.string.animals_pic_filter_all_request),
                mType,
                mGender,
                mKind,
                this.getString(R.string.all_active_animal_pic_json));
    }

    public void showSpinnerDialog(String mKindJson,String mTypeJson){

        SpinnerDialog mSpinnerDialog = new SpinnerDialog(mContext,mKindJson,mTypeJson , new SpinnerDialog.DialogListener() {
            @Override
            public void ready(String mGender, String mKind, String mType) {
                if (mGender.equals("זכר")){
                    mGender="1";
                }else if(mGender.equals("נקבה")){
                    mGender="2";
                }
                getIntent().putExtra(getString(R.string.filter_gender_id),mGender);
                getIntent().putExtra(getString(R.string.filter_kind_id),mKind);
                getIntent().putExtra(getString(R.string.filter_type_id),mType);
                getPetsImageTask( true,mGender,  mKind,  mType);
            }

            public void cancelled() {
                // do your code here
            }
        });
        mSpinnerDialog.show();
    }

}
