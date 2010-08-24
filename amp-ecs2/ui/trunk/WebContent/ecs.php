<?php
	openlog('mylog', LOG_PID | LOG_ODELAY,LOG_LOCAL4);
	syslog(LOG_ERR, 'ECS - EP');

	include 'utilDbConnection.php';
		

	function getServerId($conn, $serverName){

		$query = "SELECT id FROM servers WHERE name LIKE '".$serverName."'";
		$result = pg_query($conn, $query) or syslog(LOG_ERR, "Error in query: $query. ". pg_last_error($conn));
		// get the number of rows in the resultset
		$frow = pg_fetch_row($result);
		$id = -1;
		if ($frow != false)
			$id = $frow[0];
				
		if ($id == -1){
			syslog(LOG_ERR, "Inserting server name:". $serverName);
			$query = "INSERT INTO servers(name) VALUES ('".$serverName."')";
			$result = pg_query($conn, $query) or syslog(LOG_ERR, "Error in query: $query. ". pg_last_error($conn));
						
			$query = "SELECT id FROM servers WHERE name LIKE '".$serverName."'";
			$result = pg_query($conn, $query) or syslog(LOG_ERR, "Error in query: $query. ". pg_last_error($conn));
			// get the number of rows in the resultset
			$frow = pg_fetch_row($result);
			if ($frow != false)
				$id = $frow[0];
		}
		if ($id == -1)
			syslog(LOG_ERR, "Could not insert server name: ". $serverName);
		return $id;
	}
	
	function getUserId($conn, $serverId, $login, $fullName, $password){

		$query = "SELECT id FROM users WHERE serverId=".$serverId." AND login LIKE '".$login."'";
		$result = pg_query($conn, $query) or syslog(LOG_ERR, "Error in query: $query. ". pg_last_error($conn));
		// get the number of rows in the resultset
		$frow = pg_fetch_row($result);
		$id = -1;
		if ($frow != false)
			$id = $frow[0];
				
		if ($id == -1){
			syslog(LOG_ERR, "Inserting user:". $login);
			$query = "INSERT INTO users(serverId, login, fullName, password) VALUES (".$serverId.", '".$login."', '".$fullName."','".$password."')";
			$result = pg_query($conn, $query) or syslog(LOG_ERR, "Error in query: $query. ". pg_last_error($conn));
						
			$query = "SELECT id FROM users WHERE serverId=".$serverId." AND login LIKE '".$login."'";
			$result = pg_query($conn, $query) or syslog(LOG_ERR, "Error in query: $query. ". pg_last_error($conn));
			// get the number of rows in the resultset
			$frow = pg_fetch_row($result);
			if ($frow != false)
				$id = $frow[0];
		}
		if ($id == -1)
			syslog(LOG_ERR, "Could not insert user: ". $login);
		return $id;
	}
	
	function getErrorId($conn, $stackTrace){
		$stackTrace = str_replace("'", "''", $stackTrace);
		$query = "SELECT id FROM errors WHERE md5 LIKE md5('".$stackTrace."')";
		$result = pg_query($conn, $query) or syslog(LOG_ERR, "Error in query: $query. ". pg_last_error($conn));
		// get the number of rows in the resultset
		$frow = pg_fetch_row($result);
		$id = -1;
		if ($frow != false)
			$id = $frow[0];
				
		if ($id == -1){
			syslog(LOG_ERR, "Inserting new error");
			$query = "SELECT nextval('errorGroup_id_seq');";
			$result = pg_query($conn, $query) or syslog(LOG_ERR, "Error in query: $query. ". pg_last_error($conn));
			// get the number of rows in the resultset
			$frow = pg_fetch_row($result);
			if ($frow != false)
				$groupId = $frow[0];
			
			$query = "INSERT INTO errorGroup(id) VALUES (".$groupId.")";
			$result = pg_query($conn, $query) or syslog(LOG_ERR, "Error in query: $query. ". pg_last_error($conn));
			
			$query = "INSERT INTO errors(md5, stackTrace, errorGroup) VALUES (md5('".$stackTrace."') , '".$stackTrace."', ".$groupId.")";
			$result = pg_query($conn, $query) or syslog(LOG_ERR, "Error in query: $query. ". pg_last_error($conn));
			
			$query = "SELECT id FROM errors WHERE md5 LIKE md5('".$stackTrace."')";
			$result = pg_query($conn, $query) or syslog(LOG_ERR, "Error in query: $query. ". pg_last_error($conn));
			// get the number of rows in the resultset
			$frow = pg_fetch_row($result);
			if ($frow != false)
				$id = $frow[0];
				
			$query = "UPDATE errorGroup SET mainError=".$id." WHERE id=".$groupId;
			$result = pg_query($conn, $query) or syslog(LOG_ERR, "Error in query: $query. ". pg_last_error($conn));
				
		}
		if ($id == -1)
			syslog(LOG_ERR, "Could not insert error!");
		return $id;
	}	
	
	function getLastOccurrenceDate($conn, $errorId, $serverId){
		$query = "SELECT lastOccurrence FROM lastOccurrences WHERE errorGroup=(select errorGroup from errors where id=".$errorId.") and server=".$serverId.";";
		//$query = "SELECT lastOccurrence FROM errors WHERE id=".$errorId;
		$result = pg_query($conn, $query) or syslog(LOG_ERR, "Error in query: $query. ". pg_last_error($conn));
		// get the number of rows in the resultset
		$frow = pg_fetch_row($result);
		$id = -1;
		$result = -1;
		if ($frow != false){
			$id = $frow[0];
			if ($id != null){
				$query = "SELECT sceneId FROM occurrences WHERE id=".$id;

				$result = pg_query($conn, $query) or syslog(LOG_ERR, "Error in query: $query. ". pg_last_error($conn));
				// get the number of rows in the resultset
				$frow = pg_fetch_row($result);
				$id = -1;
				if ($frow != false){
					$id = $frow[0];
					$query = "SELECT date FROM scenes WHERE id=".$id;
					$result = pg_query($conn, $query) or syslog(LOG_ERR, "Error in query: $query. ". pg_last_error($conn));
					// get the number of rows in the resultset
					$frow = pg_fetch_row($result);
					$id = -1;
					if ($frow != false){
						$result = $frow[0];
					}
				}
			}
		}
			
		return $result;
	}
	
	function updateLastOccurrence($conn, $errorId, $id){
		$query = "SELECT lastOccurrence FROM lastOccurrences WHERE errorGroup=(select errorGroup from errors where id=".$errorId.") and server=(select serverId from occurrences where id=".$id.");";
		$result = pg_query($conn, $query) or syslog(LOG_ERR, "Error in query: $query. ". pg_last_error($conn));
		$frow = pg_fetch_row($result);
		if ($frow == false){
			$query = "insert into lastOccurrences values ((select errorGroup from errors where id=".$errorId."), (select serverId from occurrences where id=".$id."), ".$id.");";
			$result = pg_query($conn, $query) or syslog(LOG_ERR, "Error in query: $query. ". pg_last_error($conn));	
		}
		else{
			$query = "update lastOccurrences SET lastOccurrence=".$id." where errorGroup=(select errorGroup from errors where id=".$errorId.") and server=(select serverId from occurrences where id=".$id.");";
			$result = pg_query($conn, $query) or syslog(LOG_ERR, "Error in query: $query. ". pg_last_error($conn));
		}
	}	
	
	function addScene($conn, $date, $browser, $sessionId){
		$browser = str_replace("'", "''", $browser);
		$id = -1;
		$query = "SELECT nextval('scenes_id_seq');";
		$result = pg_query($conn, $query) or syslog(LOG_ERR, "Error in query: $query. ". pg_last_error($conn));
		// get the number of rows in the resultset
		$frow = pg_fetch_row($result);
		if ($frow != false)
			$id = $frow[0];
		
		if ($id != -1){
			$query = "INSERT INTO scenes(id, date, browser, sessionId) VALUES (".$id.", '".$date."', '".$browser."', '".$sessionId."')";
			$result = pg_query($conn, $query) or syslog(LOG_ERR, "Error in query: $query. ". pg_last_error($conn));
		}	

		if ($id == -1)
			syslog(LOG_ERR, "Could not insert scene!");
		return $id;
		
	}
	
	function addOccurrence($conn, $serverId, $errorId, $userId, $sceneId){
		$id = -1;
		$query = "SELECT nextval('occurrences_id_seq');";
		$result = pg_query($conn, $query) or syslog(LOG_ERR, "Error in query: $query. ". pg_last_error($conn));
		// get the number of rows in the resultset
		$frow = pg_fetch_row($result);
		if ($frow != false)
			$id = $frow[0];
		
		if ($id != -1){
			$query = "INSERT INTO occurrences(id, serverId, errorId, userId, sceneId) VALUES (".$id." ,".$serverId.", ".$errorId.", ".$userId.", ".$sceneId.")";
			$result = pg_query($conn, $query) or syslog(LOG_ERR, "Error in query: $query. ". pg_last_error($conn));
		}	

		if ($id == -1)
			syslog(LOG_ERR, "Could not insert occurrence!");
		return $id;
		
	}

	function getParameters($conn, $serverName, $report){
		//
		// TODO
		// TODO: LOG IP;
		// TODO
		//
		syslog(LOG_ERR, "Server '".$serverName."' get parameters, report =".$report);
		$serverId = getServerId($conn, $serverName);
		
		$id = -1;
		$query = "SELECT updatedelay FROM servers WHERE id=".$serverId;
		$result = pg_query($conn, $query) or syslog(LOG_ERR, "Error in query: $query. ". pg_last_error($conn));
		// get the number of rows in the resultset
		$frow = pg_fetch_row($result);
		if ($frow != false)
			$id = $frow[0];
		else
			$id = 300000; //5mins
		
		$result = $serverName.", ".$id;
		
		if ($report == "SERVER_START_UP")
			syslog(LOG_ERR, "Server '".$serverName."' is starting up!");
		else
		if ($report == "SERVER_SHUTDOWN")
			syslog(LOG_ERR, "Server '".$serverName."' is shuting down!");
		
		return $result;
	}

	function sendError($conn, $serverName, $count, $stackTrace, $users, $userScenes){
		//
		// TODO
		// TODO: LOG IP;
		// TODO
		//	
		syslog(LOG_ERR, "Send Error (".$serverName.", ".$count.", ".$stackTrace."...)");
		$serverId = getServerId($conn, $serverName);
		$errorId = getErrorId($conn, $stackTrace);
		syslog(LOG_ERR, "ServerId ".$serverId." & errorId ".$errorId);
		
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
				$noid = addOccurrence($conn, $serverId, $errorId, $userId, $sceneId);
				
				if ($maxDate == null){
					$maxDate = $errorScene['date'];
					$maxNoid = $noid;
				}
				else{
					if (strtotime($maxDate) < strtotime($errorScene['date'])){
						$maxDate = $errorScene['date'];
						$maxNoid = $noid;
					}
				}
			}
		}
		syslog(LOG_ERR, " >> updating last occurences");
		$lastOccurrence = getLastOccurrenceDate($conn, $errorId, $serverId);
		if ($maxDate != null){
			if ($lastOccurrence == -1 || strtotime($lastOccurrence) < strtotime($maxDate))
				updateLastOccurrence($conn, $errorId, $maxNoid);
		}
		syslog(LOG_ERR, " >>> done with errors");
	}
		
	$handle = fopen('php://input','r');
	$jsonInput = fgets($handle);
	$jsonInput = utf8_encode($jsonInput); 
	$decoded = json_decode($jsonInput,true);
	
	
	/*
	syslog(LOG_ERR, "Server Name is:".$decoded['ServerName']);
	syslog(LOG_ERR, "Server Id: ". getServerId($connection, $decoded['ServerName']));
	syslog(LOG_ERR, "User Id: ". getUserId($connection, 6, "gigi@test.org", "Gigi R", "secretPass"));
	syslog(LOG_ERR, "Error Id: ". getErrorId($connection, "New werere error test PHP"));
	syslog(LOG_ERR, "Update Occurence: ". updateLastOccurrence($connection, 121, "2009-12-15 15:43:8"));
	//syslog(LOG_ERR, "Add Scene: ". addScene($connection, "2009-12-15 15:43:8", "Browser ARty", "Arty"));
	//syslog(LOG_ERR, "Add Occurrence: ". addOccurrence($connection, 6, 121, 9, 265));
	syslog(LOG_ERR, "get params: ". getParameters($connection, $decoded['ServerName'], "SERVER_START_UP"));
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
		else{
			syslog(LOG_ERR, " ***WRONG METHOD*** Length:".strlen($jsonInput));
			syslog(LOG_ERR, " ***WRONG METHOD*** Input:".$jsonInput);
			$len=1;
			while ($len < strlen($jsonInput)) {
				syslog(LOG_ERR, " ***WRONG METHOD*** Input:".substr($jsonInput, $len, 100));
				$len = $len + 100;
			}
	    	fclose($handle);
		}
	
	
	//var_dump($jsonInput);
	//var_dump($decoded);
	fclose($handle);
?>