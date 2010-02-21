delimiter $
CREATE PROCEDURE fixCategoryValuesIndex(category_class_name varchar(255))
begin declare r int default 0;
declare no_more_rows int default 0;
declare class_id double;
declare cur_cat_id double; 
declare cur_class_id double;
DECLARE cur_cat CURSOR FOR SELECT id, amp_category_class_id FROM amp_category_value;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET no_more_rows = 1;
select id into class_id from amp_category_class where category_name=category_class_name; 
OPEN cur_cat;
REPEAT FETCH cur_cat INTO cur_cat_id,cur_class_id;
if cur_class_id=class_id then update amp_category_value set index_column=r where id=cur_cat_id;
set r=r+1;
end if;
UNTIL no_more_rows = 1 END REPEAT;
CLOSE cur_cat;
END;$
