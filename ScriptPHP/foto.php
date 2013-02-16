<?php

$conn = mysql_connect('62.149.150.123', 'Sql382536', '7cd2a23a');
if (!$conn) {
	die ('Non riesco a connettermi: ' . mysql_error());
}

$db = mysql_select_db('Sql382536_1', $conn);
if (!$db) {
	die ("Errore nella selezione del database: " . mysql_error());
}


$tipoFoto = $_POST["tipofoto"];
$nFoto = $_POST["nFoto"];

$i=0;



while ($nFoto>$i){
    $idProd = $_POST[$i];
    
    $qIdF = mysql_query("SELECT IdFoto FROM  `FotoLinks` WHERE  `IdArticolo` = $idProd");

    while($e=mysql_fetch_assoc($qIdF)){
	$id = $e["IdFoto"];
	$qUrl = mysql_query("SELECT Nomefile, IdTipoFoto FROM Foto WHERE ID = $id ");

	$url=mysql_fetch_assoc($qUrl);
	
	if($url["IdTipoFoto"]==$tipoFoto){
		$output[$i]=$url["Nomefile"];
		 break;
	}
    }
    if($output[$i]==NULL){
        $output[$i]= "643.JPG";
    }
    $i++;
}




    for($i = 0; $i<$nFoto;$i++){
        print $output[$i] . "\n";
       
    }
  



mysql_close($conn);
?>