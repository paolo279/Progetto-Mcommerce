<?php
$conn = mysql_connect('62.149.150.123', 'Sql382536', '7cd2a23a');
if (!$conn) {
	die ('Non riesco a connettermi: ' . mysql_error());
}

$db = mysql_select_db('Sql382536_1', $conn);
if (!$db) {
	die ("Errore nella selezione del database: " . mysql_error());
}


$idcat = $_POST["idcategoria"];

$qCercaProd = mysql_query("SELECT `NumeroArticoliNodo` FROM  `Articoli categorie` WHERE  `ID categoria articolo` = $idcat ");

$qCercaCat = mysql_query("SELECT `NumeroArticoliNodo` , `ID categoria articolo` FROM  `Articoli categorie` WHERE  `ID nodo padre` = $idcat ");

$qPullProd = mysql_query("SELECT IdProduct , Description , ProdCode , priceList FROM  `WebOggetti` WHERE  `CategoryID` = $idcat AND  `IdArticoloMasterTagliaColore` IS NULL");


$qnt=mysql_fetch_array($qCercaProd);

if($qnt[0]!=0){
	while($e=mysql_fetch_assoc($qPullProd)) {
		$output[]=$e; }
} else {
	while($subcat=mysql_fetch_assoc($qCercaCat)){
		
		$idcat = $subcat['ID categoria articolo'];
		
		$qPullProd1 = mysql_query("SELECT IdProduct , Description , ProdCode , priceList FROM  `WebOggetti` WHERE  `CategoryID` = $idcat AND  `IdArticoloMasterTagliaColore` IS NULL");
		$qCercaCat1 = mysql_query("SELECT `NumeroArticoliNodo` , `ID categoria articolo` FROM  `Articoli categorie` WHERE  `ID nodo padre` = $idcat ");
		
		if($subcat["NumeroArticoliNodo"]!=0){
			while($e=mysql_fetch_assoc($qPullProd1)) {
				$output[]=$e;}
		}else {
			while($subcat=mysql_fetch_assoc($qCercaCat1)){
			
				$idcat = $subcat['ID categoria articolo'];
			
				$qPullProd1 = mysql_query("SELECT IdProduct , Description , ProdCode , priceList FROM  `WebOggetti` WHERE  `CategoryID` = $idcat AND  `IdArticoloMasterTagliaColore` IS NULL");
			
				if($subcat["NumeroArticoliNodo"]!=0){
					while($e=mysql_fetch_assoc($qPullProd1)) {
						$output[]=$e;}
					}
			}	
		}
		
	}
}


print(json_encode($output));



mysql_close($conn);
?>