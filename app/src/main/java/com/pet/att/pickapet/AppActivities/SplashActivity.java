package com.pet.att.pickapet.AppActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pet.att.pickapet.HTTP.UserLoginTask;
import com.pet.att.pickapet.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp1=this.getSharedPreferences("Login", MODE_PRIVATE);
        String userEmail=sp1.getString("UserEmail", null);
        String password= sp1.getString("Password", null);
         if (userEmail==null || password == null){
             Intent intent = new Intent(this, LoginActivity.class);
             startActivity(intent);
         }else{



                                                                                     /*The First arg is  request name
                                                                                       The Second arg is  email for json
                                                                                       The Third arg is the putString value
                                                                                       The Forth arg is the password*/

//             new LoginUserTask(SplashActivity.this,this, sp1).execute(this.getString(R.string.user_request),
//                                                                                      userEmailBody,
//                                                                                      this.getString(R.string.curent_user_details_json),
//                                                                                      password);

                                                              /*The First arg is  request name
                                                               The Second arg is  email for json
                                                               The Third arg is entered password for json
                                                               The Forth arg is the putString value
                                                              */
             new UserLoginTask(SplashActivity.this,this ,sp1)
                     .execute(this.getString(R.string.user_login_request), userEmail,password,this.getString(R.string.curent_user_details_json));



         }
        finish();
    }
}


