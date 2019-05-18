<?php
function sendSMS($mob, $otp)
{
//Your authentication key
$authKey = urlencode("268998AaXy7HlSUfQz5c97392b");

//Multiple mobiles numbers separated by comma
$mobileNumber = urlencode($mob);
$msg = urlencode("Your OTP IS :".$otp);
//Sender ID,While using route4 sender id should be 6 characters long.
$senderId = "TESTIN";

//Your message to send, Add URL encoding here.
$message = urlencode("Test message");

//Define route 
$route = 4;
$country = 91;


$curl = curl_init();

curl_setopt_array($curl, array(
  CURLOPT_URL => "http://api.msg91.com/api/sendhttp.php?route=4&sender=TESTIN&mobiles=".$mobileNumber."&authkey=".$authKey."&encrypt=&message=".$msg."&flash=&unicode=&afterminutes=&response=4&campaign=&country=91",
  CURLOPT_RETURNTRANSFER => true,
  CURLOPT_ENCODING => "",
  CURLOPT_MAXREDIRS => 10,
  CURLOPT_TIMEOUT => 30,
  CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
  CURLOPT_CUSTOMREQUEST => "GET",
  CURLOPT_SSL_VERIFYHOST => 0,
  CURLOPT_SSL_VERIFYPEER => 0,
));

$response = curl_exec($curl);
$err = curl_error($curl);

curl_close($curl);

if ($err) {
  echo "cURL Error #:" . $err;
} else {
  echo $response;
}

}


?>