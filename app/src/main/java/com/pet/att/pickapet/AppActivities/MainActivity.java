package com.pet.att.pickapet.AppActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.pet.att.pickapet.HTTP.GetAnimalPreLoadPageTask;
import com.pet.att.pickapet.HTTP.PetsImagesTask;
import com.pet.att.pickapet.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            new PetsImagesTask(MainActivity.this,this)
                    .execute(this.getString(R.string.animals_owner_active_request),
                            this.getString(R.string.animals_pic_request),
                            this.getString(R.string.all_active_animal_pic_json));
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

        return super.onOptionsItemSelected(item);
    }
}
