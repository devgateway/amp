delete from (select * from amp_category_value v, amp_category_class c where v.amp_category_class_id=c.id 
	and c.keyName='funding_status');

insert into amp_category_class (category_name,description,is_multiselect,is_ordered,keyName)
select 'Funding Status','',0,0,'funding_status' from DUAL where
(select count(id) from amp_category_class where keyName='funding_status') < 1;

insert into amp_category_value (category_value,amp_category_class_id,index_column)
 select  'No information', c.id , 0 from  amp_category_class c
   where c.keyName='funding_status'  and (select count(*) from amp_category_value where category_value='No information') <1;

insert into amp_category_value (category_value,amp_category_class_id,index_column)
 select  'No request', c.id , 1 from  amp_category_class c
   where c.keyName='funding_status'  and (select count(*) from amp_category_value where category_value='No request') <1;


insert into amp_category_value (category_value,amp_category_class_id,index_column)
 select  'Request sent, no answer', c.id , 2 from  amp_category_class c
   where c.keyName='funding_status'  and (select count(*) from amp_category_value where category_value='Request sent, no answer') <1;

insert into amp_category_value (category_value,amp_category_class_id,index_column)
 select  'Negotiating', c.id , 3 from  amp_category_class c
   where c.keyName='funding_status'  and (select count(*) from amp_category_value where category_value='Negotiating') <1;
   
   
insert into amp_category_value (category_value,amp_category_class_id,index_column)
 select  'Partially acquired', c.id , 4 from  amp_category_class c
   where c.keyName='funding_status'  and (select count(*) from amp_category_value where category_value='Partially acquired') <1;
   
insert into amp_category_value (category_value,amp_category_class_id,index_column)
 select  'Totally acquired', c.id , 5 from  amp_category_class c
   where c.keyName='funding_status'  and (select count(*) from amp_category_value where category_value='Totally acquired') <1;