package com.example.testjson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;


public class SecondActivity extends MainActivity {
	
	public String dati;
	public Vector<String> v = new Vector<String>();
	public Vector<Integer> id = new Vector<Integer>();
	public String cat;
	public ListView due;
	public ListAdapter lista;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second);
		
		
		
		cat = getIntent().getExtras().getString("categoria");
		
		due = (ListView) findViewById(R.id.ListView2);
		
		lista = new ArrayAdapter(this, android.R.layout.simple_list_item_1,v);
		
		//due.setAdapter(lista);
		
		new SubcategoryTask().execute();
		
		due.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
					int c = id.get(arg2);
					Intent intent = new Intent(SecondActivity.this,ListArticoli.class);
					intent.putExtra("id", c);
					
					startActivity(intent);
				};
				
			
			
		});

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public class SubcategoryTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			StringBuilder builder = new StringBuilder();		
			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://www.sportincontro.it/test/subcategorie.php");
			try {
				List<NameValuePair> nameValuePair= new ArrayList<NameValuePair>();
				nameValuePair.add(new BasicNameValuePair("categoria", cat));
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
				HttpResponse response = client.execute(httpPost);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 200) {
					HttpEntity entity = response.getEntity();
					InputStream content = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(content));
					String line;
					while ((line = reader.readLine()) != null) 
					{
						builder.append(line);
						
					} //end while
					dati = builder.toString();
					
				} 
				
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				JSONArray jsonArray = new JSONArray(dati);
				
				for(int i=0; i<jsonArray.length(); i++){
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					v.add(jsonArray.getJSONObject(i).getString("Descrizione categoria"));
					id.add(jsonArray.getJSONObject(i).getInt("ID categoria articolo"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}	
			
			return null;
		}

		protected void onPostExecute(Void unused) {
			
			due.setAdapter(lista);
		
		}
		
		
	}
}
