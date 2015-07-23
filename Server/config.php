<?php
define("SPARK_KEY", "24a04c9715814a251363025cd1105b7d03d8589f");


define("DB_SERVER", "127.0.0.1");
define("DB_USER", "xeonjake");
define("DB_PASSWORD", "");
define("DB_NAME", "suggest_app");

date_default_timezone_set('UTC');


$db = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_NAME);

if ($db->connect_errno >0) die("SERVER PROBLEM: ".$db->connect_error);

//For php 5.6
//const PHP_ERRORS = [1 => 'maximum file size in php.ini exveeded', 2 => 'Maximum file size in HTML form exceeded', 3 => 'Only part of file was uploaded', 4 => 'No file was selected to upload.'];