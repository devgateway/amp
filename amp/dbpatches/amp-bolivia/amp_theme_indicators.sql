-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.0.24-standard


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema amp_bolivia
--


DROP TABLE amp_theme_indicators;
CREATE TABLE `amp_theme_indicators` (
  `amp_theme_ind_id` bigint(20) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `code` varchar(255) default NULL,
  `type` varchar(255) default NULL,
  `creation_date` datetime default NULL,
  `value_type` int(11) default NULL,
  `category` int(11) default NULL,
  `np_indicator` tinyint(1) default NULL,
  `description` text,
  PRIMARY KEY  (`amp_theme_ind_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
INSERT INTO `amp_theme_indicators` (`amp_theme_ind_id`,`name`,`code`,`type`,`creation_date`,`value_type`,`category`,`np_indicator`,`description`) VALUES 
 (1,'GDP Growth','12345','A','2007-02-01 00:00:00',1,3,0,NULL),
 (5,'Primary Health Care Coverage','HEA2','A','2006-11-20 00:00:00',0,1,1,'test'),
 (12,'Persons living below 1 USD per day','ECO3','D',NULL,NULL,3,0,NULL),
 (13,'Polio Vaccination Coverage','HLT1','A',NULL,NULL,1,0,NULL),
 (14,'Primary Education Coverage','EDU1','A',NULL,NULL,0,1,'gfhdgfhd'),
 (45,'Mortalidad infantil','12345','descending','2007-01-16 00:00:00',NULL,3,1,'tasa de mortalidad infantil (por mil nacidos vivos)'),
 (46,'medicos por cada 1,000 personas','123456','ascending','2007-01-01 00:00:00',NULL,1,1,'medicos por cada 1,000 personas'),
 (47,'Horas de Educacion de salud en escuelas primarias','001234','ascending','2007-01-16 00:00:00',NULL,1,1,'Horas de Educacion de salud en escuelas primarias'),
 (48,'Horas de educacion fisica en las escuelas primarias','0012345','asecnding','2007-01-01 00:00:00',NULL,1,1,'Horas de educacion fisica en las escuelas primarias'),
 (50,'Pobreza extrema y el hambre','011','descending','2007-01-01 00:00:00',NULL,3,1,'MDG - Pobreza extrema y el hambre');
INSERT INTO `amp_theme_indicators` (`amp_theme_ind_id`,`name`,`code`,`type`,`creation_date`,`value_type`,`category`,`np_indicator`,`description`) VALUES 
 (51,'Free Secondary Education','EDU2','D','2007-01-16 00:00:00',NULL,0,0,NULL),
 (52,'Local Government services training','NSSDS/13','A','2007-01-01 00:00:00',NULL,1,1,'TEST'),
 (53,'Teachers training','NSSDS/12','A','2007-01-24 00:00:00',NULL,0,0,NULL),
 (55,'Houses built','housing','A',NULL,NULL,1,1,NULL),
 (56,'Civil Servants Training','NSSDS/11','A','2007-02-01 00:00:00',NULL,0,0,'test'),
 (59,'system1','sys1','A','2007-02-15 00:00:00',NULL,0,0,'wwww'),
 (60,'ind1','i1','A','2007-02-16 00:00:00',NULL,0,0,'test'),
 (61,'ind2','i2','A','2007-02-16 00:00:00',NULL,0,0,'dddddddd');



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
