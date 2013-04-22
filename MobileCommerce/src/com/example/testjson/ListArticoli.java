// Activity che viene lanciata dopo la scelta della categoria o della ricerca e visulizza la lista dei prodotti

package com.example.testjson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
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
		
		
		// prendo i riferimenti alle view 
		due = (ListView) findViewById(R.id.listView3);
		img = (ImageView) findViewById(R.id.imageView2);
		
		
		// ArrayList di dati per il simpleadapter custum
		arrayData=new ArrayList<HashMap<String,Object>>(); 
		String[] from = {"Description","priceList","priceSell","icona"};
		int[] to ={R.id.titoloArt,R.id.priceList,R.id.priceSell,R.id.imageView2};
		lista = new ExtendedSimpleAdapter(getApplicationContext(), arrayData, R.layout.articolo_row, from, to);
		
		
		if(isNetworkAvailable(this)){
			
		// dialog per l'attesa del caricamento della lista
		dialog = ProgressDialog.show(ListArticoli.this, null, "Caricamento articoli..."); 
		
		
		//nel caso l'intent ricevuta riguarda una ricerca esegue questo codice
		if(getIntent().getExtras().containsKey("query")){
			
			String query = getIntent().getExtras().getString("query");
			
			// parte il task per la ricerca della query nel database
			new SerchTask(query).execute(); 
			
		}else{
			
			// se non è una ricerca allora prende il riferimento all'id della categoria e lancia il task per la creazione della lista articoli
			cat = getIntent().getExtras().getInt("id"); 
			new ListaArticoliTask().execute();
		}
		
		
		//click su un oggetto della lista che apre la scheda articolo !
		due.setOnItemClickListener(new OnItemClickListener() { 

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				// nell'intent inserisco i dati già in possesso per la scheda dell'articolo scelto
				Intent intent = new Intent(ListArticoli.this, SchedaArticolo.class);
				intent.putExtra("ProdCode", cod.get(arg2));
				intent.putExtra("id", id.get(arg2));
				intent.putExtra("Description", desc.get(arg2));
				intent.putExtra("PriceList", prl.get(arg2));
				intent.putExtra("PriceSell", prv.get(arg2));
				startActivity(intent);
				
			}
		});
		}
	}
	
	
	//quando l'activity viene "distrutta" verrà cancellata anche la cache nella Memoria del Telefono
	protected void onDestroy(){	
		super.onDestroy(); 
		lista.imageLoader.clearCache();
	}
	

	
	public class ListaArticoliTask extends AsyncTask<Void, String, String> {

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			// esegue una post http con l'id della categoria scelta
			StringBuilder builder = new StringBuilder();		
			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://www.sportincontro.it/test/articoli.php");
			try {
				
				//inserisce come paramentro della post l'id della categoria scelta
				String stringId = Integer.toString(cat);
				List<NameValuePair> nameValuePair= new ArrayList<NameValuePair>();
				nameValuePair.add(new BasicNameValuePair("idcategoria", stringId));
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
				HttpResponse response = client.execute(httpPost);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 200) {
					
					//se l'esito è positivo crea una stringa con i dati della pagina
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
				
				//dopo aver prelevato i dati crea una array di ogg. JSON e inserisce i valori nelle varie liste
				JSONArray jsonArray = new JSONArray(dati);
				
				for(int i=0; i<jsonArray.length(); i++){
					
					desc.add(jsonArray.getJSONObject(i).getString("Description"));
					id.add(jsonArray.getJSONObject(i).getString("IdProduct"));
					cod.add(jsonArray.getJSONObject(i).getString("ProdCode"));
					
					//array per i 2 prezzi che verranno calcolati con il metodo getPrice()
					String[] price = new String[2];
					price = getPrices(jsonArray.getJSONObject(i).getString("priceList"));
					
					
					prl.add(price[0]);
					prv.add(price[1]);
					
				}
					//dopo aver preso le stringhe per costruire la lista, viene eseguito il task per prelevare gli url delle foto
					new TaskImg().execute();
					
					
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			
			
		
		}
	}
	
	
	   	//task per il download delle Url relative alle foto degli articoli
        class TaskImg extends AsyncTask<Void, Void, Void>{
        	
        	//array dove verranno salvate le url delle img
        	String[] imageUrls;
        	

			@Override
			protected Void doInBackground(Void... par) {
				// TODO Auto-generated method stub
				
				
	        	
				//esegue una post http con valori il numero di foto da prendere e il tipo di foto da trovare
				//in questo caso quella piccola che è di tipo 2
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
							//controllo che la linea non contiene spazi e in caso li converte
							line = line.replace(" ","%20");
							
							imageUrls[j] = "http://www.sportincontro.it/files/sport_incontro_Files/Foto/"+line;
							j++;
						} 
	
	    			
					
	     			
	     		} catch (MalformedURLException e) {
	     			// TODO Auto-generated catch block
	     			e.printStackTrace();
	     		} catch (IOException e) {
	     			// TODO Auto-generated catch block
	     			e.printStackTrace();
	     		}
				return null;
			
			}
			
        
			protected void onPostExecute(Void v) {
				
				// dopo l'eseguzione del task inserisce tutti i valori una mappa per l'adapter e lo setta
				for(int i=0;i<id.size();i++){
					
					HashMap<String,Object> articleMap=new HashMap<String, Object>();
					
					articleMap.put("icona",imageUrls[i]); 
					articleMap.put("Description", desc.get(i));
					articleMap.put("priceList", prl.get(i));
					articleMap.put("priceSell", prv.get(i));
					
					arrayData.add(articleMap);
				}
				
				
					dialog.dismiss();
					due.setAdapter(lista);
				
					
				
			}
        }
        
        
        //task per visualizzare gli articoli della ricerca
        public class SerchTask extends AsyncTask<Void,String,String>{
        	String query;
        	
        	public SerchTask(String query){
        		this.query = query;
        	}
        	
        	
    		@Override
    		protected String doInBackground(Void... params) {
    			// TODO Auto-generated method stub
    			
    			
    			//esegue una Post per effettuare la ricerca nel database.
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
    			
    			//al termine del task verifica se la ricerca ha portato risultati
    			if(dati.equals("null")){
    				
    				//se non ci sono risultati stoppa il dialog e riporta in HomeActivity con un Toast
    				dialog.dismiss();
    				Toast toast = Toast.makeText(getApplicationContext(), "Articolo non Trovato", 1000);
    				toast.show();
					Intent intent = new Intent(ListArticoli.this,MainActivity.class);
					startActivity(intent);
					
				}else{
    			
    			try {
    				
    				//Se ci sono risultati crea un array json con i dati passati e li inserisce i valori nei vettori
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
    						//dopo aver prelevato i dati escegue il task per prendere l'immagini 
        					new TaskImg().execute();
        		
    				
        			} catch (JSONException e) {
        				e.printStackTrace();
        			}
    	
				}
    		}

        }    
        
     
    //metodo per calcolare i prezzi di listino e di vendita dalla stringa del db
	public  String[] getPrices(String pricemap){
		
		//leva i primi 4 caratteri
		String a = pricemap.substring(4);
		
		//poi crea due stringhe con i prezzi di vendita e di listino
		
		int j = a.indexOf("|");
		
		String prL = a.substring(0, j);
		String b = a.substring(j+8);
		int k = b.indexOf("|");
		String prV = b.substring(0, k);
		
		//aggiunge l'iva e arrotonda i prezzi
		NumberFormat nf = NumberFormat.getInstance(Locale.ITALIAN);
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
		double plivainc =  Double.parseDouble(prL)*1.21;
		double pvivainc =  Double.parseDouble(prV)*1.21;
		
		//inserisce i due prezzi in un array
		String[] price ={nf.format(plivainc),nf.format(pvivainc)};
		
		return price;
	}
	


}
	
