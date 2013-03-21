//Activity che visualizza il carrello in una webview 

package com.example.testjson;

import org.apache.http.util.EncodingUtils;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class Cart extends MainActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cart_layout);
		
		//riferimento alla WebView e attivazione del javascript e dello zoom
		final WebView mywv = (WebView) findViewById(R.id.webView1);
		mywv.getSettings().setJavaScriptEnabled(true);
		mywv.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
		String url = getIntent().getExtras().getString("url");

		mywv.loadUrl(url);
	}
	
}
