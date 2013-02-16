<?php
$conn = mysql_connect('62.149.150.123', 'Sql382536', '7cd2a23a');
if (!$conn) {
	die ('Non riesco a connettermi: ' . mysql_error());
}

$db = mysql_select_db('Sql382536_1', $conn);
if (!$db) {
	die ("Errore nella selezione del database: " . mysql_error());
}


$cat = $_POST['categoria'];

$query = mysql_query("SELECT `ID categoria articolo` FROM  `Articoli categorie` WHERE  `Descrizione categoria` = '$cat'");

$res=mysql_fetch_array($query);

$query1 = mysql_query("SELECT `Descrizione categoria` , `ID categoria articolo` FROM  `Articoli categorie` WHERE  `ID nodo padre` =$res[0]  AND NumeroArticoliNodoEFigli != 0 ");

while($e=mysql_fetch_assoc($query1)) {
        $output[]=$e; }

print(json_encode($output));


mysql_close($conn);
?>