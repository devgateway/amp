delete from amp_fields_templates where field in (select id from amp_fields_visibility where name="Sectors Percentage");
delete from amp_fields_visibility where name="Sectors Percentage";
delete from amp_fields_templates where field in (select id from amp_fields_visibility where name="Sector");
delete from amp_fields_visibility where name="Sector";
delete from amp_fields_templates where field in (select id from amp_fields_visibility where name="Primary");
delete from amp_fields_visibility where name="Primary";
delete from amp_fields_templates where field in (select id from amp_fields_visibility where name="Secondary");
delete from amp_fields_visibility where name="Secondary";
delete from amp_fields_templates where field in (select id from amp_fields_visibility where name="Sub-Sector");
delete from amp_fields_visibility where name="Sub-Sector";
delete from amp_fields_templates where field in (select id from amp_fields_visibility where name="Sub-Sub-Sector");
delete from amp_fields_visibility where name="Sub-Sub-Sector";
update amp_columns set columnName="Primary Sector" where columnName="Sector";
update amp_columns set columnName="Primary Sub-Sector" where columnName="Sub-Sector";
update amp_columns set columnName="Primary Sub-Sub-Sector" where columnName="Sub-Sub-Sector";
CREATE or replace VIEW `v_secondary_sub_sub_sectors` AS select `sa`.`amp_activity_id` AS `amp_activity_id`,`s`.`name` AS `name`,`s`.`amp_sector_id` AS `amp_sector_id`,`sa`.`sector_percentage` AS `sector_percentage` from ((`amp_activity_sector` `sa` join `amp_sector` `s`) join `amp_sector_scheme` `ss` on((`sa`.`amp_sector_id` = `s`.`amp_sector_id`))) where ((`s`.`sector_code` > 10000) and `s`.`amp_sec_scheme_id` in (select `amp_classification_config`.`classification_id` AS `classification_id` from `amp_classification_config` where (`amp_classification_config`.`name` = 'Secondary')) and (`s`.`amp_sec_scheme_id` = `ss`.`amp_sec_scheme_id`)) order by `sa`.`amp_activity_id`,`s`.`name`; 
insert into amp_columns(columnName, cellType, extractorView, relatedContentPersisterClass) values("Secondary Sub-Sub Sector", "org.dgfoundation.amp.ar.cell.TextCell", "v_secondary_sub_sub_sectors", NULL);

