USE AMP_BOLIVIA
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


SELECT  concat('UPDATE STARTED DATABASE AT ',CURRENT_TIME) as msg;
select @amp_sec_scheme_id:= amp_sec_scheme_id from AMP_SECTOR_SCHEME where sec_scheme_code ='BOL_IMP';
insert into AMP_SECTOR (amp_sec_scheme_id,  sector_code,   name,   old_id)
select @amp_sec_scheme_id,c.codsec,c.descsec,c.codsec from sisfin_db.sec c where c.codsec not in (select old_id from AMP_SECTOR);
select concat(ROW_COUNT(),' sectors were added') as msg;


INSERT INTO AMP_SECTOR (amp_sec_scheme_id,  sector_code,   name)
SELECT  sch.amp_sec_scheme_id,  c.valdato, c.interp
FROM sisfin_db.claves AS c, AMP_SECTOR_SCHEME AS sch
WHERE sch.sec_scheme_code='BOL_COMPO_IMP' AND c.nomdato='cvetipcomp'
and  (select count(*) from AMP_SECTOR where
amp_sec_scheme_id=sch.amp_sec_scheme_id and sector_code=c.valdato and name=c.interp)= 0 ;
select concat(ROW_COUNT(),' components were added') as msg;


INSERT INTO AMP_ORGANISATION (old_id,name,org_code,acronym)
SELECT codage, nomage, codage, codage FROM sisfin_db.`age` as o where trim(codage) not in (select trim(old_id) from  AMP_ORGANISATION);
select concat(ROW_COUNT(),' organisations were added') as msg;


UPDATE AMP_ORGANISATION AS org, sisfin_db.`age` AS o, AMP_ORG_TYPE AS t
SET org.org_type_id=t.amp_org_type_id
WHERE org.old_id=o.codage AND o.cvebimulti='B' AND t.org_type_code='BIL';
select concat(ROW_COUNT(),' AMP_ORGANISATION BIL were updated') as msg;


UPDATE AMP_ORGANISATION AS org, sisfin_db.`age` AS o, AMP_ORG_GROUP AS aog
SET org.org_grp_id=aog.amp_org_grp_id
WHERE org.old_id=o.codage and aog.org_grp_code = o.cveorg;
select concat(ROW_COUNT(),' AMP_ORGANISATION GROUP were updated') as msg;


UPDATE AMP_ORGANISATION AS org, sisfin_db.`age` AS o, AMP_ORG_GROUP AS aog
SET org.org_grp_id=aog.amp_org_grp_id
WHERE org.old_id=o.codage and aog.org_grp_code = 'ALEMA' and  o.cveorg = 'ALEM';
select concat(ROW_COUNT() ,' Multiple codes  were updated') as msg;


INSERT INTO AMP_ORGANISATION(old_id,name,org_code,acronym,org_type_id)
SELECT codent,
       noment,
       codent,
       codent,
       tt.amp_org_type_id
FROM sisfin_db.`ent` as e,
     amp_org_type as tt
      where tt.org_type_code = 'OTHER' and
      not exists (select a.codage from sisfin_db.`age` as a where a.codage =
      e.codent and upper(a.nomage) = upper(e.noment))
      and e.codent not in (select old_id  from AMP_ORGANISATION);
select concat(ROW_COUNT() ,' Executing Agencies  were updated') as msg;


INSERT INTO amp_terms_assist(terms_assist_code, terms_assist_name,old_id)
SELECT lvl.valdato,
       lvl.interp,
       lvl.valdato
FROM sisfin_db.`claves` lvl
WHERE lvl.nomdato = 'cvecoop'
and lvl.valdato not in (select terms_assist_code from amp_terms_assist);
select concat(ROW_COUNT() ,' amp_terms_assist were updated') as msg;


insert into CLASI_PND(code, description)
select Cod_PND,
       Descripcion
from sisfin_db.Clasif_PND
where Cod_PND not in (select code from CLASI_PND);
select concat(ROW_COUNT() ,' CLASI_PND were updated') as msg;



