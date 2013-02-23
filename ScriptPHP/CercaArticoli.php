<?php

$conn = mysql_connect('62.149.150.123', 'Sql382536', '7cd2a23a');
if (!$conn) {
	die ('Non riesco a connettermi: ' . mysql_error());
}

$db = mysql_select_db('Sql382536_1', $conn);
if (!$db) {
	die ("Errore nella selezione del database: " . mysql_error());
}


$query = $_POST["query"];

$qSerch = mysql_query("SELECT IdProduct , Description , ProdCode , priceList FROM  `WebOggetti` WHERE  `Description` LIKE  '%".$query."%' AND  `CustomT1Desc` IS NULL ");


while($e=mysql_fetch_assoc($qSerch)) {
	$output[]=$e; }

        
print(json_encode($output));



mysql_close($conn);

?>
