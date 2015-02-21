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
import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.project.blood_donation_service.Donor_Information.listing;
import com.project.blood_donation_service.MyExpandableListAdapter;

public class Refined_Expand  extends Activity
{
	private ArrayList<Parent> parents=new ArrayList<Parent>();
	
	String bgroup,city,refine_op,bank_username;
	JSONArray json;
	ExpandableListView donor_list;
	
	
	SharedPreferences SHARING;
	public static String fileName="BloodDonation";
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.refined_);
		
		//getExpandableListView().setGroupIndicator(null);
        //getExpandableListView().setDividerHeight(2);
        //registerForContextMenu(getExpandableListView());
		
		donor_list=(ExpandableListView)findViewById(R.id.expandable);
		donor_list.setGroupIndicator(null);
		donor_list.setDividerHeight(3);
		
		Bundle bundle=getIntent().getExtras();
		bgroup=bundle.getString("BLOOD");
		city=bundle.getString("CITY");
		refine_op=bundle.getString("REFINE");
		
		setTitle("Blood : "+bgroup+",City : "+city+",Status : "+refine_op);
		
		SHARING=getSharedPreferences(fileName, 0);
		String title=SHARING.getString("USERNAME","Your Session is end.");
		bank_username=title;
	    new listing().execute();	
	}
	
	class listing extends AsyncTask<Void,Void,String>
	{
		String res="";
		InputStream is=null;
		ProgressDialog pDialog;
		
		protected void onPreExecute()
		{
			super.onPreExecute();
			pDialog = new ProgressDialog(Refined_Expand.this);
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
				
				for(int i=0;i<jArray.length();i++)
				{
				   Parent obj_par=new Parent();
				   Child obj_child=new Child();
				   
				   JSONObject jsonObject=jArray.getJSONObject(i);
				       
			       String donor_name=jsonObject.getString("donor_name");
			       String donor_govt_id=jsonObject.getString("donor_govt_id");
			       String donor_contact_no=jsonObject.getString("donor_contact_no");
			       String donor_address=jsonObject.getString("donor_address");
			       String donor_city=jsonObject.getString("donor_city");
			       
			       obj_par.setText1(donor_name);
			       obj_par.setText2(donor_govt_id);
			       
			       obj_par.setChildren(new ArrayList<Child>());
			       
			       obj_child.setText1(donor_contact_no);
			       obj_child.setText2(donor_address);
			       obj_child.setText3(donor_city);
			       
			       obj_par.getChildren().add(obj_child);
			       
			       parents.add(obj_par);
			       
				}
			loadHost();
				
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
				HttpPost httpPost=new HttpPost("http://169.254.80.80/blood/donor_list_andro.php");
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
	public void loadHost()
	{
		/*if (this.getExpandableListAdapter() == null)
        {
             //Create ExpandableListAdapter Object
            final MyExpandableListAdapter mAdapter = new MyExpandableListAdapter(parents,Refined_Expand.this);
             
            // Set Adapter to ExpandableList Adapter
            this.setListAdapter(mAdapter);   
        }
        else
        {
             // Refresh ExpandableListView data 
            ((MyExpandableListAdapter)getExpandableListAdapter()).notifyDataSetChanged();
        }  */
		final MyExpandableListAdapter mAdapter = new MyExpandableListAdapter(parents,Refined_Expand.this);
        
        // Set Adapter to ExpandableList Adapter
        //this.setListAdapter(mAdapter);
        donor_list.setAdapter(mAdapter);
	}

}
