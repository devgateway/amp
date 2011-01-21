CREATE OR REPLACE VIEW v_description AS 
  select 
     amp_activity.amp_activity_id AS amp_activity_id,
    dg_editor.BODY AS ebody 
    
  from 
    amp_activity, dg_editor 
  where 
    amp_activity.description  = dg_editor.EDITOR_KEY
  order by 
    amp_activity.amp_activity_id;