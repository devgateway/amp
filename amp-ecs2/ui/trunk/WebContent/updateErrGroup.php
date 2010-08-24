<?php
	include 'utilDbConnection.php';
	include 'utilErrorGroup.php';
	
	
	// generate and execute a query
		
	$id = $_GET['id'];
	$value = $_GET['value'];

	updateErrorGroup($id, $value, $connection);
	
	
	// close database connection
	pg_close($connection);

?>
