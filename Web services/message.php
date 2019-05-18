<?php

include_once('./connectivity.php');
include_once('./smsText.php');
if ($_SERVER['REQUEST_METHOD'] == "POST") {

    $user_id = isset($_POST['user_id']) ? mysqli_real_escape_string($link, $_POST['user_id']) : "";
    $friend_id = isset($_POST['friend_id']) ? mysqli_real_escape_string($link, $_POST['friend_id']) : "";
 
	$msg_from = isset($_POST['msg_from']) ? mysqli_real_escape_string($link, $_POST['msg_from']) : "";
    $msg_to = isset($_POST['msg_to']) ? mysqli_real_escape_string($link, $_POST['msg_to']) : "";
	$msg = isset($_POST['msg']) ? mysqli_real_escape_string($link, $_POST['msg']) : "";
 
    $json = array();

    if (isset($_POST['get_message'])) {
		
        $sql = "SELECT * FROM message WHERE msg_from ='" . $friend_id . "' and  msg_to ='" . $user_id . "' and status = 0";
        $result = mysqli_query($link, $sql);
        if ($result) {
            if (mysqli_num_rows($result) == 0) {
                $json = array("status" => 0, "msg" => "");
            } else {
            while ($row = mysqli_fetch_array($result)) {
                $data = array("msg" => $row['msg']);
                array_push($json, $data);
            }
			$json = array("status" => 1, "result" => $json);
			
			$sql = "UPDATE message set status = 1  WHERE msg_from ='" . $friend_id . "' and  msg_to ='" . $user_id . "'";
        		
			$result = mysqli_query($link, $sql);
            
			}
        }
    }
	else if (isset($_POST['send_msg'])) {
		
        $sql = "INSERT INTO message (`msg_from`,`msg_to`,`msg`,`status`) values('$msg_from','$msg_to','$msg',0)";
	
        $result = mysqli_query($link, $sql);
        if ($result) {

			$json = array("status" => 1, "msg" => "send");

			}else{
				$json = array("status" => 0, "msg" => 'Error');
        }
    }


	else {
        $json = array("status" => 0, "msg" => "Request method not accepted");
    }

    @mysqli_close($link);
    /* Output header */
    header('Content-type: application/json');
    echo json_encode($json);
}
?>
