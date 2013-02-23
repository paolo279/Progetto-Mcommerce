package com.fedorvlasov.lazylist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {
    
    ListView list;
    LazyAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        list=(ListView)findViewById(R.id.list);
        adapter=new LazyAdapter(this, mStrings);
        list.setAdapter(adapter);
        
        Button b=(Button)findViewById(R.id.button1);
        b.setOnClickListener(listener);
    }
    
    @Override
    public void onDestroy()
    {
        list.setAdapter(null);
        super.onDestroy();
    }
    
    public OnClickListener listener=new OnClickListener(){
        @Override
        public void onClick(View arg0) {
            adapter.imageLoader.clearCache();
            adapter.notifyDataSetChanged();
        }
    };
    
    private String[] mStrings={
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/24036.JPG",
    		"http://www.sportincontro.it/files/sport_incontro_Files/Foto/23051.JPG",
    };
}