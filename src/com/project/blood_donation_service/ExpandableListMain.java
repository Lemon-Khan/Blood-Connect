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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class ExpandableListMain extends  ExpandableListActivity
{
	private ArrayList<Parent> parents=new ArrayList<Parent>();
	
	String bgroup,city;
    
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.refined_);
        
        Bundle bundle=getIntent().getExtras();
		bgroup=bundle.getString("BLOOD");
		city=bundle.getString("CITY");
	
        setTitle("Donor searching for-Blood : "+bgroup+",City : "+city);
        
        // Set ExpandableListView values ***********
        getExpandableListView().setGroupIndicator(null);
        getExpandableListView().setDividerHeight(2);
        registerForContextMenu(getExpandableListView());
        
        new listing().execute();
        
        //Creating static data in arraylist
        //final ArrayList<Parent> dummyList = buildDummyData();
         
        // Adding ArrayList data to ExpandableListView values
        //loadHosts();
    }
    private ArrayList<Parent> buildDummyData()
    {
        // Creating ArrayList of type parent class to store parent class objects
        final ArrayList<Parent> list = new ArrayList<Parent>();
        
        for (int i = 1; i < 4; i++)
        {
            //Create parent class object
              final Parent parent = new Parent();
             
              // Set values in parent class object
                 
              parent.setText1("Parent "+i);
              parent.setText2("Disable App"+i);
              parent.setChildren(new ArrayList<Child>());
               
              // Create Child class object 
                final Child child = new Child();
                child.setText1("" + i);
                child.setText2("Child 0");
                 
                //Add Child class object to parent class object
                parent.getChildren().add(child);
                
                list.add(parent);
         }
       return list;
    }
    private void loadHosts()
    {
        // Check for ExpandableListAdapter object
        if (this.getExpandableListAdapter() == null)
        {
             //Create ExpandableListAdapter Object
            final MyExpandableListAdapter mAdapter = new MyExpandableListAdapter();
             
            // Set Adapter to ExpandableList Adapter
            this.setListAdapter(mAdapter);
        }
        else
        {
             // Refresh ExpandableListView data 
            ((MyExpandableListAdapter)getExpandableListAdapter()).notifyDataSetChanged();
        }   
    }
    private class MyExpandableListAdapter extends BaseExpandableListAdapter
    {
         
 
        private LayoutInflater inflater;
 
        public MyExpandableListAdapter()
        {
            // Create Layout Inflator
            inflater = LayoutInflater.from(ExpandableListMain.this);
        }
     
         
        // This Function used to inflate parent rows view
         
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parentView)
        {
            final Parent parent = parents.get(groupPosition);
             
            // Inflate grouprow.xml file for parent rows
            convertView = inflater.inflate(R.layout.grouprow, parentView, false); 
             
            // Get grouprow.xml file elements and set values
            ((TextView) convertView.findViewById(R.id.text1)).setText(parent.getText1());
            ((TextView) convertView.findViewById(R.id.text2)).setText(parent.getText2());
            
            if(isExpanded)
            {
            	((ImageView) convertView.findViewById(R.id.imageView1)).setImageResource(R.drawable.minus);
            }
            else
            {
            	((ImageView) convertView.findViewById(R.id.imageView1)).setImageResource(R.drawable.plus);
            }
            
            return convertView;
        }
 
         
        // This Function used to inflate child rows view
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parentView)
        {
            final Parent parent = parents.get(groupPosition);
            final Child child = parent.getChildren().get(childPosition);
             
            // Inflate childrow.xml file for child rows
            convertView = inflater.inflate(R.layout.childrow, parentView, false);
             
            // Get childrow.xml file elements and set values
            ((TextView) convertView.findViewById(R.id.text1)).setText(child.getText1());
            ((TextView) convertView.findViewById(R.id.text2)).setText(child.getText2());
            ((TextView) convertView.findViewById(R.id.text3)).setText(child.getText3());
            
             
            return convertView;
        }
 
         
        @Override
        public Object getChild(int groupPosition, int childPosition)
        {
            //Log.i("Childs", groupPosition+"=  getChild =="+childPosition);
            return parents.get(groupPosition).getChildren().get(childPosition);
        }
 
        //Call when child row clicked
        @Override
        public long getChildId(int groupPosition, int childPosition)
        {
            /****** When Child row clicked then this function call *******/
           return childPosition;
        }
 
        @Override
        public int getChildrenCount(int groupPosition)
        {
            int size=0;
            if(parents.get(groupPosition).getChildren()!=null)
                size = parents.get(groupPosition).getChildren().size();
            return size;
        }
      
         
        @Override
        public Object getGroup(int groupPosition)
        {
            Log.i("Parent", groupPosition+"=  getGroup ");
             
            return parents.get(groupPosition);
        }
 
        @Override
        public int getGroupCount()
        {
            return parents.size();
        }
 
        //Call when parent row clicked
        @Override
        public long getGroupId(int groupPosition)
        {
            //Log.i("Parent", groupPosition+"=  getGroupId "+ParentClickStatus);
             
            /*if(groupPosition==2 && ParentClickStatus!=groupPosition){
                 
                //Alert to user
                Toast.makeText(getApplicationContext(), "Parent :"+groupPosition , 
                        Toast.LENGTH_LONG).show();
            }
             
            ParentClickStatus=groupPosition;
            
            if(ParentClickStatus==0)
              ParentClickStatus=-1;*/
           
             return groupPosition;
        }
 
        @Override
        public void notifyDataSetChanged()
        {
            // Refresh List rows
            super.notifyDataSetChanged();
        }
 
        @Override
        public boolean isEmpty()
        {
            return ((parents == null) || parents.isEmpty());
        }
 
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition)
        {
            return true;
        }
 
        @Override
        public boolean hasStableIds()
        {
            return true;
        }
 
        @Override
        public boolean areAllItemsEnabled()
        {
            return true;
        }
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
				HttpPost httpPost=new HttpPost("http://169.254.80.80/blood/after_search_andro.php");
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
