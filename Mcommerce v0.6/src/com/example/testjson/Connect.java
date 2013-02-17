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


public class Connect {
	
	public static String dati;
	public static Vector<String> v = new Vector<String>();
	public Vector<Integer> id = new Vector<Integer>();
	


	

	
	
	public static void getCategorie() {

				
				StringBuilder builder = new StringBuilder();		
				HttpClient client = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost("http://www.sportincontro.it/test/categorie.php");
				try {
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
						v.add(jsonArray.getJSONObject(i).getString("Descrizione categoria"));}
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				
				
				
			}
			

	
	public void getSubCategorie(String categoria){
		
		StringBuilder builder = new StringBuilder();		
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost("http://www.sportincontro.it/test/subcategorie.php");
		try {
			List<NameValuePair> nameValuePair= new ArrayList<NameValuePair>();
			nameValuePair.add(new BasicNameValuePair("categoria", categoria));
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
				v.add(jsonArray.getJSONObject(i).getString("Descrizione categoria"));
				id.add(jsonArray.getJSONObject(i).getInt("ID categoria articolo"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}	
		
	}
	
	public void getArticoli(int id){
		
		StringBuilder builder = new StringBuilder();		
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost("http://www.sportincontro.it/test/articoli.php");
		try {
			String stringId = ""+id;
			List<NameValuePair> nameValuePair= new ArrayList<NameValuePair>();
			nameValuePair.add(new BasicNameValuePair("idcategoria", stringId));
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
				v.add(jsonArray.getJSONObject(i).getString("Description"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void getTaglie(String codice){
		
	}
}
