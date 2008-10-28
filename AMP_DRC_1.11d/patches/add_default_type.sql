UPDATE amp_components SET type=null;
REPLACE INTO amp_component_type (type_id,code,name,enable) values (1,"DEF","AMP Default Type",1);
UPDATE amp_components SET type=1;
ALTER table amp_components CHANGE COLUMN type type BIGINT; 