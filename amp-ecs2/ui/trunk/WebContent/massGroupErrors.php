<?php
	include 'utilDbConnection.php';
	include 'utilErrorGroup.php';
	openlog('mylog', LOG_PID | LOG_ODELAY,LOG_LOCAL4);
	syslog(LOG_ERR, 'ECS - EP');
	
	
	//outputFields needed in filter.php
	$outputFields = "distinct(my.id)";
	include 'filter.php';
	
	$value = $_GET['value'];
	$mainquery = $mainquery1.$query.$mainquery2; //no order by causes
	$result = pg_query($connection, $mainquery) or die("Error in query: $query.
	" . pg_last_error($connection));
	$rows = pg_num_rows($result);
    for ($i=0; $i<$rows; $i++){
        $row = pg_fetch_row($result, $i);
        updateErrorGroup2($row[0], $value, $connection);
        syslog(LOG_ERR, "updateErrorGroup2(".$row[0].",".$value.")");
    }
		
	// close database connection
	pg_close($connection);

?>
