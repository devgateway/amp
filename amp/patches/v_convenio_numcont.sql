 CREATE OR REPLACE VIEW `v_convenio_numcont` AS
	select `amp_activity_id` AS `amp_activity_id`,
	CASE `convenio_numcont`
	WHEN '-' THEN NULL
	WHEN '.' THEN NULL
	ELSE `convenio_numcont`
	END  AS `numcont`
	from `amp_activity`
	order by `amp_activity`.`amp_activity_id`;


insert into `amp_columns` (columnName, cellType, extractorView)
values ('Contract Number', 'org.dgfoundation.amp.ar.cell.TextCell', 'v_convenio_numcont');

