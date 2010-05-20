<?php
	openlog('mylog', LOG_PID | LOG_ODELAY,LOG_LOCAL4);
	syslog(LOG_INFO, 'ECS - EP');

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
		syslog(LOG_INFO, "Could not open connection to database server");
	}
	

	function getServerId($conn, $serverName){

		$query = "SELECT id FROM servers WHERE name LIKE '".$serverName."'";
		$result = pg_query($conn, $query) or syslog(LOG_INFO, "Error in query: $query. ". pg_last_error($conn));
		// get the number of rows in the resultset
		$frow = pg_fetch_row($result);
		$id = -1;
		if ($frow != false)
			$id = $frow[0];
				
		if ($id == -1){
			syslog(LOG_INFO, "Inserting server name:". $serverName);
			$query = "INSERT INTO servers(name) VALUES ('".$serverName."')";
			$result = pg_query($conn, $query) or syslog(LOG_INFO, "Error in query: $query. ". pg_last_error($conn));
						
			$query = "SELECT id FROM servers WHERE name LIKE '".$serverName."'";
			$result = pg_query($conn, $query) or syslog(LOG_INFO, "Error in query: $query. ". pg_last_error($conn));
			// get the number of rows in the resultset
			$frow = pg_fetch_row($result);
			if ($frow != false)
				$id = $frow[0];
		}
		if ($id == -1)
			syslog(LOG_INFO, "Could not insert server name: ". $serverName);
		return $id;
	}
	
	function getUserId($conn, $serverId, $login, $fullName, $password){

		$query = "SELECT id FROM users WHERE serverId=".$serverId." AND login LIKE '".$login."'";
		$result = pg_query($conn, $query) or syslog(LOG_INFO, "Error in query: $query. ". pg_last_error($conn));
		// get the number of rows in the resultset
		$frow = pg_fetch_row($result);
		$id = -1;
		if ($frow != false)
			$id = $frow[0];
				
		if ($id == -1){
			syslog(LOG_INFO, "Inserting user:". $login);
			$query = "INSERT INTO users(serverId, login, fullName, password) VALUES (".$serverId.", '".$login."', '".$fullName."','".$password."')";
			$result = pg_query($conn, $query) or syslog(LOG_INFO, "Error in query: $query. ". pg_last_error($conn));
						
			$query = "SELECT id FROM users WHERE serverId=".$serverId." AND login LIKE '".$login."'";
			$result = pg_query($conn, $query) or syslog(LOG_INFO, "Error in query: $query. ". pg_last_error($conn));
			// get the number of rows in the resultset
			$frow = pg_fetch_row($result);
			if ($frow != false)
				$id = $frow[0];
		}
		if ($id == -1)
			syslog(LOG_INFO, "Could not insert user: ". $login);
		return $id;
	}
	
	function getErrorId($conn, $stackTrace){
		$stackTrace = str_replace("'", "''", $stackTrace);
		$query = "SELECT id FROM errors WHERE md5 LIKE md5('".$stackTrace."')";
		$result = pg_query($conn, $query) or syslog(LOG_INFO, "Error in query: $query. ". pg_last_error($conn));
		// get the number of rows in the resultset
		$frow = pg_fetch_row($result);
		$id = -1;
		if ($frow != false)
			$id = $frow[0];
				
		if ($id == -1){
			syslog(LOG_INFO, "Inserting new error");
			$query = "INSERT INTO errors(md5, stackTrace) VALUES (md5('".$stackTrace."') , '".$stackTrace."')";
			$result = pg_query($conn, $query) or syslog(LOG_INFO, "Error in query: $query. ". pg_last_error($conn));
						
			$query = "SELECT id FROM errors WHERE md5 LIKE md5('".$stackTrace."')";
			$result = pg_query($conn, $query) or syslog(LOG_INFO, "Error in query: $query. ". pg_last_error($conn));
			// get the number of rows in the resultset
			$frow = pg_fetch_row($result);
			if ($frow != false)
				$id = $frow[0];
		}
		if ($id == -1)
			syslog(LOG_INFO, "Could not insert error!");
		return $id;
	}	
	function getLastOccurrence($conn, $errorId){

		$query = "SELECT lastOccurrence FROM errors WHERE id=".$errorId;
		$result = pg_query($conn, $query) or syslog(LOG_INFO, "Error in query: $query. ". pg_last_error($conn));
		// get the number of rows in the resultset
		$frow = pg_fetch_row($result);
		$id = -1;
		if ($frow != false)
			$id = $frow[0];
		
		return $id;
	}
	
	function updateLastOccurrence($conn, $errorId, $date){

		$query = "UPDATE errors SET lastOccurrence='".$date."' WHERE id=".$errorId;
		$result = pg_query($conn, $query) or syslog(LOG_INFO, "Error in query: $query. ". pg_last_error($conn));

	}	
	
	function addScene($conn, $date, $browser, $sessionId){
		$browser = str_replace("'", "''", $browser);
		$id = -1;
		$query = "SELECT nextval('scenes_id_seq');";
		$result = pg_query($conn, $query) or syslog(LOG_INFO, "Error in query: $query. ". pg_last_error($conn));
		// get the number of rows in the resultset
		$frow = pg_fetch_row($result);
		if ($frow != false)
			$id = $frow[0];
		
		if ($id != -1){
			$query = "INSERT INTO scenes(id, date, browser, sessionId) VALUES (".$id.", '".$date."', '".$browser."', '".$sessionId."')";
			$result = pg_query($conn, $query) or syslog(LOG_INFO, "Error in query: $query. ". pg_last_error($conn));
		}	

		if ($id == -1)
			syslog(LOG_INFO, "Could not insert scene!");
		return $id;
		
	}
	
	function addOccurrence($conn, $serverId, $errorId, $userId, $sceneId){
		$id = -1;
		$query = "SELECT nextval('occurrences_id_seq');";
		$result = pg_query($conn, $query) or syslog(LOG_INFO, "Error in query: $query. ". pg_last_error($conn));
		// get the number of rows in the resultset
		$frow = pg_fetch_row($result);
		if ($frow != false)
			$id = $frow[0];
		
		if ($id != -1){
			$query = "INSERT INTO occurrences(id, serverId, errorId, userId, sceneId) VALUES (".$id." ,".$serverId.", ".$errorId.", ".$userId.", ".$sceneId.")";
			$result = pg_query($conn, $query) or syslog(LOG_INFO, "Error in query: $query. ". pg_last_error($conn));
		}	

		if ($id == -1)
			syslog(LOG_INFO, "Could not insert occurrence!");
		return $id;
		
	}

	function getParameters($conn, $serverName, $report){
		//
		// TODO
		// TODO: LOG IP;
		// TODO
		//
		syslog(LOG_INFO, "Server '".$serverName."' get parameters, report =".$report);
		$serverId = getServerId($conn, $serverName);
		
		$id = -1;
		$query = "SELECT updatedelay FROM servers WHERE id=".$serverId;
		$result = pg_query($conn, $query) or syslog(LOG_INFO, "Error in query: $query. ". pg_last_error($conn));
		// get the number of rows in the resultset
		$frow = pg_fetch_row($result);
		if ($frow != false)
			$id = $frow[0];
		else
			$id = 300000; //5mins
		
		$result = $serverName.", ".$id;
		
		if ($report == "SERVER_START_UP")
			syslog(LOG_INFO, "Server '".$serverName."' is starting up!");
		else
		if ($report == "SERVER_SHUTDOWN")
			syslog(LOG_INFO, "Server '".$serverName."' is shuting down!");
		
		return $result;
	}

	function sendError($conn, $serverName, $count, $stackTrace, $users, $userScenes){
		//
		// TODO
		// TODO: LOG IP;
		// TODO
		//	
		syslog(LOG_INFO, "Send Error (".$serverName.", ".$count.", ".$stackTrace."...)");
		$serverId = getServerId($conn, $serverName);
		$errorId = getErrorId($conn, $stackTrace);
		syslog(LOG_INFO, "ServerId ".$serverId." & errorId ".$errorId);
		
		$madDate = null;
		
		for ($i = 0; $i < count($users); $i++){
			$errorUser = $users[$i];
			
			$userId = getUserId($conn, $serverId, $errorUser['login'], $errorUser['fullName'], $errorUser['password']);
			
			$scenesList = $userScenes[$i];
			if ($scenesList == null)
				continue;
			
			for ($j = 0; $j < count($scenesList); $j++){
				$errorScene = $scenesList[$j];
				
				$sceneId = addScene($conn, $errorScene['date'], $errorScene['browser'], $errorScene['sessionId']);
				addOccurrence($conn, $serverId, $errorId, $userId, $sceneId);
				
				if ($maxDate == null)
					$maxDate = $errorScene['date'];
				else{
					if (strtotime($maxDate) < strtotime($errorScene['date']))
						$maxDate = $errorScene['date'];
				}
			}
		}
		
		$lastOccurrence = getLastOccurrence($conn, $errorId);
		if ($maxDate != null){
			if ($lastOccurrence == -1 || strtotime($lastOccurrence) < strtotime($maxDate))
				updateLastOccurrence($conn, $errorId, $maxDate);
		}
	}
	
	
	$handle = fopen('php://input','r');
	$jsonInput = fgets($handle);
	$decoded = json_decode($jsonInput,true);
	
	/*
	syslog(LOG_INFO, "Server Name is:".$decoded['ServerName']);
	syslog(LOG_INFO, "Server Id: ". getServerId($connection, $decoded['ServerName']));
	syslog(LOG_INFO, "User Id: ". getUserId($connection, 6, "gigi@test.org", "Gigi R", "secretPass"));
	syslog(LOG_INFO, "Error Id: ". getErrorId($connection, "New werere error test PHP"));
	syslog(LOG_INFO, "Update Occurence: ". updateLastOccurrence($connection, 121, "2009-12-15 15:43:8"));
	//syslog(LOG_INFO, "Add Scene: ". addScene($connection, "2009-12-15 15:43:8", "Browser ARty", "Arty"));
	//syslog(LOG_INFO, "Add Occurrence: ". addOccurrence($connection, 6, 121, 9, 265));
	syslog(LOG_INFO, "get params: ". getParameters($connection, $decoded['ServerName'], "SERVER_START_UP"));
	*/
	$ecsMethod = $decoded['ecsMethod'];
	
	if ($ecsMethod == "sendError"){
		$eki = $decoded['eki'];
		sendError($connection, $decoded['ServerName'], $eki['count'], $eki['stackTrace'], $eki['users'], $eki['userScenes']);
		echo "success";		
	}
	else
	if ($ecsMethod == "getParameters"){
		$result = getParameters($connection, $decoded['ServerName'], $decoded['report']);
		echo "success\n";
		echo $result."\n";
	}
	
	
	//var_dump($jsonInput);
	//var_dump($decoded);
	fclose($handle);
?>