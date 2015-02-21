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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Donor extends Activity
{
	Spinner blood,city;
	Button search;
	String blood_selected,city_selected;
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
            "--Sylhet Division--",
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
		setContentView(R.layout.donors);
		
		blood=(Spinner)findViewById(R.id.blood);
		city=(Spinner)findViewById(R.id.city);
		search=(Button)findViewById(R.id.search);
		
		ArrayAdapter<String>blood_=new ArrayAdapter<String>(Donor.this,android.R.layout.simple_spinner_item,blood_list);
		blood.setAdapter(blood_);
		blood.setOnItemSelectedListener(new selected_());
		
		ArrayAdapter<String>city_=new ArrayAdapter<String>(Donor.this,android.R.layout.simple_spinner_item,city_list);
		city.setAdapter(city_);
		city.setOnItemSelectedListener(new selected_());
		
		search.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v)
			{
			  if(validation())
			  new availablity().execute();
			  else
			  {
				validation_error();   
			  }
			}
		  });
	}
	
	protected void onPause()
	{
		super.onPause();
		finish();
	}

    public boolean validation()
    {
    	if(blood_selected.equals("Select Blood Group") || (city_selected.equals("Select City")||city_selected.equals("--Dhaka Division--")||city_selected.equals("--Barisal Division--")||city_selected.equals("--Khulna Division--")||city_selected.equals("--Rajshahi Division--")||city_selected.equals("--Rangpur Division--")|| (city_selected.equals("--Sylhet Division--"))))
		return false;
    	else
    	return true;
    }
    public void validation_error()
    {
    	AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("Illegal Input");
		builder.setMessage("Please select only legal option.");
	    builder.setPositiveButton("Ok",null);
	    AlertDialog dialog=builder.show();
    }
	public class selected_ implements OnItemSelectedListener
	{
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3)
		{
		 if(arg0.getId()==R.id.blood)
		  {
			blood_selected=blood.getSelectedItem().toString();	 
		  }
		  else
		  {
			 city_selected=city.getSelectedItem().toString();	 
		  }
		 }
		public void onNothingSelected(AdapterView<?> arg0)
		{
			
		}
	}
	class availablity extends AsyncTask<Void,Void,String>
	{
		String res="";
		InputStream is=null;
		ProgressDialog pDialog;
		
		protected void onPreExecute()
		{
			super.onPreExecute();
			pDialog = new ProgressDialog(Donor.this);
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
				
				if(code==1)
				{
					  Intent i=new Intent(Donor.this,ExpandableListMain.class);
					  //Intent i=new Intent(Donor.this,Donor_Information.class);
					  Bundle bundle=new Bundle();
					  bundle.putString("BLOOD",blood_selected);
					  bundle.putString("CITY",city_selected);
					  i.putExtras(bundle);
					  startActivity(i);  	
				}
				else
				{
					Toast.makeText(getApplicationContext(),"SORRY,Donor does not available.",Toast.LENGTH_LONG).show();
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
			
			pairs.add(new BasicNameValuePair("bgroup",blood_selected));
			pairs.add(new BasicNameValuePair("city",city_selected));
			
			try
			{
				HttpClient client=new DefaultHttpClient();
				HttpPost httpPost=new HttpPost("http://169.254.80.80/blood/availablity_andro.php");
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
