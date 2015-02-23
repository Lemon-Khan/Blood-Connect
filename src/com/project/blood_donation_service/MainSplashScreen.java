package com.project.blood_donation_service;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
 
public class MainSplashScreen extends Activity {
	ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_splash_screen);
        image = (ImageView)findViewById(R.id.imag);
// METHOD 1     
         
         /****** Create Thread that will sleep for 5 seconds *************/        
        Thread background = new Thread() {
            public void run() {
                 
                try {
                    sleep(2300); 
                    Intent i=new Intent(getBaseContext(),MainActivity.class);
                    startActivity(i);        
                    finish();    
                } catch (Exception e) {
                 
                }
            }
        };
         
        background.start();     

    }
     
    @Override
    protected void onDestroy() {
         
        super.onDestroy();
         
    }
}