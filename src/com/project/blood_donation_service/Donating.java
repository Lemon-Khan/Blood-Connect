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

import com.project.blood_donation_service.Add_Donor.registering;
import com.project.blood_donation_service.Donor.selected_;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Donating extends Activity implements OnFocusChangeListener
{
	EditText donor_id,date;
	Spinner no_of_bags;
	Button donate;
	SharedPreferences SHARING;
	public static String fileName="BloodDonation";
	String bank_username,_id,_bags,_date,list_number[]={"1","2"};
	int donor_ok=0;
	
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
		setContentView(R.layout.donating);
		
		donor_id=(EditText)findViewById(R.id.donor_id);
		no_of_bags=(Spinner)findViewById(R.id.no_bags);
		date=(EditText)findViewById(R.id.contact);
		donate=(Button)findViewById(R.id.donate);
		
		ArrayAdapter<String>blood_=new ArrayAdapter<String>(Donating.this,android.R.layout.simple_spinner_item,list_number);
		no_of_bags.setAdapter(blood_);
		no_of_bags.setOnItemSelectedListener(new selected_());
		
		SHARING=getSharedPreferences(fileName, 0);
		String title=SHARING.getString("USERNAME","Your Session is end.");
        setTitle("Donation in,"+title);
		
		bank_username=title;
		
		donor_id.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			public void onFocusChange(View v, boolean hasFocus)
			{
				if(!hasFocus)
				{
					new existing_donor_check().execute();
					//Toast.makeText(getApplicationContext(),"It's working lemon",Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		date.setOnFocusChangeListener(new View.OnFocusChangeListener(){
		    public void onFocusChange(View v, boolean hasFocus) {
		        if (hasFocus)
		        {
		        	new DatePickerDialog(Donating.this,_date_, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();    
		        }
		    }
		});
		
		donate.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v)
			{
				_id=donor_id.getText().toString();
				//_bags=no_of_bags.getText().toString();
				_date=date.getText().toString();
				
				if(validation())
				{
				 if(donor_ok==1)
				 new existing_donor_availablity().execute();
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
    	if((_id.matches("[0-9]+")==false)||(_id.equals("")))
    		return false;
    	if((_bags.matches("[0-9]+")==false)||(_bags.equals("")))
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
		   _bags=no_of_bags.getSelectedItem().toString(); 
		}
		public void onNothingSelected(AdapterView<?> arg0)
		{
			
		}
	}
    class existing_donor_check extends AsyncTask<Void,Void,String>
   	{
   		String res="";
   		InputStream is=null;
   		//ProgressDialog pDialog;
   		
   		protected void onPreExecute()
   		{
   			super.onPreExecute();
   			/*pDialog = new ProgressDialog(Donating.this);
   			pDialog.setMessage("Checking existence of DONOR...");
   			pDialog.setCancelable(false);
   			pDialog.show();*/
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
   			
   			/*if(pDialog.isShowing())
   			{
   				pDialog.dismiss();
   			}*/
   			try
   			{
   				JSONObject data=new JSONObject(result);
   				
   				int code=data.getInt("message");
   				
   				if(code==-1)
   				{
   					Toast.makeText(getApplicationContext(),"SORRY,this donor can not donor blood before 3 months.",Toast.LENGTH_LONG).show();
   				}
   				else if(code==0)
   				{
   					Toast.makeText(getApplicationContext(),"SORRY,this donor does exist or registered yet.",Toast.LENGTH_LONG).show();
   				}
   				else
   				{
   					donor_ok=code;
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
   			_id=donor_id.getText().toString();
   			pairs.add(new BasicNameValuePair("id",_id));
   			
   			try
   			{
   				HttpClient client=new DefaultHttpClient();
   				HttpPost httpPost=new HttpPost("http://169.254.80.80/blood/existing_donor_check.php");
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
    class existing_donor_availablity extends AsyncTask<Void,Void,String>
	{
		String res="";
		InputStream is=null;
		ProgressDialog pDialog;
		
		protected void onPreExecute()
		{
			super.onPreExecute();
			pDialog = new ProgressDialog(Donating.this);
			pDialog.setMessage("Checking existence of DONOR...");
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
				
				if(code==1)
				{
				  new donating().execute();
				}
				else
				{
					Toast.makeText(getApplicationContext(),"SORRY,this donor does exist or registered yet.",Toast.LENGTH_LONG).show();
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
			
			pairs.add(new BasicNameValuePair("id",_id));
			
			try
			{
				HttpClient client=new DefaultHttpClient();
				HttpPost httpPost=new HttpPost("http://169.254.80.80/blood/existing_donor_availablity.php");
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
	class donating extends AsyncTask<Void,Void,String>
	{
		String res="";
		InputStream is=null;
		ProgressDialog pDialog;
		
		protected void onPreExecute()
		{
			super.onPreExecute();
			pDialog = new ProgressDialog(Donating.this);
			pDialog.setMessage("Donating...");
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
				
				if(code==1)
				{
				Toast.makeText(getApplicationContext(),"DONATION COMPLETE :)",Toast.LENGTH_SHORT).show();	
				}
				else
				{
					Toast.makeText(getApplicationContext(),"xx DONATION INCOMPLETE xx",Toast.LENGTH_LONG).show();
				}
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_LONG);
			}
			
			Intent i=new Intent(Donating.this,Admin_Rights.class);
			startActivity(i);
			
		}
		public void INSERT_DATA()
		{	
			ArrayList<NameValuePair>pairs=new ArrayList<NameValuePair>();
			//String _id=donor_id.getText().toString();
			//String _bags=no_of_bags.getText().toString();
			//String _date=date.getText().toString();
			
			pairs.add(new BasicNameValuePair("id",_id));
			pairs.add(new BasicNameValuePair("bags",_bags));
			pairs.add(new BasicNameValuePair("date",_date));
			pairs.add(new BasicNameValuePair("username",bank_username));
			try
			{
				HttpClient client=new DefaultHttpClient();
				HttpPost httpPost=new HttpPost("http://169.254.80.80/blood/existing_donor_andro.php");
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
	public void onFocusChange(View v, boolean hasFocus)
	{
			
	}
}
