use amp_burkina;



SET AUTOCOMMIT = 0;
START TRANSACTION;




 /*truncate amp_activity;*/
truncate amp_activity_location;
truncate amp_indicator_sectors;
truncate amp_activity_components;
truncate amp_activity_document;

truncate amp_activity_sector;

truncate amp_activity_internal_id;

truncate amp_activities_categoryvalues;
truncate amp_member_activities;
truncate amp_activity_km_documents;
truncate amp_activity_theme;
truncate amp_activity_program;
truncate amp_activity_componente;
truncate amp_activity_compsector;
truncate amp_activity_closing_dates;
truncate amp_me_indicator_value;
truncate amp_ahsurvey;
truncate AMP_ISSUES;
truncate ipa_contract_disbursement;
truncate ipa_contract;
truncate amp_indicator_project;
truncate amp_activity_referencedoc;


truncate  AMP_ACTIVITY  ;




delete from amp_activity_sector
where amp_sector_id in (select s.amp_sector_id from AMP_SECTOR_SCHEME sc, AMP_SECTOR s where s.amp_sec_scheme_id=sc.amp_sec_scheme_id and sc.sec_scheme_code='BURKINA_IMP' );

delete mtef from amp_funding_mtef_projection mtef
inner join AMP_FUNDING f on mtef.amp_funding_id=f.amp_funding_id;

delete from AMP_FUNDING 
where amp_activity_id not in (select a.amp_activity_id from amp_activity a);

delete from AMP_FUNDING_DETAIL 
where AMP_FUNDING_ID not in ( select f.amp_funding_id FROM  AMP_FUNDING as f );



delete s from AMP_SECTOR AS s
where exists (select * FROM AMP_SECTOR_SCHEME AS sch where sch.sec_scheme_code='BURKINA_IMP' AND s.amp_sec_scheme_id=sch.amp_sec_scheme_id);  




delete from AMP_SECTOR_SCHEME 
where sec_scheme_code='BURKINA_IMP';


delete from amp_org_role;


DELETE catval FROM amp_category_value AS catval
inner join amp_category_class cls on catval.amp_category_class_id=cls.id
where cls.keyName ='type_of_assistence'; 


COMMIT;
/*
ALTER TABLE AMP_ACTIVITY
DROP COLUMN old_id;
*/
/*ALTER TABLE AMP_ORGANISATION
DROP COLUMN old_id;
*/
