Insert into amp_category_class(category_name,keyName,is_multiselect,is_ordered) values ('Contact Title','contact_title',false,false);
Insert into amp_category_value(category_value,amp_category_class_id,index_column,field_type)(select "Mr.",id,0,0 from amp_category_class where keyName="contact_title");
Insert into amp_category_value(category_value,amp_category_class_id,index_column,field_type)(select "Mrs.",id,1,0 from amp_category_class where keyName="contact_title");
Insert into amp_category_value(category_value,amp_category_class_id,index_column,field_type)(select "Miss.",id,2,0 from amp_category_class where keyName="contact_title");
Insert into amp_category_value(category_value,amp_category_class_id,index_column,field_type)(select "Dr.",id,3,0 from amp_category_class where keyName="contact_title");
Insert into amp_category_value(category_value,amp_category_class_id,index_column,field_type)(select "Pr",id,4,0 from amp_category_class where keyName="contact_title");