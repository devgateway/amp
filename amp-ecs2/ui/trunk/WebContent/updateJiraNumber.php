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
	$query = "update errors set jiranumber='".$value."' where id=(select o.errorid from occurrences o where o.id=".$id.");";
	$result = pg_query($connection, $query) or die("Error in query: $query.
	" . pg_last_error($connection));
	
	// close database connection
	pg_close($connection);

?>
