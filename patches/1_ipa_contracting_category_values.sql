update amp_category_class set is_multiselect=b'0' where category_name like "IPA Type";

INSERT INTO amp_category_class(category_name,is_multiselect,is_ordered, keyName, description)
    VALUES('IPA Activity Type', b'0', b'0', 'ipa_activity_type', 'IPA Activity Type(edit activity step 13)');
    
set @newid := LAST_INSERT_ID();

INSERT INTO amp_category_value(category_value,amp_category_class_id, index_column, field_type)
values ('Activity 1', @newid, 0, 0), ('Activity 2', @newid, 1, 0), ('Activity 3', @newid, 2, 0);

select id into @newid from amp_category_class where keyName="ipa_act_cat";

INSERT INTO amp_category_value(category_value,amp_category_class_id, index_column, field_type)
values ('Category 1', @newid, 0, 0), ('Category 2', @newid, 1, 0), ('Category 3', @newid, 2, 0);

update amp_category_class set is_multiselect=b'0' where keyName="ipa_act_cat";

select id into @newid from amp_category_class where keyName="ipa_act_stat";

INSERT INTO amp_category_value(category_value,amp_category_class_id, index_column, field_type)
values ('Status 1', @newid, 0, 0), ('Status 2', @newid, 1, 0), ('Status 3', @newid, 2, 0);

update amp_category_class set is_multiselect=b'0' where keyName="ipa_act_stat";

