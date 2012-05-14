-- Aid Management Platform 
-- (C) 2007 Development Gateway Foundation
-- Maldives Implementation (1.07) RC2

-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.0.26


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;



--
-- Temporary table structure for view `v_global_settings_countries`
--
DROP TABLE IF EXISTS `v_global_settings_countries`;
DROP VIEW IF EXISTS `v_global_settings_countries`;
CREATE TABLE `v_global_settings_countries` (
  `id` varchar(255),
  `value` varchar(200)
);

--
-- Temporary table structure for view `v_global_settings_public_view`
--
DROP TABLE IF EXISTS `v_global_settings_public_view`;
DROP VIEW IF EXISTS `v_global_settings_public_view`;
CREATE TABLE `v_global_settings_public_view` (
  `id` varchar(255),
  `value` varchar(255)
);

--
-- Temporary table structure for view `v_global_settings_pv_budget_filter`
--
DROP TABLE IF EXISTS `v_global_settings_pv_budget_filter`;
DROP VIEW IF EXISTS `v_global_settings_pv_budget_filter`;
CREATE TABLE `v_global_settings_pv_budget_filter` (
  `id` varchar(255),
  `value` varchar(255)
);

--
-- Definition of table `util_global_settings_possible_values`
--

DROP TABLE IF EXISTS `util_global_settings_possible_values`;
CREATE TABLE  `util_global_settings_possible_values` (
  `id` bigint(20) NOT NULL auto_increment,
  `setting_name` varchar(255) NOT NULL,
  `value_id` varchar(255) NOT NULL,
  `value_shown` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `settingname` (`setting_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `util_global_settings_possible_values`
--

/*!40000 ALTER TABLE `util_global_settings_possible_values` DISABLE KEYS */;
LOCK TABLES `util_global_settings_possible_values` WRITE;
INSERT INTO `util_global_settings_possible_values` VALUES  (1,'Public View','On','On'),
 (2,'Public View','Off','Off'),
 (3,'Public View Budget Filter','On Budget','On Budget'),
 (4,'Public View Budget Filter','Off Budget','Off Budget'),
 (5,'Public View Budget Filter','All','All Activities');
UNLOCK TABLES;
/*!40000 ALTER TABLE `util_global_settings_possible_values` ENABLE KEYS */;


--
-- Definition of view `v_global_settings_countries`
--

DROP TABLE IF EXISTS `v_global_settings_countries`;
DROP VIEW IF EXISTS `v_global_settings_countries`;
CREATE VIEW  `v_global_settings_countries` AS select `dg_countries`.`ISO` AS `id`,`dg_countries`.`COUNTRY_NAME` AS `value` from `dg_countries` order by `dg_countries`.`COUNTRY_NAME`;

--
-- Definition of view `v_global_settings_public_view`
--

DROP TABLE IF EXISTS `v_global_settings_public_view`;
DROP VIEW IF EXISTS `v_global_settings_public_view`;
CREATE VIEW  `v_global_settings_public_view` AS select `util_global_settings_possible_values`.`value_id` AS `id`,`util_global_settings_possible_values`.`value_shown` AS `value` from `util_global_settings_possible_values` where (`util_global_settings_possible_values`.`setting_name` = _latin1'Public View');

--
-- Definition of view `v_global_settings_pv_budget_filter`
--

DROP TABLE IF EXISTS `v_global_settings_pv_budget_filter`;
DROP VIEW IF EXISTS `v_global_settings_pv_budget_filter`;
CREATE VIEW  `v_global_settings_pv_budget_filter` AS select `util_global_settings_possible_values`.`value_id` AS `id`,`util_global_settings_possible_values`.`value_shown` AS `value` from `util_global_settings_possible_values` where (`util_global_settings_possible_values`.`setting_name` = _latin1'Public View Budget Filter');



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;

UPDATE amp_global_settings SET possibleValues='v_global_settings_countries' WHERE settingsName='Default Country';
UPDATE amp_global_settings SET possibleValues='v_global_settings_public_view' WHERE settingsName='Public View';
UPDATE amp_global_settings SET possibleValues='v_global_settings_pv_budget_filter' WHERE settingsName='Public View Budget Filter';
