CREATE OR REPLACE VIEW v_physical_progress AS
select p.amp_activity_id, p.description, p.amp_pp_id FROM
amp_physical_performance p order by p.reporting_date;
