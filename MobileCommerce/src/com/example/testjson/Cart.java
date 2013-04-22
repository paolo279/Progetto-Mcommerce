//Activity che visualizza il carrello in una webview 

package com.example.testjson;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class Cart extends MainActivity {
	WebView mywv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cart_layout);
		
		//riferimento alla WebView e attivazione del javascript e dello zoom
		mywv = (WebView) findViewById(R.id.webView1);
		mywv.getSettings().setJavaScriptEnabled(true);
		mywv.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
		
		mywv.setWebViewClient(new WebViewClient(){
			@Override
		    public boolean shouldOverrideUrlLoading(WebView view, String url) {
				
				view.loadUrl(url);
		        return true;
		    }
			
			public void onPageFinished(WebView view, String url)
			{
				
				//inserisco del codice javascript quando finisce di caricare
			    view.loadUrl("javascript:(function() { " +
			            "document.getElementById('colDx').style.display='none'; " +
			            "document.getElementById('menu').style.display='none'; " +
			            "document.getElementById('navBar').style.display='none'; " +
			            "document.getElementById('head').style.display='none'; " +
			            "document.getElementById('colSx').style.display='none'; " +
			            "document.getElementById('bottomElements').style.display='none'; " +
			            "document.getElementById('mainTable').style.maxWidth='200px'; " +
			            "})()");

				}

		});
		
		
		//prende url passato dalla precedente activity e carica la webview
		String url = "http://www.sportincontro.it/default.asp?cmd=showCart#colMid";
		
		if(isNetworkAvailable(this)) mywv.loadUrl(url);
	}
	
	
	public boolean onCreateOptionsMenu(Menu menu) {
		
		//bottone per svuotare il carrello
		menu.add("Svuota Carrello").setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				
				//carica la url con un parametro GET per svuotare il cestino
				String cartEmpty = "http://www.sportincontro.it/default.asp?cmd=delCart";
				mywv.loadUrl(cartEmpty);
				
			
				Toast toast = Toast.makeText(getApplicationContext(), "Carrello Svuotato", 1000);
				toast.show();
				
				Intent intent = new Intent(Cart.this,MainActivity.class);
				startActivity(intent);
				
				return false;
			}
		});
		
		return true;
		
	}
	
}
