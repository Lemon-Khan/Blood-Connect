package com.project.blood_donation_service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
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

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity 
{
	Button donor,sign,exit;
    boolean server_conn=false;
    String url="http://"+Server_Info.ip_add+"index_andro.php";
	int timeout=10000;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.option);
		
		StrictMode.enableDefaults();
		
		donor=(Button)findViewById(R.id.donor);
		sign=(Button)findViewById(R.id.sign);
		exit=(Button)findViewById(R.id.exit);
		
		checkInternetConnection();
		check_server_conn();
		
		donor.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v)
			{
				Intent i=new Intent(MainActivity.this,Donor.class);
				startActivity(i);
			}
		  });
		
		 sign.setOnClickListener(new View.OnClickListener()
		       {
					public void onClick(View arg0)
					{
						Intent i=new Intent(MainActivity.this,Signing.class);
						startActivity(i);
					}
				}
			);
		 
		 exit.setOnClickListener(new View.OnClickListener()
	       {
				public void onClick(View arg0)
				{
					finish();
				}
			}
		);
				
				
	}
	public void checkInternetConnection()
	{
		    ConnectivityManager check=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			
		    NetworkInfo info=check.getActiveNetworkInfo();
			
			if(info!=null && info.isConnected())
			{
			 Toast.makeText(this,"Network is Connected",Toast.LENGTH_SHORT).show();		
			}
	        else
		    {
	           Toast.makeText(this,"Network is not Connected",Toast.LENGTH_SHORT).show();
		    }
	}
	public boolean check_server_conn()
		{
			try
				{
				    URL myUrl = new URL(url);
				    HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
				    connection.setConnectTimeout(timeout);
				    connection.connect();
				    Toast.makeText(this,"Server is on...",Toast.LENGTH_LONG).show();
				    return true;
				} 
				catch (Exception e)
				{
					Toast.makeText(this,"Sorry,Server is off...",Toast.LENGTH_LONG).show();
					return false;
				}
		}
    public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
}
