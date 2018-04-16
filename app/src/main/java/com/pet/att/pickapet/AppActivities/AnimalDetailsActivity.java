package com.pet.att.pickapet.AppActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pet.att.pickapet.AuxiliaryClasses.AnimalsPics;
import com.pet.att.pickapet.HTTP.DetailsDataTask;
import com.pet.att.pickapet.R;

import static com.android.volley.Request.Method.POST;

public class AnimalDetailsActivity extends AppCompatActivity {
    private static final String TAG = "AnimalDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_details);

        Bundle bundle = getIntent().getExtras();
        String animal_data = bundle.getString("animal_data");
        bundle.putString("animal_data",animal_data);
        if(animal_data!= null)
        {
            AnimalsPics animalsPics = new AnimalsPics(animal_data);
            if (savedInstanceState == null) {
                String animalIdJson = "aid="+ animalsPics.getAnimalId() ;
                new DetailsDataTask(AnimalDetailsActivity.this,this,  bundle , POST).execute(this.getString(R.string.animals_request),animalIdJson,"animal_details_json");
            }
        }
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



            Log.d(TAG, "Data for activity is " +animal_data);




    }
}
