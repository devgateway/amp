--insert fake orgs:
insert into amp_organisation (amp_org_id,name) 
(select distinct(f.amp_donor_org_id), 'Fake Org' from 
amp_funding f where f.amp_donor_org_id not in (select amp_org_id from amp_organisation));


-- add foreign key:
ALTER TABLE `amp_bolivia`.`amp_funding` ADD CONSTRAINT `donor_org_id` FOREIGN KEY `donor_org_id` (`amp_donor_org_id`)
    REFERENCES `amp_organisation` (`amp_org_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT;