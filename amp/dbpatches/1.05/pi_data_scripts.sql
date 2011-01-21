DROP TABLE IF EXISTS `amp_ahsurvey_indicator`;
CREATE TABLE `amp_ahsurvey_indicator` (
  amp_indicator_id bigint(20) NOT NULL auto_increment,
  name varchar(255) ,
  total_question int(11) ,
  indicator_number int(11) ,
  indicator_code varchar(255) ,
  status varchar(255) ,
  PRIMARY KEY (amp_indicator_id)
);

INSERT INTO amp_ahsurvey_indicator (amp_indicator_id, name, total_question, indicator_number, indicator_code, status) 
VALUES("1", "Proportion of aid flows to the government sector reported on government budget", "2", "1", "3", NULL);

INSERT INTO amp_ahsurvey_indicator (amp_indicator_id, name, total_question, indicator_number, indicator_code, status) 
VALUES("2", "% of TC flows implemented through coordinated programmes consistent with national development strategies", "2", "2", "4", NULL);

INSERT INTO amp_ahsurvey_indicator (amp_indicator_id, name, total_question, indicator_number, indicator_code, status) 
VALUES("3", "Proportion of aid flows to the public sector using country PFM and Proportion of donors using country PFM", "3", "3", "5a", NULL);

INSERT INTO amp_ahsurvey_indicator (amp_indicator_id, name, total_question, indicator_number, indicator_code, status) 
VALUES("4", "Proportion of aid flows to the public sector using country procurement system and Proportion of donors using country procurement system", "1", "4", "5b", NULL);

INSERT INTO amp_ahsurvey_indicator (amp_indicator_id, name, total_question, indicator_number, indicator_code, status) 
VALUES("5", "Number of parallel PIUs", "1", "5", "6", NULL);

INSERT INTO amp_ahsurvey_indicator (amp_indicator_id, name, total_question, indicator_number, indicator_code, status) 
VALUES("6", "Proportion of aid disbursed within the fiscal year it was scheduled", "1", "6", "7", NULL);

INSERT INTO amp_ahsurvey_indicator (amp_indicator_id, name, total_question, indicator_number, indicator_code, status) 
VALUES("7", "Proportion of aid flows provided as programme-based approaches (PBA)", "1", "7", "9", NULL);

INSERT INTO amp_ahsurvey_indicator (amp_indicator_id, name, total_question, indicator_number, indicator_code, status) 
VALUES("8", "Proportion of donor missions that are joint", "1", "8", "10a", NULL);

INSERT INTO amp_ahsurvey_indicator (amp_indicator_id, name, total_question, indicator_number, indicator_code, status) 
VALUES("9", "Proportion of country analytic work that is joint", "1", "9", "10b", NULL);


DROP TABLE IF EXISTS `amp_ahsurvey_question`;
CREATE TABLE `amp_ahsurvey_question` (
  amp_question_id bigint(20) NOT NULL auto_increment,
  question_text varchar(255) ,
  amp_indicator_id bigint(20) NOT NULL DEFAULT '0' ,
  question_number int(11) ,
  amp_type_id bigint(20) NOT NULL DEFAULT '0' ,
  status varchar(255) ,
  PRIMARY KEY (amp_question_id),
   KEY FK1C33F249A593FCC5 (amp_type_id),
   KEY FK1C33F24970D0B3E6 (amp_indicator_id)
);

INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status) 
VALUES("1", "Is this project corresponding to aid flows to the government sector?", "1", "1", "1", NULL);

INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status) 
VALUES("2", "Are disbursements of this project reported on the national budget?", "1", "2", "1", NULL);

INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status) 
VALUES("3", "Is the project part of a coordinated programme?", "2", "3", "1", NULL);

INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status) 
VALUES("4", "What is the technical co-operation share of this project/programme?", "2", "4", "3", NULL);

INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status) 
VALUES("5", "Does this project use the national budget execution procedures?", "3", "5", "1", NULL);

INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status) 
VALUES("6", "Does this project use the national financial reporting procedures?", "3", "6", "1", NULL);

INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status) 
VALUES("7", "Does this project use the national auditing procedures?", "3", "7", "1", NULL);

INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status) 
VALUES("8", "Is this project managed using the national procurement procedures?", "4", "8", "1", NULL);

INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status) 
VALUES("9", "Does the project use a parallel Project Implementation Unit (PIU)?", "5", "9", "1", NULL);

INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status) 
VALUES("10", "Difference(%) between planned and actual disbursements.", "6", "10", "2", NULL);

INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status) 
VALUES("11", "Is this project provided in the context of a programme-based approach?", "7", "11", "1", NULL);

INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status) 
VALUES("12", "Is this mission joint?", "8", "12", "1", NULL);

INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status) 
VALUES("13", "Is this country analytic work  joint?", "9", "13", "1", NULL);



DROP TABLE IF EXISTS `amp_ahsurvey_question_type`;
CREATE TABLE amp_ahsurvey_question_type (
  amp_type_id bigint(20) NOT NULL auto_increment,
  name varchar(255) ,
  description varchar(255) ,
  PRIMARY KEY (amp_type_id)
);


INSERT INTO amp_ahsurvey_question_type (amp_type_id, name, description) 
VALUES("1", "yes-no", "for yes/no type question");
INSERT INTO amp_ahsurvey_question_type (amp_type_id, name, description) 
VALUES("2", "calculated", "for calculated type question");
INSERT INTO amp_ahsurvey_question_type (amp_type_id, name, description) 
VALUES("3", "input", "input from user");