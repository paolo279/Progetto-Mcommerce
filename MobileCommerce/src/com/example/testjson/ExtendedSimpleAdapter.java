// estensione del SimpleAdapter per visualizzare la lista degli articoli

package com.example.testjson;


import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;



public class ExtendedSimpleAdapter extends SimpleAdapter{
    List<HashMap<String, Object>> map;
    ImageLoader imageLoader;
    String[] from;
    int layout;
    int[] to;
    Context context;
    LayoutInflater mInflater;
    
    
    public ExtendedSimpleAdapter(Context context, List<HashMap<String, Object>> data,
            int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        layout = resource;
        map = data;
        this.from = from;
        this.to = to;
        this.context = context;
    }


@Override
public View getView(int position, View convertView, ViewGroup parent) {
	
	//prende il riferimento del LayoutInflater con il metodo getSystemService
    mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
    //ritorna una View con il metodo definito successivamente
    return this.createViewFromResource(position, convertView, parent, layout);
}

private View createViewFromResource(int position, View convertView,
        ViewGroup parent, int resource) {
	
    View v;
    
    // se è il primo elemento della lista esegue l'inflater
    if (convertView == null) {
        v = mInflater.inflate(resource, parent, false);
    } else {
    	
    	// per gli elementi successivi gli associa direttamente 
        v = convertView;
    }
    
    //esegue il metodo bindView
    this.bindView(position, v);

    return v;
}


private void bindView(int position, View view) {
	
	//prende l'HashMap dell'elemento della lista
    final HashMap<String, Object> dataSet = map.get(position);
    
    if (dataSet == null) {
        return;
    }
    
    // crea l'oggetto ViewBinder e lo istanzia
    final ViewBinder binder = super.getViewBinder();
    final int count = to.length;

    //per ogni elemento della Map viene associata la risorsa del layout nell'ordine passato dall'array to[]
    for (int i = 0; i < count; i++) {
        final View v = view.findViewById(to[i]);
        if (v != null) {
            final Object data = dataSet.get(from[i]);
            String text = data == null ? "" : data.toString();
            if (text == null) {
                text = "";
            }
            
            //il boolean bound verifica se il dato è legato alla specifica View
            boolean bound = false;
            if (binder != null) {
                bound = binder.setViewValue(v, data, text);
            }
            
            //se non è bindato esegue il codice
            if (!bound) {
                if (v instanceof Checkable) {
                    if (data instanceof Boolean) {
                        ((Checkable) v).setChecked((Boolean) data);
                    } else if (v instanceof TextView) {
                    	
                        // Note: keep the instanceof TextView check at the bottom of these
                        // ifs since a lot of views are TextViews (e.g. CheckBoxes).
                    	
                        setViewText((TextView) v, text);
                    } else {
                        throw new IllegalStateException(v.getClass().getName() +
                                " should be bound to a Boolean, not a " +
                                (data == null ? "<unknown type>" : data.getClass()));
                    }
                } else if (v instanceof TextView) {
                	
                	//se è una textview imposta il testo
                    setViewText((TextView) v, text);
                } else if (v instanceof ImageView) {
                    if (data instanceof Integer) { //se è un rifermento a una view
                        setViewImage((ImageView) v, (Integer) data);  
                    } else if (data instanceof Bitmap){ //se è un bitmap
                        setViewImage((ImageView) v, (Bitmap)data);
                    } else if (data instanceof String){ // se è una url
                    	setImageFromUrl((ImageView) v, (String)data);
                    } else {
                       setViewImage((ImageView) v, text);
                    	
                    }
                } else {
                    throw new IllegalStateException(v.getClass().getName() + " is not a " +
                            " view that can be bounds by this SimpleAdapter");
                }
            }
        }
    }
}


//metodo che imposta la ImageView se viene passato un bitmap
private void setViewImage(ImageView v, Bitmap bmp){
    v.setImageBitmap(bmp);
}

//metodo che imposta la ImageView se viene passato una url
private void setImageFromUrl(ImageView v, String url){
	
	//crea un oggetto ImageLoader ed esegue il metodo di Display
	imageLoader = new ImageLoader(context);
	imageLoader.DisplayImage(url, v);
	
	
}


}