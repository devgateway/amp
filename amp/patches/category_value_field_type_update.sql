update amp_category_value set field_type=0 where category_value not in ("country","region");
update amp_category_value set field_type=1 where category_value="country";
update amp_category_value set field_type=2 where category_value="region";