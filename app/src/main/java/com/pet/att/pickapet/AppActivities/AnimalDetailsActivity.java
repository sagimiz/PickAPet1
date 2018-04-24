package com.pet.att.pickapet.AppActivities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.pet.att.pickapet.AuxiliaryClasses.ImageDetailsViewFragment;
import com.pet.att.pickapet.R;

public class AnimalDetailsActivity extends AppCompatActivity {
    private static final String TAG = "AnimalDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_details);

//        Bundle bundle = getIntent().getExtras();
//        String mAnimalImageJson = bundle.getString(this.getString(R.string.animal_image_json));
//        String mAnimalFullDetailsJson = bundle.getString(this.getString(R.string.animals_image_all_data_json));
//        initAnimalDetailsPage(mAnimalImageJson,mAnimalFullDetailsJson);

        FragmentTransaction transaction = AnimalDetailsActivity.this.getSupportFragmentManager().beginTransaction();
        ImageDetailsViewFragment fragment = new ImageDetailsViewFragment();
//        fragment.setArguments(bundle);
        transaction.replace(R.id.details_content_fragment, fragment);
        transaction.commit();

//        bundle.putString("animal_data",animal_data);
//        if(animal_data!= null)
//        {
//            AnimalsPics animalsPics = new AnimalsPics(animal_data);
//            if (savedInstanceState == null) {
//                String animalIdJson = "aid="+ animalsPics.getAnimalId() ;
//                new ImageDetailsDataTask(AnimalDetailsActivity.this,this,  bundle , POST).execute(this.getString(R.string.animals_request),animalIdJson,"animal_details_json");
//            }
//        }
//            String animalDetails = bundle.getString("animal_details_json");
//            if (animalDetails!=null);
//            try {
//                animalDetailsJson = new JSONObject(animalDetails);
//                TextView textViewAnimalName = findViewById(R.id.animal_name_text);
//                textViewAnimalName.setText(" ואני מחפש בית"+ animalDetailsJson.getString("name") + "הי קוראים לי ");
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            ImageView imageView = findViewById(R.id.animal_big_image);
//            imageView.setImageBitmap(mAnimalsPics.getMyImage());


//
//            Log.d(TAG, "Data for activity is " +mAnimalImageJson);




    }

    private void initAnimalDetailsPage(String mAnimalImageJson, String mAnimalFullDetailsJson) {

    }
}
