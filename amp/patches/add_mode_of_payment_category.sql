insert into amp_category_class (category_name,description,is_multiselect,is_ordered,keyName)
select 'Mode of Payment',"",False,False,'mode_of_payment' from DUAL where
(select count(id) from amp_category_class where keyName='mode_of_payment') < 1;

insert into amp_category_value (category_value,amp_category_class_id,index_column)
 select  'Direct Payment', c.id , 0 from  amp_category_class c
   where c.keyName="mode_of_payment"  and (select count(*) from amp_category_value where category_value='Direct Payment') <1;

insert into amp_category_value (category_value,amp_category_class_id,index_column)
 select  'Direct Funding', c.id , 1 from  amp_category_class c
   where c.keyName="mode_of_payment"  and (select count(*) from amp_category_value where category_value='Direct Funding') <1;

insert into amp_category_value (category_value,amp_category_class_id,index_column)
 select  'Reimbursable', c.id , 2 from  amp_category_class c
   where c.keyName="mode_of_payment"  and (select count(*) from amp_category_value where category_value='Reimbursable') <1;