-- fix dg_message site_id type problem:
delete from dg_message where site_id is null or site_id='amp';
alter table dg_message alter column site_id set default 0;
-- fix quartz problems
alter table qrtz_triggers alter column is_volatile type boolean using pc_chartobool(is_volatile);
alter table qrtz_fired_triggers alter column is_volatile type boolean using pc_chartobool(is_volatile);
alter table qrtz_fired_triggers alter column is_stateful type boolean using pc_chartobool(is_stateful);
alter table qrtz_fired_triggers alter column requests_recovery type boolean using pc_chartobool(requests_recovery);
alter table qrtz_job_details alter column is_volatile type boolean using pc_chartobool(is_volatile);
alter table qrtz_job_details alter column is_durable type boolean using pc_chartobool(is_durable);
alter table qrtz_job_details alter column is_stateful type boolean using pc_chartobool(is_stateful);
alter table qrtz_job_details alter column requests_recovery type boolean using pc_chartobool(requests_recovery);
--other stuff
alter table perm_map alter column object_identifier type integer using pc_chartoint(object_identifier);