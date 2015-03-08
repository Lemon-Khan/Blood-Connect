<?php

        $connection = mysql_connect('localhost','root','') or die(mysql_error());
        $database = mysql_select_db("blood_donation",$connection);
        
        $donor_name = $_POST["donor_name"];
        $id = $_POST["id"];
        $donor_blood_group = $_POST["donor_blood_group"];
        $donor_contact_no = $_POST["donor_contact_no"];
        $donor_address = $_POST["donor_address"];
        $donor_city = $_POST["donor_city"];
        $username= $_POST['username'] ;
        $division = $_POST["division"];

		$result = mysql_query("SELECT * FROM blood_bank WHERE bank_username = '$username'");
				while($row = mysql_fetch_array($result))
				{
						$bank_id = $row['bank_id'];
		  		} 
        
        $query="insert into donor (bank_id,donor_name,donor_govt_id,donor_blood_group,donor_contact_no,donor_address,donor_city,division) values ('$bank_id','$donor_name','$id','$donor_blood_group','$donor_contact_no','$donor_address','$donor_city','$division')";
        
        $flag["code"]=0;
      
    	if(mysql_query($query))
		{
			$flag["code"]=1;
		}

        print(json_encode($flag));
        mysql_close($connection);
?>