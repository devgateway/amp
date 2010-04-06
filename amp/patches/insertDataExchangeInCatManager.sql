insert into amp_category_class(category_name,keyName,is_multiselect,is_ordered) 
select 'Data Echange Category','data_exchange',false,false from dual where 
(select count(id) from amp_category_class cat where cat.keyName='data_exchange')=0;