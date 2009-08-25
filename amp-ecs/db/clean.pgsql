CREATE TABLE servers (
    id				serial		 	PRIMARY KEY,
    name			varchar(80) 	UNIQUE NOT NULL,
    updateDelay		int 			DEFAULT 300000
);

CREATE TABLE errors (
    id				serial		 	PRIMARY KEY,
    stackTrace		text			UNIQUE,
    jiraNumber		varchar(50) 	,
    resendAlert		bool 			DEFAULT false,
    lastOccurrence	timestamp			
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
    date			timestamp			,
    browser			text			
);

CREATE TABLE occurrences (
    id				serial		 	PRIMARY KEY,
    serverId		int				REFERENCES servers(id), 
    errorId			int				REFERENCES errors(id),
    userId			int				REFERENCES users(id),
    sceneId			int				REFERENCES scenes(id)
);



