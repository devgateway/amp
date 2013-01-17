create or replace view v_capital_and_exp as 
	select amp_activity_id, "Capital" from amp_activity UNION select amp_activity_id, "Expenditure" from amp_activity order by amp_activity_id;
	
insert into amp_columns(columnName, aliasName, cellType, extractorView) 
 values("Capital - Expenditure", "capital_expenditure", "org.dgfoundation.amp.ar.cell.TextCell", "v_capital_and_exp");
 