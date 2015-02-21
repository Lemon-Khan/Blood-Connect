package com.project.blood_donation_service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Signing extends Activity
{
	EditText bank,password;
	Button log_in;
	SharedPreferences SHARING;
	SharedPreferences.Editor editor;
	public static String fileName="BloodDonation";
	String name="",pass="";
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signing);
		
		log_in=(Button)findViewById(R.id.log_in);
		bank=(EditText)findViewById(R.id.bank);
		password=(EditText)findViewById(R.id.password);
		
		SHARING=getSharedPreferences(fileName, 0);
		editor=SHARING.edit();
		editor.putString("USERNAME","empty");
		editor.commit();
		
		log_in.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v)
			{
				String key_name=SHARING.getString("USERNAME","Your Session is end.");
				String key_pass=SHARING.getString("PASSWORD","Your Session is end.");
				
				name=bank.getText().toString();
			    pass=password.getText().toString();
			    
			    if(key_name.equals(name)&&key_pass.equals(pass))
			    {
			    	Intent i=new Intent(Signing.this,Admin_Rights.class);
					startActivity(i);	
			    }
			    else
			    {
					if(validation())
					{
						new signing_().execute();
				    }
					else
					{
						validation_error();
					}
			    }
			}
		  });
	}
	
	public boolean validation()
    {
		
    	if(name.equals(""))
    		return false;
    	if(pass.equals(""))
    		return false;
    	
    	return true;
    }
    public void validation_error()
    {
    	AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("Illegal ENTRY");
		builder.setMessage("Please give both USERNAME and PASSWORD");
	    builder.setPositiveButton("Ok",null);
	    AlertDialog dialog=builder.show();
    }
	class signing_ extends AsyncTask<Void,Void,String>
	{
		
		String res="";
		InputStream is=null;
		ProgressDialog pDialog;
		
		protected void onPreExecute()
		{
			super.onPreExecute();
			pDialog = new ProgressDialog(Signing.this);
			pDialog.setMessage("Signing...");
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
			
			return "";
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
				JSONObject data=new JSONObject(res);
				
				int code=data.getInt("code");
				
				if(code==1)
				{
					//Toast.makeText(getApplicationContext(),"Alhamdulillah :)",Toast.LENGTH_SHORT).show();
					go();
				}
				else
				{
					Toast.makeText(getApplicationContext(),"Wrong Username or Password",Toast.LENGTH_LONG).show();
				}
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_LONG);
			}
		}
		public void INSERT_DATA()
		{
			//String name=bank.getText().toString();
			//String pass =password.getText().toString();
			String res="";
			
			ArrayList<NameValuePair>pairs=new ArrayList<NameValuePair>();
			
			pairs.add(new BasicNameValuePair("username",name));
			pairs.add(new BasicNameValuePair("password",pass));
			
			try
			{
				HttpClient client=new DefaultHttpClient();
				HttpPost httpPost=new HttpPost("http://169.254.80.80/blood/loginpage_andro.php");
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
		public void go()
		{
			editor=SHARING.edit();
			editor.putString("USERNAME",name);
			editor.putString("PASSWORD",pass);
			editor.commit();
			Intent i=new Intent(Signing.this,Admin_Rights.class);
			startActivity(i);
		}
	  }
}
