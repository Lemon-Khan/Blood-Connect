<?php
	
		$connection = mysql_connect('localhost','root','') or die(mysql_error());
		$database = mysql_select_db("blood_donation",$connection);

		$username = $_POST["username"];
		$password = $_POST["password"];

		    $res = mysql_query("SELECT * FROM blood_given_by_donor");
			$now = time(); // or your date as well
			$status1="expired";
			while($row = mysql_fetch_array($res))
			{
				$date = $row['date_of_blood_given'];
				$donor_id = $row['donor_id'];
				$bag1 = $row['bag1'];
				$bag2 = $row['bag2'];
				$your_date = strtotime($date);
     			$datediff = $now - $your_date;
     			$diff= floor($datediff/(60*60*24));

				if($diff>10)
				{
					if($bag1=="available")
					{
						mysql_query("UPDATE blood_given_by_donor set bag1='$status1' where donor_id='$donor_id' AND date_of_blood_given='$date'");
					}
					if($bag2=="available")
					{
						mysql_query("UPDATE blood_given_by_donor set bag2='$status1' where donor_id='$donor_id' AND date_of_blood_given='$date'");
					}
				}
			}



		$sql = "SELECT count(*) FROM blood_bank WHERE bank_username = '$username' AND bank_password ='$password' LIMIT 1";
		$result = mysql_query($sql) or die(mysql_error());
		$flag['code']=0;
		if (mysql_result($result, 0) > 0)
		{
			$flag['code']=1;
		}
		
		print(json_encode($flag));
		mysql_close($connection);
?>