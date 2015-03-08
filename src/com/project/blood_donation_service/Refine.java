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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.project.blood_donation_service.Donor_Information.listing;

public class Refine  extends Activity
{
	String bgroup,city,refine_op,bank_username;
	JSONArray json;
	ListView donor_list;
	
	SharedPreferences SHARING;
	public static String fileName="BloodDonation";
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.donor_info);
		
		Bundle bundle=getIntent().getExtras();
		bgroup=bundle.getString("BLOOD");
		city=bundle.getString("CITY");
		refine_op=bundle.getString("REFINE");
		
		SHARING=getSharedPreferences(fileName, 0);
		String title=SHARING.getString("USERNAME","Your Session is end.");
		
		bank_username=title;
		
		donor_list=(ListView)findViewById(R.id.donor_list);
		setTitle("Blood group : "+bgroup+","+"City : "+city+",Status : "+refine_op);
		new listing().execute();
		
		donor_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View view, int position,long id)
			{
				try
				{
			       JSONObject jsonObject=json.getJSONObject(position);
			       
			       String donor_name=jsonObject.getString("donor_name");
			       String donor_govt_id=jsonObject.getString("donor_govt_id");
			       String donor_contact_no=jsonObject.getString("donor_contact_no");
			       String donor_address=jsonObject.getString("donor_address");
			       String donor_city=jsonObject.getString("donor_city");
			       
			       details(donor_name,donor_govt_id,donor_contact_no,donor_address,donor_city);
				}
				catch (JSONException e)
				{
					Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	public void details(String donor_name,String donor_govt_id,String donor_contact_no,String donor_address,String donor_city)
	{
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("Details");
		builder.setMessage("Name : "+donor_name+"\n"+"Govt. ID : "+donor_govt_id+"\n"+"Contact : "+donor_contact_no+"\n"+"Address : "+donor_address+"\n"+"City : "+donor_city);
	    builder.setPositiveButton("Ok",null);
	    AlertDialog dialog=builder.show();
	}
	
	class listing extends AsyncTask<Void,Void,String>
	{
		String res="";
		InputStream is=null;
		ProgressDialog pDialog;
		
		protected void onPreExecute()
		{
			super.onPreExecute();
			pDialog = new ProgressDialog(Refine.this);
			pDialog.setMessage("Searching...");
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
				JSONArray	jArray=new JSONArray(result);
				setting_data(jArray);
			}
			catch (JSONException e)
			{
				Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
			}
			
		}
		public void INSERT_DATA()
		{	
			ArrayList<NameValuePair>pairs=new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("username",bank_username));
			pairs.add(new BasicNameValuePair("bgroup",bgroup));
			pairs.add(new BasicNameValuePair("city",city));
			pairs.add(new BasicNameValuePair("refine",refine_op));
			try
			{
				HttpClient client=new DefaultHttpClient();
				HttpPost httpPost=new HttpPost("http://"+Server_Info.ip_add+"donor_list_andro.php");
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
		public void setting_data(JSONArray jArray)
		{
			json=jArray;
			donor_list.setAdapter(new DonorListView(json,Refine.this));
		}
		
	  }

}
