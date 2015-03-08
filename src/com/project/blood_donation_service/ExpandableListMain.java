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

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class ExpandableListMain extends  Activity
{
	private ArrayList<Parent> parents=new ArrayList<Parent>();
	ExpandableListView donor_list;
	String bgroup,city;
    
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refined_);
        
        donor_list=(ExpandableListView)findViewById(R.id.expandable);
		donor_list.setGroupIndicator(null);
		donor_list.setDividerHeight(3);
        
        Bundle bundle=getIntent().getExtras();
		bgroup=bundle.getString("BLOOD");
		city=bundle.getString("CITY");
	
        setTitle("Donor searching for-Blood : "+bgroup+",City : "+city);
        
        new listing().execute();
      }
    
    private void loadHosts()
    {
        
             //Create ExpandableListAdapter Object
            final MyExpandableListAdapter mAdapter = new MyExpandableListAdapter(parents,ExpandableListMain.this);
             
            // Set Adapter to ExpandableList Adapter
            donor_list.setAdapter(mAdapter);
    }
    
    class listing extends AsyncTask<Void,Void,String>
	{
		String res="";
		InputStream is=null;
		ProgressDialog pDialog;
		
		protected void onPreExecute()
		{
			super.onPreExecute();
			pDialog = new ProgressDialog(ExpandableListMain.this);
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
				
			}
			catch (JSONException e)
			{
				Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
			}
			
			loadHosts();
			
		}
		public void INSERT_DATA()
		{	
			ArrayList<NameValuePair>pairs=new ArrayList<NameValuePair>();
			
			pairs.add(new BasicNameValuePair("bgroup",bgroup));
			pairs.add(new BasicNameValuePair("city",city));
			
			try
			{
				HttpClient client=new DefaultHttpClient();
				//HttpPost httpPost=new HttpPost("http://169.254.80.80/blood/after_search_andro.php");
				HttpPost httpPost=new HttpPost("http://"+Server_Info.ip_add+"after_search_andro.php");
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
