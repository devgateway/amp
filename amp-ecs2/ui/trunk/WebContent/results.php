<?php
	include 'utilDbConnection.php';
	
	//outputFields needed in filter.php
	$outputFields = "my.id, s.name, e.md5, u.login, u.fullname, u.password, x.date, x.browser, x.sessionid, e.jiranumber, e.stacktrace, my.count";
	include 'filter.php';
	
	// generate and execute a query
	$result = pg_query($connection, $countquery) or die("Error in query: $countquery.
	" . pg_last_error($connection));
	// get the number of rows in the resultset
	$trow = pg_fetch_row($result, 0);
	$totalrows = $trow[0];
	
	$result = pg_query($connection, $mainquery) or die("Error in query: $query.
	" . pg_last_error($connection));
	
	// get the number of rows in the resultset
	$rows = pg_num_rows($result);
	if ($rows > 0){
		echo "{";
		echo "\"totalRecords\":\"".$totalrows."\",";
		echo "\"startIndex\":\"".$pagStart."\",";
		echo "\"pageSize\":\"".$pagResults."\",";
		echo "\"records\":[";
		
	    for ($i=0; $i<$rows; $i++){
	        $row = pg_fetch_row($result, $i);
	    	echo "{";
			echo "\"id\":\"".$row[0]."\",";
			echo "\"servername\":".json_encode($row[1]).",";
	    	echo "\"md5\":".json_encode($row[2]).",";
	    	echo "\"login\":".json_encode($row[3]).",";
	    	echo "\"fullname\":".json_encode($row[4]).",";
	    	echo "\"password\":".json_encode($row[5]).",";
	    	echo "\"date\":".json_encode($row[6]).",";
	    	
	    	$first  = strtok($row[7], ' ');
			$second = strtok(')');
			$third = strtok(' ');
	    	$forth = strtok('***');
	    	$os = strtok($second, ";");
	    	$os = strtok(";");$os = strtok(";");
	    	
	    	echo "\"browser\":".json_encode(str_replace("\"", "'",$forth)." (".str_replace("\"", "'",trim($os)).")").",";
	    	echo "\"browserfull\":".json_encode(str_replace("\"", "'",$row[7])).",";
	    	echo "\"session\":".json_encode($row[8]).",";
	    	echo "\"jiranumber\":".json_encode($row[9]).",";
	    	echo "\"stacktrace\":".json_encode(str_replace("\"", "'",$row[10])).",";
	    	echo "\"errsnippet\":".json_encode(str_replace("\"", "'", substr($row[10], 0, 100))."...").",";
	    	echo "\"count\":".json_encode($row[11]);
	    	echo "}";
			if ($i < $rows - 1){
				echo ",";
			}
	    }
		
		
		echo "\n]\n}";
	}
	
	
	// close database connection
	pg_close($connection);

?>
