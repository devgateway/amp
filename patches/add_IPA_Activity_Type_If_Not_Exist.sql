insert into amp_category_class (category_name,description,is_multiselect,is_ordered,keyName)
select 'IPA Activity Type',"",False,False,'ipa_activity_type' from DUAL where
(select count(id) from amp_category_class where keyName='ipa_activity_type') < 1;

insert into amp_category_value (category_value,amp_category_class_id,index_column,	field_type)
 select  'Activity 1', x.id , 0 , 0   from  amp_category_class x
   where x.keyName="ipa_activity_type"  and (select count(*) from amp_category_value where category_value='Activity 1') <1;

