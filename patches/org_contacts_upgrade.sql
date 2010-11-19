/* add temporary columns to table */
alter table amp_contact 
add (organization_id bigint(20),email varchar(50), phone varchar(50), fax varchar(50));

insert into amp_contact (name,function,email,phone,fax,is_shared,organization_id) 
select contact_person_name,contact_person_title,email,phone,fax,true,amp_org_id 
from amp_organisation where contact_person_name is not null and trim(contact_person_name)!='' ;

/* insert contact properties*/
insert into amp_contact_properties (name,value,contact_id)
(select 'contact email',email,contact_id from amp_contact ac where 
(select count(contact_id) from amp_contact_properties where contact_id=ac.contact_id and value=ac.email and name = 'contact email')=0 );

insert into amp_contact_properties(name,value,contact_id) 
(select 'contact phone',concat('0 ',phone),contact_id from amp_contact ac where 
(select count(contact_id) from amp_contact_properties where contact_id=ac.contact_id and value=ac.phone and name = 'contact phone')=0 );

insert into amp_contact_properties(name,value,contact_id) 
(select 'contact fax',fax,contact_id from amp_contact ac where 
(select count(contact_id) from amp_contact_properties where contact_id=ac.contact_id and value=ac.fax and name = 'contact fax')=0 );

/*remove bad records*/
delete from amp_contact_properties where value is null or trim(value)='' or (name='contact phone' and value='0 ');

/*fill amp_org_contact*/
insert into amp_org_contact(amp_org_id,contact_id,is_primary_contact) 
select organization_id,contact_id,false from amp_contact ac where organization_id is not null and  
(select count(id) from amp_org_contact where organization_id=ac.organization_id and contact_id=ac.contact_id and is_primary_contact is false)=0;

/*drop unused columns*/
alter table amp_contact drop column organization_id;
alter table amp_contact drop column email;
alter table amp_contact drop column phone;
alter table amp_contact drop column fax;

alter table amp_organisation drop column contact_person_name;
alter table amp_organisation drop column contact_person_title;
alter table amp_organisation drop column email;
alter table amp_organisation drop column phone;
alter table amp_organisation drop column fax;