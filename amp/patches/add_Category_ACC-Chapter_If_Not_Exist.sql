insert into amp_category_class (category_name,description,is_multiselect,is_ordered,keyName)
select 'A.C. Chapter',"",False,False,'acchapter' from DUAL where
(select count(id) from amp_category_class where keyName='acchapter') < 1;

insert into amp_category_value (category_value,amp_category_class_id,index_column,	field_type)
 select  'Budget', x.id , 0 , 0   from  amp_category_class x
   where x.keyName="acchapter"  and (select count(*) from amp_category_value where category_value='Budget') <1;

insert into amp_category_value (category_value,amp_category_class_id,index_column,	field_type)
 select  'Dedicated', x.id , 0 , 0   from  amp_category_class x
   where x.keyName="acchapter"  and (select count(*) from amp_category_value where category_value='Dedicated') <1;


insert into amp_category_value (category_value,amp_category_class_id,index_column,	field_type)
 select  'Project', x.id , 0 , 0   from  amp_category_class x
   where x.keyName="acchapter"  and (select count(*) from amp_category_value where category_value='Project') <1;

insert into amp_category_value (category_value,amp_category_class_id,index_column,	field_type)
 select  'Direct Project Funds (DPF)', x.id , 0 , 0   from  amp_category_class x
   where x.keyName="acchapter"  and (select count(*) from amp_category_value where category_value='Direct Project Funds (DPF)') <1;