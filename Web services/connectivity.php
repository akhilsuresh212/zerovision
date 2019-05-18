<?php

$link = new mysqli('localhost', 'root', '', 'zero');
if (!$link) {
    die('Could not connect to MySQL: ' . mysql_error());
}
?>
