use amp_bolivia;
SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE CLASI_PND(
  `code` varchar(10) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY(`code`)
  );


ALTER TABLE AMP_ORGANISATION
ADD COLUMN old_id varchar(255),
ADD INDEX (old_id);

ALTER TABLE AMP_CATEGORY_VALUE
ADD COLUMN old_id varchar(30),
ADD INDEX (old_id);

ALTER TABLE AMP_LEVEL
ADD COLUMN old_id varchar(30),
ADD INDEX (old_id);

ALTER TABLE AMP_ACTIVITY
ADD COLUMN old_id bigint(20),
ADD COLUMN old_status_id varchar(20),
ADD COLUMN classi_code varchar(10),
ADD INDEX (old_id),
ADD INDEX (old_status_id),
ADD INDEX (amp_id);


ALTER TABLE AMP_SECTOR
ADD COLUMN old_id varchar(20),
ADD INDEX (old_id);

ALTER TABLE amp_terms_assist
ADD COLUMN old_id varchar(20),
ADD INDEX (old_id);

ALTER table amp_region
ADD INDEX (region_code);

ALTER TABLE amp_components ADD COLUMN CodigoSISIN VARCHAR(13) AFTER `type`;
ALTER TABLE amp_components ADD INDEX `Index_CodigoSISIN` USING BTREE(`CodigoSISIN`);
