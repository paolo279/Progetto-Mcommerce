

<?php
$conn = mysql_connect('62.149.150.123', 'Sql382536', '7cd2a23a');
if (!$conn) {
	die ('Non riesco a connettermi: ' . mysql_error());
}

$db = mysql_select_db('Sql382536_1', $conn);
if (!$db) {
	die ("Errore nella selezione del database: " . mysql_error());
}

$id =  $_POST["id"];

mysql_query("set names 'utf8'");  


$q = mysql_query("SELECT CustomT2Desc, DescriptionExt1 FROM WebOggetti WHERE IdProduct = $id ");

while($e=mysql_fetch_array($q)) {
    
    
 
    
    
    
    print $e[0]."\n".$e[1];
  }
	
       


mysql_close($conn);

?>