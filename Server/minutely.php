<?php
/*Cron will automaticly run this program every minute, seeing which polls are old. Old polls have their data sent to the specified email and are removed.*/
namespace Examples\Transmisson;
require_once "config.php";
require 'vendor/autoload.php';

use SparkPost\SparkPost;
use SparkPost\Transmission;


SparkPost::setConfig(["key"=>SPARK_KEY]);


$result = $db->query("SELECT * FROM poll WHERE end_time < NOW()");

while($row = $result->fetch_array()){
    try {
    	$results = Transmission::send(array(
    		"from"=>"From Envelope <xeonjake@gmail.com>",
    		"recipients"=>array(
    			array(
    			"address"=>array(
    					"email"=>$row['email']
    				)
    			)
    		),
    		"template"=>"suggest-app-poll-dat",
            'substitutionData'=>$row,
    	));
    } catch (\Exception $exception) {
    	echo $exception->getMessage();
    }
}

$db->query("DELETE FROM poll WHERE end_time < NOW()");