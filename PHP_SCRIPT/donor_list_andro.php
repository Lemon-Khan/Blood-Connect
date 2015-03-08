<?php		
					
	$connection = mysql_connect('localhost','root','') or die(mysql_error());
	$database = mysql_select_db("blood_donation",$connection);

	$username= $_POST['username'] ;
	$result = mysql_query("SELECT * FROM blood_bank WHERE bank_username = '$username'");
	while($row = mysql_fetch_array($result))
	{
		$bank_id = $row['bank_id'];
	} 
	
    $bgroup = $_POST["bgroup"];
	$city = $_POST["city"];
	$refine = $_POST["refine"];
	
	if($refine == "Available")
	{
		$result = mysql_query("SELECT * FROM donor WHERE donor_blood_group = '$bgroup' AND donor_city='$city' AND donor_id IN (SELECT donor_id FROM blood_given_by_donor where bank_id='$bank_id' AND (bag1='available' OR bag2='available'))");
	}
	else
	{
		$result = mysql_query("SELECT * FROM donor WHERE donor_blood_group = '$bgroup' AND donor_city='$city' AND donor_id IN (SELECT donor_id FROM blood_given_by_donor where bank_id='$bank_id')");
	}

$all_info=array();

while($row = mysql_fetch_array($result))
{
	$info=array();
	$info['donor_name'] = $row['donor_name'];
	$info['donor_govt_id'] = $row['donor_govt_id'];
	$info['donor_contact_no'] = $row['donor_contact_no'];
	$info['donor_address'] = $row['donor_address'];
	$info['donor_city']=$row['donor_city'];

	array_push($all_info,$info);
}

print(json_encode($all_info));
mysql_close($connection);

	        ?>