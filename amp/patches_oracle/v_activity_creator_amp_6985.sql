CREATE OR REPLACE VIEW
 v_activity_creator AS
  select a.amp_activity_id AS amp_activity_id,
         u.FIRST_NAMES || '  ' || u.LAST_NAME AS name,atm.user_ AS user_id 
         from ((amp_activity a join amp_team_member atm on (atm.amp_team_mem_id = a.activity_creator) ) join dg_user u on (atm.user_ = u.ID) ) 
         order by a.amp_activity_id;
         