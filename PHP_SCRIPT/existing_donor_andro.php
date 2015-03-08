<?php

        $connection = mysql_connect('localhost','root','') or die(mysql_error());
        $database = mysql_select_db("blood_donation",$connection);
        
        $id = $_POST["id"];
        $bags = $_POST["bags"];
        $date = $_POST["date"];
    	$username=$_POST['username'];
		$result = mysql_query("SELECT * FROM blood_bank WHERE bank_username = '$username'");
					while($row = mysql_fetch_array($result))
					{
						$bank_id = $row['bank_id'];
  					}
        $result = mysql_query("SELECT * FROM donor WHERE donor_govt_id = '$id'");
                    while($row = mysql_fetch_array($result))
                    {
                        $donor_id = $row['donor_id'];
                    }  

         if($bags==1)
         {
         $bag1="available";
         $bag2="none";
         }
         else
         {
         $bag1="available";
         $bag2="available";
         }

        $query="insert into blood_given_by_donor (donor_id,bank_id,no_of_bags,date_of_blood_given,bag1,bag2) values ('$donor_id','$bank_id','$bags','$date','$bag1','$bag2')";
        
        $flag["code"]=0;

    	if(mysql_query($query))
		{
			$flag["code"]=1;
		}

        print(json_encode($flag));

?>