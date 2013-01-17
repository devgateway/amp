create sequence category_value_seq minvalue -1 start with -1;
UPDATE amp_category_value set index_column= category_value_seq.NEXTVAL where 
amp_category_class_id =(select id  from amp_category_class where keyName='type_of_assistence' );
drop sequence category_value_seq;