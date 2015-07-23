<?php
require_once "config.php";
if(!isset($_POST['id']) || !isset($_POST['vote'])) die('{"error": "NOT ENOUGH INFO TO PROCESS REQUEST"}');

if(!is_numeric($_POST['id'])) die('{"error":"id must be a number"}');
if($_POST['vote'] != "1" && $_POST['vote'] != "2") die('{"error":"Must pick to vote 1 or 2"}');


//Increment the chosen collumn by one
$responce = $db->query("UPDATE poll SET votes_{$_POST['vote']} = votes_{$_POST['vote']} + 1 WHERE id = {$_POST['id']}");

echo $responce ? "Worked!" : "Didn't work".$db->error;