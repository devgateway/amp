Insert into amp_category_class(category_name,keyName,is_multiselect,is_ordered) 
select 'Contact Title','contact_title',false,false from dual where 
(select count(id) from amp_category_class where category_name='Contact Title' and keyName='contact_title')=0;