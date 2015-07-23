<?php

define("DB_SERVER", "127.0.0.1");
define("DB_USER", "xeonjake");
define("DB_PASSWORD", "");
define("DB_NAME", "suggest_app");

date_default_timezone_set('UTC');

echo PHP_VERSION;
$db = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_NAME);

echo ($db->connect_errno >0) ? $db->connect_error: "</br>Works!";

//$passOptions = ["cost" => 10]; //Password hashing options
