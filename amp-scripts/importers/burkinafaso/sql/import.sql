use amp_burkina;

/* set up variables */
SET @import_time:=unix_timestamp();
SET @approved='approved';
SET @team_id=1; /* this is VIPFE Team */
SET @activity_creator=37;  /* this is ATL member */
SET @activity_code='AMP-BurkinaFaso';
SET @commitment=0;
SET @disbursment=1;
SET @funding_adjusment_planned=0;
SET @funding_adjusment_actual=1;
SET @org_role_code='MA';
SET @funding_perspective=2;
SET @funding_currency_id=21;
SET @max_order_no=0;
SET @max_level_order_no=0;
SET @funding_modality=3;
SET @financing_instrument=116; /*  This should be removed becase now we have correct query */
SET @status_class_id=6;
select @status_class_id:=c.id from amp_category_class c where c.keyName='activity_status'; 
SET @level_class_id=7;
select @level_class_id:=c.id from amp_category_class c where c.keyName='implementation_level'; 
select @max_order_no:=max(a.index_column) FROM AMP_CATEGORY_VALUE a WHERE a.amp_category_class_id=@status_class_id;
select @max_level_order_no:=max(a.index_column) FROM AMP_CATEGORY_VALUE a WHERE a.amp_category_class_id=@level_class_id;
SET @timestmp:=unix_timestamp();
SET @avtivity_desc='aim-desc-import-';
SET @avtivity_obj='aim-obj-import-';
SET @editor_order=0; /*  This is needed for KALOSHA's import tool, just should not be empty cos it is primitive int */
SET @amp_site_id = 'amp';
SET @subprog_prefix='EBRP';
select @import_time;

ALTER TABLE AMP_ACTIVITY
ADD COLUMN sector_code bigint(20),
ADD INDEX (sector_code);

ALTER TABLE AMP_CATEGORY_VALUE
ADD COLUMN CODE_FINANCEMENT varchar(30),
ADD INDEX (CODE_FINANCEMENT);





SET AUTOCOMMIT = 0;
START TRANSACTION;

/*importing sectors */

select 'importing sector shceme'; 

insert into AMP_SECTOR_SCHEME (sec_scheme_code, sec_scheme_name)
Values ('BURKINA_IMP', 'Classification budg');

update AMP_GLOBAL_SETTINGS set settingsValue =  LAST_INSERT_ID()
where settingsName = 'Default Sector Scheme';

select 'importing sectors'; 

insert into AMP_SECTOR (amp_sec_scheme_id,  sector_code,   name)
select sch.amp_sec_scheme_id,   c.CODE_SECTEUR,   c.LIBELLE_SECTEUR    
from burkina_db.secteur c, AMP_SECTOR_SCHEME sch 
where sch.sec_scheme_code='BURKINA_IMP' ;


/* import organizations and groups */


select 'importing organisation groups'; 

INSERT INTO amp_org_group
(
org_grp_name,
org_grp_code,
org_type
)
SELECT
gr.`Organization Group`,
gr.`Org group Codes`,
(select amp_org_type_id from amp_org_type where org_type_code=UPPER(substring(gr.`Organization Type`,1,3)))
FROM burkina_db.donors as gr
where not exists (select * from amp_org_group g where g.org_grp_code=gr.`Org group Codes`)
group by gr.`Org group Codes`;

select 'importing donor organisations'; 

INSERT INTO AMP_ORGANISATION
(
org_code,
acronym,
name,
org_type_id,
org_grp_id
)
SELECT
o.LIBELLE_BAILLEUR,
o.LIBELLE_BAILLEUR,
o.`Organization Name`,
(select tp.amp_org_type_id from amp_burkina.amp_org_type tp where UPPER(org_type_code)=UPPER(substring(o.`Organization Type`,1,3))) role,
(select gr.amp_org_grp_id from amp_burkina.amp_org_group gr where org_grp_code=o.`Org group Codes`) gr

FROM burkina_db.donors as o
where not exists (select * from amp_organisation  org where org.org_code=o.LIBELLE_BAILLEUR );

select 'importing Implementing agency'; 

INSERT INTO AMP_ORGANISATION
(
name,
org_code,
acronym,
org_type_id
)
SELECT
o.LIBELLE_SECTION,
o.LIBELLE_SECTION,
o.LIBELLE_SECTION,
(select amp_org_type_id from amp_org_type where org_type_code='GOUV')
FROM burkina_db.section as o
where not exists (select * from amp_organisation  org where org.org_code=o.LIBELLE_SECTION );


/* import activities */
select 'inserting into AMP_ACTIVITY fom conv';

INSERT INTO AMP_ACTIVITY 
(
sector_code,
amp_id,
name, 
description, 
objectives,
contractors, 
program_description,
`condition`, 
status_reason, 
proposed_approval_date,
proposed_start_date,
actual_completion_date,
actual_start_date,
amp_team_id,
approval_status,
activity_creator,
totalCost,
draft

)
SELECT 
c.CODE_SECTEUR,
c.CODE_CONVENTION,
c.LIBELLE_CONVENTION, 
concat(@avtivity_desc, @timestmp:=@timestmp+1),
concat(@avtivity_obj, @timestmp:=@timestmp+1),
' ',
' ',
' ',
' ',
' ',
' ',
c.DATE_DEBUT,
DATE_FIN,
@team_id,
@approved,
@activity_creator,
c.MONTANT_DEVISE_CONV/1000,
true
  
FROM  burkina_db.`convention` as c ;
/* where c.STATCONV!='D' and c.STATCONV!='C' and c.STATCONV!='A' and c.STATCONV!='5'; */ 

select 'mapping Implementing agency';  

INSERT INTO amp_org_role
(activity,organisation,role)
select a.amp_activity_id, org.amp_org_id, (Select amp_role_id from amp_role where role_code='IA')  from amp_activity a
inner join burkina_db.`convention` as c  on  c.CODE_CONVENTION =a.amp_id
inner join burkina_db.section age on age.CODE_SECTION=c.CODE_SECTION
inner join AMP_ORGANISATION org on org.org_code=age.LIBELLE_SECTION;



select 'mapping donor';  

INSERT INTO amp_org_role
(activity,organisation,role)
select a.amp_activity_id, org.amp_org_id, (Select amp_role_id from amp_role where role_code='DN')  from amp_activity a
inner join burkina_db.`convention` as c  on  c.CODE_CONVENTION =a.amp_id
inner join burkina_db.donors d on d.CODE_BAILLEUR=c.CODE_BAILLEUR
inner join AMP_ORGANISATION org on org.org_code=d.LIBELLE_BAILLEUR;



/* mapping activity and sectors */
select 'mapping activity and sectors';

insert into amp_activity_sector
(amp_activity_id,amp_sector_id,sector_percentage)
select  act.amp_activity_id, sec.amp_sector_id,  100 
from AMP_ACTIVITY as act inner join AMP_SECTOR as sec on act.sector_code=sec.sector_code;

select 'add values for financing instrument';

UPDATE AMP_FUNDING set type_of_assistance_category_value_id=null;

DELETE catval FROM amp_category_value AS catval
inner join amp_category_class cls on catval.amp_category_class_id=cls.id
where cls.keyName ='type_of_assistence'; 

insert into amp_category_value (CODE_FINANCEMENT, category_value,amp_category_class_id,index_column) 
select fin.CODE_FINANCEMENT , fin.LIBELLE_FINANCEMENT,(select id from amp_category_class cls where keyName='type_of_assistence'), fin.CODE_FINANCEMENT-2 from burkina_db.financement fin;




/* mapping fundings activities and organizations */
select 'mapping fundings activities and organizations';

INSERT into AMP_FUNDING (
financing_id,
amp_donor_org_id,
amp_activity_id, 
type_of_assistance_category_value_id,
amp_modality_id,
financing_instr_category_value_id)
SELECT 
c.N_COMPTABLE_BAILLEUR,
org.amp_org_id ,  
a.amp_activity_id,  
catval.id, 
@funding_modality,
111 
FROM burkina_db.`convention` AS c inner join   
AMP_ACTIVITY AS a on c.CODE_CONVENTION =a.amp_id
inner join burkina_db.donors d on d.CODE_BAILLEUR=c.CODE_BAILLEUR
inner join AMP_ORGANISATION org on org.org_code=d.LIBELLE_BAILLEUR
inner join  amp_category_value catVal on c.CODE_FINANCEMENT=catVal.CODE_FINANCEMENT;



/* importing currencies */
select 'importing currencies fundings';
INSERT INTO amp_currency(
currency_code,
country_name,
active_flag)
SELECT CODE_MONNAIE, LIBELLE_MONNAIE, FLAG_TRANCHE 
FROM burkina_db.monnaie
where CODE_MONNAIE not in (select currency_code from amp_currency);



/* importing planned fundings */
select 'importing planned (initial) fundings';

INSERT INTO AMP_FUNDING_DETAIL 
(
adjustment_type, 
transaction_type,  
transaction_date, 
transaction_amount,
org_role_code,
perspective_id,
AMP_FUNDING_ID,
amp_currency_id
)
SELECT
@funding_adjusment_actual, 
@commitment, 
c.DATE_DEBUT,
c.MONTANT_DEVISE_CONV/1000,
@org_role_code,
@funding_perspective,
f.amp_funding_id,
cur.amp_currency_id
FROM burkina_db.convention  as c inner join AMP_ACTIVITY as a on c.CODE_CONVENTION=a.amp_id
inner join AMP_FUNDING as f on f.amp_activity_id=a.amp_activity_id
inner join amp_currency as cur on cur.currency_code=c.MONNAIE;



select 'correcting invalid dates in fundings';

update amp_funding_detail 
set transaction_date='2011-01-01 01:01:01'
where transaction_date is null or transaction_date like '0000-00-00%';



/* correcting activity dates */
select 'correcting 0000-00-00 dates in activities';

update amp_activity
set actual_start_date=null
where actual_start_date='0000-00-00 00:00:00'; 

update amp_activity
set proposed_start_date=null
where proposed_start_date='0000-00-00 00:00:00';  

update amp_activity
set proposed_approval_date=null
where proposed_approval_date='0000-00-00 00:00:00';  

update amp_activity
set actual_approval_date=null
where actual_approval_date='0000-00-00 00:00:00'; 

update amp_activity
set actual_completion_date=null
where actual_completion_date='0000-00-00 00:00:00'; 



COMMIT;

/* REMOVE TEMPORARY INDEXES */

DROP INDEX CODE_FINANCEMENT ON  AMP_CATEGORY_VALUE;

DROP INDEX sector_code ON AMP_ACTIVITY;


/*removing temporary columnes  */
ALTER TABLE AMP_CATEGORY_VALUE
DROP COLUMN CODE_FINANCEMENT;

ALTER TABLE AMP_ACTIVITY
DROP COLUMN sector_code;

/* end */
