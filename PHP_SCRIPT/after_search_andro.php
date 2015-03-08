<?php
		
        $connection = mysql_connect('localhost','root','') or die(mysql_error());
        $database = mysql_select_db("blood_donation",$connection);
		$bgroup=$_POST['bgroup'];
		$city=$_POST['city'];
      
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

		$result = mysql_query("SELECT * FROM donor WHERE donor_blood_group = '$bgroup' AND donor_city='$city' AND donor_id IN (SELECT donor_id FROM blood_given_by_donor WHERE (bag1='available' OR bag2='available'))");
if($city=='Dhaka Division' || $city=='Barisal Division' || $city=='Khulna Division' || $city=='Rajshahi Division' || $city=='Rangpur Division' || $city=='Sylhet Division' )
	{
     $result = mysql_query("SELECT * FROM donor WHERE donor_blood_group = '$bgroup' AND division='$city' AND donor_id IN (SELECT donor_id FROM blood_given_by_donor where bag1='available' OR bag2='available')");  
    }
    else
	{
	   $result = mysql_query("SELECT * FROM donor WHERE donor_blood_group = '$bgroup' AND donor_city='$city' AND donor_id IN (SELECT donor_id FROM blood_given_by_donor WHERE (bag1='available' OR bag2='available'))");				
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