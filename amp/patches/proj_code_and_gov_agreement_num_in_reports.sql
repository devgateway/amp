/*project code and government agreement number columns*/
insert into amp_columns (columnName, cellType, extractorView) 
(select 'Project Code','org.dgfoundation.amp.ar.cell.TextCell','v_project_code' 
from dual where (select count(columnId) from amp_columns where extractorView="v_project_code")=0);

insert into amp_columns (columnName, cellType, extractorView) 
(select 'Government Agreement Number','org.dgfoundation.amp.ar.cell.TextCell','v_gov_agreement_number' 
from dual where (select count(columnId) from amp_columns where extractorView="v_gov_agreement_number")=0);

/* create/replace views */
create or replace view `v_project_code` AS 
(select a.amp_activity_id, a.project_code from amp_activity a  where trim(a.project_code)!="");

create or replace view `v_gov_agreement_number` AS 
(select a.amp_activity_id, a.gov_agreement_number from amp_activity a  where trim(a.gov_agreement_number)!="");