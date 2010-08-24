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

?>