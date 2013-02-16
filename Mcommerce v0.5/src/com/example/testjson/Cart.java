package com.example.testjson;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class Cart extends MainActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cart_layout);
		
		WebView mywv = (WebView) findViewById(R.id.webView1);
		mywv.getSettings().setJavaScriptEnabled(true);
		mywv.getSettings().setDisplayZoomControls(true);
		mywv.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
		
		String url = getIntent().getExtras().getString("url");
		
		mywv.loadUrl(url);
	}
	
}
