INSERT INTO amp_component_type (type_id,code,name,enable)
values (1,"DEF","AMP Default Type",1);

update amp_components set type=1;


alter table amp_components
 CHANGE  COLUMN type type BIGINT;
 