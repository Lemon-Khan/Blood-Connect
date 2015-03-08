<?php
        $connection = mysql_connect('localhost','root','') or die(mysql_error());
        $database = mysql_select_db("blood_donation",$connection);
        
        $patient_name = $_POST["patient_name"];
        $id = $_POST["id"];
        $patient_blood_group = $_POST["patient_blood_group"];
        $disease = $_POST["disease"];
        $patient_contact_no = $_POST["patient_contact_no"];
        $patient_address = $_POST["patient_address"];
        $patient_city = $_POST["patient_city"];
        $username= $_POST["username"] ;

		$result = mysql_query("SELECT * FROM blood_bank WHERE bank_username = '$username'");
				while($row = mysql_fetch_array($result))
				{
						$bank_id = $row['bank_id'];
		  		} 
        
        $query="insert into patient(bank_id,patient_name,patient_govt_id,patient_blood_group,patient_disease,patient_contact_no,patient_address,patient_city) values ('$bank_id','$patient_name','$id','$patient_blood_group','$disease','$patient_contact_no','$patient_address','$patient_city')";

        $flag["code"]=0;

    	if(mysql_query($query))
		{
		 $flag["code"]=1;
		}
		print(json_encode($flag));
        mysql_close($connection);


?>





