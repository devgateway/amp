CREATE TABLE servers (
    id				serial		 	PRIMARY KEY,
    name			varchar(80) 	UNIQUE NOT NULL,
    updateDelay		int 			DEFAULT 300000
);


CREATE TABLE errorGroup (
    id				serial		 	PRIMARY KEY,
    mainError		int								
);

CREATE TABLE errors (
    id				serial		 	PRIMARY KEY,
    md5				text			UNIQUE,
    stackTrace		text			,
    jiraNumber		varchar(50) 	,
    resendAlert		bool 			DEFAULT false,
    lastOccurrence	int				DEFAULT null, 
    errorGroup		int				REFERENCES errorGroup(id) 		
);

CREATE TABLE users (
    id				serial		 	PRIMARY KEY,
    serverId		int				REFERENCES servers(id),
    login			varchar(80)		NOT NULL,
    fullName		varchar(120)	,
    password		varchar(80)		,
    UNIQUE (serverId, login)
);

CREATE TABLE scenes (
    id				serial		 	PRIMARY KEY,
    date			timestamp		,
    browser			text			,
    sessionId		text
);

CREATE TABLE occurrences (
    id				serial		 	PRIMARY KEY,
    serverId		int				REFERENCES servers(id), 
    errorId			int				REFERENCES errors(id),
    userId			int				REFERENCES users(id),
    sceneId			int				REFERENCES scenes(id)
);

CREATE TABLE lastOccurrences (
    errorGroup		int				REFERENCES errorGroup(id),
    server			int				REFERENCES servers(id),
    lastOccurrence	int				REFERENCES occurrences(id)
);
ALTER TABLE lastOccurrences ADD PRIMARY KEY (errorGroup, server);

select setval('scenes_id_seq', 1);
select setval('occurrences_id_seq', 1);
select setval('errorGroup_id_seq', 1);
select setval('users_id_seq', 1);
select setval('errors_id_seq', 1);
select setval('servers_id_seq', 1);


create language plpgsql;

CREATE OR REPLACE FUNCTION getCount(groupId integer) RETURNS integer AS '
  DECLARE
    c integer;
  BEGIN
    SELECT INTO c count(*) from errors e, occurrences o where e.errorGroup=groupId and o.errorId=e.id;
    IF NOT FOUND THEN
      return 0;
    END IF;
    return c;
  END;' LANGUAGE plpgsql;



