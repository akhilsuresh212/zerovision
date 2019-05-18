<?php

include_once('./connectivity.php');
include_once('./smsText.php');
if ($_SERVER['REQUEST_METHOD'] == "POST") {

    $user_id = isset($_POST['user_id']) ? mysqli_real_escape_string($link, $_POST['user_id']) : "";
    $username = isset($_POST['username']) ? mysqli_real_escape_string($link, $_POST['username']) : "";
    $user_password = isset($_POST['user_password']) ? mysqli_real_escape_string($link, $_POST['user_password']) : "";
    $mob = isset($_POST['mob']) ? mysqli_real_escape_string($link, $_POST['mob']) : "";
    $user_type = isset($_POST['user_type']) ? mysqli_real_escape_string($link, $_POST['user_type']) : "";
    $user_status = isset($_POST['user_status']) ? mysqli_real_escape_string($link, $_POST['user_status']) : "";
    $otp = isset($_POST['otp']) ? mysqli_real_escape_string($link, $_POST['otp']) : "";
    $json = array();

    if (isset($_POST['new_user'])) {
        // Insert data into data base
        $sql = "SELECT * FROM user_login WHERE user_id ='" . $user_id . "' ";
        $result = mysqli_query($link, $sql);
        if ($result) {
            if (mysqli_num_rows($result) > 0) {
                $json = array("status" => 0, "msg" => "user already exists!");
            } else {

                $otp = rand(100000, 999999);
                $sql = " INSERT INTO `user_login`(`user_id`,`username`,`user_password`,`mob`,`user_type`,`otp`,`user_status` )";
                $sql .= "VALUES('$user_id','$username','$user_password','$mob','$user_type',$otp,0);";
                $qur = mysqli_query($link, $sql);
                if ($qur) {
                    $json = array("status" => 1, "msg" => "Done User added!", "account_status" => "NOT VERIFIED");
                    //sendSMS($mob, $otp);
                } else {
                    $json = array("status" => 0, "msg" => "Error adding user!");
                }
            }
        }
    } else if (isset($_POST['user_verification'])) {

        $sql = "SELECT * FROM user_login WHERE user_id = '$user_id' ";
        $result = mysqli_query($link, $sql);
        if ($result) {
            if (mysqli_num_rows($result) > 0) {
                $sql = " UPDATE `user_login` ";
                $sql .= " SET user_status =1 WHERE user_id ='" . $user_id . "' and otp = $otp";
                $qur = mysqli_query($link, $sql);
                if ($qur) {
                    $json = array("status" => 1, "msg" => "Account Verified");
                } else {
                    $json = array("status" => 0, "msg" => "Invalid OTP!");
                }
            } else {
                $json = array("status" => 0, "msg" => "Invalid UserID!");
            }
        }
    } else if (isset($_POST['user_login'])) {
        $sql = "SELECT * FROM user_login WHERE user_id = '$user_id' AND user_password = '$user_password' ";
        $result = mysqli_query($link, $sql);
        if ($result) {
            if (mysqli_num_rows($result) > 0) {
                if ($row = mysqli_fetch_array($result)) {
                    if ($row['user_status'] == 0) {
                        $json = array("status" => -1, "msg" => "Not Verified" );
                    } else {
                        $json = array("status" => 1, "msg" => "Login Sucessfull" , "user_type" => $row['user_type'] );
                    }
                }
            } else {
                $json = array("status" => 0, "msg" => "Invalid username or password!");
            }
        } else {
            $json = array("status" => 0, "msg" => "Error login!");
        }
    } else if (isset($_POST['user_list'])) {

        $sql = "SELECT user_id,username FROM user_login ";
        $result = mysqli_query($link, $sql);
        if ($result) {
            while ($row = mysqli_fetch_array($result)) {
                $data = array("user_id" => $row['user_id'], "username" => $row['username']);
                array_push($json, $data);
            }
        } else {
            $json = array("status" => 0, "msg" => "Error Data!");
        }
    } else if (isset($_POST['send_otp'])) {
        // Update data into data base
        $otp = rand(100000, 999999);
        $sql = " UPDATE `user_login` ";
        $sql .= " SET otp = $otp ";
        $sql .= " WHERE user_id ='" . $user_id . "'";
        $qur = mysqli_query($link, $sql);
        if ($qur) {
            if (mysqli_affected_rows($link) > 0) {
                $sql = "SELECT mob FROM user_login WHERE user_id ='" . $user_id . "' ";
                $result = mysqli_query($link, $sql);
                if ($result) {
                    if ($row = mysqli_fetch_array($result)) {
                        $mob = $row['mob'];
                        sendSMS($mob, $otp);
                        $json = array("status" => 1, "msg" => "OTP Send successfully!");
                    }
                }
            }
        } else {
            $json = array("status" => 0, "msg" => "Error OTP SEND!");
        }
    } else if (isset($_POST['user_update_password'])) {
        // Update data into data base
        $sql = " UPDATE `user_login` ";
        $sql .= " SET user_password ='" . $user_password . "', otp= 0 ";
        $sql .= " WHERE user_id ='" . $user_id . "' and otp = $otp ";
        $qur = mysqli_query($link, $sql);
        if ($qur) {
            if (mysqli_affected_rows($link) > 0) {
                $json = array("status" => 1, "msg" => "update successfully!");
            } else {
                $json = array("status" => 0, "msg" => "Invalid Data!");
            }
        } else {
            $json = array("status" => 0, "msg" => "Error change password!");
        }
    } else if (isset($_POST['user_profile'])) {
        $sql = "SELECT username,mob FROM user_login WHERE user_id ='" . $user_id . "'";
        $result = mysqli_query($link, $sql);
        if ($result) {
            while ($row = mysqli_fetch_array($result)) {
                $data = array("username" => $row['username'],
                    "mob" => $row['mob'],
                    "email" => $user_id
                );
                array_push($json, $data);
            }
            $json = array("status" => 1, "result" => $json);
        } else {
            $json = array("status" => 0, "msg" => "Error DataaaAA!");
        }
    } else if (isset($_POST['otp_verify'])) {
        $sql = "SELECT * FROM user_login WHERE user_id = '$user_id' AND user_status = $otp ";
        $result = mysqli_query($link, $sql);

        if ($result) {
            if (mysqli_num_rows($result) > 0) {
                $sql = " UPDATE `user_login` ";
                $sql .= " SET user_status = 1 ";
                $sql .= " WHERE user_id ='" . $user_id . "'";

                $qur = mysqli_query($link, $sql);
                if ($qur) {
                    $json = array("status" => 1, "msg" => "update successfully!");
                } else {
                    $json = array("status" => 0, "msg" => "Error update user!");
                }
            }
        } else {
            $json = array("status" => 0, "msg" => "Invalid OTP");
        }
    } else {
        $json = array("status" => 0, "msg" => "Request method not accepted");
    }

    @mysqli_close($link);
    /* Output header */
    header('Content-type: application/json');
    echo json_encode($json);
}
?>
