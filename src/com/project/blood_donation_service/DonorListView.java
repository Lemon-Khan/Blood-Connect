package com.project.blood_donation_service;

import org.json.JSONArray;
import org.json.JSONObject;

import com.project.blood_donation_service.Donor_Information.listing;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DonorListView extends BaseAdapter
{
	private JSONArray json;
	private Activity activity;
	private static LayoutInflater inflater=null;
	
	public DonorListView(JSONArray jArray,Activity act)
	{
		json=jArray;
		activity=act;
		inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public int getCount()
	{
		return json.length();
	}

	public Object getItem(int position)
	{
		return position;
	}

	public long getItemId(int position)
	{
		
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		ListCell cell;
		
		if(convertView==null)
		{
			convertView=inflater.inflate(R.layout.cell,null);
			cell=new ListCell();
			cell.name=(TextView)convertView.findViewById(R.id.donor_name);
			cell.contact=(TextView)convertView.findViewById(R.id.donor_contact);
			
			convertView.setTag(cell);
		}
		else
		{
			cell=(ListCell)convertView.getTag();
		}
		
		try
		{
			JSONObject json_obj=json.getJSONObject(position);
			cell.name.setText(json_obj.getString("donor_name"));
			cell.contact.setText(json_obj.getString("donor_contact_no"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return convertView;
	}
	
	private class ListCell
	{
		TextView name;
		TextView contact;
	}
}
