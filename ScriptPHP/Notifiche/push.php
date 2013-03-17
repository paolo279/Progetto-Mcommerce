<?php

ini_set("display_errors",1);
ini_set("display_errors",1);
// database
define("DB_HOST", "62.149.150.123");
define("DB_USER", "Sql382536");
define("DB_PASSWORD", "7cd2a23a");
define("DB_NAME", "Sql382536_3");
define("DB_CHARSET","utf8");
$mysqli=new mysqli(DB_HOST,DB_USER,DB_PASSWORD,DB_NAME);
if($mysqli->connect_errno){
    echo "Failed to connect to MySQL:(".$mysqli->connect_errno.") ".$mysqli->connect_error;
}
class GCM{
    function __construct(){}
    public function send_notification($registatoin_ids,$message){
        // GOOGLE API KEY
        define("GOOGLE_API_KEY","AIzaSyCXUoQxGHSnyoCbbCXwp9n2e3vX-kPp0hg");
        $url="https://android.googleapis.com/gcm/send";
        $fields=array(
            "registration_ids"=>$registatoin_ids,
            "data"=>$message,
        );
        var_dump($fields);
        $headers=array(
            "Authorization: key=".GOOGLE_API_KEY,
            "Content-Type: application/json"
        );
        $ch=curl_init();
        curl_setopt($ch,CURLOPT_URL,$url);
        curl_setopt($ch,CURLOPT_POST,true);
        curl_setopt($ch,CURLOPT_HTTPHEADER,$headers);
        curl_setopt($ch,CURLOPT_RETURNTRANSFER,true);
        curl_setopt($ch,CURLOPT_SSL_VERIFYPEER,false);
        curl_setopt($ch,CURLOPT_POSTFIELDS,json_encode($fields));
        $result=curl_exec($ch);
        if($result===FALSE){
            die("Curl failed: ".curl_error($ch));
        }
        curl_close($ch);
        echo $result;
    }
}
// ======================
//=INVIA LE NOTIFICHE AGLI UTENTI =
// ======================
$messaggio="Ciao, sono una notifica!";
$pushCounter=0;
$registatoin_ids=array();
$result2=$mysqli->query("SELECT * FROM android_udid");
while($obj2=$result2->fetch_object()){
    $token=$obj2->registration_id;
    if($token!=""){
        $registatoin_ids[]=$token;
        $pushCounter++;
    }
}
if($pushCounter>0){
    $gcm=new GCM();
    $message=array("price"=>$messaggio);
    $result_android=$gcm->send_notification($registatoin_ids,$message);
    echo $result_android;
}
?>
