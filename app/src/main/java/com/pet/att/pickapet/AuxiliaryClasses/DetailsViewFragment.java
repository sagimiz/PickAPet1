package com.pet.att.pickapet.AuxiliaryClasses;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.pet.att.pickapet.R;
import org.json.JSONException;
import org.json.JSONObject;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link DetailsViewFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link DetailsViewFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class DetailsViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ANIMAL_DATA = "animal_details_json";
    private static final String USER_DATA = "user_json";
    private static final String IMAGE_DATA = "animal_data";
    private static Context mContext;
    private static AppCompatActivity mActivity;



    // TODO: Rename and change types of parameters
    private String mImageJson;
    private String mAnimalJson;
    private String mUserJson;

//    private OnFragmentInteractionListener mListener;

    public DetailsViewFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment DetailsViewFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static DetailsViewFragment newInstance(Context context) {
//        DetailsViewFragment fragment = new DetailsViewFragment();
//        Bundle args = new Bundle();
//        mContext = context;
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_view, container, false);

        mAnimalJson = this.getArguments().getString(ANIMAL_DATA);
        mUserJson = this.getArguments().getString(USER_DATA);
        mImageJson = this.getArguments().getString(IMAGE_DATA);


        AnimalsPics animalsPics = new AnimalsPics(mImageJson);
        try {
            JSONObject animalDataJson = new JSONObject(mAnimalJson);
            JSONObject userDataJson = new JSONObject(mUserJson);
            TextView animalNameTextView = (TextView) view.findViewById(R.id.animal_name_text);
            TextView animalBDateTextView = (TextView) view.findViewById(R.id.animal_bdate_text);
            TextView ownerNameTextView = (TextView) view.findViewById(R.id.owner_name_text);
            TextView ownerCityTextView = (TextView) view.findViewById(R.id.owner_city_text);
            TextView ownerPhoneTextView = (TextView) view.findViewById(R.id.owner_phone_text);
            ImageView imageView = (ImageView) view.findViewById(R.id.animal_big_image);


            animalNameTextView.setText(animalDataJson.getString("name")+ " hello i am ");
            imageView.setImageBitmap(animalsPics.getMyImage());
            animalBDateTextView.setText(animalDataJson.getString("bday"));
            ownerNameTextView.setText(userDataJson.getString("fname")+userDataJson.getString("lname") );
            ownerCityTextView.setText(userDataJson.getString("adress")+userDataJson.getString("city") );
            ownerPhoneTextView.setText(userDataJson.getString("phone"));

        } catch (JSONException e) {
            e.printStackTrace();
        }









        // Inflate the layout for this fragment
        return view;


    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
