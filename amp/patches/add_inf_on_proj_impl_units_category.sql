insert into amp_category_class (category_name, keyName, is_multiselect, is_ordered) values ('Project Implementing Unit ', 'project_impl_unit', false, true);
insert into amp_category_value (amp_category_class_id, category_value, index_column) values ((select id from amp_category_class where keyName='project_impl_unit'), 'Parallel Project Implementing', 0);
insert into amp_category_value (amp_category_class_id, category_value, index_column) values ((select id from amp_category_class where keyName='project_impl_unit'), 'Embedded Project Implementing', 1);
insert into amp_category_value (amp_category_class_id, category_value, index_column) values ((select id from amp_category_class where keyName='project_impl_unit'), 'No Project Implementing Unit', 2);
