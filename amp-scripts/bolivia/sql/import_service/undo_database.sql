use amp_bolivia;
SET FOREIGN_KEY_CHECKS=0;
/*SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;*/

SET AUTOCOMMIT = 0;
START TRANSACTION;

/*truncate amp_activities_categoryvalues;*/
DELETE FROM ac
USING amp_activity as act, sisfin_db.`conv` AS con, amp_activities_categoryvalues as ac
where con.numconv=act.amp_id and ac.amp_activity_id=act.amp_activity_id;


/* truncate amp_activity_componente; */
DELETE FROM ac
USING amp_activity as act, sisfin_db.`conv` AS con, amp_activity_componente as ac
where con.numconv=act.amp_id and ac.amp_activity_id=act.amp_activity_id;

/*  delete mappings with sectors (which are colled components) and activities  
delete cs FROM amp_activity_compsector as cs
where EXISTS (SELECT * FROM amp_activity AS act, sisfin_db.`conv` AS con WHERE con.numconv=act.amp_id AND act.amp_activity_id=cs.amp_activity_id);
*/
truncate amp_activity_compsector;

/*truncate amp_activity_program;*/
DELETE FROM cs
USING amp_activity_program as cs,amp_activity AS act, sisfin_db.`conv` AS con
WHERE con.numconv=act.amp_id AND act.amp_activity_id=cs.amp_activity_id;


truncate amp_activity_location;
/*
delete actloc from amp_activity_location_persent as actloc
where exists (select * from amp_activity act, sisfin_db.`conv` c  where c.numconv=act.amp_id and act.amp_activity_id=actloc.amp_activity_id );
*/

 /*truncate amp_activity;*/
DELETE FROM act
USING AMP_ACTIVITY AS act, sisfin_db.`conv` c
where c.numconv=act.amp_id;


/*delete from amp_currency_rate 
where to_currency_code != 'USD';*/

delete from amp_currency
where currency_code in (select sigla_mda from sisfin_db.clasif_moneda);


delete from AMP_LEVEL
where level_code='N'  or  level_code='M'  or  level_code='D' ;

delete from amp_activity_sector
where amp_sector_id in (select s.amp_sector_id from AMP_SECTOR_SCHEME sc, AMP_SECTOR s where s.amp_sec_scheme_id=sc.amp_sec_scheme_id and sc.sec_scheme_code='BOL_IMP' );

/*===============*/

delete FROM amp_activity_closing_dates
where amp_activity_id not in (select a.amp_activity_id from amp_activity a);

delete FROM amp_org_role
where activity not in (select a.amp_activity_id from amp_activity a);

delete FROM amp_me_indicator_value
where activity_id not in (select a.amp_activity_id from amp_activity a);

delete from amp_activities_categoryvalues
where amp_activity_id not in (select a.amp_activity_id from amp_activity a);

delete from AMP_ISSUES
where amp_activity_id not in (select a.amp_activity_id from amp_activity a);

delete from amp_activity_sector
where amp_activity_id  not in (select a.amp_activity_id from amp_activity a);

delete from DG_EDITOR
where EDITOR_KEY  like '%aim-desc-import-%' or EDITOR_KEY like '%aim-obj-import-%' ;

delete from amp_terms_assist
where terms_assist_code in  (SELECT lvl.valdato FROM sisfin_db.`claves` lvl WHERE lvl.nomdato='cvecoop');

delete from AMP_FUNDING
where amp_activity_id not in (select a.amp_activity_id from amp_activity a);

delete from AMP_FUNDING_DETAIL
where AMP_FUNDING_ID not in ( select f.amp_funding_id FROM  AMP_FUNDING as f );

delete from amp_activity_theme; /* this table is not used anymore, so deleteing everithing here. */

delete from amp_ahsurvey
where (
amp_activity_id not in (select amp_activity_id from amp_activity)
); 

delete from amp_activity_internal_id
where amp_org_id not in (select aorg.amp_org_id from AMP_ORGANISATION as aorg); 


delete from amp_org_role
where organisation not in (select aorg.amp_org_id from AMP_ORGANISATION as aorg); 

/* just cleans this table from incorect records */
delete from amp_ahsurvey
where amp_org_id not in (select aorg.amp_org_id from AMP_ORGANISATION as aorg); 
/*  in case we import in surveys too then we will need to delete surveys that have imported rg ID's*/

delete from amp_pledges
where amp_org_id in 
(
select aorg.amp_org_id from AMP_ORGANISATION as aorg
where  aorg.org_code in (select a.codage from sisfin_db.age a)
);

delete from amp_organisation_sector
where amp_org_id in 
(
select aorg.amp_org_id from AMP_ORGANISATION as aorg
where  aorg.org_code in (select a.codage from sisfin_db.age a)
);

delete asi.* from
amp_sector_indicator as asi,
amp_sector as s,
amp_sector_scheme as sch
where sch.sec_scheme_code='BOL_IMP' AND s.amp_sec_scheme_id=sch.amp_sec_scheme_id
and asi.amp_sector_id = s.amp_sector_id;

/*
delete from AMP_SECTOR
where sector_code  in (select c.codsec from sisfin_db.sec c);
*/

delete s from AMP_SECTOR AS s
where exists (select * FROM AMP_SECTOR_SCHEME AS sch where sch.sec_scheme_code='BOL_IMP' AND s.amp_sec_scheme_id=sch.amp_sec_scheme_id);


delete s from AMP_SECTOR AS s
where exists (select * FROM AMP_SECTOR_SCHEME AS sch where sch.sec_scheme_code='BOL_COMPO_IMP' AND s.amp_sec_scheme_id=sch.amp_sec_scheme_id);  


delete from AMP_SECTOR_SCHEME
where sec_scheme_code='BOL_IMP';

delete from AMP_SECTOR_SCHEME
where sec_scheme_code='BOL_COMPO_IMP';


delete ignore from AMP_ORGANISATION
where  org_code in (select a.codage from sisfin_db.age a);

delete ignore from AMP_ORGANISATION
where  org_code in (select e.codent from sisfin_db.ent e);

delete loc from amp_location as loc, amp_region as ar,  sisfin_db.`claves` as c
where c.nomdato='cvedep'  and ar.country_id='bo' and ar.region_code=c.valdato and ar.region_code is not null and loc.region_id=ar.amp_region_id;

delete ar from  amp_region as ar,  sisfin_db.`claves` as c
where c.nomdato='cvedep'  and ar.country_id='bo' and ar.region_code=c.valdato and ar.region_code is not null;

delete actloc from amp_activity_location as actloc
where not exists (select * from amp_activity act where act.amp_activity_id=actloc.amp_activity_id );

DELETE catval FROM amp_category_value AS catval, amp_category_class AS catclass
where catval.amp_category_class_id=catclass.id  AND catclass.keyName ='financing_instrument'; 

COMMIT;


/* REMOVE TEMPORARY INDEXES */
DROP INDEX old_id ON amp_terms_assist;

DROP INDEX old_id ON  AMP_SECTOR;

DROP INDEX old_id ON AMP_ORGANISATION;

DROP INDEX old_id ON AMP_CATEGORY_VALUE;

DROP INDEX old_status_id ON AMP_ACTIVITY;
DROP INDEX old_id ON AMP_ACTIVITY;

DROP INDEX old_id ON AMP_LEVEL;

DROP INDEX region_code on amp_region;

/*removing temporary columnes  */
DROP TABLE CLASI_PND;

ALTER TABLE amp_terms_assist
DROP COLUMN old_id;

ALTER TABLE AMP_SECTOR
DROP COLUMN old_id;

ALTER TABLE AMP_ORGANISATION
DROP COLUMN old_id;

ALTER TABLE AMP_CATEGORY_VALUE
DROP COLUMN old_id;

ALTER TABLE AMP_ACTIVITY
DROP COLUMN old_status_id,
DROP COLUMN old_id,
DROP COLUMN classi_code;

ALTER TABLE AMP_LEVEL
DROP COLUMN old_id;


DELETE FROM  ac
USING amp_activity_components as ac, amp_components as c
where c.CodigoSISIN is not null and ac.amp_component_id = c.amp_component_id;


DELETE FROM  acf
USING amp_component_funding as acf, amp_components as c
where c.CodigoSisin is not null and acf.amp_component_id = c.amp_component_id;

DELETE FROM amp_components where CodigoSISIN is not null;

ALTER TABLE amp_components DROP INDEX `Index_CodigoSISIN`;
ALTER TABLE amp_components DROP COLUMN `CodigoSISIN`;


/* end */