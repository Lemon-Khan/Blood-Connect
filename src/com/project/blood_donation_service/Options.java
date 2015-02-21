package com.project.blood_donation_service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Options extends Activity
{
	Button donor,sign,exit;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.option);
		
		donor=(Button)findViewById(R.id.donor);
		sign=(Button)findViewById(R.id.sign);
		exit=(Button)findViewById(R.id.exit);
		
		donor.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v)
			{
				Intent i=new Intent(Options.this,Donor.class);
				startActivity(i);
			}
		  });
		
		 sign.setOnClickListener(new View.OnClickListener()
		       {
					public void onClick(View arg0)
					{
						Intent i=new Intent(Options.this,Signing.class);
						startActivity(i);
					}
				}
			);
		 
		 exit.setOnClickListener(new View.OnClickListener()
	       {
				public void onClick(View arg0)
				{
					finish();
				}
			}
		);
				
	}
	
}
