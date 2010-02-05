insert into amp_contact_properties (contact_id,name,value) 
select contact_id,'contact email',email from amp_contact where email is not null;

insert into amp_contact_properties (contact_id,name,value) 
select contact_id,'contact phone',phone from amp_contact where phone is not null and phone!='';

insert into amp_contact_properties (contact_id,name,value)  
select contact_id,'contact phone',concat('home ',mobilephone) from amp_contact where mobilephone is not null 
and mobilephone!='';

insert into amp_contact_properties (contact_id,name,value) 
select contact_id,'contact fax',fax from amp_contact where fax is not null and fax!='';

alter table amp_contact drop email;
alter table amp_contact drop phone;
alter table amp_contact drop fax;
alter table amp_contact drop mobilephone;