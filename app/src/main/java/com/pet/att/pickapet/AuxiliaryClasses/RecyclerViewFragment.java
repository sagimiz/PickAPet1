package com.pet.att.pickapet.AuxiliaryClasses;

import android.app.Activity;
import android.content.Context;
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
    protected Context mCurrentContext;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mCurrentActivity = this.getActivity().getParent();
        mCurrentContext= this.getContext();
        args  = getArguments();
        mSwipeLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh);
        mSwipeLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.d(TAG, "onRefresh called from SwipeRefreshLayout");
                        new RefreshImagesTask((AppCompatActivity) mCurrentActivity, mCurrentContext,args, new OnTaskCompleted() {
                            @Override
                            public void onTaskCompleted() {
                                String jsonValue = getArguments().getString(getString(R.string.all_active_animal_pic_json));
                                Log.d (TAG,"The JSON String is " + jsonValue);
                                mAnimalsPics=getAnimalsPicsDataset(jsonValue);
                                mAdapter.refreshPics(mAnimalsPics);
                                mSwipeLayout.setRefreshing(false);
                            }
                        }).execute(mCurrentContext.getString(R.string.animals_owner_active_request),
                                        mCurrentContext.getString(R.string.animals_pic_request),
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
        this.initDataset();

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

    private void initDataset() {
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

    private AnimalsPics[] getAnimalsPicsDataset(String mAnimalJsonString) {
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

}
