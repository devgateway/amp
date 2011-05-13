CREATE OR REPLACE VIEW
 `v_activity_creator` AS
  select `a`.`amp_activity_id` AS `amp_activity_id`,
         concat(`u`.`FIRST_NAMES`, _latin1'  ',`u`.`LAST_NAME`) AS `name`,`atm`.`user_` AS `user_id` 
         from ((`amp_activity` `a` join `amp_team_member` `atm`) join `dg_user` `u`) 
         where ((`atm`.`amp_team_mem_id` = `a`.`activity_creator`) and (`atm`.`user_` = `u`.`ID`)) order by `a`.`amp_activity_id`;
