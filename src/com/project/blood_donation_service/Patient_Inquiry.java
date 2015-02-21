package com.project.blood_donation_service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.project.blood_donation_service.Donating.existing_donor_check;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Patient_Inquiry extends Activity 
{
	EditText patient_govt_id;
	Button next;
	TextView message;
	
	SharedPreferences SHARING;
	public static String fileName="BloodDonation";
	
	String patient_id,bank_username;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patient_inquiry);
		
		SHARING=getSharedPreferences(fileName, 0);
		String title=SHARING.getString("USERNAME","Your Session is end.");
		setTitle("Patient Inquiry in,"+title);
		
		bank_username=title;
		
		patient_govt_id=(EditText)findViewById(R.id.patient_govt_id);
		next=(Button)findViewById(R.id.next);
		message=(TextView)findViewById(R.id.inq_mess);
		
		/*patient_govt_id.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			public void onFocusChange(View v, boolean hasFocus)
			{
				if(!hasFocus)
				{
					patient_id=patient_govt_id.getText().toString();
					
					if(patient_id.equals("")==false)
					new availablity().execute();
			    }
			}
		});*/
				
		next.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v)
			{
				patient_id=patient_govt_id.getText().toString();
				
				if(validation())
				{
					new availablity().execute();
				}
				else
				{
					validation_error();
				}
			}
		  });
	}
	public boolean validation()
    {
     	if((patient_id.matches("[0-9]+")==false)||(patient_id.equals("")))
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
	
	class availablity extends AsyncTask<Void,Void,String>
	{
		String res="";
		InputStream is=null;
		ProgressDialog pDialog;
		
		protected void onPreExecute()
		{
			super.onPreExecute();
			pDialog = new ProgressDialog(Patient_Inquiry.this);
			pDialog.setMessage("Checking Availablity...");
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
				int go=data.getInt("go");
				int total=data.getInt("total_blood");
				String blood_mark=data.getString("blood");
				
							
				if(code==1 && go==1 && total<5)
				{
					  Intent i=new Intent(Patient_Inquiry.this,Taken_Blood.class);
					  Bundle bundle=new Bundle();
					  bundle.putString("GOVT_ID",patient_id);
					  bundle.putString("BLOOD",blood_mark);
					  bundle.putString("TOTAL_BLOOD",Integer.toString(total));
					  i.putExtras(bundle);
					  startActivity(i);  	
				}
				else
				{
					Toast.makeText(getApplicationContext(),"SORRY,You can't proceed.",Toast.LENGTH_LONG).show();
					
					String m ="";
					
					if(code==0)
					{
						m+="Doner is not registered.\n";
					}
					
					if(total>5)
					{
					   m+="Sorry,Your blood limit will be crossed.\n";
					}
					message.setText(m);
				}
			}
			catch (JSONException e)
			{
				Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
			}
			
		}
		public void INSERT_DATA()
		{	
			ArrayList<NameValuePair>pairs=new ArrayList<NameValuePair>();
			
			pairs.add(new BasicNameValuePair("govt_id",patient_id));
			
			try
			{
				HttpClient client=new DefaultHttpClient();
				HttpPost httpPost=new HttpPost("http://169.254.80.80/blood/existing_patient_before_validation_andro.php");
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

