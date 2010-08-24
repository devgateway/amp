<?php
	include 'utilDbConnection.php';
	
	// generate and execute a query
		
	$id = $_GET['id'];
	$value = $_GET['value'];
	if ($id == null || trim($id) == ""){
		return;
	}
	$query = "update errors set jiranumber='".$value."' where errorGroup=".$id.";";
	$result = pg_query($connection, $query) or die("Error in query: $query.
	" . pg_last_error($connection));
	
	// close database connection
	pg_close($connection);

?>
