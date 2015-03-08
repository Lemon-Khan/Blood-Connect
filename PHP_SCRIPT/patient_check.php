<?php
$connection = mysql_connect('localhost','root','') or die(mysql_error());

$database = mysql_select_db("blood_donation",$connection);

$id=$_POST["id"];

$data=mysql_query("SELECT * FROM patient where patient_govt_id='$id' LIMIT 1");

$flag['message']=1;

if(mysql_num_rows($data)>0)
{
	$flag['message']=0;
}

print(json_encode($flag));

?>
