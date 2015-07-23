<?php
namespace Examples\Transmisson;
require_once "../config.php";
require '../vendor/autoload.php';

use SparkPost\SparkPost;
use SparkPost\Transmission;


SparkPost::setConfig(["key"=>SPARK_KEY]);

try {
	$results = Transmission::send(array(
		"from"=>"From Envelope <xeonjake@gmail.com>",
		"recipients"=>array(
			array(
			"address"=>array(
					"email"=>"mydogisjibe@gmail.com"
				)
			)
		),
		"template"=>"suggest-app-poll-dat",
        'substitutionData'=>array('question'=>'Who is awesome?', 'opt_1' => 'Brent', 'opt_2'=>'Brent of corse!', 'vote_1' => '69', 'vote_2' => '72'),
	));
	echo 'Congrats you can use your SDK!';
} catch (\Exception $exception) {
	echo $exception->getMessage();
}