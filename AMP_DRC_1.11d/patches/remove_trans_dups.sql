
DROP TABLE IF EXISTS `dg_message_dup`;
CREATE TABLE  `dg_message_dup` (
  `MESSAGE_KEY` varchar(255) character set utf8 collate utf8_bin NOT NULL default '',
  `LANG_ISO` char(2) character set utf8 collate utf8_bin NOT NULL default '',
  `SITE_ID` varchar(100) character set utf8 collate utf8_bin NOT NULL default '',
  `MESSAGE_UTF8` text character set utf8 collate utf8_bin,
  `CREATED` datetime default NULL,
  PRIMARY KEY  (`MESSAGE_KEY`,`LANG_ISO`,`SITE_ID`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8; 

DROP TABLE IF EXISTS `dg_message_temp`;
CREATE TABLE  `dg_message_temp` (
  `MESSAGE_KEY` varchar(255) character set utf8 collate utf8_bin NOT NULL default '',
  `LANG_ISO` char(2) character set utf8 collate utf8_bin NOT NULL default '',
  `SITE_ID` varchar(100) character set utf8 collate utf8_bin NOT NULL default '',
  `MESSAGE_UTF8` text character set utf8 collate utf8_bin,
  `CREATED` datetime default NULL,
  PRIMARY KEY  (`MESSAGE_KEY`,`LANG_ISO`,`SITE_ID`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into dg_message_temp 
select * from dg_message;

delete from dg_message_temp  
where trim(message_key)='';         

insert into dg_message_dup (message_key,lang_iso,site_id,message_utf8,created) 
SELECT lower(m.message_key) as mkey, lower(m.lang_iso) as miso, m.site_id msid, 'test',now() FROM dg_message_temp m 
group by lower(m.message_key), lower(m.lang_iso), m.site_id;         

update dg_message_dup mt, dg_message_temp m 
set mt.message_utf8=m.message_utf8, mt.created=m.created 
where mt.message_key=lower(m.message_key) and mt.lang_iso=lower(m.lang_iso) and mt.site_id=m.site_id;    

DROP TABLE IF EXISTS `dg_message`;
CREATE TABLE  `dg_message` (
  `MESSAGE_KEY` varchar(255) character set utf8 collate utf8_bin NOT NULL default '',
  `LANG_ISO` char(2) character set utf8 collate utf8_bin NOT NULL default '',
  `SITE_ID` varchar(100) character set utf8 collate utf8_bin NOT NULL default '',
  `MESSAGE_UTF8` text character set utf8 collate utf8_bin,
  `CREATED` datetime default NULL,
  PRIMARY KEY  (`MESSAGE_KEY`,`LANG_ISO`,`SITE_ID`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



insert into dg_message 
select * from dg_message_dup;

