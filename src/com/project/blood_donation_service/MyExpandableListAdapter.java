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
import android.content.Context;
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

   public class MyExpandableListAdapter extends BaseExpandableListAdapter
    {
         
    	private ArrayList<Parent> parents=new ArrayList<Parent>();
        private LayoutInflater inflater;
 
        public MyExpandableListAdapter(ArrayList<Parent> parents,Activity a)
        {
        	this.parents=parents;
            // Create Layout Inflator
            inflater = LayoutInflater.from(a);
        	//inflater =(LayoutInflater)a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    
 
