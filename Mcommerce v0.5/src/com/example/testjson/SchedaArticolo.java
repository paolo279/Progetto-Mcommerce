package com.example.testjson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SchedaArticolo extends MainActivity {
	
	private ProgressDialog pd;
	public String dati, id, idP, url;
	public TextView colore, desc;
	public ImageView img;
	public Button btn, buy;
	public String[] taglie;
	public AlertDialog scelta;
	public AlertDialog.Builder builder;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedarticolo);
		
		//prendo i riferimenti ai widget
		TextView uno = (TextView) findViewById(R.id.textView1);
		TextView due = (TextView) findViewById(R.id.textView2);
		colore = (TextView) findViewById(R.id.textView3);
		desc = (TextView) findViewById(R.id.textView4);
		img = (ImageView) findViewById(R.id.imageView1);
		btn = (Button) findViewById(R.id.button1);
		buy = (Button) findViewById(R.id.button2);
		
		
		//parte il progressdialog
		pd = ProgressDialog.show(SchedaArticolo.this, "Caricamento...", "Sto caricando");
		
		uno.setText(getIntent().getExtras().getString("Description"));
		due.setText(getIntent().getExtras().getString("ProdCode"));
		id= getIntent().getExtras().getString("id");
		
		// preleva tutti i dati !!
		new DownloadImg().execute();
		new SchedaArtTask().execute();
		
		
		
		
		// gestione evento di tocco sul bottone "Taglie Disponibili"
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				new TaglieTask().execute(); 
				
					//in caso viene selezionata una taglia il Bottone "Acquista" diventa cliccabile e lo aggiunge al carrello !
	
					buy.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							
							Intent intent = new Intent(SchedaArticolo.this,Cart.class);
							intent.putExtra("url", url);
							startActivity(intent);
							
							
						}
					});	
			}
		});
		
	}
	
	
	//Task per il download dei dati

	public class SchedaArtTask extends AsyncTask<Void, String, String> {

		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			StringBuilder builder = new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://www.sportincontro.it/test/schedart.php");
			
			try {
				List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
				nameValuePair.add(new BasicNameValuePair("id",id));
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
						
					} 
					dati = builder.toString();
					System.out.println(dati);
				}
				
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return dati;
		}
		
		protected void onPostExecute(String dati){
			try {
				
				JSONArray jsonArray = new JSONArray(dati);
				
				System.out.print(jsonArray.toString());
				
				colore.setText(jsonArray.getJSONObject(0).getString("CustomT2Desc"));
				String htmlText = jsonArray.getJSONObject(0).getString("DescriptionExt1");
				desc.setText(Html.fromHtml(htmlText));
				pd.dismiss();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	// Task per il download della foto
	
	private class DownloadImg extends AsyncTask<Void,Bitmap,Bitmap> {
	   	 
        protected void onPostExecute(Bitmap bitmap) {
        	img.setImageBitmap(bitmap);
       }
        
        @Override
        protected Bitmap doInBackground(Void... params) {
        	

        	HttpURLConnection connection;
        	StringBuilder builder = new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://www.sportincontro.it/test/foto.php");
    		try {
    			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
    			nameValuePair.add(new BasicNameValuePair("0",id));
    			nameValuePair.add(new BasicNameValuePair("nFoto","1"));
    			nameValuePair.add(new BasicNameValuePair("tipofoto","3"));
    			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
    			HttpResponse response = client.execute(httpPost);
				
	        
					HttpEntity entity = response.getEntity();
					InputStream content = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(content));
					String line;
					while ((line = reader.readLine()) != null) 
					{
						builder.append(line);
						
					} 
					
					String imageUrl = "http://www.sportincontro.it/files/sport_incontro_Files/Foto/";
					imageUrl = imageUrl+builder.toString();
    			
				System.out.println(imageUrl);
				
    			connection = (HttpURLConnection) new URL(imageUrl).openConnection();
    			

    			connection.setDoInput(true);
    			connection.connect();
    			InputStream is = connection.getInputStream();
    			Bitmap bitmap =BitmapFactory.decodeStream(is);
    			is.close();
    			
    			return bitmap;

     			
     		} catch (MalformedURLException e) {
     			// TODO Auto-generated catch block
     			e.printStackTrace();
     		} catch (IOException e) {
     			// TODO Auto-generated catch block
     			e.printStackTrace();
     		}
			return null;
        }
	}
	
	
		//Task per la visualizzazione delle taglie in un AlertDialog
	
        public class TaglieTask extends AsyncTask<Void,String,String>{

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				StringBuilder builder = new StringBuilder();
				HttpClient client = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost("http://www.sportincontro.it/test/taglie.php");
				
				try {
					List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
					nameValuePair.add(new BasicNameValuePair("id",id));
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
							
						} 
						dati = builder.toString();
					}
					
					
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return dati;
			}
        	
		
			@SuppressLint("ResourceAsColor")
			protected void onPostExecute(String dati){
				try {
					final JSONArray jsonArray = new JSONArray(dati);
					
					System.out.print(jsonArray.toString());
					
					taglie = new String[jsonArray.length()];
					final Vector<String> IdProduct = new Vector<String>();
					final Vector<String> CategoryID = new Vector<String>();
					
					for(int i=0; i<taglie.length; i++){
						
						taglie[i]= jsonArray.getJSONObject(i).getString("CustomT1Desc");
						IdProduct.add(jsonArray.getJSONObject(i).getString("IdProduct"));
						CategoryID.add(jsonArray.getJSONObject(i).getString("CategoryID"));
						
					}
					
					builder = new AlertDialog.Builder(SchedaArticolo.this);
					builder.setTitle("Taglie Disponibili:");
					builder.setItems(taglie, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
							idP = IdProduct.elementAt(which);
							url ="http://www.sportincontro.it/test/CartPost.php?idP="+idP;
							System.out.println(url);
							
							//String url ="http://www.sportincontro.it/default.asp?cmd=getProd&cmdID="+getProd+"&idC="+idC+"&pType=-1";
							//Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
							//startActivity(intent);
							
							buy.setTextColor(Color.rgb(0, 255, 0));
							Toast toast = Toast.makeText(getApplicationContext(), taglie[which]+" selezionata", 1000);
							toast.show();
						}
					});
					
					scelta = builder.create();
					scelta.show();
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
        }
        
        @Override
    	public boolean onCreateOptionsMenu(Menu menu) {
    		// Inflate the menu; this adds items to the action bar if it is present.
    		//getMenuInflater().inflate(R.menu.activity_main, menu);
    		
    		
    		menu.add("Carrello").setOnMenuItemClickListener(new OnMenuItemClickListener() {
				
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					// TODO Auto-generated method stub
					
					Intent intent = new Intent(SchedaArticolo.this,Cart.class);
					url = "http://www.sportincontro.it/default.asp?cmd=showCart";
					intent.putExtra("url", url);
					startActivity(intent);
					return false;
				}
			});
    		
    		menu.add("Home").setOnMenuItemClickListener(new OnMenuItemClickListener() {
				
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					// TODO Auto-generated method stub
					
					Intent intent = new Intent(SchedaArticolo.this,MainActivity.class);
					startActivity(intent);
					return false;
				}
			});
    		
    		menu.add("Chi Siamo").setOnMenuItemClickListener(new OnMenuItemClickListener() {	
    			@Override
    			public boolean onMenuItemClick(MenuItem item) {
    				// TODO Auto-generated method stub
    				Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.sportincontro.it"));
    				startActivity(intent);
    				return false;
    			}
    		} );
    		
    		
    		menu.add("Scrivici").setOnMenuItemClickListener(new OnMenuItemClickListener() {
    			@Override
    			public boolean onMenuItemClick(MenuItem item) {
    				// TODO Auto-generated method stub
    				Intent email = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:info@sportincontro.it"));
    				startActivity(email);
    				return false;
    			}
    		});
    		
    		menu.add("Telefonaci").setOnMenuItemClickListener(new OnMenuItemClickListener() {
    			@Override
    			public boolean onMenuItemClick(MenuItem item) {
    				// TODO Auto-generated method stub
    				Intent tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel://+39062310844"));
    				startActivity(tel);
    				return false;
    			}
    		});
    		return true;
    	}
    		
    	
    
}
