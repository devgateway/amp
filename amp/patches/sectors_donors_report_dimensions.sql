update amp_columns set relatedContentPersisterClass = 'org.digijava.module.aim.dbentity.AmpSector' where columnName in ('Sector','Sub-Sector');
update amp_columns set relatedContentPersisterClass = 'org.digijava.module.aim.dbentity.AmpOrgGroup' where columnName='Donor Group';
update amp_columns set relatedContentPersisterClass = 'org.digijava.module.aim.dbentity.AmpOrgType' where columnName='Donor Type';
update amp_columns set relatedContentPersisterClass = 'org.digijava.module.aim.dbentity.AmpOrganisation' where columnName='Donor Agency';