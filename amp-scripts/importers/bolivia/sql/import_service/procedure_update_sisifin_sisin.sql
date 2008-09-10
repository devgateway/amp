drop procedure if exists sp_update_db;
CREATE  PROCEDURE `sp_update_db`()
    NOT DETERMINISTIC
    SQL SECURITY DEFINER
    COMMENT ''
BEGIN

/* set up variables */
SET @import_time:=unix_timestamp();
SET @approved='approved';
SET @team_id=24; /* this is VIPFE Team */
SET @activity_creator=92;  /* this is ATL member */
SET @activity_code='AMP-BOLIVIA';
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


select 'importing sectors';

insert into AMP_SECTOR (amp_sec_scheme_id,  sector_code,   name,   old_id)
select sch.amp_sec_scheme_id,   c.codsec,   c.descsec, c.codsec
from sisfin_db.sec c, AMP_SECTOR_SCHEME sch
where sch.sec_scheme_code='BOL_IMP'
 and (SELECT count(*) FROM AMP_SECTOR where OLD_ID =C.CODSEC)=0;



select 'importing components (sectors)';

INSERT INTO AMP_SECTOR (amp_sec_scheme_id,  sector_code,   name)
SELECT  sch.amp_sec_scheme_id,  c.valdato, c.interp
FROM sisfin_db.claves AS c,  AMP_SECTOR_SCHEME AS sch
WHERE sch.sec_scheme_code='BOL_COMPO_IMP'  AND c.nomdato='cvetipcomp' and
(SELECT COUNT(*) FROM AMP_SECTOR WHERE AMP_SEC_SCHEME_ID =SCH.AMP_SEC_SCHEME_ID AND SECTOR_CODE = C.VALDATO AND NAME = C.INTERP) =0;


/* import organizations  from the table AGE, also there are more organizatins on the TABLE ENT*/

select 'importing organisations';

INSERT INTO AMP_ORGANISATION
(
old_id,
name,
org_code,
acronym
)
SELECT
codage,
nomage,
codage,
codage
FROM sisfin_db.`age` as o
WHERE (SELECT count(*) FROM AMP_ORGANISATION x where TRIM(x.OLD_ID)= TRIM(CODAGE))=0;

/* setting up organization types */
select 'update AMP_ORGANISATION MUL';

UPDATE AMP_ORGANISATION AS org, sisfin_db.`age` AS o, AMP_ORG_TYPE AS t
SET org.org_type_id=t.amp_org_type_id
WHERE org.old_id=o.codage AND o.cvebimulti='M' AND t.org_type_code='MUL';

select 'update AMP_ORGANISATION BIL';

UPDATE AMP_ORGANISATION AS org, sisfin_db.`age` AS o, AMP_ORG_TYPE AS t
SET org.org_type_id=t.amp_org_type_id
WHERE org.old_id=o.codage AND o.cvebimulti='B' AND t.org_type_code='BIL';

select 'update AMP_ORGANISATION GROUP';

UPDATE AMP_ORGANISATION AS org, sisfin_db.`age` AS o, AMP_ORG_GROUP AS aog
SET org.org_grp_id=aog.amp_org_grp_id
WHERE org.old_id=o.codage and aog.org_grp_code = o.cveorg;

/* AMP-2970 - Multiple codes for Alemania*/
UPDATE AMP_ORGANISATION AS org, sisfin_db.`age` AS o, AMP_ORG_GROUP AS aog
SET org.org_grp_id=aog.amp_org_grp_id
WHERE org.old_id=o.codage and aog.org_grp_code = 'ALEMA' and  o.cveorg = 'ALEM';


/* import organizations  from the table ENT checking if were not already added from the table AGE */

select 'importing executing Agencies';

INSERT INTO AMP_ORGANISATION
(
old_id,
name,
org_code,
acronym,
org_type_id
)
SELECT
codent,
noment,
codent,
codent,
tt.amp_org_type_id
FROM sisfin_db.`ent` as e,  amp_org_type as tt
where  tt.org_type_code='OTHER' and not exists
	(select a.codage from sisfin_db.`age` as a where a.codage=e.codent   and upper(a.nomage)=upper(e.noment))
and  (SELECT count(*) FROM AMP_ORGANISATION x where E.CODENT=x.OLD_ID )=0;

/* terms and assist */
select 'amp_terms_assist';

INSERT INTO amp_terms_assist(terms_assist_code, terms_assist_name,old_id)
SELECT lvl.valdato, lvl.interp, lvl.valdato
FROM sisfin_db.`claves` lvl
WHERE lvl.nomdato='cvecoop' and   (SELECT count(*) FROM AMP_TERMS_ASSIST x where x.TERMS_ASSIST_CODE=LVL.VALDATO)=0;

/*import Casificacion NPD*/
insert into CLASI_PND (code,description)
select Cod_PND,Descripcion from sisfin_db.Clasif_PND where
(SELECT count(*)  FROM CLASI_PND x where x.CODE=COD_PND)=0;

/* import activities */
select 'inserting into AMP_ACTIVITY fom conv';

INSERT INTO AMP_ACTIVITY
(
old_id,
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
convenio_date_filter, -- AMP-2387
convenio_numcont,
actual_start_date,
amp_team_id,
approval_status,
proj_cost_amount,
proj_cost_date,
activity_creator,
totalCost,
old_status_id,
draft,
classi_code
)
SELECT
c.numconv,
c.numconv,
c.nomconv,
concat(@avtivity_desc, @timestmp:=@timestmp+1),
concat(@avtivity_obj, @timestmp:=@timestmp+1),
' ',
' ',
' ',
' ',
c.fechprogefec,
c.fechprogprdes,
c.fechproguldes,
c.fechproguldes, -- AMP-2387 I don't want to rewrite actual_completion_date
numcont,
c.fechcont,
@team_id,
@approved,
montorig,
fechcont,
@activity_creator,
montous,
cvealc,
0,
Cod_PND
FROM  sisfin_db.`conv` as c
where c.STATCONV!='C' and c.STATCONV!='A' and
(SELECT count(*) from AMP_ACTIVITY x where x.old_id=C.NUMCONV) = 0;

UPDATE AMP_ACTIVITY A, (SELECT * FROM SISFIN_DB.`CONV`) AS C
	SET A.NAME = C.NOMCONV,
    A.PROPOSED_APPROVAL_DATE = C.FECHPROGEFEC,
    A.PROPOSED_START_DATE = C.FECHPROGPRDES,
    A.ACTUAL_COMPLETION_DATE = C.FECHPROGULDES,
    A.CONVENIO_DATE_FILTER = C.FECHPROGULDES,
    A.CONVENIO_NUMCONT = NUMCONT,
    A.ACTUAL_START_DATE = C.FECHCONT,
    A.APPROVAL_STATUS = @approved,
    A.TOTALCOST = MONTOUS,
    A.OLD_STATUS_ID = CVEALC,
    A.DRAFT = 0,
    A.CLASSI_CODE = COD_PND,
	A.AMP_ID=NUMCONV,
	A.proj_cost_amount=montorig,
	A.proj_cost_date=fechcont
WHERE C.STATCONV != 'C' AND C.STATCONV != 'A' AND       C.NUMCONV = A.OLD_ID;

/* START - AMP-2387 */
DROP TEMPORARY TABLE IF EXISTS `tmp_tbl_convenio_date_filter`;
CREATE TEMPORARY TABLE `tmp_tbl_convenio_date_filter` (
  `numconv` VARCHAR(5) NOT NULL,
  `maxfechvigenm` DATETIME NOT NULL,
  PRIMARY KEY (`numconv`)
)ENGINE = InnoDB;


insert into tmp_tbl_convenio_date_filter
select e.numconv, max(e.fechvigenm)
from sisfin_db.enm e
where  e.tipenm = 'PU' and e.fechvigenm is not null
group by e.numconv;

update amp_activity as a, tmp_tbl_convenio_date_filter tmp
set a.convenio_date_filter = tmp.maxfechvigenm
where a.old_id = tmp.numconv;

DROP TEMPORARY TABLE `tmp_tbl_convenio_date_filter`;
/* END - AMP-2387 */


/* mapping contacts */
select 'mapping contacts';

update amp_activity as a, sisfin_db.`usu` as u, sisfin_db.`conv` c
set a.mofed_cnt_last_name=u.nombreusuario
where a.old_id=c.numconv and c.codusu=u.codusu;


/* mapping executing agencies (organizations) to activities using the ENT-CONVENIO relation */
select 'mapping executing agencies to activities';

TRUNCATE TABLE amp_org_role;

insert into amp_org_role (activity,organisation,role,percentage)
select
	a.amp_activity_id,
	o.amp_org_id,
	ar.amp_role_id,
	m.porcpart
from
	sisfin_db.`conv_entejec` as m,
	amp_activity as a,
	amp_organisation as o ,
	amp_role as ar,
  	sisfin_db.`ent` as ent

where
	ar.role_code='EA'
	and a.old_id=m.numconv
    and ent.codent=o.old_id
	and o.old_id = m.codentejec
	and upper(ent.noment)=upper(o.name);


INSERT INTO AMP_ORG_ROLE(ACTIVITY, ORGANISATION, ROLE, PERCENTAGE)
SELECT A.AMP_ACTIVITY_ID,
       O.AMP_ORG_ID,
       1,
       NULL
FROM AMP_FUNDING A
     INNER JOIN AMP_ORGANISATION O ON A.AMP_DONOR_ORG_ID = O.AMP_ORG_ID;

/* importing issues */
select 'importing issues 1';
TRUNCATE TABLE AMP_ISSUES;

INSERT INTO AMP_ISSUES(name, amp_activity_id)
SELECT c.sit_actual, a.amp_activity_id
FROM sisfin_db.`conv` AS c, amp_activity AS a
WHERE (c.sit_actual is not null) AND c.numconv=a.old_id;

select 'importing issues 2';

insert into amp_issues(name,amp_activity_id)
SELECT c.tramite_actual, a.amp_activity_id
FROM sisfin_db.`conv` c, amp_activity a
where (c.tramite_actual is not null) and c.numconv=a.old_id;

select 'importing issues 3';

insert into amp_issues(name,amp_activity_id)
SELECT c.Tip_ejecucion, a.amp_activity_id
FROM sisfin_db.`conv` c, amp_activity a
where (c.Tip_ejecucion is not null) and c.numconv=a.old_id ;

select 'importing issues 4';

insert into amp_issues(name,amp_activity_id)
SELECT c.marca, a.amp_activity_id
FROM sisfin_db.`conv` c, amp_activity a
where (c.marca is not null) and c.numconv=a.old_id;


/* mapping activity and sectors */
select 'mapping activity and sectors';
TRUNCATE TABLE AMP_ACTIVITY_SECTOR;

insert into amp_activity_sector
(amp_activity_id,amp_sector_id,sector_percentage)
select  act.amp_activity_id, sec.amp_sector_id,  ac.porcsec
from AMP_ACTIVITY as act, AMP_SECTOR as sec, sisfin_db.conv_sec as ac
where act.old_id=ac.numconv and sec.old_id=ac.codsec ;

/* mapping activity and statuses */
select 'mapping activity and statuses';
TRUNCATE TABLE AMP_ACTIVITIES_CATEGORYVALUES;

INSERT INTO amp_activities_categoryvalues (amp_activity_id, amp_categoryvalue_id)
SELECT act.amp_activity_id, cat.id
FROM AMP_ACTIVITY as act,    AMP_CATEGORY_VALUE as cat,    sisfin_db.`conv` acto, sisfin_db.`claves` as cla
WHERE cat.amp_category_class_id=@status_class_id and cat.category_value=cla.interp and acto.numconv=act.old_id
and acto.statconv=cla.valdato and cla.nomdato='statconv';

/*
FROM AMP_ACTIVITY as act,    AMP_CATEGORY_VALUE as cat,    sisfin_db.`conv` co, sisfin_db.`claves` as cla
WHERE co.numconv=act.old_id and co.statconv=cat.old_id and cla;
*/

/* mapping implementation levels */
select 'mapping implementation levels';

INSERT INTO amp_activities_categoryvalues (amp_activity_id, amp_categoryvalue_id)
SELECT act.amp_activity_id, cat.id
FROM AMP_ACTIVITY as act,    AMP_CATEGORY_VALUE as cat,    sisfin_db.`conv` acto, sisfin_db.`claves` as cla
WHERE cat.amp_category_class_id=@level_class_id and cat.category_value=cla.interp and acto.numconv=act.old_id
and acto.cvealc=cla.valdato and cla.nomdato='cvealc';


select 'mapping descriptions for english';
TRUNCATE TABLE DG_EDITOR;
INSERT INTO DG_EDITOR
(EDITOR_KEY, LANGUAGE, SITE_ID, BODY,LAST_MOD_DATE,CREATION_IP,ORDER_INDEX)
SELECT a.description, 'en', @amp_site_id ,c.descconv,now() ,'127.0.0.1',@editor_order
FROM sisfin_db.`conv` AS c, AMP_ACTIVITY AS a
WHERE c.numconv=a.old_id ;

/* mapping descriptions for spanish*/
select 'mapping descriptions for spanish';

INSERT INTO DG_EDITOR
(EDITOR_KEY, LANGUAGE, SITE_ID, BODY,LAST_MOD_DATE,CREATION_IP,ORDER_INDEX)
SELECT a.description, 'es', @amp_site_id ,c.descconv,now() ,'127.0.0.1',@editor_order
FROM sisfin_db.`conv` AS c, AMP_ACTIVITY AS a
WHERE c.numconv=a.old_id ;



/* mapping fundings activities and organizations */
select 'mapping fundings activities and organizations';
TRUNCATE TABLE AMP_FUNDING_DETAIL;
TRUNCATE TABLE AMP_FUNDING;

INSERT into AMP_FUNDING (
financing_id,
amp_donor_org_id,
amp_activity_id,
type_of_assistance_category_value_id,
amp_modality_id,
financing_instr_category_value_id)
SELECT
o.codage,
org.amp_org_id ,
a.amp_activity_id,
catval.id,
@funding_modality,
111
FROM sisfin_db.`conv` AS c,
AMP_ACTIVITY AS a,
AMP_ORGANISATION AS org,
sisfin_db.`age` AS o,
sisfin_db.`claves` AS ta,
amp_category_value AS catval
WHERE c.numconv=a.old_id AND org.old_id=o.codage  and upper(org.name)=upper(o.nomage)
AND c.codage=org.old_id AND ta.nomdato='cvecoop'
AND ta.valdato=c.cvecoop AND catval.category_value=ta.interp
AND catval.amp_category_class_id=10;


/* importing currencies */
select 'importing currencies (initial) fundings';
INSERT INTO amp_currency(
currency_code,
country_name,
currency_name,
active_flag)
SELECT sigla_mda, moneda, pais, 1
FROM sisfin_db.clasif_moneda
WHERE (SELECT count(*) FROM AMP_CURRENCY x where trim(upper(x.CURRENCY_CODE))=trim(upper(SIGLA_MDA)))=0;

select 'importing  old currencies used on the  transactions ';
/*some currencies could be deleted  so we need check if there is old currencies used on the  transactions*/
INSERT INTO AMP_CURRENCY(CURRENCY_CODE, COUNTRY_NAME, CURRENCY_NAME, ACTIVE_FLAG)
SELECT
distinct
CVEMONORIG,
       NULL,
       CVEMONORIG,
       1
FROM SISFIN_DB.DESEM
WHERE
      (SELECT count(*) FROM AMP_CURRENCY x where upper(trim(x.CURRENCY_CODE))=upper((CVEMONORIG)))=0
              and CVEMONORIG is not null;


INSERT INTO amp_currency (currency_code,country_name,currency_name,active_flag)
select
   distinct
	cvemonorig,
	null,
	cvemonorig,
	1
from
	sisfin_db.enm
where
 (SELECT count(*) FROM AMP_CURRENCY x where upper(trim(x.CURRENCY_CODE))=upper((CVEMONORIG)))=0
              and CVEMONORIG is not null;

select 'end old currencies  ...... ';

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
amp_currency_id,
fixed_exchange_rate
)
SELECT
@funding_adjusment_planned,
@commitment,
enm.fechvigenm,
enm.montorig,
@org_role_code,
@funding_perspective,
f.amp_funding_id,
cu.amp_currency_id,
enm.tipcam
FROM sisfin_db.`enm` as enm, AMP_ACTIVITY as a, AMP_FUNDING as f, amp_currency as cu
WHERE  a.old_id=enm.numconv
and a.amp_activity_id=f.amp_activity_id
and enm.cvemonorig=cu.currency_code
and enm.numenm=0;


/* mapping additional commintments from ENM table.*/
select 'mapping additional commintments from ENM table.';

INSERT INTO AMP_FUNDING_DETAIL
(
adjustment_type,
transaction_type,
transaction_date,
transaction_amount,
org_role_code,
perspective_id,
AMP_FUNDING_ID,
amp_currency_id,
fixed_exchange_rate
)
SELECT
@funding_adjusment_actual,
@commitment,
enm.fechvigenm,
enm.montorig,
@org_role_code,
@funding_perspective,
f.amp_funding_id,
cu.amp_currency_id,
enm.tipcam
FROM sisfin_db.`enm` as enm, AMP_ACTIVITY as a, AMP_FUNDING as f, amp_currency as cu
WHERE  a.old_id=enm.numconv
and a.amp_activity_id=f.amp_activity_id
and enm.cvemonorig=cu.currency_code
and enm.numenm>0
and enm.montorig!=0;

select 'importing actual disbursments';
INSERT INTO AMP_FUNDING_DETAIL
(
adjustment_type,
transaction_type,
transaction_date,
transaction_amount,
org_role_code,
perspective_id,
AMP_FUNDING_ID,
amp_currency_id,
fixed_exchange_rate
)
SELECT
@funding_adjusment_actual,
@disbursment,
dsm.fechdesem,
dsm.montorig,
@org_role_code,
@funding_perspective,
f.amp_funding_id,
cu.amp_currency_id,
dsm.tipcam
FROM sisfin_db.`desem` as dsm, AMP_ACTIVITY as a, AMP_FUNDING as f, amp_currency as cu
WHERE  a.old_id=dsm.numconv
and a.amp_activity_id=f.amp_activity_id
and dsm.cvemonorig=cu.currency_code
and lower(dsm.tipdesem)='e';


select 'importing planned disbursments';
INSERT INTO AMP_FUNDING_DETAIL
(
adjustment_type,
transaction_type,
transaction_date,
transaction_amount,
org_role_code,
perspective_id,
AMP_FUNDING_ID,
amp_currency_id,
fixed_exchange_rate
)
SELECT
@funding_adjusment_planned,
@disbursment,
dsm.fechdesem,
dsm.montorig,
@org_role_code,
@funding_perspective,
f.amp_funding_id,
cu.amp_currency_id,
dsm.tipcam
FROM sisfin_db.`desem` as dsm, AMP_ACTIVITY as a, AMP_FUNDING as f, amp_currency as cu
WHERE  a.old_id=dsm.numconv
and a.amp_activity_id=f.amp_activity_id
and dsm.cvemonorig=cu.currency_code
and lower(dsm.tipdesem)='p';




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


select '==type of credit==';

/* removing old values, we are going to replace financing instrument meaning, it will be called Type of credit for Bolivia */
select 'remove previous category values for financing instrument';

/*DELETE catval FROM amp_category_value AS catval, amp_category_class AS catclass where catval.amp_category_class_id=catclass.id  AND catclass.keyName ='financing_instrument';*/

/*  importing new values: there are just 3 records */
select 'importing new category values for Type of Credit';

SET @temp_cat_val=-1;

INSERT INTO amp_category_value(category_value,amp_category_class_id,index_column)
SELECT cla.interp, catclass.id, @temp_cat_val:=@temp_cat_val+1
FROM amp_category_class AS catclass, sisfin_db.`claves` AS cla
WHERE cla.nomdato='cvecred' AND catclass.keyName='financing_instrument' and
(select count(*)FROM  AMP_CATEGORY_VALUE AS CATVAL,
    AMP_CATEGORY_CLASS AS CATCLASS WHERE
    CATVAL.AMP_CATEGORY_CLASS_ID = CATCLASS.ID AND upper(CATCLASS.KEYNAME) ='FINANCING_INSTRUMENT'
	and upper(CATVAL.category_value)=upper(CLA.INTERP))=0;


/* mapping credit types to activities */
select 'mapping founding credit types to activity fundings';

UPDATE amp_activity AS act,
sisfin_db.`conv` AS con,
amp_category_value AS catval,
amp_category_class AS catclass,
sisfin_db.`claves` AS cla,
amp_funding AS fnd
SET act.credit_type_id=catval.id, fnd.financing_instr_category_value_id=catval.id
WHERE act.old_id=con.numconv
AND fnd.amp_activity_id=act.amp_activity_id
AND catclass.keyName='financing_instrument'
AND catval.amp_category_class_id = catclass.id
AND cla.nomdato='cvecred'
AND cla.interp=catval.category_value
AND con.cvecred=cla.valdato;


/* ==Regions== */
select 'importin regions';

insert into amp_region
(name, country_id, region_code)
select interp, 'bo', valdato from sisfin_db.`claves` as c
where c.nomdato='cvedep' and  (select count(*) from AMP_REGION x where trim(upper(x.NAME))=  trim(upper(INTERP)) )=0;

select 'inserting locations';

insert into amp_location
(name,country,region,country_id,region_id)
select r.name, 'Bolivia', r.name, 'bo', r.amp_region_id from amp_region r, sisfin_db.`claves` as c
where c.nomdato='cvedep' and r.region_code=c.valdato
 and
      (select count(*) from amp_location where trim(upper(NAME)) = trim(upper(
      R.NAME)) and upper(COUNTRY) = 'BOLIVIA' and upper(COUNTRY_ID) = 'BO' and
      REGION_ID = R.AMP_REGION_ID) = 0;

select 'mapping locations to activities';

truncate table amp_activity_location;

insert into amp_activity_location
(amp_activity_id, amp_location_id,location_percentage)
select act.amp_activity_id, loc.amp_location_id, condep.porcdep
from amp_activity as act,
amp_location as loc,
amp_region as reg,
sisfin_db.`claves` as c,
sisfin_db.`conv_dep` as condep
where
act.amp_id=condep.numconv
and loc.region_id=reg.amp_region_id
and reg.region_code=c.valdato
and c.nomdato='cvedep'
and condep.cvedep=c.valdato;


/*  mapping components (sectors) */
TRUNCATE TABLE AMP_ACTIVITY_COMPONENTE;

select ' mapping components (sectors)';
INSERT INTO amp_activity_componente (amp_activity_id, amp_sector_id, percentage)
SELECT act.amp_activity_id, sec.amp_sector_id, bcomp.porccomp
FROM amp_sector AS sec, amp_sector_scheme AS sch, sisfin_db.comp AS bcomp, sisfin_db.`conv` AS con, amp_activity AS act
WHERE sec.amp_sec_scheme_id=sch.amp_sec_scheme_id
AND sch.sec_scheme_code='BOL_COMPO_IMP'
AND act.amp_id=con.numconv
AND bcomp.numconv=con.numconv
AND bcomp.cvetipcomp=sec.sector_code;

/* mapping activities and themes(programs) */

select 'mapping activities and themes(programs)';
TRUNCATE TABLE amp_activity_theme;

INSERT INTO amp_activity_theme
(amp_activity_id,amp_theme_id)
select act.amp_activity_id, prog.amp_theme_id
from  amp_activity as act, amp_theme as prog, sisfin_db.`conv` as acto
where act.old_id=acto.numconv and prog.theme_code = concat('EBRP', substring(acto.Cod_EBRP,2));

TRUNCATE TABLE amp_activity_program;
insert INTO amp_activity_program
(amp_activity_id,amp_program_id,program_percentage,program_setting)
select  act.amp_activity_id, prog.amp_theme_id, 100, progset.amp_program_settings_id
from amp_activity as act, amp_theme as prog, amp_program_settings as progset, sisfin_db.`conv` as con
where act.amp_id=con.numconv
and prog.theme_code = concat('EBRP', substring(con.Cod_EBRP,2))
and progset.name like 'National Plan Objective';


/*Adding donor to activities*/


insert into amp_org_role (
  activity ,
  organisation ,
  role ,
  percentage
)


select
a.amp_activity_id,
o.amp_org_id,1,null
 from amp_funding
a inner join amp_organisation  o on a.amp_donor_org_id=o.amp_org_id
where a.amp_activity_id not in
(select x.activity from amp_org_role x
         where x.activity=a.amp_activity_id
               and x.role=1
               and x.organisation=o.amp_org_id
         );




/*clean duplicated agencies*/
select 'cleaning duplicated agencies';

  delete
      from
          amp_organisation
      where
           name in
                (
                select table1.name from
                (
			select count(*), name from amp_organisation group by name having count(*) > 1) as table1
                )
                and amp_org_id not in (select  organisation from amp_org_role  )
                and amp_org_id not in (select amp_donor_org_id from amp_funding);

update amp_activity_sector set classification_config_id=1;

select 'UPDATE FOR SISIN';



SET @commitment=0;
SET @disbursment=1;
SET @expenditure=2;

SET @funding_adjusment_planned=0;
SET @funding_adjusment_actual=1;

SET @usd_currency_id=48;
SET @mofed_perspective_id=2;

SET @base_url_sisin='http://www.google.com?sisinCode=';



SELECT 'Inserting Proyectos references into Component table';
INSERT INTO amp_components (CodigoSISIN)
SELECT distinct CodigoSISIN FROM sisin_db.seguimiento_financiero s
where CodConvExt != '00000' and CodConvExt != '99999' and (select count(*) from amp_components x where x.CodigoSISIN=CodigoSISIN)=0 ;


SELECT 'Updating Components with Proyecto data';
UPDATE amp_components a,
sisin_db.proyecto p
SET a.title = p.NombreProyecto,
a.description = p.DescripcionProyecto,
a.code = p.CodigoSISIN,
a.url = CONCAT(@base_url_sisin,p.CodigoSISIN)
where a.CodigoSISIN = p.CodigoSISIN;

DELETE FROM amp_activity_components;

SELECT 'Inserting references from Proyectos to Activities';
INSERT INTO amp_activity_components(amp_activity_id, amp_component_id)
SELECT
a.amp_activity_id,
c.amp_component_id
FROM amp_components c
join sisin_db.seguimiento_financiero sf on c.CodigoSISIN = sf.CodigoSisin
join amp_activity a on a.amp_id = sf.CodConvExt
group by a.amp_activity_id, c.amp_component_id;

DELETE FROM AMP_COMPONENT_FUNDING;

SELECT 'Inserting Monto Programado Funding Data';
INSERT INTO amp_component_funding (transaction_type,adjustment_type,currency_id,perspective_id,amp_component_id,activity_id,transaction_amount,transaction_date,reporting_date,exchange_rate)
SELECT
@commitment as transaction_type,
@funding_adjusment_planned as adjustment_type,
@usd_currency_id as currency_id,
@mofed_perspective_id as perspective_id,
c.amp_component_id,
a.amp_activity_id,
sf.MontoProgramado as transaction_amount,
STR_TO_DATE(CONCAT(sf.Mes, '/01/', sf.Ano), '%m/%d/%Y') as transaction_date,
FechaRegistro as reporting_date,
tc.tipodecambio as exchange_rate
FROM amp_components c
join sisin_db.seguimiento_financiero sf on c.CodigoSISIN = sf.CodigoSisin
join amp_activity a on a.amp_id = sf.CodConvExt and sf.MontoProgramado != 0
join sisin_db.tabla_tipocambiogestion tc on tc.ano = sf.Ano;


SELECT 'Inserting Monto Reprogramado Funding Data';
INSERT INTO amp_component_funding (transaction_type,adjustment_type,currency_id,perspective_id,amp_component_id,activity_id,transaction_amount,transaction_date,reporting_date,exchange_rate)
SELECT
@commitment as transaction_type,
@funding_adjusment_actual as adjustment_type,
@usd_currency_id as currency_id,
@mofed_perspective_id as perspective_id,
c.amp_component_id,
a.amp_activity_id,
sf.MontoReprogramado as transaction_amount,
STR_TO_DATE(CONCAT(sf.Mes, '/01/', sf.Ano), '%m/%d/%Y') as transaction_date,
FechaRegistro as reporting_date,
tc.tipodecambio as exchange_rate
FROM amp_components c
join sisin_db.seguimiento_financiero sf on c.CodigoSISIN = sf.CodigoSisin
join amp_activity a on a.amp_id = sf.CodConvExt and sf.MontoReprogramado != 0
join sisin_db.tabla_tipocambiogestion tc on tc.ano = sf.Ano;


SELECT 'Inserting Monto Ejecutado Funding Data';
INSERT INTO amp_component_funding (transaction_type,adjustment_type,currency_id,perspective_id,amp_component_id,activity_id,transaction_amount,transaction_date,reporting_date,exchange_rate)
SELECT
@expenditure,
@funding_adjusment_actual,
@usd_currency_id,
@mofed_perspective_id,
c.amp_component_id,
a.amp_activity_id,
sf.MontoEjecutado as transaction_amount,
STR_TO_DATE(CONCAT(sf.Mes, '/01/', sf.Ano), '%m/%d/%Y') as transaction_date,
FechaRegistro as reporting_date,
tc.tipodecambio as exchange_rate
FROM amp_components c
join sisin_db.seguimiento_financiero sf on c.CodigoSISIN = sf.CodigoSisin
join amp_activity a on a.amp_id = sf.CodConvExt and sf.MontoEjecutado != 0
join sisin_db.tabla_tipocambiogestion tc on tc.ano = sf.Ano;




COMMIT;
END;