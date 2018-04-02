package com.pet.att.pickapet.AppActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by mizrahi on 07/03/2018.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp1=this.getSharedPreferences("Login", MODE_PRIVATE);
        String userName=sp1.getString("UserName", null);
        String password= sp1.getString("Password", null);
        Intent intent = (userName==null || password == null)? new Intent(this, LoginActivity.class) :new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}
