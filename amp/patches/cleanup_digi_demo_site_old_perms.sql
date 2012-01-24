delete from dg_module_instance where site_id=(select id from dg_site where name='Demo Site');
delete from dg_group where site_id=(select id from dg_site where name='Demo Site');
delete from dg_site_domain where site_id=(select id from dg_site where name='Demo Site');
delete from dg_site where name='Demo Site';