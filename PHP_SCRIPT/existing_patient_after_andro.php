<?php
        $connection = mysql_connect('localhost','root','') or die(mysql_error());
        $database = mysql_select_db("blood_donation",$connection);
        
        $bags = $_POST["bags"];
        $date = $_POST["datepicker"];
    	$username=$_POST["username"];

    	$flag["code"]=0;
    	
    	$result = mysql_query("SELECT * FROM blood_bank WHERE bank_username = '$username'");

			while($row = mysql_fetch_array($result))
			{
				$bank_id = $row['bank_id'];
		    }

		     $govt_id=$_POST['patient_id'];

		    $result = mysql_query("SELECT * FROM patient WHERE patient_govt_id = '$govt_id'");

			while($row = mysql_fetch_array($result))
			{
			 $patient_id=$row['patient_id'];
		    }

	    $query="insert into blood_taken_by_patient (patient_id,bank_id,no_of_bags,date_of_blood_taken) values ('$patient_id','$bank_id','$bags','$date')";
	  
	    $counter=0;
	    $bgroup=$_POST['bgroup'];

	    for($i=0;$i<$bags;$i++)
	    {
	    	$data=mysql_query("UPDATE blood_given_by_donor SET bag1='donated' WHERE bank_id='$bank_id' AND bag1='available' AND donor_id IN (select donor_id from donor where donor_blood_group='$bgroup') LIMIT 1");
	    	$cal=mysql_affected_rows();
	    	if($cal==1)
	    	{
	    		$counter++;
	    	}
	    }
	   	$bags=$bags-$counter;
	   	if($bags!=0)
	   	{
	   		for($i=0;$i<$bags;$i++)
	    	{
		    	$data=mysql_query("UPDATE blood_given_by_donor SET bag2='donated' WHERE bank_id='$bank_id' AND bag2='available' AND donor_id IN (select donor_id from donor where donor_blood_group='$bgroup') LIMIT 1");
		    	if($data)
		    	{
		    		$counter++;
		    	}
	        }
	   	}
	    if(mysql_query($query))
		{
			$flag["code"]=1;
			print(json_encode($flag));
		}
		else
		{
			print(json_encode($flag));
		}
		mysql_close($connection);
?>

<!-- ................................................PHP SCRIPT END.................................................. -->