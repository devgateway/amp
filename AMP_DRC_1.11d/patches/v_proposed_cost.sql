delimiter $
DROP FUNCTION IF EXISTS getSQLDateFormat$
CREATE FUNCTION getSQLDateFormat() RETURNS varchar(12) 
begin 
declare gsFormat varchar(12);
select settingsValue into gsFormat from amp_global_settings where settingsName='Default Date Format';
if gsFormat='dd/MM/yyyy' then return '%d/%m/%Y';
end if;
if gsFormat='dd/MMM/yyyy' then return '%d/%b/%Y';
end if;
if gsFormat='MMM/dd/yyyy' then return '%b/%d/%Y';
end if;
if gsFormat='MM/dd/yyyy' then return '%m/%d/%Y';
end if;
return 'none';
END;$
update amp_activity set proj_cost_date= str_to_date(proj_cost_date,getSQLDateFormat()) where proj_cost_date is not null$
alter table amp_activity modify proj_cost_date datetime$
CREATE OR REPLACE VIEW v_proposed_cost AS 
SELECT amp_activity_id as amp_activity_id, amp_activity_id as object_id, proj_cost_amount as amount,
proj_cost_currcode as currency,proj_cost_date as currency_date FROM amp_activity where proj_cost_amount is not null$
insert into amp_columns(columnName, aliasName,cellType, extractorView) values("Proposed project cost", "proposed_cost","org.dgfoundation.amp.ar.cell.AmountCell", "v_proposed_cost")$