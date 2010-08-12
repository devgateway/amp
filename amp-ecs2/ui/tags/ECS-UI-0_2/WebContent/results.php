<?php
	// database access parameters
	// alter this as per your configuration
	$host = "localhost";
	$user = "ecs";
	$pass = "ecs";
	$db = "amp_ecs";
	
	// open a connection to the database server
	$connection = pg_connect ("host=$host dbname=$db user=$user password=$pass");
	
	if (!$connection)
	{
		die("Could not open connection to database server");
	}
	
	
	$sortField = $_GET['sort'];
	$sortDir = $_GET['dir'];
	$pagStart = $_GET['startIndex'];
	$pagResults = $_GET['results'];
	if ($pagStart == null)
		$pagStart = 0;
	if ($pagResults == null)
		$pagResults = 10;
	
	$mainquery = "select o.id, s.name, e.md5, u.login, u.fullname, u.password, x.date, x.browser, x.sessionid, e.jiranumber, e.stacktrace from occurrences o, servers s, errors e, users u, scenes x where o.serverid=s.id and o.errorid=e.id and o.userid=u.id and o.sceneid=x.id";
	$countquery = "select count(*) from occurrences o, servers s, errors e, users u, scenes x where o.serverid=s.id and o.errorid=e.id and o.userid=u.id and o.sceneid=x.id";

	$query = "";
	
	$filter = $_GET['fLogin']; 
	if ($filter != null){
		$query = $query." and u.login like '".$filter."'";
	}
	
	$filter = $_GET['fServer']; 
	if ($filter != null){
		$query = $query." and s.name like '".$filter."'";
	}
	
	$filter = $_GET['fLogin']; 
	if ($filter != null){
		$query = $query." and u.login like '".$filter."'";
	}
	
	$filter = $_GET['startDate']; 
	if ($filter != null){
		$query = $query." and x.date >= '".$filter."'";
	}

	$filter = $_GET['stopDate']; 
	if ($filter != null){
		$query = $query." and x.date <= '".$filter."'";
	}
	
	$countquery = $countquery.$query;
	$query = $mainquery.$query;

	// generate and execute a query
	$result = pg_query($connection, $countquery) or die("Error in query: $countquery.
	" . pg_last_error($connection));
	// get the number of rows in the resultset
	$trow = pg_fetch_row($result, 0);
	$totalrows = $trow[0];
	
	
	$sortField = $_GET['sort'];
	$sortDir = $_GET['dir'];
	
	$tableSort="o.id";
	if ($sortField == "servername"){
		$tableSort="s.name";
	}
	if ($sortField == "login"){
		$tableSort="u.login";
	}
	if ($sortField == "fullname"){
		$tableSort="u.fullname";
	}
	if ($sortField == "date"){
		$tableSort="x.date";
	}
	if ($sortField == "jiranumber"){
		$tableSort="e.jiranumber";
	}
	if ($sortField == "session"){
		$tableSort="x.sessionid";
	}
	
	
	$query = $query." order by ".$tableSort." ".$sortDir." OFFSET ".$pagStart." LIMIT ".$pagResults." ";
	
	$result = pg_query($connection, $query) or die("Error in query: $query.
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
	    	echo "\"errsnippet\":".json_encode(str_replace("\"", "'", substr($row[10], 0, 100))."...");
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
