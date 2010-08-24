<?php
	$sortField = $_GET['sort'];
	$sortDir = $_GET['dir'];
	$pagStart = $_GET['startIndex'];
	$pagResults = $_GET['results'];
	if ($pagStart == null)
		$pagStart = 0;
	if ($pagResults == null)
		$pagResults = 10;
	
	//$mainquery = "select o.id, s.name, e.md5, u.login, u.fullname, u.password, x.date, x.browser, x.sessionid, e.jiranumber, e.stacktrace from occurrences o, servers s, errors e, users u, scenes x where o.serverid=s.id and o.errorid=e.id and o.userid=u.id and o.sceneid=x.id";
	//$countquery = "select count(*) from occurrences o, servers s, errors e, users u, scenes x where o.serverid=s.id and o.errorid=e.id and o.userid=u.id and o.sceneid=x.id";
	
	//$mainquery = "select g.id, s.name, e.md5, u.login, u.fullname, u.password, x.date, x.browser, x.sessionid, e.jiranumber, e.stacktrace, getCount(g.id) as count from errorGroup g, occurrences o, servers s, errors e, users u, scenes x where g.mainError=e.id and e.lastOccurrence=o.id and o.serverid=s.id and o.errorid=e.id and o.userid=u.id and o.sceneid=x.id";
	//$countquery = "select count(*) from errorGroup g, occurrences o, servers s, errors e, users u, scenes x where g.mainError=e.id and e.lastOccurrence=o.id and o.serverid=s.id and o.errorid=e.id and o.userid=u.id and o.sceneid=x.id";
	//select g.id as id, s.id as sid, count(e.md5) as count from errorGroup g, occurrences o, servers s, errors e, users u, scenes x where g.mainError=e.id and e.id=o.errorid and o.serverid=s.id and o.errorid=e.id and o.userid=u.id and o.sceneid=x.id group by g.id, s.id

	$mainquery1 = "select ".$outputFields." from 
	(select mm.id, mm.sid, count(mm.eid) as count from (select g.id as id, s.id as sid, e.id as eid from errorGroup g, occurrences o, servers s, errors e, users u, scenes x 
	where e.errorGroup=g.id and e.id=o.errorid and o.serverid=s.id and o.errorid=e.id and o.userid=u.id and o.sceneid=x.id";
	
	
	$mainquery2 =") mm group by mm.id, mm.sid) as my, servers s, lastOccurrences lo, occurrences o, errors e, users u, scenes x where s.id = my.sid and lo.errorGroup=my.id and lo.server=my.sid and lo.lastOccurrence=o.id and o.errorId=e.id and o.userId=u.id and o.sceneId=x.id ";
	
	$countquery1 = "select count (*) from (select mm.id, mm.sid, count(mm.eid) as count from (select g.id as id, s.id as sid, e.id as eid from errorGroup g, occurrences o, servers s, errors e, users u, scenes x 
	where e.errorGroup=g.id and e.id=o.errorid and o.serverid=s.id and o.errorid=e.id and o.userid=u.id and o.sceneid=x.id";
	$countquery2 = ") mm group by mm.id, mm.sid) as my;";
	
	
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
	
	$filter = $_GET['fCustom']; 
	if ($filter != null){
		$query = $query." and e.stackTrace like '".$filter."%'";
	}
	
	
	$countquery = $countquery1.$query.$countquery2;
	
	
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

	
	$mainquery = $mainquery1.$query.$mainquery2." order by ".$tableSort." ".$sortDir." OFFSET ".$pagStart." LIMIT ".$pagResults." ";
	//$query = $query." order by ".$tableSort." ".$sortDir." OFFSET ".$pagStart." LIMIT ".$pagResults." ";
?>