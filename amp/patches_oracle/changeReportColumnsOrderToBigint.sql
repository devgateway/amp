alter table amp_report_column add (tmp_order_id number);
update amp_report_column set tmp_order_id=order_id;
alter table amp_report_column drop column order_id;
alter table amp_report_column add (order_id number);
update amp_report_column set order_id=tmp_order_id;
alter table amp_report_column drop column tmp_order_id;