<?php
	include 'utilDbConnection.php';
	
	// generate and execute a query
		
	$quer = $_GET['query'];
	$results = $_GET['results'];
	if ($quer == null || $quer == ""){
		$quer = "";
	}
	
	$query = "select s.name, count(o.id) from servers s left outer join occurrences o on s.id=o.serverid where s.name like '%".$quer."%' group by s.name order by s.name LIMIT ".$results." ";
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
			echo "\"name\":\"".$row[0]."\",";
			echo "\"noerrors\":\"".$row[1]."\",";
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
