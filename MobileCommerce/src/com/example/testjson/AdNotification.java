package com.example.testjson;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;


public class AdNotification extends MainActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ad_layout);
		
		WebView adW = (WebView) findViewById(R.id.adView);
		adW.getSettings().setJavaScriptEnabled(true);
		adW.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
		adW.loadUrl("http://www.sportincontro.it");
	}

	

}
