<?php

define("DB_HOST", "62.149.150.123");
define("DB_USER", "Sql382536");
define("DB_PASSWORD", "7cd2a23a");
define("DB_NAME", "Sql382536_3");
 
$mysqli=new mysqli(DB_HOST,DB_USER,DB_PASSWORD,DB_NAME);
if($mysqli->connect_errno){
    echo "Failed to connect to MySQL:(".$mysqli->connect_errno.") ".$mysqli->connect_error;
}
$registration_id=isset($_POST["regId"]) ? $_POST["regId"] : "";
$registration_id=$mysqli->real_escape_string($registration_id);
if($registration_id!=""){
    $res=$mysqli->query(sprintf("SELECT * FROM android_udid WHERE registration_id='%s'",$registration_id));
    if($res->num_rows==0){
        $mysqli->query(sprintf("INSERT INTO android_udid(registration_id) VALUES('%s')",$registration_id));
    }
}
?>
