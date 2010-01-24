DROP TEMPORARY TABLE IF EXISTS temp_id;

create TEMPORARY table temp_id select * from amp_activity_internal_id;

DROP TABLE  IF EXISTS amp_activity_internal_id;

CREATE TABLE amp_activity_internal_id (
  amp_activity_id bigint(20) NOT NULL default '0',
  amp_org_id bigint(20) NOT NULL default '0',
  internal_id varchar(255) default NULL,
  id bigint(20) NOT NULL auto_increment,
  PRIMARY KEY  (id),
  KEY FK57852C483524C250 (amp_activity_id),
  KEY FK75FAD06888CB673180DA2D81 (amp_org_id),
  CONSTRAINT FK75FAD0683524C250 FOREIGN KEY (amp_activity_id) REFERENCES amp_activity (amp_activity_id),
  CONSTRAINT FK75FAD06888CB6731 FOREIGN KEY (amp_org_id) REFERENCES amp_organisation (amp_org_id)
);

insert into amp_activity_internal_id (amp_activity_id,amp_org_id,internal_id) select amp_activity_id,amp_org_id,internal_id from temp_id;