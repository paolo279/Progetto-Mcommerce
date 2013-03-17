<head></head>

<?php
$conn = mysql_connect('62.149.150.123', 'Sql382536', '7cd2a23a');
if (!$conn) {
	die ('Non riesco a connettermi: ' . mysql_error());
}

$db = mysql_select_db('Sql382536_1', $conn);
if (!$db) {
	die ("Errore nella selezione del database: " . mysql_error());
}

$id =  35821;//$_POST["id"];

$q = mysql_query("SELECT CustomT2Desc, DescriptionExt1 FROM WebOggetti WHERE IdProduct = $id ");

while($e=mysql_fetch_array($q)) {
    
    
    $e[1]=  htmlentities($e[1], ENT_QUOTES, 'ISO-8859-1');
    
    print $e[0]."\n".$e[1];
  }
	
       


mysql_close($conn);

?>