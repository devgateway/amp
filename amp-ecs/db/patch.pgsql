CREATE TABLE lastOccurrences (
    errorGroup		int				REFERENCES errorGroup(id),
    server			int				REFERENCES servers(id),
    lastOccurrence	int				REFERENCES occurrences(id)
);
ALTER TABLE lastOccurrences ADD PRIMARY KEY (errorGroup, server);

insert into lastOccurrences  select my.id, my.sid, err.lastOccurrence from (select g.id, s.id as sid, count(e.md5) 
from errorGroup g, occurrences o, servers s, errors e, users u, scenes x 
where g.mainError=e.id and e.id=o.errorid and o.serverid=s.id and o.errorid=e.id and o.userid=u.id and o.sceneid=x.id group by g.id, s.id) as my, errorGroup gr, errors err where gr.id=my.id and err.id=gr.mainError;

ALTER TABLE errors DROP COLUMN lastOccurrence;

