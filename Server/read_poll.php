<?php
require_once "config.php";

//Select a random poll
/*
SELECT poll.*, i1.location AS img_1 i2.location AS img_2 FROM poll 
    INNER JOIN image i1 ON poll.img_1 = i1.id 
    INNER JOIN image i2 ON poll.img_2 = i2.id
LIMIT 1
*/
$result = $db->query("SELECT poll.*, i1.location AS img_1, i2.location AS img_2 FROM poll INNER JOIN image i1 ON poll.img_1 = i1.id INNER JOIN image i2 ON poll.img_2 = i2.id ORDER BY RAND() LIMIT 1") or die($db->error);

$result = $result->fetch_array();
echo '{"success":"Data collected","poll":{"id":'.$result['id'].',"question":"'.$result['question'].'", "email":"'.$result['email'].'","opt1":{"text":"'.$result['opt_1'].'","img":"'.$result['img_1'].'"},"opt2":{"text":"'.$result['opt_2'].'","img":"'.$result['img_2'].'"}}}';