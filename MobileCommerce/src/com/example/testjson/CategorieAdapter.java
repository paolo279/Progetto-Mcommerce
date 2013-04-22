//estensione dell'ArrayAdapter per la visualizzazione delle categorie di home

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
		
		//prendiamo la stringa nella posizione
		String obj = getItem(position);
		
		// tramite ViewHolder manteniamo i riferimenti alla TextView
		ViewHolder holder;
		
		if (v == null) {
			// questo procedimento viene fatto per il primo elemento della lista
			
			//l'inflater ci permette di istanziare un oggetto da una risorsa XML
            v = inflater.inflate(resource, parent, false);
            
            //istanziamo l'holder e settiamo la categoria con la risorsa del layout
            holder = new ViewHolder();
            holder.categoria = (TextView) v.findViewById(R.id.categoria);
            
            // con il metodo setTag() associamo l'oggetto alla view
            v.setTag(holder);
            
			} else {
				
				//nei successivi elementi riutilizziamo l'holder per associare l'oggetto successivo
				holder = (ViewHolder) v.getTag();
			}
		
		//impostiamo il testo nella TextView
		holder.categoria.setText(obj);
  
		return v;
		
		
	}

}
