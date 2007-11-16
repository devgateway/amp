DELIMITER $$

DROP FUNCTION IF EXISTS `getYesNo`$$
CREATE FUNCTION `getYesNo`(val bit(1)) RETURNS char(3) CHARSET latin1
begin
if val = 0 then 
   return 'no';
else
   return 'yes';
end if;
END$$

DELIMITER ;


DROP VIEW IF EXISTS `v_drafts`;
CREATE VIEW `v_drafts` AS select `amp_activity`.`amp_activity_id` AS `amp_activity_id`,`amp_local`.`getYesNo`(`amp_activity`.`draft`) AS `draft` from `amp_activity` order by `amp_activity`.`amp_activity_id`
insert into amp_columns(ColumnName, cellType, extractorView) values ("Draft", "org.dgfoundation.amp.ar.cell.TextCell", "v_drafts");
