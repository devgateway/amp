CREATE OR REPLACE FUNCTION  "APPLYTHOUSANDSFORVISIBILITY" 
    (
      d IN NUMBER )
    RETURN CHAR
  AS
  BEGIN
    DECLARE
      thousands CHAR(5);
    BEGIN
       SELECT settingsValue
         INTO thousands
         FROM amp_global_settings
        WHERE settingsName = 'Amounts in Thousands';
      IF thousands         ='true' THEN
        RETURN d*0.001;
      END IF;
      RETURN d;
    END;
  END;
/
 CREATE OR REPLACE FUNCTION  "DATEDIFF" (d1 IN DATE, d2 IN DATE)
RETURN INTEGER IS
begin
DECLARE r NUMBER;
BEGIN  
select trunc(d1-d2) INTO r from dual;
return r;
END;
end;
/
 CREATE OR REPLACE FUNCTION  "GETEXCHANGE" (
   currency IN CHAR,
    cdate   IN DATE )
    RETURN NUMBER
  IS
       BEGIN
       declare retVal number;
       BEGIN
          IF currency='USD' THEN
            retVal  :=1;
          ELSE
             SELECT COUNT(exchange_rate)
             INTO retVal
             FROM amp_currency_rate
             WHERE to_currency_code = currency AND
                   exchange_rate_date = cdate;
            IF retVal   > 0 THEN
               SELECT exchange_rate
               INTO retVal
               FROM amp_currency_rate
               WHERE to_currency_code = currency AND
                     exchange_rate_date = cdate;
            ELSE
                SELECT count(exchange_rate)
               INTO retVal
               FROM amp_currency_rate
               WHERE to_currency_code = currency AND
                     exchange_rate_date = cdate;
              IF retVal                > 0 THEN
                 SELECT exchange_rate
                 INTO retVal
                 FROM amp_currency_rate
                 WHERE to_currency_code = currency AND
                       exchange_rate_date = cdate;
              ELSE
                 SELECT exchange_rate
                 INTO retVal
                 FROM (SELECT exchange_rate, ROW_NUMBER() OVER(order by
                  exchange_rate_date) AS rid FROM amp_currency_rate WHERE
                   to_currency_code = currency AND exchange_rate_date <= cdate
                    ORDER BY exchange_rate_date DESC) t1
                 WHERE t1.rid = 1;
              END IF;
  END IF;
  END IF;
  return retVal;
    END;
END;
/
 CREATE OR REPLACE FUNCTION  "GETEXCHANGEWITHFIXED" (
   currency IN CHAR,
    cdate   IN DATE,
    fixedExchangeRate NUMBER)
    RETURN NUMBER
  IS
    begin
      if fixedExchangeRate is not null then 
        return fixedExchangeRate;
      end if;
      return getexchange(currency,cdate);
  
END;
/
 CREATE OR REPLACE FUNCTION  "GETPARENTSECTORID" 
    (
      sectorId IN NUMBER)
    RETURN NUMBER
  IS
  BEGIN
    DECLARE
      sid NUMBER;
      pid NUMBER;
      n   NUMBER;
    BEGIN
       SELECT COUNT(*) INTO n FROM amp_sector WHERE amp_sector_id=sectorId;
      IF n=0 THEN
        RETURN sectorId;
      ELSE
        sid:=sectorId;
        LOOP
           SELECT
            CASE
              WHEN parent_sector_id IS NULL
              THEN -1
              ELSE parent_sector_id
            END
             INTO sid
             FROM amp_sector
            WHERE amp_sector_id = sid;
          IF sid                = -1 THEN
            EXIT;
          ELSE
            pid:=sid;
          END IF;
        END LOOP;
        RETURN pid;
      END IF;
    END;
  END;
/
 CREATE OR REPLACE FUNCTION  "GETREPORTCOLUMNID" 
    (
      cn IN VARCHAR2)
    RETURN NUMBER
  IS
  BEGIN
    DECLARE
      id NUMBER;
    BEGIN
       SELECT columnId INTO id FROM amp_columns WHERE columnName=cn AND rownum=1;
      RETURN id;
    END;
  END;
/
 CREATE OR REPLACE FUNCTION  "GETSECTORNAME" 
    (
      sectorId IN NUMBER)
    RETURN VARCHAR2
  IS
  BEGIN
    DECLARE
      ret VARCHAR2(500);
    BEGIN
       SELECT name INTO ret FROM amp_sector WHERE amp_sector_id=sectorId;
      RETURN ret;
    END;
  END;
/
 CREATE OR REPLACE FUNCTION  "GETSQLDATEFORMAT" 
    RETURN VARCHAR
  IS
  BEGIN
    DECLARE
      gsFormat VARCHAR(12);
      n        NUMBER;
    BEGIN
       SELECT COUNT(*)
         INTO n
         FROM amp_global_settings
        WHERE settingsName = 'Default Date Format';
      IF n                 = 0 THEN
        RETURN 'none';
      ELSE
         SELECT settingsValue
           INTO gsFormat
           FROM amp_global_settings
          WHERE settingsName = 'Default Date Format';
        IF gsFormat          ='dd/MM/yyyy' THEN
          RETURN 'DD/MM/YYYY';
        END IF;
        IF gsFormat='dd/MMM/yyyy' THEN
          RETURN '%DD/MON/YYYY';
        END IF;
        IF gsFormat='MMM/dd/yyyy' THEN
          RETURN 'MON/DD/yyyy';
        END IF;
        IF gsFormat='MM/dd/yyyy' THEN
          RETURN 'MM/DD/yyyy';
        END IF;
      END IF;
      RETURN 'none';
    END;
  END;
/
 CREATE OR REPLACE FUNCTION  "GETTRANSACTIONAMMOUNTUSD" (
      fdId LONG)
    RETURN NUMBER
  IS
  BEGIN

    DECLARE
      r    NUMBER;
      code VARCHAR(3);
      dateFD TIMESTAMP;
      amount NUMBER;
    BEGIN
       SELECT transaction_amount/fixed_exchange_rate
         INTO r
         FROM amp_funding_detail
        WHERE amp_fund_detail_id =fdId
      AND fixed_exchange_rate   IS NOT NULL;
      IF r                      IS NOT NULL THEN
        RETURN r;
      END IF;
       SELECT cur.currency_code,
        transaction_date       ,
        transaction_amount
         INTO code,
        dateFD    ,
        amount
         FROM amp_currency cur
      INNER JOIN amp_funding_detail det
           ON cur.amp_currency_id    =det.amp_currency_id
        WHERE det.amp_fund_detail_id =fdId;
      RETURN amount/getExchange(code,dateFD);
    END;
  END;
/
 

CREATE OR REPLACE PROCEDURE  "CREATE_AMP_SEQ" (   seqName IN varchar2 ,
   tableName IN varchar2,
   idName varchar2)
IS
BEGIN
  DECLARE
   NID NUMBER;
   TGR_NAME VARCHAR2(30);
BEGIN

TGR_NAME:=SUBSTR(seqName,0, LENGTH(seqName)-3)||'TGR';
EXECUTE IMMEDIATE  'SELECT MAX('||idName||') + 1 from '||tableName INTO NID;

if NID is null then
NID:=1;
end if;

EXECUTE IMMEDIATE  'CREATE  SEQUENCE '||seqName||' INCREMENT BY 1 START WITH ' ||TO_CHAR(NID)|| ' MAXVALUE 1E24 MINVALUE 1 NOCYCLE CACHE 20 NOORDER';


EXECUTE IMMEDIATE ('CREATE OR REPLACE TRIGGER '||trim(TGR_NAME)
||' BEFORE INSERT OR UPDATE ON '||trim(tableName)
||' REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW'
||' DECLARE '
||' v_newVal NUMBER(12) := 0;'
||' v_incval NUMBER(12) := 0;'
||' BEGIN'
||' IF INSERTING AND :new.'||trim(idName)||' IS NULL THEN '
||' SELECT  '||seqName||'.NEXTVAL INTO v_newVal FROM DUAL;'
||' IF v_newVal = 1 THEN'
||'  SELECT max('||trim(idName)||') INTO v_newVal FROM '||trim(tableName)||';'
|| ' v_newVal := v_newVal + 1;'
||' LOOP'
||' EXIT WHEN v_incval>=v_newVal;'
||' SELECT '||trim(seqName)||'.nextval INTO v_incval FROM dual;'
||' END LOOP;'
||' END IF;'
||' mysql_utilities.identity := v_newVal; '
||' :new.'||trim(idName)||' := v_newVal;'
||' END IF;'
||' END;');


EXECUTE IMMEDIATE  'ALTER TRIGGER '||TGR_NAME||' enable';
COMMIT;
END;
END;
/
 

CREATE OR REPLACE FORCE VIEW  "V_ACCESSION_INSTRUMENTS" ("AMP_ACTIVITY_ID", "NAME") AS 
  select acc.amp_activity_id AS amp_activity_id,
       acv.category_value AS name
  from amp_activities_categoryvalues acc,
       amp_category_value acv
       , amp_category_class ac
  where acv.id = acc.amp_categoryvalue_id and
        ac.id = acv.amp_category_class_id and
        ac.keyName = 'accessioninstr'
/
CREATE OR REPLACE FORCE VIEW  "V_ACTIVITY_CHANGED_BY" ("AMP_ACTIVITY_ID", "NAME", "USER_ID") AS 
  select a.amp_activity_id AS amp_activity_id,
         u.EMAIL AS "NAME",
         atm.USER_ AS user_id
  from amp_activity a,
       amp_team_member atm,
       dg_user u
  where atm.amp_team_mem_id = a.updated_by and
        atm.USER_ = u.ID
  order by a.amp_activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_ACTIVITY_CREATOR" ("AMP_ACTIVITY_ID", "NAME", "USER_ID") AS 
  select a.amp_activity_id AS amp_activity_id,
       u.FIRST_NAMES || ' ' || u.LAST_NAME AS name,
       atm.USER_ AS user_id
  from amp_activity a,
       amp_team_member atm,
       dg_user u
  where atm.amp_team_mem_id = a.activity_creator and
        atm.USER_ = u.ID
  order by a.amp_activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_ACTORS" ("AMP_ACTIVITY_ID", "NAME", "AMP_ACTOR_ID") AS 
  select ai.amp_activity_id AS amp_activity_id,
       act.name AS name,
       act.amp_actor_id AS amp_actor_id
  from amp_activity a,
       amp_measure m,
       amp_issues ai,
       amp_actor act
  where ai.amp_activity_id = a.amp_activity_id and
        ai.amp_issue_id = m.amp_issue_id and
        act.amp_measure_id = m.amp_measure_id
  order by ai.amp_activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_ACTUAL_APPROVAL_DATE" ("AMP_ACTIVITY_ID", "ACTUAL_APPROVAL_DATE") AS 
  select amp_activity.amp_activity_id AS amp_activity_id,
         amp_activity.actual_approval_date AS actual_approval_date
  from amp_activity
  order by amp_activity.amp_activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_ACTUAL_COMPLETION_DATE" ("AMP_ACTIVITY_ID", "ACTUAL_COMPLETION_DATE") AS 
  select amp_activity.amp_activity_id AS amp_activity_id,
         TO_CHAR(amp_activity.actual_completion_date, 'YYYY-MM-DD') AS
          actual_completion_date
  from amp_activity
  order by amp_activity.amp_activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_ACTUAL_PROPOSED_DATE" ("AMP_ACTIVITY_ID", "PROPOSED_APPROVAL_DATE") AS 
  select amp_activity.amp_activity_id AS amp_activity_id,
         amp_activity.proposed_approval_date AS proposed_approval_date
  from amp_activity
  order by amp_activity.amp_activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_ACTUAL_START_DATE" ("AMP_ACTIVITY_ID", "ACTUAL_START_DATE") AS 
  select amp_activity.amp_activity_id AS amp_activity_id,
         TO_CHAR(amp_activity.actual_start_date, 'YYYY-MM-DD') AS
          actual_start_date
  from amp_activity
  order by amp_activity.amp_activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_AC_CHAPTERS" ("AMP_ACTIVITY_ID", "NAME") AS 
  select acc.amp_activity_id AS amp_activity_id,
       acv.category_value AS name
  from amp_activities_categoryvalues acc,
       amp_category_value acv,
       amp_category_class ac
  where acv.id = acc.amp_categoryvalue_id and
        ac.id = acv.amp_category_class_id and
        ac.keyName = 'acchapter'
/
CREATE OR REPLACE FORCE VIEW  "V_AMPID" ("AMP_ACTIVITY_ID", "AMP_ID") AS 
  select amp_activity.amp_activity_id AS amp_activity_id,
         amp_activity.amp_id AS amp_id
  from amp_activity
  order by amp_activity.amp_activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_AMP_ID" ("AMP_ACTIVITY_ID", "AMP_ID") AS 
  select amp_activity.amp_activity_id AS amp_activity_id,
         amp_activity.amp_id AS amp_id
  from amp_activity
/
CREATE OR REPLACE FORCE VIEW  "V_AMP_THEME" ("ID", "VALUE") AS 
  select amp_theme.amp_theme_id AS id,
         amp_theme.name AS value
  from amp_theme
/
CREATE OR REPLACE FORCE VIEW  "V_BENEFICIARY_AGENCY" ("AMP_ACTIVITY_ID", "NAME", "AMP_ORG_ID") AS 
  select f.activity AS amp_activity_id,
         o.name AS name,
         f.organisation AS amp_org_id
  from amp_org_role f,
       amp_organisation o,
       amp_role r
  where f.organisation = o.amp_org_id and
        f.role = r.amp_role_id and
        r.role_code = 'BA'
  order by f.activity,
           o.name
/
CREATE OR REPLACE FORCE VIEW  "V_BENEFICIARY_AGENCY_GROUPS" ("AMP_ACTIVITY_ID", "NAME", "AMP_ORG_GRP_ID") AS 
  select f.activity AS amp_activity_id,
         b.org_grp_name AS name,
         b.amp_org_grp_id AS amp_org_grp_id
  from amp_org_role f,
       amp_organisation o,
       amp_role r,
       amp_org_group b
  where f.organisation = o.amp_org_id and
        f.role = r.amp_role_id and
        r.role_code = 'BA' and
        o.org_grp_id = b.amp_org_grp_id
  order by f.activity,
           o.name
/
CREATE OR REPLACE FORCE VIEW  "V_BOLIVIA_COMPONENT_CODE" ("AMP_ACTIVITY_ID", "CODE") AS 
  select a.amp_activity_id AS amp_activity_id,
         c.code AS code
  from amp_activity_components a,
       amp_components c
  where a.amp_component_id = c.amp_component_id
  order by a.amp_activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_COMPONENTE" ("AMP_ACTIVITY_ID", "NAME", "AMP_SECTOR_ID", "PERCENTAGE") AS 
  select sa.amp_activity_id AS amp_activity_id,
         s.name AS name,
         sa.amp_sector_id AS amp_sector_id,
         sa.percentage AS percentage
  from amp_activity_componente sa,
       amp_sector s
  where sa.amp_sector_id = s.amp_sector_id
  order by sa.amp_activity_id,
           s.name
/
CREATE OR REPLACE FORCE VIEW  "V_COMPONENTS" ("AMP_ACTIVITY_ID", "TITLE", "AMP_COMPONENT_ID") AS 
  select distinct a.amp_activity_id AS amp_activity_id,
         b.title AS title,
         b.amp_component_id AS amp_component_id
  from amp_activity a,
       amp_components b,
       amp_component_funding c
  where a.amp_activity_id = c.activity_id and
        b.amp_component_id = c.amp_component_id
/
CREATE OR REPLACE FORCE VIEW  "V_COMPONENT_DESCRIPTION" ("AMP_ACTIVITY_ID", "TITLE", "AMP_COMPONENT_ID") AS 
  select distinct a.amp_activity_id AS amp_activity_id,
         b.description AS title,
         b.amp_component_id AS amp_component_id
  from amp_activity a,
       amp_components b,
       amp_component_funding c
  where a.amp_activity_id = c.activity_id and
        b.amp_component_id = c.amp_component_id
/
CREATE OR REPLACE FORCE VIEW  "V_COMPONENT_FUNDING" ("AMP_ACTIVITY_ID", "AMP_COMPONENT_FUNDING_ID", "AMP_FUND_DETAIL_ID", "COMPONENT_NAME", "TRANSACTION_TYPE", "ADJUSTMENT_TYPE", "TRANSACTION_DATE", "TRANSACTION_AMOUNT", "CURRENCY_ID", "CURRENCY_CODE") AS 
  select f.activity_id AS amp_activity_id,
         f.amp_component_funding_id AS amp_component_funding_id,
         f.amp_component_funding_id AS amp_fund_detail_id,
         c.title AS component_name,
         f.transaction_type AS transaction_type,
         f.adjustment_type AS adjustment_type,
         f.transaction_date AS transaction_date,
         f.transaction_amount AS transaction_amount,
         f.currency_id AS currency_id,
         cu.currency_code AS currency_code
  from amp_component_funding f,
       amp_components c,
       amp_currency cu
  where cu.amp_currency_id = f.currency_id and
        f.amp_component_id = c.amp_component_id
  order by f.activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_COMPONENT_TYPE" ("AMP_ACTIVITY_ID", "COMPONENT_TYPE", "COMPONENT_TYPE_ID") AS 
  select 
    aac.amp_activity_id AS amp_activity_id,
    ct.name AS component_type,
    ct.type_id AS component_type_id 
  from 
    amp_components c ,amp_component_type ct , amp_activity_components aac 
  where 
    c.type = ct.type_id and aac.amp_component_id = c.amp_component_id
/
CREATE OR REPLACE FORCE VIEW  "V_CONTACT_NAME" ("AMP_ACTIVITY_ID", "CONTACT", "CONTACT_ID") AS 
  select amp_activity.amp_activity_id AS amp_activity_id,
         ' '         || amp_activity.cont_first_name || ' ' ||
          amp_activity.cont_last_name AS contact,
         amp_activity.amp_activity_id AS contact_id
        from amp_activity
        where amp_activity.cont_first_name || amp_activity.cont_last_name <> ''
        union
        select amp_activity.amp_activity_id AS amp_activity_id,
               ' '               || amp_activity.mofed_cnt_first_name || ' ' ||
                amp_activity.mofed_cnt_last_name AS contact,
               amp_activity.amp_activity_id AS contact_id
        from amp_activity
        where trim(' ' || amp_activity.mofed_cnt_first_name || ' ' ||
         amp_activity.mofed_cnt_last_name) <> ''
/
CREATE OR REPLACE FORCE VIEW  "V_CONTRACTING_AGENCY" ("AMP_ACTIVITY_ID", "NAME", "AMP_ORG_ID") AS 
  select f.activity AS amp_activity_id,
               o.name AS name,
               f.organisation AS amp_org_id
  from amp_org_role f,
       amp_organisation o,
       amp_role r
  where f.organisation = o.amp_org_id and
        f.role = r.amp_role_id and
        r.role_code = 'CA'
  order by f.activity,
           o.name
/
CREATE OR REPLACE FORCE VIEW  "V_CONTRIBUTION_FUNDING" ("AMP_ACTIVITY_ID", "AMP_FUNDING_ID", "AMP_FUNDING_DETAIL_ID", "DONOR_NAME", "TRANSACTION_AMOUNT", "TRANSACTION_DATE", "CURRENCY_CODE", "TERMS_ASSIST_NAME", "FINANCING_INSTRUMENT_NAME", "AMP_ORG_ID", "ORG_GRP_ID", "TERMS_ASSIST_ID") AS 
  select eu.amp_activity_id AS amp_activity_id,
         eu.id AS amp_funding_id,
         euc.id AS amp_funding_detail_id,
         o.name AS donor_name,
         euc.amount AS transaction_amount,
         euc.transaction_date AS transaction_date,
         c.currency_code AS currency_code,
         acv_term.category_value AS terms_assist_name,
         acv_mod.category_value AS financing_instrument_name,
         o.amp_org_id AS amp_org_id,
         o.org_grp_id AS org_grp_id,
         acv_term.id AS terms_assist_id
  from amp_eu_activity eu,
       amp_eu_activity_contributions euc,
       amp_currency c,
       amp_category_value acv_term,
       amp_category_value acv_mod,
       amp_organisation o
  where eu.id = euc.eu_activity_id and
        euc.amount_currency = c.amp_currency_id and
        acv_term.id = euc.financing_type_categ_val_id and
        acv_mod.id = euc.FINANCING_INSTR_CATEGORY_VALUE and
        o.amp_org_id = euc.donor_id
  order by eu.amp_activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_CONVENIO_NUMCONT" ("AMP_ACTIVITY_ID", "NUMCONT") AS 
  select amp_activity.amp_activity_id AS amp_activity_id,
         case amp_activity.convenio_numcont when ' -' then NULL when '.' then
          NULL else amp_activity.convenio_numcont end AS numcont
  from amp_activity
  order by amp_activity.amp_activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_COSTING_DONORS" ("AMP_ACTIVITY_ID", "NAME", "DONOR_ID") AS 
  select eu.amp_activity_id AS amp_activity_id,
         o.name AS name,
         euc.donor_id AS donor_id
  from amp_activity a,
       amp_eu_activity eu,
       amp_eu_activity_contributions euc,
       amp_organisation o
  where a.amp_activity_id = eu.amp_activity_id and
        eu.id = euc.eu_activity_id and
        euc.donor_id = o.amp_org_id
  order by o.name
/
CREATE OR REPLACE FORCE VIEW  "V_COSTS" ("AMP_ACTIVITY_ID", "EU_ID", "COST", "CURRENCY_CODE", "CURRENCY_DATE", "EXCHANGE_RATE") AS 
  select eu.amp_activity_id AS amp_activity_id,
         eu.id AS eu_id,
         eu.total_cost AS cost,
         c.currency_code AS currency_code,
         eu.transaction_date AS currency_date,
         getExchange(c.currency_code, eu.transaction_date) AS exchange_rate
  from amp_eu_activity eu,
       amp_currency c
  where eu.total_cost_currency_id = c.amp_currency_id
/
CREATE OR REPLACE FORCE VIEW  "V_CREATION_DATE" ("AMP_ACTIVITY_ID", "CREATION_DATE") AS 
  select a.amp_activity_id AS amp_activity_id,
         a.DATE_CREATED AS creation_date
  from amp_activity a
  order by a.amp_activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_CREDIT_DONATION" ("AMP_ACTIVITY_ID", "NAME", "ID") AS 
  select v.amp_activity_id AS amp_activity_id,

          case  v.name
          when   'Donacion'  then  'Donacion'
          else
           'Credito' end AS name,
          case when  v.name  = 'Donacion'  then 0 else 1 end  AS id
         from v_financing_instrument v
/
CREATE OR REPLACE FORCE VIEW  "V_DATE_FORMATS" ("ID", "VALUE") AS 
  select a.value_id AS id,
       a.value_shown AS value
  from UTIL_GLOBAL_SETTINGS_POSSIBLE_ a
  where a.setting_name =
   'Default Date Format'
/
CREATE OR REPLACE FORCE VIEW  "V_DEFAULT_NUMBER_FORMAT" ("ID", "VALUE") AS 
  select a.value_id AS id,
         a.value_shown AS value
  from UTIL_GLOBAL_SETTINGS_POSSIBLE_ a
  where a.setting_name =
   'Default Number Format'
/
CREATE OR REPLACE FORCE VIEW  "V_DESCRIPTION" ("AMP_ACTIVITY_ID", "EBODY") AS 
  select distinct amp_activity.amp_activity_id AS amp_activity_id,
         trim(dg_editor.BODY) as ebody
  from amp_activity,
       dg_editor
  where amp_activity.description = dg_editor.EDITOR_KEY
  order by amp_activity.amp_activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_DONORS" ("AMP_ACTIVITY_ID", "NAME", "AMP_DONOR_ORG_ID", "ORG_GRP_ID", "ORG_TYPE_ID") AS 
  select f.amp_activity_id AS amp_activity_id,
         o.name AS name,
         f.amp_donor_org_id AS amp_donor_org_id,
         o.org_grp_id AS org_grp_id,
         o.org_type_id AS org_type_id
  from amp_funding f,
       amp_organisation o
  where f.amp_donor_org_id = o.amp_org_id
  order by f.amp_activity_id,
           o.name
/
CREATE OR REPLACE FORCE VIEW  "V_DONOR_COMMITMENT_DATE" ("AMP_ACTIVITY_ID", "TRANSACTION_DATE") AS 
  select f.amp_activity_id AS amp_activity_id,
         fd.transaction_date AS transaction_date
  from amp_funding f,
       amp_funding_detail fd
  where f.amp_funding_id = fd.amp_funding_id and
        fd.transaction_type = 0
  order by
     f.amp_activity_id , fd.transaction_date
/
CREATE OR REPLACE FORCE VIEW  "V_DONOR_DATE_HIERARCHY" ("AMP_ACTIVITY_ID", "AMP_FUND_DETAIL_ID", "FULL_DATE", "year", "month", "MONTH_NAME", "QUARTER", "QUARTER_NAME") AS 
  select 
    a.amp_activity_id AS amp_activity_id,
    fd.amp_fund_detail_id AS amp_fund_detail_id,
    fd.transaction_date AS full_date,
    to_char(fd.transaction_date,'YYYY') AS "year",
    to_char(fd.transaction_date,'MM') 	AS "month",
    to_char(fd.transaction_date,'MONTH') AS month_name,
 	   case 
    when to_char(fd.transaction_date,'MM') < 4   then 1 
    when to_char(fd.transaction_date,'MM') > 3 	and   to_char(fd.transaction_date,'MM') < 7 then 2 
    when to_char(fd.transaction_date,'MM') > 6 	and to_char(fd.transaction_date,'MM') <  10 then 3
    when to_char(fd.transaction_date,'MM') > 9  then 4  
    
    end as quarter,
    
    case 
    when to_char(fd.transaction_date,'MM') < 4   then 'Q1' 
    when to_char(fd.transaction_date,'MM') > 3 and   to_char(fd.transaction_date,'MM') < 7 then 'Q2'
    when to_char(fd.transaction_date,'MM') > 6 and to_char(fd.transaction_date,'MM') <  10 then 'Q3'
    when to_char(fd.transaction_date,'MM') > 9  then 'Q4' 
    
    end as quarter_name
    
    from 
    amp_activity a,
    amp_funding f ,
    amp_funding_detail fd 
  where 
    a.amp_activity_id = f.amp_activity_id and f.amp_funding_id = fd.amp_funding_id
/
CREATE OR REPLACE FORCE VIEW  "V_DONOR_FUNDING" ("AMP_ACTIVITY_ID", "AMP_FUNDING_ID", "AMP_FUND_DETAIL_ID", "DONOR_NAME", "TRANSACTION_TYPE", "ADJUSTMENT_TYPE", "TRANSACTION_DATE", "TRANSACTION_AMOUNT", "CURRENCY_CODE", "TERMS_ASSIST_ID", "TERMS_ASSIST_NAME", "FIXED_EXCHANGE_RATE", "ORG_GRP_NAME", "DONOR_TYPE_NAME", "FINANCING_INSTRUMENT_NAME", "FINANCING_INSTRUMENT_ID", "ORG_ID", "ORG_GRP_ID", "ORG_TYPE_ID") AS 
  select f.amp_activity_id AS amp_activity_id,
         f.amp_funding_id AS amp_funding_id,
         fd.amp_fund_detail_id AS amp_fund_detail_id,
         d.name AS donor_name,
         fd.transaction_type AS transaction_type,
         fd.adjustment_type AS adjustment_type,
         fd.transaction_date AS transaction_date,
         fd.transaction_amount AS transaction_amount,
         c.currency_code AS currency_code,
         cval.id AS terms_assist_id,
         cval.category_value AS terms_assist_name,
         fd.fixed_exchange_rate AS fixed_exchange_rate,
         b.org_grp_name AS org_grp_name,
         ot.org_type AS donor_type_name,
         cval2.category_value AS financing_instrument_name,
         cval2.id AS financing_instrument_id,
         d.amp_org_id AS org_id,
         d.org_grp_id AS org_grp_id,
         ot.amp_org_type_id AS org_type_id
  from  amp_funding f,
        amp_funding_detail fd,
        amp_category_value cval,
        amp_currency c,
        amp_organisation d,
        amp_org_group b,
        amp_org_type ot,
        amp_category_value cval2
  where 
         cval2.id = f.financing_instr_category_value and
        c.amp_currency_id = fd.amp_currency_id and
        f.amp_funding_id = fd.amp_funding_id and
        cval.id = f.type_of_assistance_category_va and
        d.amp_org_id = f.amp_donor_org_id and
        d.org_grp_id = b.amp_org_grp_id and
        ot.amp_org_type_id = d.org_type_id
  order by f.amp_activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_DONOR_GROUPS" ("AMP_ACTIVITY_ID", "NAME", "AMP_ORG_GRP_ID") AS 
  select a.amp_activity_id AS amp_activity_id,
         b.org_grp_name AS name,
         b.amp_org_grp_id AS amp_org_grp_id
  from amp_funding a,
       amp_organisation c,
       amp_org_group b
  where a.amp_donor_org_id = c.amp_org_id and
        c.org_grp_id = b.amp_org_grp_id
  order by
     a.amp_activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_DONOR_TYPE" ("AMP_ACTIVITY_ID", "ORG_TYPE", "ORG_TYPE_ID") AS 
  select f.amp_activity_id AS amp_activity_id,
         ot.org_type AS org_type,
         ot.amp_org_type_id AS org_type_id
  from amp_funding f,
       amp_organisation o,
       amp_org_type ot
  where f.amp_donor_org_id = o.amp_org_id and
        ot.amp_org_type_id = o.org_type_id
  order by
     f.amp_activity_id , ot.org_type
/
CREATE OR REPLACE FORCE VIEW  "V_EXECUTING_AGENCY" ("AMP_ACTIVITY_ID", "NAME", "AMP_ORG_ID", "PERCENTAGE") AS 
  select f.activity AS amp_activity_id,
         o.name AS name,
         f.organisation AS amp_org_id,
         f.percentage AS percentage
  from amp_org_role f,
       amp_organisation o,
       amp_role r where  f.organisation = o.amp_org_id and f.role = r.amp_role_id
        and r.role_code = 'EA'
  order by
     f.activity , o.name
/
CREATE OR REPLACE FORCE VIEW  "V_EXECUTING_AGENCY_GROUPS" ("AMP_ACTIVITY_ID", "NAME", "AMP_ORG_GRP_ID") AS 
  select f.activity AS amp_activity_id,
         b.org_grp_name AS name,
         b.amp_org_grp_id AS amp_org_grp_id
  from amp_org_role f,
       amp_organisation o,
       amp_role r,
       amp_org_group b

       where f.organisation = o.amp_org_id and f.role =
        r.amp_role_id and r.role_code = 'EA' and o.org_grp_id = b.amp_org_grp_id

  order by
     f.activity , o.name
/
CREATE OR REPLACE FORCE VIEW  "V_FINANCING_INSTRUMENT" ("AMP_ACTIVITY_ID", "NAME", "AMP_MODALITY_ID") AS 
  select f.amp_activity_id AS amp_activity_id,
         val.category_value AS name,
         f.FINANCING_INSTR_CATEGORY_VALUE AS amp_modality_id
  from amp_funding f
       ,amp_category_value val
  where
    f.FINANCING_INSTR_CATEGORY_VALUE = val.id

     union
  select eu.amp_activity_id AS amp_activity_id,
         val.category_value AS category_value,
         eu_con.FINANCING_INSTR_CATEGORY_VALUE
  from amp_eu_activity eu,
       amp_eu_activity_contributions eu_con,
       amp_category_value val
  where  eu_con.eu_activity_id = eu.id  and
         eu_con.FINANCING_INSTR_CATEGORY_VALUE = val.id
  order by
    amp_activity_id,name
/
CREATE OR REPLACE FORCE VIEW  "V_G _SETTINGS_COUNTRIES" ("ID", "VALUE") AS 
  select dg_countries.ISO AS id,
         dg_countries.COUNTRY_NAME AS "VALUE"
  from dg_countries
  order by dg_countries.COUNTRY_NAME
/
CREATE OR REPLACE FORCE VIEW  "V_G_SETTINGS_CURR_FISCAL_YEAR" ("ID", "VALUE") AS 
  select UTIL_GLOBAL_SETTINGS_POSSIBLE_.value_id AS id,
         UTIL_GLOBAL_SETTINGS_POSSIBLE_.value_shown AS "VALUE"
  from UTIL_GLOBAL_SETTINGS_POSSIBLE_
  where UTIL_GLOBAL_SETTINGS_POSSIBLE_.setting_name = 'Current Fiscal Year'
/
CREATE OR REPLACE FORCE VIEW  "V_G_SETTINGS_DEFAULT_CALENDAR" ("ID", "VALUE") AS 
  select amp_fiscal_calendar.amp_fiscal_cal_id AS id,
         amp_fiscal_calendar.name AS value
  from amp_fiscal_calendar
/
CREATE OR REPLACE FORCE VIEW  "V_G_SETTINGS_DEF_COMP_TYPE" ("ID", "VALUE") AS 
  select amp_component_type.type_id AS id,
         amp_component_type.name AS value
  from amp_component_type
  where amp_component_type.enable = 1
/
CREATE OR REPLACE FORCE VIEW  "V_G_SETTINGS_FEATURE_TEMPLATES" ("ID", "VALUE") AS 
  select amp_feature_templates.id AS id,
         amp_feature_templates.featureTemplateName AS value
  from amp_feature_templates
/
CREATE OR REPLACE FORCE VIEW  "V_G_SETTINGS_FILTER_REPORTS" ("ID", "VALUE") AS 
  select UTIL_GLOBAL_SETTINGS_POSSIBLE_.value_id AS id,
         UTIL_GLOBAL_SETTINGS_POSSIBLE_.value_shown AS value
  from UTIL_GLOBAL_SETTINGS_POSSIBLE_
  where UTIL_GLOBAL_SETTINGS_POSSIBLE_.setting_name = 'Filter reports by month'
/
CREATE OR REPLACE FORCE VIEW  "V_G_SETTINGS_PUBLIC_VIEW" ("ID", "VALUE") AS 
  select UTIL_GLOBAL_SETTINGS_POSSIBLE_.value_id AS id,
         UTIL_GLOBAL_SETTINGS_POSSIBLE_.value_shown AS "VALUE"
  from UTIL_GLOBAL_SETTINGS_POSSIBLE_
  where UTIL_GLOBAL_SETTINGS_POSSIBLE_.setting_name = 'Public View'
/
CREATE OR REPLACE FORCE VIEW  "V_G_SETTINGS_PV_BUDGET_FILTER" ("ID", "VALUE") AS 
  select UTIL_GLOBAL_SETTINGS_POSSIBLE_.value_id AS id,
         UTIL_GLOBAL_SETTINGS_POSSIBLE_.value_shown AS value
  from UTIL_GLOBAL_SETTINGS_POSSIBLE_
  where UTIL_GLOBAL_SETTINGS_POSSIBLE_.setting_name =
  'Public View Budget Filter'
/
CREATE OR REPLACE FORCE VIEW  "V_G_SETTINGS_SECTOR_SCHEMES" ("ID", "VALUE") AS 
  select amp_sector_scheme.amp_sec_scheme_id AS id,
         amp_sector_scheme.sec_scheme_name AS value
  from amp_sector_scheme
  order by amp_sector_scheme.sec_scheme_name
/
CREATE OR REPLACE FORCE VIEW  "V_G_SETTINGS_SH_COMP_FUND_YEAR" ("ID", "VALUE") AS 
  select UTIL_GLOBAL_SETTINGS_POSSIBLE_.value_id AS id,
         UTIL_GLOBAL_SETTINGS_POSSIBLE_.value_shown AS "VALUE"
  from UTIL_GLOBAL_SETTINGS_POSSIBLE_
  where
  UTIL_GLOBAL_SETTINGS_POSSIBLE_.setting_name ='Show Component Funding by Year'
/
CREATE OR REPLACE FORCE VIEW  "V_G_SETTINGS_TEMPL_VISIBILITY" ("ID", "VALUE") AS 
  select amp_templates_visibility.id AS id,
         amp_templates_visibility.name AS "VALUE"
  from amp_templates_visibility
/
CREATE OR REPLACE FORCE VIEW  "V_IMPLEMENTATION_LEVEL" ("AMP_ACTIVITY_ID", "NAME", "LEVEL_CODE") AS 
  select aac.amp_activity_id AS amp_activity_id,
         acv.category_value AS name,
         aac.amp_categoryvalue_id AS level_code
  from amp_category_value acv,
       amp_category_class acc,
       amp_activities_categoryvalues aac
  where acv.id = aac.amp_categoryvalue_id and
        acc.id = acv.amp_category_class_id and
        acc.keyName = 'implementation_level'
/
CREATE OR REPLACE FORCE VIEW  "V_IMPLEMENTATION_LOCATION" ("AMP_ACTIVITY_ID", "NAME", "AMP_STATUS_ID") AS 
  select aac.amp_activity_id AS amp_activity_id,
         acv.category_value AS name,
         aac.amp_categoryvalue_id AS amp_status_id
  from amp_category_value acv,
       amp_category_class acc,
       amp_activities_categoryvalues aac
  where acv.id = aac.amp_categoryvalue_id and
        acc.id = acv.amp_category_class_id and
        acc.keyName = 'implementation_location'
/
CREATE OR REPLACE FORCE VIEW  "V_IMPLEMENTING_AGENCY" ("AMP_ACTIVITY_ID", "NAME", "AMP_ORG_ID") AS 
  select f.activity AS amp_activity_id,
         o.name AS name,
         f.organisation AS amp_org_id
  from amp_org_role f,
       amp_organisation o,
       amp_role r where
        f.organisation = o.amp_org_id and f.role = r.amp_role_id
        and r.role_code = 'IA'
  order by
     f.activity , o.name
/
CREATE OR REPLACE FORCE VIEW  "V_IMPLEMENTING_AGENCY_GROUPS" ("AMP_ACTIVITY_ID", "NAME", "AMP_ORG_GRP_ID") AS 
  select f.activity AS amp_activity_id,
         b.org_grp_name AS name,
         b.amp_org_grp_id AS amp_org_grp_id
  from amp_org_role f,
       amp_organisation o,
       amp_role r,
       amp_org_group b where f.organisation = o.amp_org_id and f.role =
        r.amp_role_id and r.role_code = 'IA' and o.org_grp_id = b.amp_org_grp_id

  order by
     f.activity , o.name
/
CREATE OR REPLACE FORCE VIEW  "V_ISSUES" ("AMP_ACTIVITY_ID", "NAME", "AMP_ISSUE_ID") AS 
  select ai.amp_activity_id AS amp_activity_id,
         ai.name AS name,
         ai.amp_issue_id AS amp_issue_id
  from amp_issues ai,
       amp_activity a
  where ai.amp_activity_id = a.amp_activity_id
  order by ai.amp_activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_MEASURES_TAKEN" ("AMP_ACTIVITY_ID", "NAME", "AMP_MEASURE_ID") AS 
  select ai.amp_activity_id AS amp_activity_id,
         m.name AS name,
         m.amp_measure_id AS amp_measure_id
  from amp_activity a,
       amp_measure m,
       amp_issues ai
  where ai.amp_activity_id = a.amp_activity_id and
        ai.amp_issue_id = m.amp_issue_id
  order by
     ai.amp_activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_NATIONALOBJECTIVES" ("AMP_ACTIVITY_ID", "NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select a.amp_activity_id AS amp_activity_id,
         t.name AS "NAME",
         ap.amp_program_id AS amp_program_id,
         ap.program_percentage AS program_percentage
  from amp_activity a,
       AMP_ACTIVITY_PROGRAM ap  , amp_theme   t

       where
        a.amp_activity_id = ap.amp_activity_id
        and ap.program_setting = 1
        and t.amp_theme_id  = ap.amp_program_id
        and t.parent_theme_id  is not null
        and  t.LEVEL_= 2
         order by  a.amp_activity_id , t.name
/
CREATE OR REPLACE FORCE VIEW  "V_NATIONALOBJECTIVES_ALL_LEVEL" ("AMP_ACTIVITY_ID", "PROGRAM_PERCENTAGE", "AMP_PROGRAM_ID", "N1", "L1", "N2", "L2", "N3", "L3", "N4", "L4", "N5", "L5", "N6", "L6", "N7", "L7", "N8", "L8") AS 
  select
    a.amp_activity_id AS amp_activity_id,
    a.program_percentage AS program_percentage,
    a.amp_program_id AS amp_program_id,
    b."NAME" AS n1,
    b.LEVEL_ AS l1,
    b1."NAME" AS n2,
    b1.LEVEL_ AS l2,
    b2."NAME" AS n3,
    b2.LEVEL_ AS l3,
    b3."NAME" AS n4,
    b3.LEVEL_ AS l4,
    b4."NAME" AS n5,
    b4.LEVEL_ AS l5,
    b5."NAME" AS n6,
    b5.LEVEL_ AS l6,
    b6."NAME" AS n7,
    b6.LEVEL_ AS l7,
    b7."NAME" AS n8,
    b7.LEVEL_  AS l8
  from
    amp_activity_program a
    join amp_theme b  on (a.amp_program_id = b.amp_theme_id)
    left join amp_theme b1 on (b1.amp_theme_id = b.parent_theme_id)
    left join amp_theme b2 on (b2.amp_theme_id = b1.parent_theme_id )
     left join amp_theme b3 on (b3.amp_theme_id = b2.parent_theme_id)
      left join amp_theme b4 on (b4.amp_theme_id = b3.parent_theme_id)
      left join amp_theme b5 on (b5.amp_theme_id = b4.parent_theme_id )
      left join amp_theme b6 on (b6.amp_theme_id = b5.parent_theme_id)
      left join amp_theme b7 on (b7.amp_theme_id = b6.parent_theme_id)
/
CREATE OR REPLACE FORCE VIEW  "V_NATIONALOBJECTIVES_LEVEL_0" ("AMP_ACTIVITY_ID", "NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select amp_activity_id,"NAME",amp_program_id,program_percentage from
   ( select
    a.amp_activity_id AS amp_activity_id,
    case 0
    when a.l1 then a.n1
    when a.l2 then a.n2
    when a.l3 then a.n3
    when a.l4 then a.n4
    when a.l5 then a.n5
    when a.l6 then a.n6
    when a.l7 then a.n7
    when a.l8 then a.n8
    end
    AS "NAME",
    a.amp_program_id AS amp_program_id,
    a.program_percentage AS program_percentage
  from
    v_nationalobjectives_all_level a) t1 where t1."NAME" is not null
/
CREATE OR REPLACE FORCE VIEW  "V_NATIONALOBJECTIVES_LEVEL_1" ("AMP_ACTIVITY_ID", "NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select amp_activity_id,"NAME",amp_program_id,program_percentage from
   ( select v_nationalobjectives_all_level.amp_activity_id AS
   amp_activity_id,
         case 1 when v_nationalobjectives_all_level.l1 then
          v_nationalobjectives_all_level.n1 when
           v_nationalobjectives_all_level.l2 then
            v_nationalobjectives_all_level.n2 when
             v_nationalobjectives_all_level.l3 then
              v_nationalobjectives_all_level.n3 when
               v_nationalobjectives_all_level.l4 then
                v_nationalobjectives_all_level.n4 when
                 v_nationalobjectives_all_level.l5 then
                  v_nationalobjectives_all_level.n5 when
                   v_nationalobjectives_all_level.l6 then
                    v_nationalobjectives_all_level.n6 when
                     v_nationalobjectives_all_level.l7 then
                      v_nationalobjectives_all_level.n7 when
                       v_nationalobjectives_all_level.l8 then
                        v_nationalobjectives_all_level.n8 end AS "NAME",
         v_nationalobjectives_all_level.amp_program_id AS amp_program_id,
         v_nationalobjectives_all_level.program_percentage AS
          program_percentage
  from v_nationalobjectives_all_level) t1 where t1."NAME" is not null
/
CREATE OR REPLACE FORCE VIEW  "V_NATIONALOBJECTIVES_LEVEL_2" ("AMP_ACTIVITY_ID", "NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select amp_activity_id,"NAME",amp_program_id,program_percentage from
   (select v_nationalobjectives_all_level.amp_activity_id AS
   amp_activity_id,
         case 2 when v_nationalobjectives_all_level.l1 then
          v_nationalobjectives_all_level.n1 when
           v_nationalobjectives_all_level.l2 then
            v_nationalobjectives_all_level.n2 when
             v_nationalobjectives_all_level.l3 then
              v_nationalobjectives_all_level.n3 when
               v_nationalobjectives_all_level.l4 then
                v_nationalobjectives_all_level.n4 when
                 v_nationalobjectives_all_level.l5 then
                  v_nationalobjectives_all_level.n5 when
                   v_nationalobjectives_all_level.l6 then
                    v_nationalobjectives_all_level.n6 when
                     v_nationalobjectives_all_level.l7 then
                      v_nationalobjectives_all_level.n7 when
                       v_nationalobjectives_all_level.l8 then
                        v_nationalobjectives_all_level.n8 end AS "NAME",
         v_nationalobjectives_all_level.amp_program_id AS amp_program_id,
         v_nationalobjectives_all_level.program_percentage AS
          program_percentage
  from v_nationalobjectives_all_level) t1 where t1."NAME" is not null
/
CREATE OR REPLACE FORCE VIEW  "V_NATIONALOBJECTIVES_LEVEL_3" ("AMP_ACTIVITY_ID", "NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select amp_activity_id,"NAME",amp_program_id,program_percentage from
   (select v_nationalobjectives_all_level.amp_activity_id AS
   amp_activity_id,
         case 3 when v_nationalobjectives_all_level.l1 then
          v_nationalobjectives_all_level.n1 when
           v_nationalobjectives_all_level.l2 then
            v_nationalobjectives_all_level.n2 when
             v_nationalobjectives_all_level.l3 then
              v_nationalobjectives_all_level.n3 when
               v_nationalobjectives_all_level.l4 then
                v_nationalobjectives_all_level.n4 when
                 v_nationalobjectives_all_level.l5 then
                  v_nationalobjectives_all_level.n5 when
                   v_nationalobjectives_all_level.l6 then
                    v_nationalobjectives_all_level.n6 when
                     v_nationalobjectives_all_level.l7 then
                      v_nationalobjectives_all_level.n7 when
                       v_nationalobjectives_all_level.l8 then
                        v_nationalobjectives_all_level.n8 end AS "NAME",
         v_nationalobjectives_all_level.amp_program_id AS amp_program_id,
         v_nationalobjectives_all_level.program_percentage AS
          program_percentage
  from v_nationalobjectives_all_level) t1 where t1."NAME" is not null
/
CREATE OR REPLACE FORCE VIEW  "V_NATIONALOBJECTIVES_LEVEL_4" ("AMP_ACTIVITY_ID", "NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select amp_activity_id,"NAME",amp_program_id,program_percentage from
   (select v_nationalobjectives_all_level.amp_activity_id AS
   amp_activity_id,
         case 4 when v_nationalobjectives_all_level.l1 then
          v_nationalobjectives_all_level.n1 when
           v_nationalobjectives_all_level.l2 then
            v_nationalobjectives_all_level.n2 when
             v_nationalobjectives_all_level.l3 then
              v_nationalobjectives_all_level.n3 when
               v_nationalobjectives_all_level.l4 then
                v_nationalobjectives_all_level.n4 when
                 v_nationalobjectives_all_level.l5 then
                  v_nationalobjectives_all_level.n5 when
                   v_nationalobjectives_all_level.l6 then
                    v_nationalobjectives_all_level.n6 when
                     v_nationalobjectives_all_level.l7 then
                      v_nationalobjectives_all_level.n7 when
                       v_nationalobjectives_all_level.l8 then
                        v_nationalobjectives_all_level.n8 end AS "NAME",
         v_nationalobjectives_all_level.amp_program_id AS amp_program_id,
         v_nationalobjectives_all_level.program_percentage AS
          program_percentage
  from v_nationalobjectives_all_level) t1 where t1."NAME" is not null
/
CREATE OR REPLACE FORCE VIEW  "V_NATIONALOBJECTIVES_LEVEL_5" ("AMP_ACTIVITY_ID", "NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select amp_activity_id,"NAME",amp_program_id,program_percentage from
   (select v_nationalobjectives_all_level.amp_activity_id AS
   amp_activity_id,
         case 5 when v_nationalobjectives_all_level.l1 then
          v_nationalobjectives_all_level.n1 when
           v_nationalobjectives_all_level.l2 then
            v_nationalobjectives_all_level.n2 when
             v_nationalobjectives_all_level.l3 then
              v_nationalobjectives_all_level.n3 when
               v_nationalobjectives_all_level.l4 then
                v_nationalobjectives_all_level.n4 when
                 v_nationalobjectives_all_level.l5 then
                  v_nationalobjectives_all_level.n5 when
                   v_nationalobjectives_all_level.l6 then
                    v_nationalobjectives_all_level.n6 when
                     v_nationalobjectives_all_level.l7 then
                      v_nationalobjectives_all_level.n7 when
                       v_nationalobjectives_all_level.l8 then
                        v_nationalobjectives_all_level.n8 end AS "NAME",
         v_nationalobjectives_all_level.amp_program_id AS amp_program_id,
         v_nationalobjectives_all_level.program_percentage AS
          program_percentage
  from v_nationalobjectives_all_level) t1 where t1."NAME" is not null
/
CREATE OR REPLACE FORCE VIEW  "V_NATIONALOBJECTIVES_LEVEL_6" ("AMP_ACTIVITY_ID", "NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select amp_activity_id,"NAME",amp_program_id,program_percentage from
   (select v_nationalobjectives_all_level.amp_activity_id AS
   amp_activity_id,
         case 6 when v_nationalobjectives_all_level.l1 then
          v_nationalobjectives_all_level.n1 when
           v_nationalobjectives_all_level.l2 then
            v_nationalobjectives_all_level.n2 when
             v_nationalobjectives_all_level.l3 then
              v_nationalobjectives_all_level.n3 when
               v_nationalobjectives_all_level.l4 then
                v_nationalobjectives_all_level.n4 when
                 v_nationalobjectives_all_level.l5 then
                  v_nationalobjectives_all_level.n5 when
                   v_nationalobjectives_all_level.l6 then
                    v_nationalobjectives_all_level.n6 when
                     v_nationalobjectives_all_level.l7 then
                      v_nationalobjectives_all_level.n7 when
                       v_nationalobjectives_all_level.l8 then
                        v_nationalobjectives_all_level.n8 end AS "NAME",
         v_nationalobjectives_all_level.amp_program_id AS amp_program_id,
         v_nationalobjectives_all_level.program_percentage AS
          program_percentage
  from v_nationalobjectives_all_level) t1 where t1."NAME" is not null
/
CREATE OR REPLACE FORCE VIEW  "V_NATIONALOBJECTIVES_LEVEL_7" ("AMP_ACTIVITY_ID", "NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select amp_activity_id,"NAME",amp_program_id,program_percentage from
   (select v_nationalobjectives_all_level.amp_activity_id AS
   amp_activity_id,
         case 7 when v_nationalobjectives_all_level.l1 then
          v_nationalobjectives_all_level.n1 when
           v_nationalobjectives_all_level.l2 then
            v_nationalobjectives_all_level.n2 when
             v_nationalobjectives_all_level.l3 then
              v_nationalobjectives_all_level.n3 when
               v_nationalobjectives_all_level.l4 then
                v_nationalobjectives_all_level.n4 when
                 v_nationalobjectives_all_level.l5 then
                  v_nationalobjectives_all_level.n5 when
                   v_nationalobjectives_all_level.l6 then
                    v_nationalobjectives_all_level.n6 when
                     v_nationalobjectives_all_level.l7 then
                      v_nationalobjectives_all_level.n7 when
                       v_nationalobjectives_all_level.l8 then
                        v_nationalobjectives_all_level.n8 end AS "NAME",
         v_nationalobjectives_all_level.amp_program_id AS amp_program_id,
         v_nationalobjectives_all_level.program_percentage AS
          program_percentage
  from v_nationalobjectives_all_level) t1 where t1."NAME" is not null
/
CREATE OR REPLACE FORCE VIEW  "V_NATIONALOBJECTIVES_LEVEL_8" ("AMP_ACTIVITY_ID", "NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select amp_activity_id,"NAME",amp_program_id,program_percentage from
   (select v_nationalobjectives_all_level.amp_activity_id AS
   amp_activity_id,
         case 8 when v_nationalobjectives_all_level.l1 then
          v_nationalobjectives_all_level.n1 when
           v_nationalobjectives_all_level.l2 then
            v_nationalobjectives_all_level.n2 when
             v_nationalobjectives_all_level.l3 then
              v_nationalobjectives_all_level.n3 when
               v_nationalobjectives_all_level.l4 then
                v_nationalobjectives_all_level.n4 when
                 v_nationalobjectives_all_level.l5 then
                  v_nationalobjectives_all_level.n5 when
                   v_nationalobjectives_all_level.l6 then
                    v_nationalobjectives_all_level.n6 when
                     v_nationalobjectives_all_level.l7 then
                      v_nationalobjectives_all_level.n7 when
                       v_nationalobjectives_all_level.l8 then
                        v_nationalobjectives_all_level.n8 end AS "NAME",
         v_nationalobjectives_all_level.amp_program_id AS amp_program_id,
         v_nationalobjectives_all_level.program_percentage AS
          program_percentage
  from v_nationalobjectives_all_level) t1 where t1."NAME" is not null
/
CREATE OR REPLACE FORCE VIEW  "V_OBJECTIVES" ("AMP_ACTIVITY_ID", "EBODY") AS 
  select amp_activity.amp_activity_id AS amp_activity_id,
         trim (dg_editor.BODY) AS ebody
  from amp_activity,
       dg_editor
  where amp_activity.objectives = dg_editor.EDITOR_KEY
  order by amp_activity.amp_activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_ON_OFF_BUDGET" ("AMP_ACTIVITY_ID", "BUDGET") AS 
  select a.amp_activity_id AS amp_activity_id,
         case a.budget
         when '1' then 'yes' else  'no' end AS budget
  from amp_activity a
  order by a.amp_activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_ORGANIZATION_PROJECTID" ("AMP_ACTIVITY_ID", "TXT", "AMP_ORG_ID") AS 
  select aaii.amp_activity_id AS amp_activity_id,
          org.name ||' -- '||aaii.internal_id AS txt ,
         aaii.amp_org_id AS amp_org_id
  from amp_activity_internal_id aaii,
       amp_organisation org
  where aaii.amp_org_id = org.amp_org_id
/
CREATE OR REPLACE FORCE VIEW  "V_PHYSICAL_DESCRIPTION" ("AMP_ACTIVITY_ID", "DESCRIPTION") AS 
  select a.amp_activity_id AS amp_activity_id,
         a.description AS description
  from amp_physical_performance a
  order by a.amp_activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_PHYSICAL_PROGRESS" ("AMP_ACTIVITY_ID", "DESCRIPTION", "AMP_PP_ID") AS 
  select p.amp_activity_id AS amp_activity_id,
         p.description AS description,
         p.amp_pp_id AS amp_pp_id
  from amp_physical_performance p
  order by p.reporting_date
/
CREATE OR REPLACE FORCE VIEW  "V_PHYSICAL_TITLE" ("AMP_ACTIVITY_ID", "DESCRIPTION") AS 
  select a.amp_activity_id AS amp_activity_id,
         a.description AS description
  from amp_physical_performance a
  order by a.amp_activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_PRIMARYPROGRAM" ("AMP_ACTIVITY_ID", "NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select a.amp_activity_id AS amp_activity_id,
         t.name AS "NAME",
         ap.amp_program_id AS amp_program_id,
         ap.program_percentage AS program_percentage
  from amp_activity a,
       amp_activity_program ap,  amp_theme   t  where   t.amp_theme_id  =
            ap.amp_program_id and
        a.amp_activity_id = ap.amp_activity_id and
        ap.program_setting = 2
  order by
     a.amp_activity_id , t.name
/
CREATE OR REPLACE FORCE VIEW  "V_PRIMARYPROGRAM_ALL_LEVEL" ("AMP_ACTIVITY_ID", "PROGRAM_PERCENTAGE", "AMP_PROGRAM_ID1", "N1", "L1", "AMP_PROGRAM_ID2", "N2", "L2", "AMP_PROGRAM_ID3", "N3", "L3", "AMP_PROGRAM_ID4", "N4", "L4", "AMP_PROGRAM_ID5", "N5", "L5", "AMP_PROGRAM_ID6", "N6", "L6", "AMP_PROGRAM_ID7", "N7", "L7", "AMP_PROGRAM_ID8", "N8", "L8") AS 
  select 
    a.amp_activity_id AS amp_activity_id,
    a.program_percentage AS program_percentage,
    b.amp_theme_id AS amp_program_id1,
    b.name AS n1,
    b.level_ AS l1,
    b1.amp_theme_id AS amp_program_id2,
    b1.name AS n2,
    b1.level_ AS l2,
    b2.amp_theme_id AS amp_program_id3,
    b2.name AS n3,
    b2.level_ AS l3,
    b3.amp_theme_id AS amp_program_id4,
    b3.name AS n4,
    b3.level_ AS l4,
    b4.amp_theme_id AS amp_program_id5,
    b4.name AS n5,
    b4.level_ AS l5,
    b5.amp_theme_id AS amp_program_id6,
    b5.name AS n6,
    b5.level_ AS l6,
    b6.amp_theme_id AS amp_program_id7,
    b6.name AS n7,
    b6.level_ AS l7,
    b7.amp_theme_id AS amp_program_id8,
    b7.name AS n8,
    b7.level_ AS l8 
  from 
    amp_activity_program a join 
    amp_theme b on a.amp_program_id = b.amp_theme_id 
    left join amp_theme b1 on b1.amp_theme_id = b.parent_theme_id 
    left join amp_theme b2 on b2.amp_theme_id = b1.parent_theme_id 
    left join amp_theme b3 on b3.amp_theme_id = b2.parent_theme_id 
    left join amp_theme b4 on b4.amp_theme_id = b3.parent_theme_id 
    left join amp_theme b5 on b5.amp_theme_id = b4.parent_theme_id 
    left join amp_theme b6 on b6.amp_theme_id = b5.parent_theme_id 
    left join amp_theme b7 on b7.amp_theme_id = b6.parent_theme_id 
  where 
    a.program_setting = 2
/
CREATE OR REPLACE FORCE VIEW  "V_PRIMARYPROGRAM_CACHED" ("AMP_ACTIVITY_ID", "NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE", "PROGRAM_SETTING") AS 
  select a.amp_activity_id AS amp_activity_id,
   
         decode(program_setting, 3, t.name, NULL) AS name, 
         ap.amp_program_id AS amp_program_id,
         ap.program_percentage AS program_percentage,
         ap.program_setting AS program_setting
  from amp_activity a
       left join amp_activity_program ap on a.amp_activity_id = ap.amp_activity_id
       left join amp_theme t on t.amp_theme_id = ap.amp_program_id
  order by a.amp_activity_id, t.name
/
CREATE OR REPLACE FORCE VIEW  "V_PRIMARYPROGRAM_LEVEL_0" ("AMP_ACTIVITY_ID", "NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select 
		amp_activity_id,
       name,
       amp_program_id,
       sum(program_percentage) as program_percentage
from (
      select v_primaryprogram_all_level.amp_activity_id AS amp_activity_id,
             case             0
               when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.n1
               when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.n2
               when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.n3
               when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.n4
               when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.n5
               when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.n6
               when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.n7
               when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.n8
             end AS name,
             case             0
               when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.amp_program_id1
               when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.amp_program_id2
               when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.amp_program_id3
               when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.amp_program_id4
               when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.amp_program_id5
               when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.amp_program_id6
               when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.amp_program_id7
               when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.amp_program_id8
             end AS amp_program_id,
             v_primaryprogram_all_level.program_percentage AS program_percentage
      from v_primaryprogram_all_level
     ) t1
group by 
	amp_activity_id,
       name,
       amp_program_id
having name is not null
/
CREATE OR REPLACE FORCE VIEW  "V_PRIMARYPROGRAM_LEVEL_1" ("AMP_ACTIVITY_ID", "NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select 
		amp_activity_id,
       name,
       amp_program_id,
       sum(program_percentage) as program_percentage
from (
      select v_primaryprogram_all_level.amp_activity_id AS amp_activity_id,
             case             1
               when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.n1
               when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.n2
               when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.n3
               when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.n4
               when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.n5
               when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.n6
               when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.n7
               when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.n8
             end AS name,
             case             1
               when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.amp_program_id1
               when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.amp_program_id2
               when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.amp_program_id3
               when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.amp_program_id4
               when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.amp_program_id5
               when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.amp_program_id6
               when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.amp_program_id7
               when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.amp_program_id8
             end AS amp_program_id,
             v_primaryprogram_all_level.program_percentage AS program_percentage
      from v_primaryprogram_all_level
     ) t1
group by 
	amp_activity_id,
       name,
       amp_program_id
having name is not null
/
CREATE OR REPLACE FORCE VIEW  "V_PRIMARYPROGRAM_LEVEL_2" ("AMP_ACTIVITY_ID", "NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select 
		amp_activity_id,
       name,
       amp_program_id,
       sum(program_percentage) as program_percentage
from (
      select v_primaryprogram_all_level.amp_activity_id AS amp_activity_id,
             case 2
               when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.n1
               when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.n2
               when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.n3
               when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.n4
               when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.n5
               when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.n6
               when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.n7
               when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.n8
             end AS name,
             case 2
               when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.amp_program_id1
               when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.amp_program_id2
               when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.amp_program_id3
               when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.amp_program_id4
               when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.amp_program_id5
               when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.amp_program_id6
               when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.amp_program_id7
               when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.amp_program_id8
             end AS amp_program_id,
             v_primaryprogram_all_level.program_percentage AS program_percentage
      from v_primaryprogram_all_level
     ) t1
group by 
	amp_activity_id,
       name,
       amp_program_id
having name is not null
/
CREATE OR REPLACE FORCE VIEW  "V_PRIMARYPROGRAM_LEVEL_3" ("AMP_ACTIVITY_ID", "NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select 
		amp_activity_id,
       name,
       amp_program_id,
       sum(program_percentage) as program_percentage
from (
      select v_primaryprogram_all_level.amp_activity_id AS amp_activity_id,
             case 3
               when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.n1
               when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.n2
               when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.n3
               when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.n4
               when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.n5
               when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.n6
               when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.n7
               when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.n8
             end AS name,
             case 3
               when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.amp_program_id1
               when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.amp_program_id2
               when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.amp_program_id3
               when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.amp_program_id4
               when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.amp_program_id5
               when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.amp_program_id6
               when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.amp_program_id7
               when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.amp_program_id8
             end AS amp_program_id,
             v_primaryprogram_all_level.program_percentage AS program_percentage
      from v_primaryprogram_all_level
     ) t1
group by 
	amp_activity_id,
       name,
       amp_program_id
having name is not null
/
CREATE OR REPLACE FORCE VIEW  "V_PRIMARYPROGRAM_LEVEL_4" ("AMP_ACTIVITY_ID", "NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select 
		amp_activity_id,
       name,
       amp_program_id,
       sum(program_percentage) as program_percentage
from (
      select v_primaryprogram_all_level.amp_activity_id AS amp_activity_id,
             case 4
               when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.n1
               when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.n2
               when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.n3
               when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.n4
               when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.n5
               when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.n6
               when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.n7
               when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.n8
             end AS name,
             case 4
               when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.amp_program_id1
               when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.amp_program_id2
               when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.amp_program_id3
               when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.amp_program_id4
               when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.amp_program_id5
               when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.amp_program_id6
               when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.amp_program_id7
               when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.amp_program_id8
             end AS amp_program_id,
             v_primaryprogram_all_level.program_percentage AS program_percentage
      from v_primaryprogram_all_level
     ) t1
group by 
	amp_activity_id,
       name,
       amp_program_id
having name is not null
/
CREATE OR REPLACE FORCE VIEW  "V_PRIMARYPROGRAM_LEVEL_5" ("AMP_ACTIVITY_ID", "NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select 
		amp_activity_id,
       name,
       amp_program_id,
       sum(program_percentage) as program_percentage
from (
      select v_primaryprogram_all_level.amp_activity_id AS amp_activity_id,
             case 5
               when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.n1
               when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.n2
               when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.n3
               when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.n4
               when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.n5
               when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.n6
               when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.n7
               when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.n8
             end AS name,
             case 5
               when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.amp_program_id1
               when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.amp_program_id2
               when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.amp_program_id3
               when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.amp_program_id4
               when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.amp_program_id5
               when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.amp_program_id6
               when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.amp_program_id7
               when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.amp_program_id8
             end AS amp_program_id,
             v_primaryprogram_all_level.program_percentage AS program_percentage
      from v_primaryprogram_all_level
     ) t1
group by 
	amp_activity_id,
       name,
       amp_program_id
having name is not null
/
CREATE OR REPLACE FORCE VIEW  "V_PRIMARYPROGRAM_LEVEL_6" ("AMP_ACTIVITY_ID", "NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select 
		amp_activity_id,
       name,
       amp_program_id,
       sum(program_percentage) as program_percentage
from (
      select v_primaryprogram_all_level.amp_activity_id AS amp_activity_id,
             case 6
               when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.n1
               when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.n2
               when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.n3
               when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.n4
               when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.n5
               when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.n6
               when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.n7
               when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.n8
             end AS name,
             case 6
               when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.amp_program_id1
               when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.amp_program_id2
               when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.amp_program_id3
               when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.amp_program_id4
               when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.amp_program_id5
               when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.amp_program_id6
               when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.amp_program_id7
               when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.amp_program_id8
             end AS amp_program_id,
             v_primaryprogram_all_level.program_percentage AS program_percentage
      from v_primaryprogram_all_level
     ) t1
group by 
	amp_activity_id,
       name,
       amp_program_id
having name is not null
/
CREATE OR REPLACE FORCE VIEW  "V_PRIMARYPROGRAM_LEVEL_7" ("AMP_ACTIVITY_ID", "NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select 
		amp_activity_id,
       name,
       amp_program_id,
       sum(program_percentage) as program_percentage
from (
      select v_primaryprogram_all_level.amp_activity_id AS amp_activity_id,
             case 7
               when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.n1
               when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.n2
               when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.n3
               when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.n4
               when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.n5
               when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.n6
               when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.n7
               when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.n8
             end AS name,
             case 7
               when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.amp_program_id1
               when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.amp_program_id2
               when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.amp_program_id3
               when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.amp_program_id4
               when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.amp_program_id5
               when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.amp_program_id6
               when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.amp_program_id7
               when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.amp_program_id8
             end AS amp_program_id,
             v_primaryprogram_all_level.program_percentage AS program_percentage
      from v_primaryprogram_all_level
     ) t1
group by 
	amp_activity_id,
       name,
       amp_program_id
having name is not null
/
CREATE OR REPLACE FORCE VIEW  "V_PRIMARYPROGRAM_LEVEL_8" ("AMP_ACTIVITY_ID", "NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select 
		amp_activity_id,
       name,
       amp_program_id,
       sum(program_percentage) as program_percentage
from (
      select v_primaryprogram_all_level.amp_activity_id AS amp_activity_id,
             case 8
               when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.n1
               when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.n2
               when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.n3
               when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.n4
               when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.n5
               when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.n6
               when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.n7
               when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.n8
             end AS name,
             case 8
               when v_primaryprogram_all_level.l1 then v_primaryprogram_all_level.amp_program_id1
               when v_primaryprogram_all_level.l2 then v_primaryprogram_all_level.amp_program_id2
               when v_primaryprogram_all_level.l3 then v_primaryprogram_all_level.amp_program_id3
               when v_primaryprogram_all_level.l4 then v_primaryprogram_all_level.amp_program_id4
               when v_primaryprogram_all_level.l5 then v_primaryprogram_all_level.amp_program_id5
               when v_primaryprogram_all_level.l6 then v_primaryprogram_all_level.amp_program_id6
               when v_primaryprogram_all_level.l7 then v_primaryprogram_all_level.amp_program_id7
               when v_primaryprogram_all_level.l8 then v_primaryprogram_all_level.amp_program_id8
             end AS amp_program_id,
             v_primaryprogram_all_level.program_percentage AS program_percentage
      from v_primaryprogram_all_level
     ) t1
group by 
	amp_activity_id,
       name,
       amp_program_id
having name is not null
/
CREATE OR REPLACE FORCE VIEW  "V_PROJECT_CATEGORY" ("AMP_ACTIVITY_ID", "NAME", "AMP_CATEGORY_ID") AS 
  select aac.amp_activity_id AS amp_activity_id,
         acv.category_value AS "NAME",
         aac.amp_categoryvalue_id AS amp_category_id
  from amp_category_value acv,
       amp_category_class acc,
       amp_activities_categoryvalues aac
  where acv.id = aac.amp_categoryvalue_id and
        acc.id = acv.amp_category_class_id and
        acc.keyName = 'project_category'
/
CREATE OR REPLACE FORCE VIEW  "V_PROJECT_ID" ("AMP_ACTIVITY_ID", "NAME") AS 
  select i.amp_activity_id AS amp_activity_id,
          i.internal_id||' '||o.name||''  AS "NAME"
  from amp_organisation o,
       amp_activity_internal_id i
  where o.amp_org_id = i.amp_org_id
/
CREATE OR REPLACE FORCE VIEW  "V_PROPOSED_COMPLETION_DATE" ("AMP_ACTIVITY_ID", "PROPOSED_COMPLETION_DATE") AS 
  select amp_activity.amp_activity_id AS amp_activity_id,
         amp_activity.proposed_completion_date AS proposed_completion_date
  from amp_activity
  order by amp_activity.amp_activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_PROPOSED_COST" ("AMP_ACTIVITY_ID", "OBJECT_ID", "OBJECT_ID2", "TRANSACTION_AMOUNT", "CURRENCY_CODE", "TRANSACTION_DATE") AS 
  select amp_activity.amp_activity_id AS amp_activity_id,
         amp_activity.amp_activity_id AS object_id,
         amp_activity.amp_activity_id AS object_id2,
         amp_activity.proj_cost_amount AS transaction_amount,
         amp_activity.proj_cost_currcode AS currency_code,
         amp_activity.proj_cost_date AS transaction_date
  from amp_activity
  where amp_activity.proj_cost_amount is not null
/
CREATE OR REPLACE FORCE VIEW  "V_PURPOSES" ("AMP_ACTIVITY_ID", "EBODY") AS 
  select amp_activity.amp_activity_id AS amp_activity_id,
         trim(dg_editor.BODY) AS ebody
  from amp_activity,
       dg_editor
  where amp_activity.purpose = dg_editor.EDITOR_KEY
  order by amp_activity.amp_activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_REGIONAL_FUNDING" ("AMP_ACTIVITY_ID", "AMP_REGIONAL_FUNDING_ID", "AMP_FUND_DETAIL_ID", "REGION_NAME", "TRANSACTION_TYPE", "ADJUSTMENT_TYPE", "TRANSACTION_DATE", "TRANSACTION_AMOUNT", "CURRENCY_CODE", "REGION_ID") AS 
  select 
    f.activity_id AS amp_activity_id,
    f.amp_regional_funding_id AS amp_regional_funding_id,
    f.amp_regional_funding_id AS amp_fund_detail_id,
    r.location_name AS region_name,
    f.transaction_type AS transaction_type,
    f.adjustment_type AS adjustment_type,
    f.transaction_date AS transaction_date,
    f.transaction_amount AS transaction_amount,
    c.currency_code AS currency_code,
    f.region_location_id AS region_id 
  from 
      amp_regional_funding f,
     amp_category_value_location r,
     amp_currency c, 
     amp_category_value v 
  where 
    c.amp_currency_id = f.currency_id and 
    f.region_location_id = r.id and 
    r.parent_category_value = v.id 
    and v.category_value ='Region' 
  order by 
    f.activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_REGIONAL_GROUP" ("AMP_ACTIVITY_ID", "NAME", "AMP_ORG_ID") AS 
  select f.activity AS amp_activity_id,
         o.name AS "NAME",
         f.organisation AS amp_org_id
  from amp_org_role f,
       amp_organisation o,
       amp_role r where
       f.organisation = o.amp_org_id and f.role = r.amp_role_id
        and r.role_code = 'RG'
  order by
     f.activity , o.name
/
CREATE OR REPLACE FORCE VIEW  "V_REGIONS" ("AMP_ACTIVITY_ID", "REGION", "REGION_ID", "LOCATION_PERCENTAGE") AS 
  select ra.amp_activity_id AS amp_activity_id,
         l.region AS region,
         l.region_location_id AS region_id,
         sum(ra.location_percentage) AS location_percentage
  from amp_activity_location ra,
       amp_location l,
       amp_category_value_location cvl
  where
      l.region_id  is not null and ra.amp_location_id = l.amp_location_id and l.location_id = cvl.id
  group by
     ra.amp_activity_id ,l.region, l.region_id ,l.region_location_id
  order by      ra.amp_activity_id , l.region
/
CREATE OR REPLACE FORCE VIEW  "V_RESPONSIBLE_ORGANISATION" ("AMP_ACTIVITY_ID", "NAME", "AMP_ORG_ID") AS 
  select f.activity AS amp_activity_id,
         o.name AS "NAME",
         f.organisation AS amp_org_id
  from amp_org_role f,
       amp_organisation o,
       amp_role r where f.organisation = o.amp_org_id and f.role = r.amp_role_id
        and r.role_code = 'RO'
  order by
     f.activity , o.name
/
CREATE OR REPLACE FORCE VIEW  "V_RESPONSIBLE_ORG_GROUPS" ("AMP_ACTIVITY_ID", "NAME", "AMP_ORG_GRP_ID") AS 
  select f.activity AS amp_activity_id,
         b.org_grp_name AS "NAME",
         b.amp_org_grp_id AS amp_org_grp_id
  from amp_org_role f,
       amp_organisation o,
       amp_role r,
       amp_org_group b where
       f.organisation = o.amp_org_id and f.role =
        r.amp_role_id and r.role_code = 'RO' and o.org_grp_id = b.amp_org_grp_id

  order by
     f.activity , o.name
/
CREATE OR REPLACE FORCE VIEW  "V_RESULTS" ("AMP_ACTIVITY_ID", "EBODY") AS 
  select amp_activity.amp_activity_id AS amp_activity_id,
         trim(dg_editor.BODY) AS ebody
  from amp_activity,
       dg_editor
  where amp_activity.results = dg_editor.EDITOR_KEY
  order by amp_activity.amp_activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_SECONDARYPROGRAM" ("AMP_ACTIVITY_ID", "NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select a.amp_activity_id AS amp_activity_id,
         t.name AS "NAME",
         ap.amp_program_id AS amp_program_id,
         ap.program_percentage AS program_percentage
  from amp_activity a,
       amp_activity_program ap  , amp_theme   t
       where  a.amp_activity_id = ap.amp_activity_id and
        ap.program_setting = 3  and t.amp_theme_id  =
            ap.amp_program_id
  order by
     a.amp_activity_id , t.name
/
CREATE OR REPLACE FORCE VIEW  "V_SECONDARYPROGRAM_ALL_LEVEL" ("AMP_ACTIVITY_ID", "PROGRAM_PERCENTAGE", "AMP_PROGRAM_ID1", "N1", "L1", "AMP_PROGRAM_ID2", "N2", "L2", "AMP_PROGRAM_ID3", "N3", "L3", "AMP_PROGRAM_ID4", "N4", "L4", "AMP_PROGRAM_ID5", "N5", "L5", "AMP_PROGRAM_ID6", "N6", "L6", "AMP_PROGRAM_ID7", "N7", "L7", "AMP_PROGRAM_ID8", "N8", "L8") AS 
  select 
    a.amp_activity_id AS amp_activity_id,
    a.program_percentage AS program_percentage,
    b.amp_theme_id AS amp_program_id1,
    b.name AS n1,
    b.level_ AS l1,
    b1.amp_theme_id AS amp_program_id2,
    b1.name AS n2,
    b1.level_ AS l2,
    b2.amp_theme_id AS amp_program_id3,
    b2.name AS n3,
    b2.level_ AS l3,
    b3.amp_theme_id AS amp_program_id4,
    b3.name AS n4,
    b3.level_ AS l4,
    b4.amp_theme_id AS amp_program_id5,
    b4.name AS n5,
    b4.level_ AS l5,
    b5.amp_theme_id AS amp_program_id6,
    b5.name AS n6,
    b5.level_ AS l6,
    b6.amp_theme_id AS amp_program_id7,
    b6.name AS n7,
    b6.level_ AS l7,
    b7.amp_theme_id AS amp_program_id8,
    b7.name AS n8,
    b7.level_ AS l8 
  from 
     amp_activity_program a, 
     amp_theme b, 
     amp_theme b1, 
     amp_theme b2, 
     amp_theme b3, 
     amp_theme b4, 
     amp_theme b5, 
     amp_theme b6, 
     amp_theme b7 
  where  a.program_setting = 3 and 
    a.amp_program_id = b.amp_theme_id
    and b1.amp_theme_id = b.parent_theme_id 
    and b2.amp_theme_id = b1.parent_theme_id 
    and b3.amp_theme_id = b2.parent_theme_id 
    and b4.amp_theme_id = b3.parent_theme_id 
    and b5.amp_theme_id = b4.parent_theme_id 
    and b6.amp_theme_id = b5.parent_theme_id 
    and b7.amp_theme_id = b6.parent_theme_id
/
CREATE OR REPLACE FORCE VIEW  "V_SECONDARYPROGRAM_CACHED" ("AMP_ACTIVITY_ID", "NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE", "PROGRAM_SETTING") AS 
  select 
    a.amp_activity_id AS amp_activity_id,
    decode(ap.program_setting,3,t.name,NULL) AS name,
    ap.amp_program_id AS amp_program_id,
    ap.program_percentage AS program_percentage,
    ap.program_setting AS program_setting 
  from 
    amp_activity a 
    left join amp_activity_program ap on a.amp_activity_id = ap.amp_activity_id 
    left join amp_theme t on t.amp_theme_id = ap.amp_program_id 
  order by 
    a.amp_activity_id,t.name
/
CREATE OR REPLACE FORCE VIEW  "V_SECONDARYPROGRAM_LEVEL_0" ("NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select 
amp_activity_id
name,
amp_program_id,
sum(program_percentage) program_percentage
from(
select 
    v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,
    
    case 0 
    when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
    when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
    when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
    when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
    when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
    when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
    when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
    when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 
    end AS name,
    case 0 
    when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
    when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
    when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
    when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
    when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
    when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
    when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
    when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 
    end  AS amp_program_id,
    v_secondaryprogram_all_level.program_percentage AS program_percentage 
  from 
    v_secondaryprogram_all_level )  t1 
group by amp_activity_id,name,amp_program_id 
having name is not null
/
CREATE OR REPLACE FORCE VIEW  "V_SECONDARYPROGRAM_LEVEL_1" ("NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select 
amp_activity_id
name,
amp_program_id,
sum(program_percentage) program_percentage
from(
select 
    v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,
    
    case 1 
    when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
    when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
    when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
    when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
    when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
    when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
    when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
    when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 
    end AS name,
    case 1 
    when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
    when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
    when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
    when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
    when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
    when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
    when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
    when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 
    end  AS amp_program_id,
    v_secondaryprogram_all_level.program_percentage AS program_percentage 
  from 
    v_secondaryprogram_all_level )  t1 
group by amp_activity_id,name,amp_program_id 
having name is not null
/
CREATE OR REPLACE FORCE VIEW  "V_SECONDARYPROGRAM_LEVEL_2" ("NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select 
amp_activity_id
name,
amp_program_id,
sum(program_percentage) program_percentage
from(
select 
    v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,
    
    case 2 
    when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
    when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
    when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
    when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
    when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
    when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
    when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
    when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 
    end AS name,
    case 2 
    when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
    when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
    when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
    when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
    when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
    when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
    when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
    when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 
    end  AS amp_program_id,
    v_secondaryprogram_all_level.program_percentage AS program_percentage 
  from 
    v_secondaryprogram_all_level )  t1 
group by amp_activity_id,name,amp_program_id 
having name is not null
/
CREATE OR REPLACE FORCE VIEW  "V_SECONDARYPROGRAM_LEVEL_3" ("NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select 
amp_activity_id
name,
amp_program_id,
sum(program_percentage) program_percentage
from(
select 
    v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,
    
    case 3 
    when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
    when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
    when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
    when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
    when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
    when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
    when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
    when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 
    end AS name,
    case 3 
    when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
    when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
    when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
    when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
    when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
    when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
    when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
    when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 
    end  AS amp_program_id,
    v_secondaryprogram_all_level.program_percentage AS program_percentage 
  from 
    v_secondaryprogram_all_level )  t1 
group by amp_activity_id,name,amp_program_id 
having name is not null
/
CREATE OR REPLACE FORCE VIEW  "V_SECONDARYPROGRAM_LEVEL_4" ("NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select 
amp_activity_id
name,
amp_program_id,
sum(program_percentage) program_percentage
from(
select 
    v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,
    
    case 4 
    when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
    when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
    when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
    when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
    when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
    when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
    when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
    when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 
    end AS name,
    case 4 
    when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
    when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
    when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
    when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
    when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
    when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
    when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
    when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 
    end  AS amp_program_id,
    v_secondaryprogram_all_level.program_percentage AS program_percentage 
  from 
    v_secondaryprogram_all_level )  t1 
group by amp_activity_id,name,amp_program_id 
having name is not null
/
CREATE OR REPLACE FORCE VIEW  "V_SECONDARYPROGRAM_LEVEL_5" ("NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select 
amp_activity_id
name,
amp_program_id,
sum(program_percentage) program_percentage
from(
select 
    v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,
    
    case 5 
    when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
    when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
    when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
    when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
    when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
    when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
    when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
    when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 
    end AS name,
    case 5 
    when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
    when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
    when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
    when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
    when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
    when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
    when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
    when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 
    end  AS amp_program_id,
    v_secondaryprogram_all_level.program_percentage AS program_percentage 
  from 
    v_secondaryprogram_all_level )  t1 
group by amp_activity_id,name,amp_program_id 
having name is not null
/
CREATE OR REPLACE FORCE VIEW  "V_SECONDARYPROGRAM_LEVEL_6" ("NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select 
amp_activity_id
name,
amp_program_id,
sum(program_percentage) program_percentage
from(
select 
    v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,
    
    case 6 
    when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
    when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
    when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
    when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
    when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
    when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
    when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
    when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 
    end AS name,
    case 6 
    when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
    when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
    when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
    when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
    when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
    when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
    when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
    when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 
    end  AS amp_program_id,
    v_secondaryprogram_all_level.program_percentage AS program_percentage 
  from 
    v_secondaryprogram_all_level )  t1 
group by amp_activity_id,name,amp_program_id 
having name is not null
/
CREATE OR REPLACE FORCE VIEW  "V_SECONDARYPROGRAM_LEVEL_7" ("NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select 
amp_activity_id
name,
amp_program_id,
sum(program_percentage) program_percentage
from(
select 
    v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,
    
    case 7 
    when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
    when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
    when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
    when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
    when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
    when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
    when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
    when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 
    end AS name,
    case 7 
    when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
    when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
    when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
    when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
    when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
    when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
    when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
    when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 
    end  AS amp_program_id,
    v_secondaryprogram_all_level.program_percentage AS program_percentage 
  from 
    v_secondaryprogram_all_level )  t1 
group by amp_activity_id,name,amp_program_id 
having name is not null
/
CREATE OR REPLACE FORCE VIEW  "V_SECONDARYPROGRAM_LEVEL_8" ("NAME", "AMP_PROGRAM_ID", "PROGRAM_PERCENTAGE") AS 
  select 
amp_activity_id
name,
amp_program_id,
sum(program_percentage) program_percentage
from(
select 
    v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,
    
    case 8 
    when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
    when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
    when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
    when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
    when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
    when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
    when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
    when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 
    end AS name,
    case 8 
    when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
    when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
    when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
    when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
    when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
    when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
    when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
    when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 
    end  AS amp_program_id,
    v_secondaryprogram_all_level.program_percentage AS program_percentage 
  from 
    v_secondaryprogram_all_level )  t1 
group by amp_activity_id,name,amp_program_id 
having name is not null
/
CREATE OR REPLACE FORCE VIEW  "V_SECONDARY_SECTORS" ("AMP_ACTIVITY_ID", "SECTORNAME", "AMP_SECTOR_ID", "SECTOR_PERCENTAGE", "AMP_SECTOR_SCHEME_ID") AS 
  select
  amp_activity_id,
  sectorname,
  amp_sector_id,
  sum(sector_percentage) as sector_percentage,
  amp_sector_scheme_id
  from(
  select
  sa.amp_activity_id AS amp_activity_id,
  getSectorName(getParentSectorId(s.amp_sector_id)) AS  sectorname ,
  getParentSectorId (s.amp_sector_id)  AS  amp_sector_id ,
  sa.sector_percentage AS  sector_percentage ,
  s.amp_sec_scheme_id  AS  amp_sector_scheme_id
  from  amp_activity_sector   sa  ,
        amp_sector   s  ,
        amp_sector_scheme   ss
  where
       s.amp_sector_id  =  sa.amp_sector_id  and  s.amp_sec_scheme_id   in (
       select amp_classification_config.classification_id AS classification_id
        from amp_classification_config
        where amp_classification_config.name = 'Secondary'
          )
  and s.amp_sec_scheme_id  =  ss.amp_sec_scheme_id)  t

  group by  amp_activity_id, sectorname ,amp_sector_id,amp_sector_scheme_id
  order by      amp_activity_id ,sectorname
/
CREATE OR REPLACE FORCE VIEW  "V_SECONDARY_SUB_SECTORS" ("AMP_ACTIVITY_ID", "NAME", "AMP_SECTOR_ID", "SECTOR_PERCENTAGE") AS 
  select sa.amp_activity_id AS amp_activity_id,
         s.name AS "NAME",
         s.amp_sector_id AS amp_sector_id,
         sa.sector_percentage AS sector_percentage
  from amp_activity_sector sa,
       amp_sector s,
       amp_sector_scheme ss
  where  sa.amp_sector_id = s.amp_sector_id and
       s.sector_code  > 1000 and  s.amp_sec_scheme_id
  in (
  select amp_classification_config.classification_id AS classification_id
  from amp_classification_config
  where amp_classification_config.name = 'Secondary') and
     s.amp_sec_scheme_id  =  ss.amp_sec_scheme_id
  order by
     sa.amp_activity_id , s.name
/
CREATE OR REPLACE FORCE VIEW  "V_SECONDARY_SUB_SUB_SECTORS" ("AMP_ACTIVITY_ID", "NAME", "AMP_SECTOR_ID", "SECTOR_PERCENTAGE") AS 
  select sa.amp_activity_id AS amp_activity_id,
         s.name AS "NAME",
         s.amp_sector_id AS amp_sector_id,
         sa.sector_percentage AS sector_percentage
  from amp_activity_sector sa,
       amp_sector s,
       amp_sector_scheme ss
  where
       s.sector_code  > 10000 and  s.amp_sec_scheme_id
  in ( select amp_classification_config.classification_id AS classification_id
  from amp_classification_config
  where sa.amp_sector_id = s.amp_sector_id and amp_classification_config.name = 'Secondary') and
     s.amp_sec_scheme_id  =  ss.amp_sec_scheme_id
  order by
     sa.amp_activity_id , s.name
/
CREATE OR REPLACE FORCE VIEW  "V_SECTORS" ("AMP_ACTIVITY_ID", "SECTORNAME", "AMP_SECTOR_ID", "SECTOR_PERCENTAGE", "AMP_SECTOR_SCHEME_ID", "SEC_SCHEME_NAME") AS 
  select
  amp_activity_id,
  sectorname,amp_sector_id,
  sum(sector_percentage) sector_percentage,
  amp_sector_scheme_id,
  sec_scheme_name
  from (
  select sa.amp_activity_id AS amp_activity_id,
         getSectorName(getParentSectorId(s.amp_sector_id)) AS  sectorname ,
     getParentSectorId (s.amp_sector_id)  AS  amp_sector_id ,
     sa.sector_percentage  ,
     s.amp_sec_scheme_id  AS  amp_sector_scheme_id ,
     ss.sec_scheme_name  AS  sec_scheme_name
  from
       amp_activity_sector   sa  ,  amp_sector   s  ,  amp_sector_scheme   ss
  where
       s.amp_sector_id  =  sa.amp_sector_id  and  s.amp_sec_scheme_id
  in (

  select amp_classification_config.classification_id AS classification_id
  from amp_classification_config
  where amp_classification_config.name = 'Primary') and
     s.amp_sec_scheme_id  =  ss.amp_sec_scheme_id  ) t
  group by
      amp_activity_id,sectorname,amp_sector_id ,amp_sector_scheme_id,sec_scheme_name
  order by
     amp_activity_id ,sectorname
/
CREATE OR REPLACE FORCE VIEW  "V_SECTORS_CACHED" ("AMP_ACTIVITY_ID", "SECTORNAME", "AMP_SECTOR_ID", "AMP_SECTOR_SCHEME_ID", "SEC_SCHEME_NAME", "SECTOR_PERCENTAGE", "TRANSACTION_AMOUNT", "TRANSACTION_TYPE", "ADJUSTMENT_TYPE", "TRANSACTION_DATE", "FIXED_EXCHANGE_RATE", "CURRENCY_CODE") AS 
  select sa.amp_activity_id AS amp_activity_id,
         getSectorName(getParentSectorId(s.amp_sector_id)) AS sectorname,
         getParentSectorId(s.amp_sector_id) AS amp_sector_id,
         s.amp_sec_scheme_id AS amp_sector_scheme_id,
         ss.sec_scheme_name AS sec_scheme_name,
         sa.sector_percentage AS sector_percentage,
         ((fd.transaction_amount * sa.sector_percentage) / 100) AS transaction_amount,
         fd.transaction_type AS transaction_type,
         fd.adjustment_type AS adjustment_type,
         fd.transaction_date AS transaction_date,
         fd.fixed_exchange_rate AS fixed_exchange_rate,
         c.currency_code AS currency_code
  from
  amp_funding_detail fd,
  amp_sector_scheme ss,
  amp_classification_config cc,
  amp_sector s  ,
  amp_activity_sector sa ,
  amp_funding f ,
  amp_currency c
where
cc.name = 'Primary'
and cc.classification_id = ss.amp_sec_scheme_id
and sa.classification_config_id = cc.id
and s.amp_sec_scheme_id = ss.amp_sec_scheme_id
and sa.amp_activity_id = f.amp_activity_id
and f.amp_funding_id = fd.amp_funding_id
and fd.amp_currency_id = c.amp_currency_id
and sa.amp_sector_id = s.amp_sector_id

  order by sa.amp_activity_id,
           getSectorName(getParentSectorId(s.amp_sector_id)),
           fd.transaction_type,
           f.amp_funding_id
/
CREATE OR REPLACE FORCE VIEW  "V_SECTOR_GROUP" ("AMP_ACTIVITY_ID", "NAME", "AMP_ORG_ID") AS 
  select f.activity AS amp_activity_id,
         o.name AS "NAME",
         f.organisation AS amp_org_id
  from amp_org_role f,
       amp_organisation o,
       amp_role r where f.organisation = o.amp_org_id and f.role = r.amp_role_id
        and r.role_code = 'SG'
  order by
     f.activity , o.name
/
CREATE OR REPLACE FORCE VIEW  "V_STATUS" ("AMP_ACTIVITY_ID", "NAME", "AMP_STATUS_ID") AS 
  select aac.amp_activity_id AS amp_activity_id,
         acv.category_value AS "NAME",
         aac.amp_categoryvalue_id AS amp_status_id
  from amp_category_value acv,
       amp_category_class acc,
       amp_activities_categoryvalues aac
  where acv.id = aac.amp_categoryvalue_id and
        acc.id = acv.amp_category_class_id and
        acc.keyName = 'activity_status'
/
CREATE OR REPLACE FORCE VIEW  "V_SUB_SECTORS" ("AMP_ACTIVITY_ID", "NAME", "AMP_SECTOR_ID", "SECTOR_PERCENTAGE") AS 
  select sa.amp_activity_id AS amp_activity_id,
         s.name AS "NAME",
         s.amp_sector_id AS amp_sector_id,
         sa.sector_percentage AS sector_percentage
  from amp_activity_sector sa,
       amp_sector s,
       amp_sector_scheme ss
  where sa.amp_sector_id = s.amp_sector_id and
       s.sector_code  > 1000 and  s.amp_sec_scheme_id
  in (
  select amp_classification_config.classification_id AS classification_id
  from amp_classification_config
  where amp_classification_config.name = 'Primary') and
     s.amp_sec_scheme_id  =  ss.amp_sec_scheme_id
  order by
     sa.amp_activity_id , s.name
/
CREATE OR REPLACE FORCE VIEW  "V_SUB_SUB_SECTORS" ("AMP_ACTIVITY_ID", "NAME", "AMP_SECTOR_ID", "SECTOR_PERCENTAGE") AS 
  select sa.amp_activity_id AS amp_activity_id,
         s.name AS "NAME",
         s.amp_sector_id AS amp_sector_id,
         sa.sector_percentage AS sector_percentage
  from amp_activity_sector sa,
       amp_sector s
  where  sa.amp_sector_id = s.amp_sector_id  and
      s.sector_code  > 10000
  order by
     sa.amp_activity_id , s.name
/
CREATE OR REPLACE FORCE VIEW  "V_TEAMS" ("AMP_ACTIVITY_ID", "NAME", "AMP_TEAM_ID") AS 
  select a.amp_activity_id AS amp_activity_id,
         t.name AS "NAME",
         t.amp_team_id AS amp_team_id
  from amp_activity a,
       amp_team t
  where a.amp_team_id = t.amp_team_id
  order by a.amp_activity_id,
           t.amp_team_id
/
CREATE OR REPLACE FORCE VIEW  "V_TERMS_ASSIST" ("AMP_ACTIVITY_ID", "TERMS_ASSIST_NAME", "TERMS_ASSIST_CODE") AS 
  select
         a.amp_activity_id AS amp_activity_id,
         val.category_value AS terms_assist_name,
         val.id AS terms_assist_code
  from amp_activity a,
       amp_funding fund,
       amp_category_value val
  where fund.amp_activity_id = a.amp_activity_id and
        val.id = fund.TYPE_OF_ASSISTANCE_CATEGORY_VA
  group by
     a.amp_activity_id , val.id  ,val.category_value
  order by
     a.amp_activity_id , val.category_value
/
CREATE OR REPLACE FORCE VIEW  "V_TITLES" ("AMP_ACTIVITY_ID", "NAME", "TITLE_ID", "DRAFT", "STATUS") AS 
  select amp_activity.amp_activity_id AS amp_activity_id,
         amp_activity.name AS "NAME",
         amp_activity.amp_activity_id AS title_id,
         amp_activity.draft AS draft,
         amp_activity.approval_status AS status
  from amp_activity
  order by amp_activity.amp_activity_id
/
CREATE OR REPLACE FORCE VIEW  "V_UPDATED_DATE" ("AMP_ACTIVITY_ID", "DATE_UPDATED") AS 
  select a.amp_activity_id AS amp_activity_id,
         a.date_updated AS date_updated
  from amp_activity a
  order by a.amp_activity_id
/
