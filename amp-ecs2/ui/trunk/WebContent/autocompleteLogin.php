<?php
	include 'utilDbConnection.php';
	
	// generate and execute a query
		
	$quer = $_GET['query'];
	$results = $_GET['results'];
	if ($quer == null || $quer == ""){
		$quer = "";
	}
	
	$query = "select u.login, u.fullname, s.name from users u, servers s where u.serverid=s.id and (lower(u.login) like lower('%".$quer."%') OR lower(u.fullname) like lower('%".$quer."%')) order by u.login asc LIMIT ".$results." ";
	$result = pg_query($connection, $query) or die("Error in query: $query.
	" . pg_last_error($connection));
	
	// get the number of rows in the resultset
	$rows = pg_num_rows($result);
	if ($rows > 0){
		echo "{";
		//echo "\"totalRecords\":".$totalrows.",";
		//echo "\"startIndex\":0,";
		//echo "\"pageSize\":".$pagResults.",";
		echo "\"records\":[";
		
	    for ($i=0; $i<$rows; $i++){
	        $row = pg_fetch_row($result, $i);
	    	echo "{";
			echo "\"login\":\"".$row[0]."\",";
			echo "\"fullname\":\"".$row[1]."\",";
			echo "\"servername\":\"".$row[2]."\",";
			echo "}";
			if ($i < $rows - 1){
				echo ",";
			}
	    }
		
		
		echo "\n]\n}";
		/*
	    echo "[";
	    for ($i=0; $i<$rows; $i++){
	        $row = pg_fetch_row($result, $i);
			echo "[";
			for ($j=0; $j<count($row); $j++){
				echo "\"";
				echo $row[$j];
				
				echo "\"";
				if ($j < count($row) - 1)
					echo ",";
			}
			echo "]";
			if ($i < $rows - 1){
				echo ",";
			}
	    }
	    echo "]";
	    */
	}
	
	
	// close database connection
	pg_close($connection);

?>
