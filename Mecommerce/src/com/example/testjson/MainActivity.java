// Activity di home

package com.example.testjson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;




public class MainActivity extends Activity {
	
	public String dati;
	public List<String> v = new ArrayList<String>();
	public ListView uno;
	public CategorieAdapter lista;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ImageView serch_button = (ImageView) findViewById(R.id.serchButton);
		final EditText serch_box = (EditText) findViewById(R.id.editText1);
		lista= new CategorieAdapter(this, R.layout.categoria_row, v);
	
		
		uno = (ListView) findViewById(R.id.listView1);
		new CategoryTask().execute();
		
		
		
		
		uno.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
					String b = v.get(arg2);
					
					Intent intent = new Intent(MainActivity.this,SecondActivity.class);
					intent.putExtra("categoria", b);
					
					//startService(new Intent(getApplicationContext(), AdService.class));
					startActivity(intent);
				};	
		});
		
		// quando viene dato il focus alla editText si cancella la scritta iniziale !!
		serch_box.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				serch_box.setText("");
				
			}
		});
		
		
		// al click sulla lente si avvia la ricerca
		serch_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String query = serch_box.getText().toString();

				if(query.matches("^\\s*$") || query.matches("") ){
					Toast toast = Toast.makeText(getApplicationContext(), "Ricerca non valida", 1000);
					toast.show();
				}else{
					
					Intent intent = new Intent(getApplicationContext(), ListArticoli.class);
					intent.putExtra("query", query);
					startActivity(intent);
				}
				
				
			}
		});
		
		
		
			
		
		
	
	}
		
			
			  

	
	public class CategoryTask extends AsyncTask<Void, String, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
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
			
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void unused) {
			
			uno.setAdapter(lista);
			
			
			
			
		
		}
		
		
		
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_main, menu);
		
		
		menu.add("Carrello").setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(getApplicationContext(),Cart.class);
				String cartUrl = "http://www.sportincontro.it/default.asp?cmd=showCart";
				intent.putExtra("url", cartUrl);
				startActivity(intent);
				return false;
			}
		});
		
		menu.add("Home").setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(getApplicationContext(),MainActivity.class);
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


