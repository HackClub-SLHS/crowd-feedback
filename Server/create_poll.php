<?php
require_once "config.php";
$img_errors = [1 => 'maximum file size in php.ini exveeded', 2 => 'Maximum file size in HTML form exceeded', 3 => 'Only part of file was uploaded', 4 => 'No file was selected to upload.'];

$upload_dir = "img/";


if(!isset($_FILES['img1'])   || !isset($_FILES['img2']) ||
   !isset($_POST['opt1'])    || !isset($_POST['opt2'])  ||
   !isset($_POST['question'])|| !isset($_POST['email'])   )die('{"error": "NOT ENOUGH INFO TO PROCESS REQUEST"}');


/*-----------------------------------------------------------
            File Checking and Setting
-------------------------------------------------------------*/

$_FILES['img1']['error'] == 0 or die("Image1 error: ". $img_errors[$_FILES['img1']['error']]);

$_FILES['img2']['error'] == 0 or die("Image2 error: ". $img_errors[$_FILES['img2']['error']]);


//Find a unique filename
$now = time();
while (file_exists($filename1 = $upload_dir . $now . 'poll_image'."-".$_FILES['img1']['name'])) $now++;

@move_uploaded_file($_FILES['img1']['tmp_name'], $filename1) or die('{"error": "Permissions or related error moving img1 to '.$filename1.'"}');


//Find a unique filename
$now = time();
while (file_exists($filename2 = $upload_dir . $now . 'poll_image'."-".$_FILES['img2']['name'])) $now++;

move_uploaded_file($_FILES['img2']['tmp_name'], $filename2) or die('{"error": "Permissions or related error moving img2 to '.$filename2.'"');


/*-----------------------------------------------------------
                 Updating Database
-------------------------------------------------------------*/

$result = $db->query("INSERT INTO image (location) VALUES ('$filename1');");
if(!$result) die('{"error": "img1\'s name ($filename1) wouldn\'t save to database: '.$db->error.'"');
$img_id1 = $db->insert_id;

$result = $db->query("INSERT INTO image (location) VALUES ('$filename2');");
if(!$result) die('{"error": "img1\'s name ($filename1) wouldn\'t save to database: '.$db->error.'"}');
$img_id2 = $db->insert_id;




$query = $db->prepare("INSERT INTO poll (opt_1, opt_2, question, email, img_1, img_2, end_time) VALUES (?, ?, ?, ?, $img_id1, $img_id2, ADDTIME(NOW(), '0:10:0.0'))") or die('{"error": "Failed to prepare db statement! '. $db->error.'"}');
$query->bind_param('ssss',   $_POST['opt1'],     $_POST['opt2'],
                             $_POST['question'], $_POST['email']) or die('{"error": "Params wouldn\'t bind!"}');
$query->execute() or die('{"error": "Query failed: '.$query->error.'"}');


$query->free_result();

echo '{"success": "poll made"}';