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
	
	// generate and execute a query
		
	$id = $_GET['id'];
	$value = $_GET['value'];
	if ($id == null || trim($id) == ""){
		return;
	}
	
	$query = "SELECT errorGroup FROM errors WHERE id=".$id;
	$result = pg_query($connection, $query) or syslog(LOG_ERR, "Error in query: $query. ". pg_last_error($connection));
	// get the number of rows in the resultset
	$frow = pg_fetch_row($result);
	$errGroup = -1;
	$result = -1;
	if ($frow != false){
		$errGroup = $frow[0];
		
		$query = "SELECT id FROM errorGroup WHERE id=".$value;
		$result = pg_query($connection, $query) or syslog(LOG_ERR, "Error in query: $query. ". pg_last_error($connection));
		// get the number of rows in the resultset
		$frow = pg_fetch_row($result);
		if ($frow != false){
			$query = "update errors set jiranumber=(select e.jiranumber from errors e, errorgroup g where g.mainerror=e.id and g.id=".$value.") where errorGroup=".$errGroup.";";
			$result = pg_query($connection, $query) or die("Error in query: $query.
			" . pg_last_error($connection));
		
			
			$query = "update errors set errorGroup='".$value."' where id in (select e.id from errors e where e.errorGroup=".$errGroup.");";
			$result = pg_query($connection, $query) or die("Error in query: $query.
			" . pg_last_error($connection));
	
			
			$query = "update lastOccurrences set errorGroup=".$value." where errorGroup=".$errGroup.";";
			$result = pg_query($connection, $query) or die("Error in query (FK error is normal!): $query.
			" . pg_last_error($connection));
			
			$query = "delete from lastOccurrences where errorGroup=".$errGroup.";";
			$result = pg_query($connection, $query) or die("Error in query: $query.
			" . pg_last_error($connection));
	
			$query = "delete from errorGroup where id=".$errGroup.";";
			$result = pg_query($connection, $query) or die("Error in query: $query.
			" . pg_last_error($connection));
		}
	}
	
	// close database connection
	pg_close($connection);

?>
