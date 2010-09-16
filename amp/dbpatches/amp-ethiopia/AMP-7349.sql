update amp_funding set financing_instr_category_value=(select min(id) from amp_category_value where category_value=(select c.category_value from amp_category_value c where c.id=financing_instr_category_value ));
update amp_eu_activity_contributions set financing_instr_category_value=(select min(id) from amp_category_value where category_value=(select c.category_value from amp_category_value c where c.id=financing_instr_category_value ));
delete from amp_category_value where id>80 and amp_category_class_id=9;
call fixCategoryValuesIndex('Financing Instrument');