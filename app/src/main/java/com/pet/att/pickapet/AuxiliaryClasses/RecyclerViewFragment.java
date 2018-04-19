package com.pet.att.pickapet.AuxiliaryClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pet.att.pickapet.AppActivities.MainActivity;
import com.pet.att.pickapet.HTTP.PetsImagesTask;
import com.pet.att.pickapet.HTTP.RefreshImagesTask;
import com.pet.att.pickapet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecyclerViewFragment extends Fragment {

    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 3;
    protected Bundle args;
    private enum LayoutManagerType { GRID_LAYOUT_MANAGER }
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected String animalJsonString;
    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    protected AnimalsPics[] mAnimalsPics;
    protected SwipeRefreshLayout mSwipeLayout;
    protected Activity mCurrentActivity;
    protected Activity mActivity;
    protected Context mCurrentContext;
    OnTaskCompleted listener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentActivity = this.getActivity();
        mCurrentContext= this.getContext();
        args  = getArguments();
        mSwipeLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh);
        mSwipeLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        String mGender = getPutExtraValue(R.string.filter_gender_id);
                        String mKind = getPutExtraValue(R.string.filter_kind_id);
                        String mType =getPutExtraValue(R.string.filter_type_id);
                        new PetsImagesTask(mCurrentActivity, mCurrentContext, new OnTaskCompleted() {
                            @Override
                            public void onTaskCompleted() {

                            }

                            @Override
                            public void onTaskCompleted(String result) {
                                Log.d (TAG,"The JSON String is " + result);
                                mAnimalsPics=getAnimalsPicsDataset(result);
                                mAdapter.refreshPics(mAnimalsPics);
                                mSwipeLayout.setRefreshing(false);
                            }

                            @Override
                            public void onTaskCompleted(Boolean result) {
                                if (result){
                                    String mAllPicsResult = mCurrentActivity.getIntent().getStringExtra(getString(R.string.all_active_animal_pic_json));
                                    onTaskCompleted(mAllPicsResult);
                                }else{
                                    mSwipeLayout.setRefreshing(false);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(mCurrentContext);
                                    builder.setMessage(mCurrentContext.getString(R.string.dialog_error_text))
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            }
                        }).execute(mCurrentContext.getString(R.string.animals_pic_filter_all_request),
                                mType,
                                mGender,
                                mKind,
                                mCurrentContext.getString(R.string.all_active_animal_pic_json));
                    }
                }
        );
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String jsonValue = getArguments().getString(getString(R.string.all_active_animal_pic_json));
        Log.d (TAG,"The JSON String is " + jsonValue);
        animalJsonString = jsonValue;
        this.initDataSet();

        View rootView = inflater.inflate(R.layout.recycler_view_frag, container, false);
        rootView.setTag(TAG);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mAdapter = new CustomAdapter(mAnimalsPics);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        listener.onTaskCompleted();
        return rootView;
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void initDataSet() {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(animalJsonString);
            mAnimalsPics = new AnimalsPics[jsonArray.length()];
            for(int i=0; i<jsonArray.length();i++){
                String imageStr = jsonArray.get(i).toString();
                JSONObject jsonObject = new JSONObject(imageStr );
                String animalId = jsonObject.get("picid").toString();
                mAnimalsPics[i] = new AnimalsPics(imageStr);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public AnimalsPics[] getAnimalsPicsDataset(String mAnimalJsonString) {
        JSONArray jsonArray = null;
        AnimalsPics[] animalsPics=null;
        try {
            jsonArray = new JSONArray(mAnimalJsonString);
            animalsPics = new AnimalsPics[jsonArray.length()];
            for(int i=0; i<jsonArray.length();i++){
                String imageStr = jsonArray.get(i).toString();
                JSONObject jsonObject = new JSONObject(imageStr );
                animalsPics[i] = new AnimalsPics(imageStr);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return animalsPics;
    }


    public AnimalsPics[] getAnimalsPics() {
        return mAnimalsPics;
    }

    public void setAnimalsPics(AnimalsPics[] mAnimalsPics) {
        this.mAnimalsPics = mAnimalsPics;
    }

    public CustomAdapter getAdapter() {
        return mAdapter;
    }

    public void setListener(OnTaskCompleted listener) {
        this.listener = listener;
    }

    private String getPutExtraValue(int value){
        String mValue=null;
        Log.d(TAG, "onRefresh called from SwipeRefreshLayout");
        try {
            mValue = mCurrentActivity.getIntent().getStringExtra(getString(value));
        }catch (Exception e){
            mValue = (mValue==null)? "" : mValue;
        }
        return (mValue==null)? "" : mValue;
    }

}
