
INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES('Pledges Titles', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_titles');

INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES('Pledges Donor', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_donor');

INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES('Pledges Aid Modality', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_aid_modality');

INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES('Pledges Primary Sector', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_primary_sector');

INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES('Pledges Regions', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_regions');

INSERT INTO `amp_columns` (`columnName`, `aliasName`, `cellType`, `extractorView`)
VALUES('Pledges Type Of Assistance', '', 'org.dgfoundation.amp.ar.cell.TextCell','v_pledges_type_of_assistance');

insert into amp_columns_order  (columnName, indexOrder) VALUES
('Pledges Columns',25);
