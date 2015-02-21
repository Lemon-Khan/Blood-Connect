package com.project.blood_donation_service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.project.blood_donation_service.Donating.donating;
import com.project.blood_donation_service.Donating.existing_donor_availablity;
import com.project.blood_donation_service.Donating.existing_donor_check;
import com.project.blood_donation_service.Donating.selected_;

public class Taken_Blood extends Activity
{
	EditText patient_id_show,patient_blood,date;
	Spinner no_bags_taken;
	Button proceed;
	TextView message;
	
	int donation_complete=0;
	
	SharedPreferences SHARING;
	public static String fileName="BloodDonation";
	
	String bank_username,_id,_bags,_date,list_number[]={"Select Bag","1","2","3","4","5"},_blood,_total;
	
	Calendar myCalendar=Calendar.getInstance();
	
	DatePickerDialog.OnDateSetListener _date_=new DatePickerDialog.OnDateSetListener()
	{
		public void onDateSet(DatePicker arg0, int year, int month, int day)
		{
			myCalendar.set(Calendar.YEAR,year);
			myCalendar.set(Calendar.MONTH,month);
			myCalendar.set(Calendar.DAY_OF_MONTH,day);
			update_label();
		}
	};
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.taken_blood);
		
		patient_id_show=(EditText)findViewById(R.id.patient_id_show);
		patient_blood=(EditText)findViewById(R.id.patient_blood);
		no_bags_taken=(Spinner)findViewById(R.id.no_bags_taken);
		date=(EditText)findViewById(R.id.date);
		message=(TextView)findViewById(R.id.Message);
		proceed=(Button)findViewById(R.id.proceed);
		
		ArrayAdapter<String>blood_=new ArrayAdapter<String>(Taken_Blood.this,android.R.layout.simple_spinner_item,list_number);
		no_bags_taken.setAdapter(blood_);
		no_bags_taken.setOnItemSelectedListener(new selected_());
		
		Bundle bundle=getIntent().getExtras();
		_id=bundle.getString("GOVT_ID");
		_blood=bundle.getString("BLOOD");
		_total=bundle.getString("TOTAL_BLOOD");
		
		patient_id_show.setText(_id);
		patient_blood.setText(_blood);
		
		SHARING=getSharedPreferences(fileName, 0);
		String title=SHARING.getString("USERNAME","Your Session is end.");
        setTitle("Blood Taken in,"+title);
		
		bank_username=title;
		
		date.setOnFocusChangeListener(new View.OnFocusChangeListener(){
		    public void onFocusChange(View v, boolean hasFocus) {
		        if (hasFocus)
		        {
		        	new DatePickerDialog(Taken_Blood.this,_date_, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();    
		        }
		    }
		});
		
		proceed.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v)
			{
				_date=date.getText().toString();
				
			    if(validation())
				{
			      if(donation_complete==1)
				  {
			    	  new blood_taking().execute();
				  }
			      else
			      Toast.makeText(getApplicationContext(),"SORRY,this can't be processed.",Toast.LENGTH_LONG).show();
				}
				else
				{
					validation_error();
				}
			}
		  });
	}
	public void update_label()
	{
		String format="yyyy-MM-dd";
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		date.setText(sdf.format(myCalendar.getTime()));
	}
	public boolean validation()
    {
    	if((_bags.matches("[1-5]+")==false)||(_bags.equals("Select Bag")))
    		return false;
    	if(_date.equals(""))
    		return false;
    	
    	return true;
    }
    public void validation_error()
    {
    	AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("Illegal ENTRY");
		builder.setMessage("Please GIVE only legal ENTRY.");
	    builder.setPositiveButton("Ok",null);
	    AlertDialog dialog=builder.show();
    }
    public class selected_ implements OnItemSelectedListener
	{
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3)
		{
		   _bags=no_bags_taken.getSelectedItem().toString();
		   
		   if(_bags.equals("Select Bag")==false)
		   new blood_taking_availablity().execute();
		}
		public void onNothingSelected(AdapterView<?> arg0)
		{
			
		}
	}
    class blood_taking_availablity extends AsyncTask<Void,Void,String>
   	{
   		String res="";
   		InputStream is=null;
   		ProgressDialog pDialog;
   		
   		protected void onPreExecute()
   		{
   			super.onPreExecute();
   			pDialog = new ProgressDialog(Taken_Blood.this);
   			pDialog.setMessage("Taking Blood availablity...");
   			pDialog.setCancelable(false);
   			pDialog.show();
   		}

   		protected String doInBackground(Void... args)
   		{
   			INSERT_DATA();
   			try
   			{
   				BufferedReader reader=new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
   				StringBuilder sb=new StringBuilder();
   				String line=null;
   				
   				while((line=reader.readLine())!=null)
   				{
   					sb.append(line+"\n");
   				}
   				is.close();
   				res=sb.toString();
   			}
   			catch(Exception e)
   			{
   				Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
   			}
   			return res;
   		}

   		protected void onPostExecute(String result)
   		{
   			super.onPostExecute(result);
   			
   			if(pDialog.isShowing())
   			{
   				pDialog.dismiss();
   			}
   			try
   			{
   				JSONObject data=new JSONObject(result);
   				
   				int code=data.getInt("message");
   				int remaining=data.getInt("remaining");
   				
   				if(code==0)
   				{
   					//Toast.makeText(getApplicationContext(),"SORRY,this donor can not donor blood before 3 months.",Toast.LENGTH_LONG).show();
   				    message.setText("Sorry,this donor can't take blood.");
   				}
   				else if(code==1)
   				{
   					//Toast.makeText(getApplicationContext(),"SORRY,this donor does exist or registered yet.",Toast.LENGTH_LONG).show();
   					message.setText("Donor is eligible for taking blood.Blood remaining-"+Integer.toString(remaining));
   					donation_complete=1;
   				}
   				
   			}
   			catch(Exception e)
   			{
   				Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_LONG);
   			}
   		}
   		public void INSERT_DATA()
   		{	
   			ArrayList<NameValuePair>pairs=new ArrayList<NameValuePair>();
   			
   			pairs.add(new BasicNameValuePair("patient_id",_id));
   			pairs.add(new BasicNameValuePair("bgroup",_blood));
   			pairs.add(new BasicNameValuePair("bags",_bags));
   			pairs.add(new BasicNameValuePair("total_blood",_total));
   			pairs.add(new BasicNameValuePair("username",bank_username));
   			
   			try
   			{
   				HttpClient client=new DefaultHttpClient();
   				HttpPost httpPost=new HttpPost("http://192.168.46.1/proj/existing_patient_after_validation_andro.php");
   				httpPost.setEntity(new UrlEncodedFormEntity(pairs));
   				HttpResponse response=client.execute(httpPost);
   				HttpEntity entity=response.getEntity();
   			    is=entity.getContent();
   			}
   			catch(Exception e)
   			{
   				Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
   			}
   		}
   	}
    
    class blood_taking extends AsyncTask<Void,Void,String>
   	{
   		String res="";
   		InputStream is=null;
   		ProgressDialog pDialog;
   		
   		protected void onPreExecute()
   		{
   			super.onPreExecute();
   			pDialog = new ProgressDialog(Taken_Blood.this);
   			pDialog.setMessage("Taking-Blood Process...");
   			pDialog.setCancelable(false);
   			pDialog.show();
   		}

   		protected String doInBackground(Void... args)
   		{
   			INSERT_DATA();
   			try
   			{
   				BufferedReader reader=new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
   				StringBuilder sb=new StringBuilder();
   				String line=null;
   				
   				while((line=reader.readLine())!=null)
   				{
   					sb.append(line+"\n");
   				}
   				is.close();
   				res=sb.toString();
   			}
   			catch(Exception e)
   			{
   				Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
   			}
   			return res;
   		}

   		protected void onPostExecute(String result)
   		{
   			super.onPostExecute(result);
   			
   			if(pDialog.isShowing())
   			{
   				pDialog.dismiss();
   			}
   			try
   			{
   				JSONObject data=new JSONObject(result);
   				int code=data.getInt("code");
   				if(code==0)
   				{
   			     Toast.makeText(getApplicationContext(),"SORRY,Process is not completed for errors.",Toast.LENGTH_LONG).show();
   				}
   				else if(code==1)
   				{
   			      Toast.makeText(getApplicationContext(),"Process Completed",Toast.LENGTH_LONG).show();
   				}
   			}
   			catch(Exception e)
   			{
   				Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_LONG);
   			}
   		}
   		public void INSERT_DATA()
   		{	
   			ArrayList<NameValuePair>pairs=new ArrayList<NameValuePair>();
   			
   			pairs.add(new BasicNameValuePair("bags",_bags));
   			pairs.add(new BasicNameValuePair("datepicker",_date));
   			pairs.add(new BasicNameValuePair("username",bank_username));
   			pairs.add(new BasicNameValuePair("patient_id",_id));
   			pairs.add(new BasicNameValuePair("bgroup",_blood));
   			
   			try
   			{
   				HttpClient client=new DefaultHttpClient();
   				HttpPost httpPost=new HttpPost("http://192.168.46.1/proj/existing_patient_after_andro.php");
   				httpPost.setEntity(new UrlEncodedFormEntity(pairs));
   				HttpResponse response=client.execute(httpPost);
   				HttpEntity entity=response.getEntity();
   			    is=entity.getContent();
   			}
   			catch(Exception e)
   			{
   				Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
   			}
   		}
   	}
 }