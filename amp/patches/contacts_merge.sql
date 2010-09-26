/*duplicate contacts*/
drop table if exists dupl_conts;
CREATE TABLE dupl_conts AS 
SELECT max(c.contact_id) as cid, count(c.contact_id) as number, c.name, c.lastname, cp.value as email FROM amp_contact c, amp_contact_properties cp  
	WHERE c.contact_id = cp.contact_id AND cp.name="contact email" 
	GROUP BY c.lastname, c.name, cp.value having number>1 order by cid;

/* THE deletable_contacts TABLE WILL HOLD THE contact_id OF THE CONTACT ENTITIES THAT WILL BE DELETED.*/
/* SO THE cid COLUMN WILL CONTAIN THE IDS OF THE CLONES (THAT WILL BE DELETED) WHILE THE replace_cid WILL*/
/* CONTAIN THE ID OF THE ORIGINAL ENTITY (THE ONE THAT REMAINS IN THE DATABASE) */
drop table if exists deletable_contacts;
CREATE TABLE deletable_contacts AS 
 SELECT c.contact_id as cid, c.name, c.lastname, cp.value as email, ac.cid as replace_cid  FROM amp_contact c, amp_contact_properties cp, dupl_conts ac 
      WHERE c.contact_id=cp.contact_id AND cp.name="contact email" AND 
      c.name=ac.name AND c.lastname=ac.lastname AND cp.value=ac.email AND c.contact_id <> ac.cid;

/*THE temp_properties TABLE WILL CONTAIN THE PROPERTIES OF THE CLONE CONTACTS (cp1) THAT ARE EXACTLY THE SAME 
-- AS THE ONES OF THE ORIGINAL CONTACT (cp2).*/
drop table if exists temp_properties;
CREATE TABLE temp_properties 
  SELECT cp1.property_id as pid1, cp1.name as name1, cp1.value as value1, cp1.contact_id as del_cid, cp2.contact_id as replace_cid, 
  cp2.property_id as pid2, cp2.name as name2, cp2.value as value2 
    FROM amp_contact_properties cp1, amp_contact_properties cp2, deletable_contacts dc 
    WHERE cp1.contact_id=dc.cid AND cp2.contact_id=dc.replace_cid AND cp1.name=cp2.name AND cp1.value=cp2.value  ;


/*WE CAN DELETE THE DUPLICATE PROPERTIES*/
DELETE FROM amp_contact_properties WHERE property_id IN (SELECT pid1 FROM temp_properties);

/*ALL THE REMAINING PROPERTIES OF THE CLONES ARE NOT DUPLICATES SO WE NEED TO ASSIGN THEM TO THE ORIGINAL*/
UPDATE amp_contact_properties cp, deletable_contacts dc SET cp.contact_id=dc.replace_cid   WHERE cp.contact_id=dc.cid;

/*UPDATE REFERENCES FROM ACTIVITY TO ORIGINAL CONTACT*/
UPDATE amp_activity_contact aac, deletable_contacts dc SET aac.contact_id=dc.replace_cid  WHERE aac.contact_id=dc.cid;

/*UPDATE REFERENCES FROM ORGANIZATION TO ORIGINAL CONTACT*/
UPDATE amp_org_contact aoc, deletable_contacts dc SET aoc.contact_id=dc.replace_cid WHERE aoc.contact_id=dc.cid;

/* RECHECK DUPLICATE PROPERTIES FOR ONE CONTACT*/
drop table if exists temp_cont_props;
create table temp_cont_props as SELECT acp1.property_id,acp1.name,acp1.value,acp1.contact_id FROM amp_contact_properties acp1 , amp_contact_properties acp2 where acp1.name=acp2.name and acp1.value=acp2.value and acp1.contact_id=acp2.contact_id and acp1.property_id<>acp2.property_id group by acp1.property_id order by contact_id,name;

/*DELETE DUPLICATE CONTACT_PROPERTIES ASSIGNED TO ONE CONTACT AND LEAVE JUST ONE PROPERTY FOR EACH DUPLICATE*/
delete from amp_contact_properties where property_id in (select property_id from temp_cont_props) and property_id not in (SELECT max(property_id) FROM temp_cont_props a group by name,contact_id);

/*DELETE CLONE CONTACTS*/
DELETE FROM amp_contact WHERE contact_id IN (SELECT cid FROM deletable_contacts );

/*delete temp tables*/
drop table dupl_conts;
drop table deletable_contacts;
drop table temp_properties;
drop table temp_cont_props;
