package com.project.blood_donation_service;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Admin_Rights extends Activity
{
	Button donor_adding,patient_adding,donating,inquiry,patient_inquiry,exiting;
	SharedPreferences SHARING;
	SharedPreferences.Editor editor;
	public static String fileName="BloodDonation";
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_rights);
		
		SHARING=getSharedPreferences(fileName, 0);
		String title=SHARING.getString("USERNAME","Your Session is end.");
		setTitle("Welcome,"+title);
		
		donor_adding=(Button)findViewById(R.id.donor_adding);
		patient_adding=(Button)findViewById(R.id.patient_adding);
		donating=(Button)findViewById(R.id.donating);
		inquiry=(Button)findViewById(R.id.inquiry);
		patient_inquiry=(Button)findViewById(R.id.patient_inq);
		exiting=(Button)findViewById(R.id.exiting);
		
		donor_adding.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v)
			{
				Intent i=new Intent(Admin_Rights.this,Add_Donor.class);
				startActivity(i);
			}
		});
		
		patient_adding.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v)
			{
				Intent i=new Intent(Admin_Rights.this,Add_Patient.class);
				startActivity(i);
			}
		});
		
		donating.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v)
			{
				Intent i=new Intent(Admin_Rights.this,Donating.class);
				startActivity(i);
			}
		});
		
		inquiry.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v)
			{
				Intent i=new Intent(Admin_Rights.this,Inquiry.class);
				startActivity(i);
			}
		});
		
		patient_inquiry.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v)
			{
				Intent i=new Intent(Admin_Rights.this,Patient_Inquiry.class);
				startActivity(i);
			}
		});
		
		exiting.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v)
			{
				editor=SHARING.edit();
				editor.clear();
				editor.commit();
				
				Intent i=new Intent(Admin_Rights.this,Options.class);
				startActivity(i);	
			}
		});
	}

}
