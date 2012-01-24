INSERT INTO amp_ahsurvey_question_type (amp_type_id, name, description)  
VALUES("1", "yes-no", "for yes/no type question") 
ON DUPLICATE KEY UPDATE description = description;
				
INSERT INTO amp_ahsurvey_question_type (amp_type_id, name, description)  
VALUES("2", "calculated", "for calculated type question") 
ON DUPLICATE KEY UPDATE description = description;
				
INSERT INTO amp_ahsurvey_question_type (amp_type_id, name, description)  
VALUES("3", "input", "input from user") 
ON DUPLICATE KEY UPDATE description = description;

INSERT INTO amp_ahsurvey_indicator (amp_indicator_id, name, total_question, indicator_number, indicator_code, status) 
VALUES("1", "Proportion of aid flows to the government sector reported on government budget", "2", "1", "3", NULL) 
ON DUPLICATE KEY UPDATE amp_indicator_id = amp_indicator_id;
				
INSERT INTO amp_ahsurvey_indicator (amp_indicator_id, name, total_question, indicator_number, indicator_code, status) 
VALUES("2", "% of TC flows implemented through coordinated programmes consistent with national development strategies", "2", "2", "4", NULL) 
ON DUPLICATE KEY UPDATE amp_indicator_id = amp_indicator_id;
				
INSERT INTO amp_ahsurvey_indicator (amp_indicator_id, name, total_question, indicator_number, indicator_code, status) 
VALUES("3", "Proportion of aid flows to the public sector using country PFM and Proportion of donors using country PFM", "3", "3", "5a", NULL) 
ON DUPLICATE KEY UPDATE amp_indicator_id = amp_indicator_id;
				
INSERT INTO amp_ahsurvey_indicator (amp_indicator_id, name, total_question, indicator_number, indicator_code, status) 
VALUES("4", "Proportion of aid flows to the public sector using country procurement system and Proportion of donors using country procurement system", "1", "4", "5b", NULL) 
ON DUPLICATE KEY UPDATE amp_indicator_id = amp_indicator_id;
				
INSERT INTO amp_ahsurvey_indicator (amp_indicator_id, name, total_question, indicator_number, indicator_code, status) 
VALUES("5", "Number of parallel PIUs", "1", "5", "6", NULL) 
ON DUPLICATE KEY UPDATE amp_indicator_id = amp_indicator_id;
				
INSERT INTO amp_ahsurvey_indicator (amp_indicator_id, name, total_question, indicator_number, indicator_code, status) 
VALUES("6", "Proportion of aid disbursed within the fiscal year it was scheduled", "1", "6", "7", NULL) 
ON DUPLICATE KEY UPDATE amp_indicator_id = amp_indicator_id;
				
INSERT INTO amp_ahsurvey_indicator (amp_indicator_id, name, total_question, indicator_number, indicator_code, status) 
VALUES("7", "Proportion of aid flows provided as programme-based approaches (PBA)", "1", "7", "9", NULL) 
ON DUPLICATE KEY UPDATE amp_indicator_id = amp_indicator_id;
				
INSERT INTO amp_ahsurvey_indicator (amp_indicator_id, name, total_question, indicator_number, indicator_code, status) 
VALUES("8", "Proportion of donor missions that are joint", "1", "8", "10a", NULL) 
ON DUPLICATE KEY UPDATE amp_indicator_id = amp_indicator_id;
				
INSERT INTO amp_ahsurvey_indicator (amp_indicator_id, name, total_question, indicator_number, indicator_code, status) 
VALUES("9", "Proportion of country analytic work that is joint", "1", "9", "10b", NULL) 
ON DUPLICATE KEY UPDATE amp_indicator_id = amp_indicator_id;

INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status)  
VALUES("1", "In this project / activity, do the disbursements made by this development partner correspond to aid flows in support to the government sector?", "1", "1", "1", NULL) 
ON DUPLICATE KEY UPDATE question_text = "In this project / activity, do the disbursements made by this development partner correspond to aid flows in support to the government sector?";
				
INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status) 
VALUES("2", "In this project / activity, are disbursements made by this development partner reported in the national budget?", "1", "2", "1", NULL) 
ON DUPLICATE KEY UPDATE question_text = "In this project / activity, are disbursements made by this development partner reported in the national budget?";
				
INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status) 
VALUES("3", "In this project / activity, is the funding provided by this particular development partner part of a coordinated programme?", "2", "3", "1", NULL) 
ON DUPLICATE KEY UPDATE question_text = "In this project / activity, is the funding provided by this particular development partner part of a coordinated programme?";
				
INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status) 
VALUES("4", "In this project / activity, what is the technical co-operation share in the funding of this particular development partner?", "2", "4", "3", NULL) 
ON DUPLICATE KEY UPDATE question_text = "In this project / activity, what is the technical co-operation share in the funding of this particular development partner?";
				
INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status) 
VALUES("5", "Does this development partner, in this particular project / activity, use the national budget execution procedures?", "3", "5", "1", NULL) 
ON DUPLICATE KEY UPDATE question_text = "Does this development partner, in this particular project / activity, use the national budget execution procedures?";
				
INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status) 
VALUES("6", "Does this development partner, in this particular project / activity, use the national financial reporting procedures?", "3", "6", "1", NULL) 
ON DUPLICATE KEY UPDATE question_text = "Does this development partner, in this particular project / activity, use the national financial reporting procedures?";
				
INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status) 
VALUES("7", "Does this development partner, in this particular project / activity, use the national auditing procedures?", "3", "7", "1", NULL) 
ON DUPLICATE KEY UPDATE question_text = "Does this development partner, in this particular project / activity, use the national auditing procedures?";
				
INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status) 
VALUES("8", "Does this development partner, in this particular project / activity, use the national procurement procedures?", "4", "8", "1", NULL) 
ON DUPLICATE KEY UPDATE question_text = "Does this development partner, in this particular project / activity, use the national procurement procedures?";
				
INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status) 
VALUES("9", "Does this development partner, in this particular project / activity, use a parallel Project Implementation Unit (PIU)?", "5", "9", "1", NULL) 
ON DUPLICATE KEY UPDATE question_text = "Does this development partner, in this particular project / activity, use a parallel Project Implementation Unit (PIU)?";
				
INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status) 
VALUES("10", "Difference(%) between planned and actual disbursements.", "6", "10", "2", NULL) 
ON DUPLICATE KEY UPDATE question_text = "Difference(%) between planned and actual disbursements.";
				
INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status)  
VALUES("11", "Is this project provided in the context of a programme-based approach?", "7", "11", "1", NULL) 
ON DUPLICATE KEY UPDATE question_text = "Is this project provided in the context of a programme-based approach?";
				
INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status)  
VALUES("12", "Is this mission joint?", "8", "12", "1", NULL) 
ON DUPLICATE KEY UPDATE question_text = "Is this mission joint?";
				
INSERT INTO amp_ahsurvey_question (amp_question_id, question_text, amp_indicator_id, question_number, amp_type_id, status)  
VALUES("13", "Is this country analytic work joint?", "9", "13", "1", NULL) 
ON DUPLICATE KEY UPDATE question_text = "Is this country analytic work joint?";