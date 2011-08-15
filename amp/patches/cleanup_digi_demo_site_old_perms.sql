delete from dg_module_instance where site_id=(select id from dg_site where name='Demo Site');
delete from dg_group where site_id=(select id from dg_site where name='Demo Site');
delete from dg_site_domain where site_id=(select id from dg_site where name='Demo Site');
delete from dg_site where name='Demo Site';
delete from dg_principal_permission_param;
delete from dg_principal_permission;