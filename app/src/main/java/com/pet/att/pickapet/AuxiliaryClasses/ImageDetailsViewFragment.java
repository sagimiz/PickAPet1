package com.pet.att.pickapet.AuxiliaryClasses;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.pet.att.pickapet.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link ImageDetailsViewFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link ImageDetailsViewFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class ImageDetailsViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ANIMAL_DATA = "animal_details_json";
//    private static final String USER_DATA = "user_json";
//    private static final String IMAGE_DATA = "animal_data";
    private static Context mContext;
    private static AppCompatActivity mActivity;
    TextView mNameTextView=null;
    TextView mDescriptionTextView=null;
    TextView mBirthDateTextView=null;
    TextView mDataTextView=null;
    TextView mOwnerNameTextView=null;
    TextView mOwnerAddressTextView=null;
    TextView mOwnerPhoneTextView=null;
    String mCallNumber=null;


    // TODO: Rename and change types of parameters
    private String mImageJson;
    private String mAnimalFullDetailsJson;

//    private OnFragmentInteractionListener mListener;

    public ImageDetailsViewFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment ImageDetailsViewFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static ImageDetailsViewFragment newInstance(Context context) {
//        ImageDetailsViewFragment fragment = new ImageDetailsViewFragment();
//        Bundle args = new Bundle();
//        mContext = context;
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext =getContext();
        mAnimalFullDetailsJson = getActivity().getIntent().getStringExtra(mContext.getString(R.string.animals_image_all_data_json));
        mImageJson = getActivity().getIntent().getStringExtra(mContext.getString(R.string.animal_image_json));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_view, container, false);

        Button mCallBtm = (Button) view.findViewById(R.id.call_btn);
        mCallBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+mCallNumber));
                startActivity(intent);
            }
        });
        AnimalsPics animalsPics = new AnimalsPics(mImageJson);
        try {
            JSONObject mAnimalDataJson = new JSONObject(mAnimalFullDetailsJson.trim());
            mNameTextView = this.setTextInTextView(view,mNameTextView,R.id.animal_name_text, view.getContext().getString(R.string.animal_details_name)
                                                                                                    + " "+ mAnimalDataJson.getString("name"));

            mDescriptionTextView = this.setTextInTextView(view,mDescriptionTextView,R.id.animal_description_text,mAnimalDataJson.getString("description"));
            String mAnimalAgeStr =view.getContext().getString(R.string.animal_details_birth_date)+ " "+ this.calculateAge(mAnimalDataJson.getString("bday"));
            mBirthDateTextView = this.setTextInTextView(view,mBirthDateTextView,R.id.animal_bdate_text,mAnimalAgeStr);

            String mAnimalDataStr = "אני ";
            mAnimalDataStr = mAnimalDataStr + mAnimalDataJson.getString("kind");

            mAnimalDataStr = mAnimalDataStr + " "+ view.getContext().getString(R.string.animal_details_type) +" "+ mAnimalDataJson.getString("type_name");
            mAnimalDataStr = mAnimalDataStr + " ואני ";
            if (mAnimalDataJson.getString("gid").equals("1")){
                mAnimalDataStr = mAnimalDataStr + " זכר ";
            }else{
                mAnimalDataStr = mAnimalDataStr + " נקבה ";
            }

            mDataTextView = this.setTextInTextView(view,mDataTextView,R.id.animal_data_text,mAnimalDataStr);

            String mAnimalOwnerStr = view.getContext().getString(R.string.animal_details_owner_name)
                    + " "+ mAnimalDataJson.getString("fname")
                    + " " + mAnimalDataJson.getString("lname");
            mOwnerNameTextView = this.setTextInTextView(view,mOwnerNameTextView,R.id.owner_name_text,mAnimalOwnerStr);

            String mAnimalAddress = view.getContext().getString(R.string.animal_details_owner_address)
                    + " " + mAnimalDataJson.getString("address")
                    + " " + mAnimalDataJson.getString("city");
            mOwnerAddressTextView = this.setTextInTextView(view,mOwnerAddressTextView,R.id.owner_city_text,mAnimalAddress);

            String mOwnerPhone = view.getContext().getString(R.string.animal_details_owner_phone) + " " + mAnimalDataJson.getString("phone");
            mCallNumber = mAnimalDataJson.getString("phone");
            mOwnerPhoneTextView = this.setTextInTextView(view,mOwnerPhoneTextView,R.id.owner_phone_text,mOwnerPhone);

            ImageView mImageView = (ImageView) view.findViewById(R.id.animal_big_image);
            Bitmap mFinalImage = animalsPics.getMyImage();
//            mFinalImage = this.getResizedBitmap(mFinalImage,mImageView.getHeight(),mImageView.getWidth());
            mImageView.setImageBitmap(mFinalImage);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Inflate the layout for this fragment
        return view;
    }


    private TextView setTextInTextView(View view, TextView mTextView, int mTextId, String inputStr){
        mTextView = (TextView) view.findViewById(mTextId);
        String mAnimalNameStr = inputStr;
        mTextView.setText(mAnimalNameStr);
        return mTextView;
    }

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static String calculateAge(String date) {

        int year = 0;
        int month = 0;
        int day = 0;
        try {
            Date date1 = dateFormat.parse(date);
            Calendar now = Calendar.getInstance();
            Calendar dob = Calendar.getInstance();
            dob.setTime(date1);
            if (dob.after(now)) {
                throw new IllegalArgumentException("Can't be born in the future");
            }
            int year1 = now.get(Calendar.YEAR);
            int year2 = dob.get(Calendar.YEAR);
            year = year1 - year2;
            int month1 = now.get(Calendar.MONTH);
            int month2 = dob.get(Calendar.MONTH);
            if (month2 > month1) {
                month = month2-month1;
                year--;
            } else if (month1 == month2) {
                int day1 = now.get(Calendar.DAY_OF_MONTH);
                int day2 = dob.get(Calendar.DAY_OF_MONTH);
                if (day2 > day1) {
                    day = day2-day1;
                    year--;
                }
            }else{
                month= month1-month2;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String finalAge="";
        if (year==0){
            if (month==0){
                finalAge= String.valueOf(day) + " ימים ";
            }else{
                finalAge = String.valueOf(month) + " חודשים ";
            }
        }else {
            finalAge = String.valueOf(year) + " שנים " + String.valueOf(month) + " חודשים ";
        }
        return finalAge ;
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
