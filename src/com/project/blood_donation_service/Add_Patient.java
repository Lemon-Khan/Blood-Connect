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
import org.json.JSONObject;

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
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.project.blood_donation_service.Add_Donor.donor_check;
import com.project.blood_donation_service.Add_Donor.registering;
import com.project.blood_donation_service.Add_Donor.selected_;

public class Add_Patient extends Activity
{
	EditText user_name,contact,address,govt_id,disease;
	Spinner blood_grp,location;
	Button add_patients;
	
	SharedPreferences SHARING;
	public static String fileName="BloodDonation";
	
	int go=0;
	String bank_username,patient_name,patient_blood,patient_contact,patient_add,patient_city,govt_id_="",disease_;
	
	String blood_list[]={"Select Blood Group","A+","B+","A-","B-","O+","O-","AB+","AB-"};
	String city_list[]={"Select City",
			"--Dhaka Division--",
			"Dhaka",
            "Bhairab",
            "Faridpur",
            "Jamalpur",
            "Kishoreganj",
            "Manikganj",
            "Madaripur",
            "Munshiganj",
            "Mymensingh",
            "Narsingdi",
            "Netrokona",
            "Rajbari",
            "Shariatpur",
            "Sherpur",
            "Tangail",
            "Tongi",
            "Gopalganj",
            "--Barisal Division--", 
            "Barguna",
            "Bakerganj",
            "Bhola",
            "Jhalokati",
            "Patuakhali",
            "Pirojpur",
            "Akhaura",
            "Cox's Bazar",
            "Bandarban",
            "Brahmanbaria",
            "Sarail",
            "Shahbazpur Town",
            "Chandpur",
            "Chaumuhani",
            "Feni",
            "Khagrachhari",
            "Laksam",
            "Lakshmipur",
            "Noakhali",
            "Rangamati",
            "Rangunia",
            "Sandwip",
            "Comilla",
            "Burichong",
			"--Khulna Division--",
			"Bagherhat",
            "Chuadanga",
            "Jessore",
            "Jhenaidah",
            "Kushtia",
            "Magura",
            "Meherpur",
         	"Narail",
            "Shatkhira",
            "--Rajshahi Division--",
            "Bogra",
            "Joypurhat",
            "Naogaon",
            "Natore",
            "Nawabganj",
            "Pabna",
            "Sirajganj",
            "Shahjadpur",
            "Ullapara",
            "Iswardi",
            "Santhia",
            "Sherpur",
            "--Rangpur Division--",
			"Saidpur",
            "Dinajpur",
            "Gaibandha",
            "Gobindaganj",
            "Kurigram",
            "Lalmonirhat",
            "Nilphamari",
            "Panchagarh",
            "Thakurgaon",
            "--Rangpur Division--",
            "Golapganj",
            "Habiganj",
            "Maulvibazar",
            "Sreemangal",
            "Sunamganj",
            "Beanibazar",
            "Barlekha"  };
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.add_patient);
		
		SHARING=getSharedPreferences(fileName, 0);
		String title=SHARING.getString("USERNAME","Your Session is end.");
		setTitle("Add Patient in,"+title);
		
		bank_username=title;
		
		user_name=(EditText)findViewById(R.id.patient_name);
		govt_id=(EditText)findViewById(R.id.govt_id);
		contact=(EditText)findViewById(R.id.contact_patient);
		address=(EditText)findViewById(R.id.address_patient);
		disease=(EditText)findViewById(R.id.disease);
		blood_grp=(Spinner)findViewById(R.id.patient_blood_grp);
		location=(Spinner)findViewById(R.id.patient_location);
		add_patients=(Button)findViewById(R.id.add_patients);
		
		govt_id.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			public void onFocusChange(View v, boolean hasFocus)
			{
				if(!hasFocus)
				{
					new patient_check().execute();
				}
			}
		});
		
		ArrayAdapter<String>blood_=new ArrayAdapter<String>(Add_Patient.this,android.R.layout.simple_spinner_item,blood_list);
		blood_grp.setAdapter(blood_);
		blood_grp.setOnItemSelectedListener(new selected_());
		
		ArrayAdapter<String>city_=new ArrayAdapter<String>(Add_Patient.this,android.R.layout.simple_spinner_item,city_list);
		location.setAdapter(city_);
		location.setOnItemSelectedListener(new selected_());
		
		add_patients.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v)
			{
				patient_name=user_name.getText().toString();
				patient_contact=contact.getText().toString();
				patient_add=address.getText().toString();
				govt_id_=govt_id.getText().toString();
				disease_=disease.getText().toString();
				
				if(validation())
				{
					if(go==1)
					new registering().execute();
					else
				    Toast.makeText(getApplicationContext(),"Sorry,Process can't be processed.",Toast.LENGTH_SHORT).show();
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
    	if((patient_name.matches("[A-Za-z]+.*")==false)||(patient_name.equals("")))
    		return false;
    	if((patient_contact.matches("[0-9]+")==false)||(patient_contact.equals("")))
    		return false;
    	if((govt_id_.matches("[0-9]+")==false)||(govt_id_.equals("")))
    		return false;
    	if((disease_.matches("[A-Za-z]+.*")==false)||(disease_.equals("")))
    		return false;
    	if(patient_add.equals(""))
    		return false;
    	if(patient_blood.equals("Select Blood Group") || (patient_city.equals("Select City")||patient_city.equals("--Dhaka Division--")||patient_city.equals("--Barisal Division--")||patient_city.equals("--Khulna Division--")||patient_city.equals("--Rajshahi Division--")||patient_city.equals("--Rangpur Division--")|| (patient_city.equals("--Sylhet Division--"))))
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
		 if(arg0.getId()==R.id.patient_blood_grp)
		  {
			 patient_blood=blood_grp.getSelectedItem().toString();	 
		  }
		  else
		  {
			  patient_city=location.getSelectedItem().toString();	 
		  }
		 }
		public void onNothingSelected(AdapterView<?> arg0)
		{
			
		}
	}
	
	class patient_check extends AsyncTask<Void,Void,String>
   	{
   		String res="";
   		InputStream is=null;
   		ProgressDialog pDialog;
   		
   		protected void onPreExecute()
   		{
   			super.onPreExecute();
   			pDialog = new ProgressDialog(Add_Patient.this);
   			pDialog.setMessage("Checking existence of PATIENT...");
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
   				if(code==0)
   				{
   					Toast.makeText(getApplicationContext(),"SORRY,this Patient registered already.",Toast.LENGTH_LONG).show();
   				}
   				else
   				{
   					go=code;
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
   			govt_id_=govt_id.getText().toString();
   			pairs.add(new BasicNameValuePair("id",govt_id_));
   			
   			try
   			{
   				HttpClient client=new DefaultHttpClient();
   				HttpPost httpPost=new HttpPost("http://192.168.46.1/proj/patient_check.php");
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
class registering extends AsyncTask<Void,Void,String>
	{
		String res="";
		InputStream is=null;
		ProgressDialog pDialog;
		
		protected void onPreExecute()
		{
			super.onPreExecute();
			pDialog = new ProgressDialog(Add_Patient.this);
			pDialog.setMessage("Registering Patient...");
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
				Toast.makeText(getApplicationContext(),"REGISTRATION COMPLETE :)",Toast.LENGTH_SHORT).show();	
				}
				else
				{
					Toast.makeText(getApplicationContext(),"REGISTRATION INCOMPLETE",Toast.LENGTH_LONG).show();
				}
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_LONG);
			}
			
			Intent i=new Intent(Add_Patient.this,Admin_Rights.class);
			startActivity(i);	
		}
		public void INSERT_DATA()
		{	
			ArrayList<NameValuePair>pairs=new ArrayList<NameValuePair>();
			
			//donor_name=user_name.getText().toString();
			//donor_contact=contact.getText().toString();
			//donor_add=address.getText().toString();
			
			pairs.add(new BasicNameValuePair("patient_name",patient_name));
			pairs.add(new BasicNameValuePair("id",govt_id_));
			pairs.add(new BasicNameValuePair("patient_blood_group",patient_blood));
			pairs.add(new BasicNameValuePair("disease",disease_));
			pairs.add(new BasicNameValuePair("patient_contact_no",patient_contact));
			pairs.add(new BasicNameValuePair("patient_address",patient_add));
			pairs.add(new BasicNameValuePair("patient_city",patient_city));
			pairs.add(new BasicNameValuePair("username",bank_username));
			
			try
			{
				HttpClient client=new DefaultHttpClient();
				HttpPost httpPost=new HttpPost("http://192.168.46.1/proj/patient_registration_andro.php");
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
