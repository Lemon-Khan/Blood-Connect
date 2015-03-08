<?php
$connection = mysql_connect('localhost','root','') or die(mysql_error());
$database = mysql_select_db("blood_donation",$connection);

$id=$_POST["patient_id"];
$bgroup=$_POST['bgroup'];
$bags=$_POST["bags"];
$remaining=5-$_POST["total_blood"];
$username=$_POST['username'];
$flag["message"]=0;
$flag["remaining"]=$remaining;

            $result = mysql_query("SELECT * FROM blood_bank WHERE bank_username = '$username'");
			while($row = mysql_fetch_array($result))
			{
				$bank_id = $row['bank_id'];
		    }

$data=mysql_query("SELECT COUNT(bag1) FROM blood_given_by_donor where bank_id='$bank_id' AND bag1='available' AND donor_id IN (select donor_id from donor where donor_blood_group='$bgroup') ");

				while($row = mysql_fetch_array($data))
				{
					$totalbag1=$row['COUNT(bag1)'];				
				}
				  
$data=mysql_query("SELECT COUNT(bag2) FROM blood_given_by_donor where bank_id='$bank_id' AND bag2='available' AND donor_id IN (select donor_id from donor where donor_blood_group='$bgroup') ");

				while($row = mysql_fetch_array($data))
				{
					$totalbag2=$row['COUNT(bag2)'];				
				} 

				$total=$totalbag1+$totalbag2;

			if($bags<=$remaining && $bags<=$total)
			{
			   $flag["message"]=1;
			   print(json_encode($flag)); 
			}
			else
			{
				$flag["message"]=0;
				print(json_encode($flag));
			}
mysql_close($connection);

?>