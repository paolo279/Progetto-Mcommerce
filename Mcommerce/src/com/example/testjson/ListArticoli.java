package com.example.testjson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

import android.R.integer;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListArticoli extends MainActivity {
	
	public String dati;
	public Bitmap icon;
	public ArrayList<String> prl = new ArrayList<String>();
	public ArrayList<String> prv = new ArrayList<String>();
	public ArrayList<String> id = new ArrayList<String>();
	public ArrayList<String> desc = new ArrayList<String>();
	public ArrayList<String> cod = new ArrayList<String>();
	public int cat;
	public ImageView img;
	public ListView due;
	public ProgressDialog dialog;
	public ExtendedSimpleAdapter lista;
	public ArrayList<HashMap<String, Object>> arrayData;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listarticoli);
		System.out.println("Terza Act lanciata!!");

		cat = getIntent().getExtras().getInt("id");
		due = (ListView) findViewById(R.id.listView3);
		img = (ImageView) findViewById(R.id.imageView2);
		
		arrayData=new ArrayList<HashMap<String,Object>>();
		
		String[] from = {"Description","priceList","priceSell","icona"};
		int[] to ={R.id.titoloArt,R.id.priceList,R.id.priceSell,R.id.imageView2};
		
		lista = new ExtendedSimpleAdapter(getApplicationContext(), arrayData, R.layout.articolo_row, from, to);
		
		dialog = ProgressDialog.show(ListArticoli.this, null, "Caricamento articoli...");
		
		
		
		if(getIntent().getExtras().containsKey("query")){
			
			String query = getIntent().getExtras().getString("query");
			new SerchTask(query).execute();
			
		}else{
			cat = getIntent().getExtras().getInt("id");
			new ListaArticoliTask().execute();
		}
		
		
		due.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ListArticoli.this, SchedaArticolo.class);
				
				intent.putExtra("ProdCode", cod.get(arg2));
				intent.putExtra("id", id.get(arg2));
				intent.putExtra("Description", desc.get(arg2));
				startActivity(intent);
				
			}
		});
	}
	
	protected void onDestroy(){
		super.onDestroy();
		lista.imageLoader.clearCache();
	}
	

	
	public class ListaArticoliTask extends AsyncTask<Void, String, String> {

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			StringBuilder builder = new StringBuilder();		
			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://www.sportincontro.it/test/articoli.php");
			try {
				String stringId = ""+cat;
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
				
				return dati;
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			return null;
		}
		
		protected void onPostExecute(String dati) {
			
			try {
				JSONArray jsonArray = new JSONArray(dati);
				
				for(int i=0; i<jsonArray.length(); i++){
					
					
					desc.add(jsonArray.getJSONObject(i).getString("Description"));
					id.add(jsonArray.getJSONObject(i).getString("IdProduct"));
					cod.add(jsonArray.getJSONObject(i).getString("ProdCode"));
					String[] price = new String[2];
					price = getPrices(jsonArray.getJSONObject(i).getString("priceList"));
					prl.add(price[0]);
					prv.add(price[1]);
					
				}
					new TaskImg().execute();
					
					
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			
			
		
		}
	}
	
	
	   	 
        class TaskImg extends AsyncTask<Void, Bitmap, Bitmap[]>{
        	
        	String[] imageUrls;
        	

			@Override
			protected Bitmap[] doInBackground(Void... par) {
				// TODO Auto-generated method stub
				
				Bitmap[] bitmaplist = new Bitmap[id.size()];
	        	
	        	StringBuilder builder = new StringBuilder();
				HttpClient client = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost("http://www.sportincontro.it/test/foto.php");
	    		try {
	    			
	    			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
	    			
	    			for(int i=0;i<id.size();i++){
	    				nameValuePair.add(new BasicNameValuePair(Integer.toString(i),id.get(i)));
	    			}
	    			nameValuePair.add(new BasicNameValuePair("nFoto", Integer.toString(id.size())));
	    			nameValuePair.add(new BasicNameValuePair("tipofoto","2"));
	    			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
	    			
	    			HttpResponse response = client.execute(httpPost);
						
						HttpEntity entity = response.getEntity();
						InputStream content = entity.getContent();
						BufferedReader reader = new BufferedReader(new InputStreamReader(content));
						imageUrls = new String[id.size()];
						String line;
						int j=0;
						while ((line = reader.readLine()) != null) 
						{
							
							line = line.replace(" ","%20");
							
							imageUrls[j] = "http://www.sportincontro.it/files/sport_incontro_Files/Foto/"+line;
							j++;
						} 
						
						
						
					
	    			return bitmaplist;
	    			
					
	     			
	     		} catch (MalformedURLException e) {
	     			// TODO Auto-generated catch block
	     			e.printStackTrace();
	     		} catch (IOException e) {
	     			// TODO Auto-generated catch block
	     			e.printStackTrace();
	     		}
				return null;
			
			}
        
			protected void onPostExecute(Bitmap bitmaplist[]) {
				
				for(int i=0;i<id.size();i++){
					
					HashMap<String,Object> articleMap=new HashMap<String, Object>();
					
					articleMap.put("icona",imageUrls[i]); //old:bitmaplist[i]
					articleMap.put("Description", desc.get(i));
					articleMap.put("priceList", prl.get(i));
					articleMap.put("priceSell", prv.get(i));
					
					arrayData.add(articleMap);
				}
				
				
					dialog.dismiss();
					due.setAdapter(lista);
				
					
				
			}
        }
        
        
        
        public class SerchTask extends AsyncTask<Void,String,String>{
        	String query;
        	
        	public SerchTask(String query){
        		this.query = query;
        	}
        	
        	
    		@Override
    		protected String doInBackground(Void... params) {
    			// TODO Auto-generated method stub
    			
    			StringBuilder builder = new StringBuilder();		
    			HttpClient client = new DefaultHttpClient();
    			HttpPost httpPost = new HttpPost("http://www.sportincontro.it/test/CercaArticoli.php");
    			try {
    				
    				List<NameValuePair> nameValuePair= new ArrayList<NameValuePair>();
    				nameValuePair.add(new BasicNameValuePair("query", query));
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
    				
    				return dati;
    				
    			} catch (ClientProtocolException e) {
    				e.printStackTrace();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    
    			return null;
    			
    		}
    		
    		
    		protected void onPostExecute(String dati) {
    			
    			///risolvere il problema della ricerca senza alcun risultato !!!
    			if(dati.equals("null")){
    				
    				dialog.dismiss();
    				Toast toast = Toast.makeText(getApplicationContext(), "Articolo non Trovato", 1000);
    				toast.show();
					Intent intent = new Intent(ListArticoli.this,MainActivity.class);
					startActivity(intent);
					
				}else{
    			
    			try {
    				JSONArray jsonArray = new JSONArray(dati);
    					
    					for(int i=0; i<jsonArray.length(); i++){
        					
        					
        					desc.add(jsonArray.getJSONObject(i).getString("Description"));
        					id.add(jsonArray.getJSONObject(i).getString("IdProduct"));
        					cod.add(jsonArray.getJSONObject(i).getString("ProdCode"));
        					String[] price = new String[2];
        					price = getPrices(jsonArray.getJSONObject(i).getString("priceList"));
        					prl.add(price[0]);
        					prv.add(price[1]);
        					
        				}
        					new TaskImg().execute();
        		
    				
        			} catch (JSONException e) {
        				e.printStackTrace();
        			}
    	
				}
    		}

        }    
        
     
		
	public  String[] getPrices(String pricemap){
		String a = pricemap.substring(4);
		
		int j = a.indexOf("|");
		
		String prL = a.substring(0, j);
		String b = a.substring(j+8);
		int k = b.indexOf("|");
		String prV = b.substring(0, k);
		System.out.println(prL);
		
		NumberFormat nf = NumberFormat.getInstance(Locale.ITALIAN);
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
		double plivainc =  Double.parseDouble(prL)*1.21;
		double pvivainc =  Double.parseDouble(prV)*1.21;
		
		String[] price ={nf.format(plivainc),nf.format(pvivainc)};
		
		return price;
	}
	
	public static Bitmap getBitmap(String url) throws MalformedURLException, IOException{
		HttpURLConnection connection;
		connection = (HttpURLConnection) new URL(url).openConnection();
	
		connection.setDoInput(true);
		connection.connect();
		InputStream is = connection.getInputStream();
		Bitmap bitmap =BitmapFactory.decodeStream(is);
		is.close();
		
		return bitmap;
		
	}
	

}
	
