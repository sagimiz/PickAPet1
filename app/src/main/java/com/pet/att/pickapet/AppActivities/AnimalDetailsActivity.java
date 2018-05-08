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
        FragmentTransaction transaction = AnimalDetailsActivity.this.getSupportFragmentManager().beginTransaction();
        ImageDetailsViewFragment fragment = new ImageDetailsViewFragment();
        transaction.replace(R.id.details_content_fragment, fragment);
        transaction.commit();

    }
}
