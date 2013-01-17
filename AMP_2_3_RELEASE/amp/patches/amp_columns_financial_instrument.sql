CREATE OR REPLACE VIEW v_financial_instrument AS  
 select aac.amp_activity_id, acv.category_value ,acv.id from amp_category_value acv, amp_category_class acc, amp_activities_categoryvalues aac where acv.amp_category_class_id=acc.id 
	and acc.keyName="financial_instrument" and aac.amp_categoryvalue_id=acv.id;

insert into amp_columns
   	(columnName,cellType,extractorView)
       values ('Financial Instrument', 'org.dgfoundation.amp.ar.cell.TrnTextCell','v_financial_instrument');
