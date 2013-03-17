package com.example.testjson;

// Gli oggetti di tipo articolo servono per essere passati all'Adapter per la lista degli articli !!

public class Articolo {
	public int productId;
	public String prodCode;
	public String titolo;
	public String imageUrl;
	public double priceList;
	public double priceSell;
	
	Articolo (int productId){
		this.productId = productId;
	}
	
	Articolo (){
		
	}
}
