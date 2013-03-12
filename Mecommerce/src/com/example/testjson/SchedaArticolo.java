// Acrtivity che visualizza la scheda dell'articolo scelto dalla lista

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
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SchedaArticolo extends MainActivity {
	
	private ProgressDialog pd;
	public String dati, id, idP, url;
	public TextView colore, desc;
	public Button btn, buy;
	public String[] taglie;
	public AlertDialog scelta;
	public AlertDialog.Builder builder;
	public Dialog dialog;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedarticolo);
		
		//prendo i riferimenti ai widget
		TextView uno = (TextView) findViewById(R.id.textView01);
		TextView due = (TextView) findViewById(R.id.textView2);
		TextView prL = (TextView) findViewById(R.id.prLisText);
		TextView prV = (TextView) findViewById(R.id.prVenText);
		
		
		colore = (TextView) findViewById(R.id.textView3);
		desc = (TextView) findViewById(R.id.textView4);
		
		ImageView img = (ImageView) findViewById(R.id.imageView2);
		
		btn = (Button) findViewById(R.id.button1);
		buy = (Button) findViewById(R.id.button2);
		
		
		//parte il progressdialog
		pd = ProgressDialog.show(SchedaArticolo.this, null, "Caricamento dati...");
		
		uno.setText(getIntent().getExtras().getString("Description"));
		due.setText(getIntent().getExtras().getString("ProdCode"));
		prL.setText(getIntent().getExtras().getString("PriceList"));
		prV.setText(getIntent().getExtras().getString("PriceSell"));
		id= getIntent().getExtras().getString("id");
		
		// preleva tutti i dati !!
		new DownloadImg(img,3).execute();
		new SchedaArtTask().execute();
		
		// dialog per vedere immagine in alta risoluzione
		img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//creo un custumdialog per l'immagine grande
				dialog = new Dialog(SchedaArticolo.this);
				dialog.setContentView(R.layout.zoom_dialog);
	
				
				ImageView image = (ImageView) dialog.findViewById(R.id.zoom_image);
				new DownloadImg(image, 4).execute();
				
			}
		});
		
		
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
		String colorString;
		String descString;
		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			StringBuilder builder = new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://www.sportincontro.it/test/schedart_1.php");
			
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
					int i = 0;
					while ((line = reader.readLine()) != null) 
					{	
						if(i==0) {
							colorString = line;
						}else {builder.append(line);}
						i++;
					} 
					descString = builder.toString();
					
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
			
				colore.setText(colorString);
				
				desc.setText(Html.fromHtml(descString));
				pd.dismiss();
			
			
		}	
	}
	
	// Task per il download della foto
	
	private class DownloadImg extends AsyncTask<Void,Bitmap,Bitmap> {
	   	 ImageView imageview;
	   	 int tipoFoto;
	   	 
	   	 DownloadImg (ImageView imageview, int tipoFoto){
	   		 this.imageview= imageview;
	   		 this.tipoFoto = tipoFoto;
	   	 }
        protected void onPostExecute(Bitmap bitmap) {
        	
        	imageview.setImageBitmap(bitmap);
        	
        	if(tipoFoto==4) dialog.show();
       }
        
        @Override
        protected Bitmap doInBackground(Void... params) {
        	

        	
        	StringBuilder builder = new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://www.sportincontro.it/test/foto.php");
    		try {
    			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
    			nameValuePair.add(new BasicNameValuePair("0",id));
    			nameValuePair.add(new BasicNameValuePair("nFoto","1"));
    			nameValuePair.add(new BasicNameValuePair("tipofoto", Integer.toString(tipoFoto)));
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
				
				Bitmap bitmap = getBitmap(imageUrl);
				
    			
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
    		
        public  Bitmap getBitmap(String url) throws MalformedURLException, IOException{
    		
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
