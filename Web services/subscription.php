<?php

include_once('./connectivity.php');
include_once('./smsText.php');
if ($_SERVER['REQUEST_METHOD'] == "POST") {

    $user_id = isset($_POST['user_id']) ? mysqli_real_escape_string($link, $_POST['user_id']) : "";
    $friend_id = isset($_POST['friend_id']) ? mysqli_real_escape_string($link, $_POST['friend_id']) : "";

    $json = array();

    if (isset($_POST['send_friends_request'])) {

        $sql = "SELECT * FROM user_login WHERE user_id ='" . $friend_id . "' ";
        $result = mysqli_query($link, $sql);
        if ($result) {
            if (mysqli_num_rows($result) == 0) {
                $json = array("status" => 0, "msg" => "user not exist!");
            } else {

                $sql = "SELECT * FROM user_login WHERE user_id ='" . $user_id . "' ";
                $result = mysqli_query($link, $sql);
                if ($result) {
                    $otp = rand(100000, 999999);
                    $sql = " INSERT INTO `friends`(`blind_id`,`friend_id`,`status` )";
                    $sql .= "VALUES('$user_id','$friend_id',0);";
                    $qur = mysqli_query($link, $sql);
                    if ($qur) {
                        $json = array("status" => 1, "msg" => "Friends request send");
                    } else {
                        $json = array("status" => 0, "msg" => "Error Friends request !");
                    }
                }
            }
        }
    }

    if (isset($_POST['friends_list'])) {

        $sql = "SELECT user_login.username,user_login.mob,user_login.user_id FROM user_login ";
        $sql .= "LEFT JOIN friends ON user_login.user_id = friends.friend_id ";
        $sql .= "WHERE friends.blind_id = '$user_id' ";
        $result = mysqli_query($link, $sql);
        if ($result) {
            while ($row = mysqli_fetch_array($result)) {
                $data = array("username" => $row['username'],
                    "mob" => $row['mob'],
                    "user_id" => $row['user_id']
                );
                array_push($json, $data);
            }
            $json = array("status" => 1, "result" => $json);
        } else {
            $json = array("status" => 0, "msg" => "Error Friends request !");
        }
    }

    @mysqli_close($link);
    /* Output header */
    header('Content-type: application/json');
    echo json_encode($json);
}
?>
