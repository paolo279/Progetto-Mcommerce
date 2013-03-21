package com.example.testjson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
import org.apache.http.util.EncodingUtils;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends MainActivity {
	EditText user_text;
	EditText pwd_text;
	String login_name;
	WebView mywv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		//prendo i riferimenti alle view
		user_text =  (EditText) findViewById(R.id.userText);
		pwd_text = (EditText) findViewById(R.id.pwdText);
		Button login_btn = (Button) findViewById(R.id.loginButton);
		Button reg_btn = (Button) findViewById(R.id.registerButton);
		mywv = new WebView(this);
		
		
		
		
		//al clik sul pulsante di login esegue l'accesso e ritorna alla MainActivity
		login_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new LoginTask().execute();
				
				
			}
		});
		
		reg_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.sportincontro.it/default.asp?cmd=regForm"));
				startActivity(intent);
			}
		});
		
	}
	
	public class LoginTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://www.sportincontro.it/default.asp");
			
				 
				try {
					List<NameValuePair> nameValuePair= new ArrayList<NameValuePair>();
					nameValuePair.add(new BasicNameValuePair("uid", "gianluca86"));
					nameValuePair.add(new BasicNameValuePair("pwd", "spinco"));
					httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
					HttpResponse response = client.execute(httpPost);
					StatusLine statusLine = response.getStatusLine();
					int statusCode = statusLine.getStatusCode();
					if (statusCode == 200) {
						System.out.println("Tutto ok");
						login_name = "Paolo Colucci";
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
			
			return null;
		}
		
		protected void onPostExecute(Void unused){
			
			String postData = "uid=gianluca86&pwd=spinco";
			mywv.postUrl("http://www.sportincontro.it/default.asp", EncodingUtils.getBytes(postData, "base64"));
			
			mywv.setWebViewClient(new WebViewClient(){
				public void onPageFinished(WebView view, String url) {
					Intent intent = getIntent();
					System.out.println("bella");
					intent.putExtra("loginName", login_name);
					LoginActivity.this.setResult(1, intent);
					LoginActivity.this.finish();
			    }
			});
			
		}
		


	}
}


