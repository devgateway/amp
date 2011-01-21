insert into amp_category_class (category_name, keyName, is_multiselect, is_ordered) values ('Phone Type', 'contact_phone_type', false, true);
insert into amp_category_value (amp_category_class_id, category_value, index_column) values ((select max(id) from amp_category_class), 'Home', 0);
insert into amp_category_value (amp_category_class_id, category_value, index_column) values ((select max(id) from amp_category_class), 'Cell', 1);
insert into amp_category_value (amp_category_class_id, category_value, index_column) values ((select max(id) from amp_category_class), 'Work', 2);
