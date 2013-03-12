//Service per inviare notifiche giornaliere a chi ha installato l'app

package com.example.testjson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;



public class AdService extends Service {
	Handler handler = new Handler();
	int test =0;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onCreate(){
		
		TimerTask myT = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				test++;
				
					lanciaNotification(test);
				
			}
		};
		
		
		Timer timer = new Timer();
		
		timer.schedule(myT, 10000 , 60000);
	
		
			
	}
	
	
	public void lanciaNotification(int j){
		
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.logo)
		        .setContentTitle("Service "+j+" Lanciato")
		        .setContentText("Complimenti hai lanciato un service !!!")
		        .setAutoCancel(true);
		
		PendingIntent pintent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0); 
		mBuilder.setContentIntent(pintent);
		Notification notifica = mBuilder.build();
		NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			
		nManager.notify(j, notifica);
	}
	
	

		
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			test++;
			while(test<5){
				lanciaNotification(test);
			}
			
			
		}
	};


}
