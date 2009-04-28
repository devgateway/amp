-- fix dg_message site_id type problem:
delete from dg_message where site_id is null or site_id='amp';
alter table dg_message alter column site_id set default 0;
alter table dg_message alter column site_id type integer USING pc_chartoint(site_id);
ALTER TABLE dg_message ADD CONSTRAINT dg_message_site_id_fk FOREIGN KEY (site_id) REFERENCES dg_site (id);
-- fix quartz problems
alter table qrtz_triggers alter column is_volatile type boolean using pc_chartobool(is_volatile);
alter table qrtz_fired_triggers alter column is_volatile type boolean using pc_chartobool(is_volatile);
alter table qrtz_fired_triggers alter column is_stateful type boolean using pc_chartobool(is_stateful);
alter table qrtz_fired_triggers alter column requests_recovery type boolean using pc_chartobool(requests_recovery);
alter table qrtz_job_details alter column is_volatile type boolean using pc_chartobool(is_volatile);
alter table qrtz_job_details alter column is_durable type boolean using pc_chartobool(is_durable);
alter table qrtz_job_details alter column is_stateful type boolean using pc_chartobool(is_stateful);
alter table qrtz_job_details alter column requests_recovery type boolean using pc_chartobool(requests_recovery);

-- fix reports order_id columns
alter table amp_report_measures alter column order_id type bigint using pc_chartoint(order_id);
alter table amp_report_column alter column order_id type bigint using pc_chartoint(order_id);
