package com.example.testjson;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CategorieAdapter extends ArrayAdapter<String>  {
	
	  private int resource;
      private LayoutInflater inflater;

	public CategorieAdapter(Context context, int resourceId,
			List<String> objects) {
		super(context, resourceId, objects);
		// TODO Auto-generated constructor stub
		resource = resourceId;
        inflater = LayoutInflater.from(context);
		
	}
	
    private static class ViewHolder {
        TextView categoria;
        
}
    
	public View getView(int position, View v, ViewGroup parent){
		
		String obj = getItem(position);
		
		ViewHolder holder;
		
		if (v == null) {
            v = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.categoria = (TextView) v.findViewById(R.id.categoria);

            v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
			}

 
		holder.categoria.setText(obj);
  
		return v;
		
		
	}

}
