CREATE OR REPLACE PROCEDURE fixCategoryValuesIndex 
(category_class_name IN varchar2)
IS
begin 
declare r number default 0;
no_more_rows number default 0;
class_id number;
cur_cat_id number; 
cur_class_id number;
CURSOR cur_cat IS SELECT id, amp_category_class_id FROM amp_category_value;
begin
select id into class_id from amp_category_class where category_name=category_class_name; 
OPEN cur_cat;
LOOP
FETCH cur_cat INTO cur_cat_id, cur_class_id;
if cur_class_id=class_id then update amp_category_value set index_column=r where id=cur_cat_id;
r:=r+1;
end if;
EXIT WHEN no_more_rows = 1;
END LOOP;
CLOSE cur_cat;
END;
end;