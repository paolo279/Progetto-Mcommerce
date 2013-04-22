// Activity di home

package com.example.testjson;

import static com.example.testjson.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.example.testjson.CommonUtilities.EXTRA_MESSAGE;
import static com.example.testjson.CommonUtilities.SENDER_ID;

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
import com.google.android.gcm.GCMRegistrar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;




public class MainActivity extends Activity {
	
	AsyncTask<Void, Void, Void> mRegisterTask;
	public String dati;
	public List<String> v = new ArrayList<String>();
	public ListView uno;
	public CategorieAdapter lista;
	public String logName;
	public TextView login_view;
	public Button logButt;
	public WebView mywv;
	public SharedPreferences userpref;
	public SharedPreferences.Editor editor;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// prendo i riferimenti ai widget
		ImageView serch_button = (ImageView) findViewById(R.id.serchButton);
		final EditText serch_box = (EditText) findViewById(R.id.editText1);
		logButt = (Button) findViewById(R.id.LoginButt);
		uno = (ListView) findViewById(R.id.listView1);
		login_view = (TextView) findViewById(R.id.LoginView);
		
		//viene creata una webview con il javascript abilitato
		mywv = new WebView(this);
		mywv.getSettings().setJavaScriptEnabled(true);
		
		// inizializzo la SheredPreferences e il nome dell'utente se è salvato
		userpref = getSharedPreferences("Username", Context.MODE_PRIVATE);
		logName = userpref.getString("Referente", null);
		
		if(logName!=null) {
			
			//se il nome è salvato, vengono impostate le TextView
			login_view.setText("Benvenuto, "+logName);
			logButt.setText("Log out");
		}
		
		// adapter con riferimento al layout e alla lista di categorie
		lista= new CategorieAdapter(this, R.layout.categoria_row, v);
		
		
		//contolla la connessione 
		if(isNetworkAvailable(this)){

			// esegue il task per recuperare le categorie
			 new CategoryTask().execute();
		
			//fa partire il metodo per l'aggancio a Google Cloud Message !
			 GCMconnessione();
		 
		
		//evento di click su una categoria
		uno.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
					//inserisco nel bundle l'id della categoria scelto e lancio la seconda activity
					String b = v.get(arg2);
					Intent intent = new Intent(MainActivity.this,SecondActivity.class);
					intent.putExtra("categoria", b);
					startActivity(intent);
				};	
		});
		
			
			//al clik sul bottone di Login viene aperta LoginActivity
			logButt.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					//se l'utente è loggato parte il metodo di logout
					if(logName!=null){ 
						logout();
					}else {
						
					// parte l'activity e attende il risultato
					Intent intent = new Intent(MainActivity.this, LoginActivity.class);
					startActivityForResult(intent, 1);
					}
				}
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
				
				//ottiene la stringa della editText e fa sparire la tastiera
				String query = serch_box.getText().toString();
				sparisciTastiera();
				
				//se la query è composta solo da spazi o vuota o è minore di 3 caratteri non esegue la ricerca
				if(query.matches("^\\s*$") || query.matches("") || query.length()< 3 ){
					
					Toast toast = Toast.makeText(getApplicationContext(), "Ricerca non valida", 1000);
					toast.show();
					
					//risetta il box 
					serch_box.setText("");
					
				}else{
					
					//altrimenti parte l'activity ListArticoli e viene passata la query per la ricerca
					Intent intent = new Intent(getApplicationContext(), ListArticoli.class);
					intent.putExtra("query", query);
					startActivity(intent);
					}
				
				
				}
			});

		}
	}
	
	
	
	//metodo per la gestione della connessione al Server GCM by Google !!	
	public void GCMconnessione(){
		
		//controlla il manifest e che il dispositivo sia registrato
		GCMRegistrar.checkDevice(this);
		  GCMRegistrar.checkManifest(this);
		  
		  //metodo per registrare il dispositivo al server
		  registerReceiver(mHandleMessageReceiver,
	                new IntentFilter(DISPLAY_MESSAGE_ACTION));
		  
		  //prende l'id del dispositivo
	        final String regId = GCMRegistrar.getRegistrationId(this);
	        if (regId.equals("")) {
	        	
	            // all'avvio registra il dispositivo e lo connette al server GCM
	            GCMRegistrar.register(this, SENDER_ID);
	           
	            } else {
	               
	            	// altrimenti controlla che sia connesso al server e in caso lo registra
	                final Context context = this;
	                mRegisterTask = new AsyncTask<Void, Void, Void>() {

	                    @Override
	                    protected Void doInBackground(Void... params) {
	                        boolean registered =
	                                ServerUtilities.register(context, regId);
	                        
	                        if (!registered) {
	                            GCMRegistrar.unregister(context);
	                        }
	                        return null;
	                    }

	                    @Override
	                    protected void onPostExecute(Void result) {
	                        mRegisterTask = null;
	                    }

	                };
	                mRegisterTask.execute(null, null, null);
	            }
	}
			  

	// metodo per prelevare le categorie principali dal server
	public class CategoryTask extends AsyncTask<Void, String, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			// esegue una chiamata Http per prendere le categorie
			StringBuilder builder = new StringBuilder();		
			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://www.sportincontro.it/test/categorie.php");
			
			try {
				HttpResponse response = client.execute(httpPost);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 200) {
					// se la risposta è arrivata legge i dati e li inserisce in una StringBuilder
					HttpEntity entity = response.getEntity();
					InputStream content = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(content));
					String line;
					while ((line = reader.readLine()) != null) 
					{
						builder.append(line);
						
					} //end while
					
					//viene creata la stringa a partire dalla StringBuilder
					dati = builder.toString();

						
				}
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				
				// crea un'array JSON con la stringa letta e inserisce i valori in una lista
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
			
			//dopo aver eseguito il task setto l'adapter
			uno.setAdapter(lista);
		}	
	}
	
	
	//metodo che viene eseguito quando ritorna un risultato dalla Login Activity
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==1){
			
			//prende il nome del referente e lo inserisce nella textview
			logName = data.getExtras().getString("loginName");
			login_view.setText("Benvenuto, "+logName);
			logButt.setText("Log out");
			
			//Salva il nome del referente nello SheredPreference
			editor = userpref.edit();
			editor.putString("Referente", logName);
	        editor.commit();
		}
	}
	
	
	
	// metodo che effettua il logout 
	public void logout(){
		
		//viene eseguito un AlertDialog per confermare il logout
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("Sei Sicuro di disconnetterti ?");
		builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				//questo comando fa in modo che per ogni link cliccato viene ricaricata la webview
				mywv.setWebViewClient(new WebViewClient(){
					@Override
				    public boolean shouldOverrideUrlLoading(WebView view, String url) {
				        view.loadUrl(url);
				        return true;
				    }
				});
			
				//viene caricato l'url in backgroud con il comando di logout che viene salvato nel brawser
				mywv.loadUrl("http://www.sportincontro.it/default.asp?cmd=logout");
				
				//il logName viene cancellato e viene salvata la SharaderPreference
				logName = null;
				editor = userpref.edit();
				editor.putString("Referente", logName);
		        editor.commit();
		        
		        //infine vengono risettati i testi
				login_view.setText("Benvenuto, Ospite");
				logButt.setText("Accedi");
			}
		});
		
		
		//se non si vuole effettuare il logout il sistema rimane fermo
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//il dialog viene creato e lanciato
		AlertDialog logoutDialog = builder.create();
		logoutDialog.show();
		
		
	
	}
	
	
	// metodo che fa sparire la tastiera dallo schermo
	public void sparisciTastiera(){
		
		InputMethodManager imm = (InputMethodManager) getSystemService(
			    INPUT_METHOD_SERVICE);
		
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	}
	
	
	
	// metodo per vedere se la connessione è disponibile
	public boolean isNetworkAvailable(Context ctx)
		 {
		     ConnectivityManager cm = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		     NetworkInfo netInfo = cm.getActiveNetworkInfo();
		     if (netInfo != null && netInfo.isConnectedOrConnecting()&& cm.getActiveNetworkInfo().isAvailable()&& cm.getActiveNetworkInfo().isConnected()) 
		     {
		         return true;
		     }
		     else
		     {
		    	 Toast toast = Toast.makeText(this, "Errore di rete", 1000);
		    	 toast.show();
					
		         return false;
		     }
		 }
		


		
		// metodo per il Brodcastreceiver quando viene inviata una notifica dal Server GCM
		private final BroadcastReceiver mHandleMessageReceiver =
	            new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
	            Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
	           
	        }
	    };
	
	
	//menu valido per tutte le activity grazie all'estensione di questa
	public boolean onCreateOptionsMenu(Menu menu) {
		
		
		//viene aperto la Cart Activity
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
		
		//si ritorna nella Main Activity
		menu.add("Home").setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(getApplicationContext(),MainActivity.class);
				startActivity(intent);
				return false;
			}
		});
		
		// Si apre il brawser a un link del sito, in questo caso l'HomePage
		menu.add("Chi Siamo").setOnMenuItemClickListener(new OnMenuItemClickListener() {	
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.sportincontro.it"));
				startActivity(intent);
				return false;
			}
		} );
		
		//viene lanciato il programma di invio mail con destinatario già impostato
		menu.add("Scrivici").setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				Intent email = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:info@sportincontro.it"));
				startActivity(email);
				return false;
			}
		});
		
		//viene effettuata una chiamata al numero dell'azienda
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


