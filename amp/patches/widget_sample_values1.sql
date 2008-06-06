
INSERT INTO `amp_da_table` (table_name,table_code) VALUES  
 ('Country Budgets','sample_budgets');  

insert into `amp_da_column`(table_name,table_code,widget_id,order_no) 
values 
('Year','syear',(select t.ID from amp_da_table as t where t.table_code='sample_budgets'),0),
('Amount','samount',(select t.ID from amp_da_table as t where t.table_code='sample_budgets'),1),
('Signed','ssignature',(select t.ID from amp_da_table as t where t.table_code='sample_budgets'),2),
('Voted Yes','svotedyes',(select t.ID from amp_da_table as t where t.table_code='sample_budgets'),3),
('Voted No','svotedno',(select t.ID from amp_da_table as t where t.table_code='sample_budgets'),4);



INSERT INTO `amp_da_value` (row_pk,cell_value,column_id) VALUES  
 (2,'183',(select c.ID from amp_da_column as c where c.table_code like 'svotedyes')), 
 (1,'180',(select c.ID from amp_da_column as c where c.table_code like 'svotedyes')), 
 (2,'someone',(select c.ID from amp_da_column as c where c.table_code like 'ssignature')), 
 (1,'someone',(select c.ID from amp_da_column as c where c.table_code like 'ssignature')), 
 (2,'17',(select c.ID from amp_da_column as c where c.table_code like 'svotedno')), 
 (1,'20',(select c.ID from amp_da_column as c where c.table_code like 'svotedno')), 
 (2,'19 Milion',(select c.ID from amp_da_column as c where c.table_code like 'samount')), 
 (1,'18 Milion',(select c.ID from amp_da_column as c where c.table_code like 'samount')), 
 (2,'2000',(select c.ID from amp_da_column as c where c.table_code like 'syear')), 
 (1,'1999',(select c.ID from amp_da_column as c where c.table_code like 'syear')); 


