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
import org.json.JSONException;
import org.json.JSONObject;

import com.project.blood_donation_service.Add_Donor.registering;
import com.project.blood_donation_service.Add_Donor.selected_;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Inquiry extends Activity
{
	Spinner blood_grp,city,refine;
	Button search;
	
	SharedPreferences SHARING;
	public static String fileName="BloodDonation";
	
	String bank_username,donor_blood,donor_city,refine_op;
	String blood_list[]={"Select Blood Group","A+","B+","A-","B-","O+","O-","AB+","AB-"};
	String refine_list[]={"Select Refine Option","Available","All"};
	
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
		setContentView(R.layout.inquiry);
		
		SHARING=getSharedPreferences(fileName, 0);
		String title=SHARING.getString("USERNAME","Your Session is end.");
		setTitle("Admin Inquiry in,"+title);
		
		bank_username=title;
		
		blood_grp=(Spinner)findViewById(R.id.donor_blood_grp);
		city=(Spinner)findViewById(R.id.donor_city_);
		refine=(Spinner)findViewById(R.id.refine);
		search=(Button)findViewById(R.id.search);
		
		
		ArrayAdapter<String>blood_=new ArrayAdapter<String>(Inquiry.this,android.R.layout.simple_spinner_item,blood_list);
		blood_grp.setAdapter(blood_);
		blood_grp.setOnItemSelectedListener(new selected_());
		
		ArrayAdapter<String>city_=new ArrayAdapter<String>(Inquiry.this,android.R.layout.simple_spinner_item,city_list);
		city.setAdapter(city_);
		city.setOnItemSelectedListener(new selected_());
		
		ArrayAdapter<String>refine_=new ArrayAdapter<String>(Inquiry.this,android.R.layout.simple_spinner_item,refine_list);
		refine.setAdapter(refine_);
		refine.setOnItemSelectedListener(new selected_());
		
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
	public boolean validation()
    {
    	if(donor_blood.equals("Select Blood Group") || (donor_city.equals("Select City")||donor_city.equals("--Dhaka Division--")||donor_city.equals("--Barisal Division--")||donor_city.equals("--Khulna Division--")||donor_city.equals("--Rajshahi Division--")||donor_city.equals("--Rangpur Division--")|| (donor_city.equals("--Sylhet Division--"))))
    		return false;
    	if(donor_city.equals("Select City"))
    		return false;
    	if(refine.equals("Select Refine Option"))
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
		 if(arg0.getId()==R.id.donor_blood_grp)
		  {
			 donor_blood=blood_grp.getSelectedItem().toString();	 
		  }
		  else if(arg0.getId()==R.id.donor_city_)
		  {
			  donor_city=city.getSelectedItem().toString();	 
		  }
		  else
		  {
			  refine_op=refine.getSelectedItem().toString();	 
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
			pDialog = new ProgressDialog(Inquiry.this);
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
					  //Intent i=new Intent(Inquiry.this,Refine.class);
					  Intent i=new Intent(Inquiry.this,Refined_Expand.class);
					  Bundle bundle=new Bundle();
					  bundle.putString("BLOOD",donor_blood);
					  bundle.putString("CITY",donor_city);
					  bundle.putString("REFINE",refine_op);
					  i.putExtras(bundle);
					  startActivity(i);  	
				}
				else
				{
					Toast.makeText(getApplicationContext(),"SORRY,Donor is not available.",Toast.LENGTH_LONG).show();
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
			
			pairs.add(new BasicNameValuePair("bgroup",donor_blood));
			pairs.add(new BasicNameValuePair("city",donor_city));
			pairs.add(new BasicNameValuePair("refine",refine_op));
			pairs.add(new BasicNameValuePair("username",bank_username));
			
			try
			{
				HttpClient client=new DefaultHttpClient();
				HttpPost httpPost=new HttpPost("http://"+Server_Info.ip_add+"refined_availablity_andro.php");
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
