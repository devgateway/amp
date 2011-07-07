SET @counter := -1;
UPDATE amp_category_value set index_column=(@counter:=@counter + 1 )where amp_category_class_id =(select id  from amp_category_class where keyName='type_of_assistence' );