ALTER TABLE amp_ahsurvey_question ADD tmp bigint(20)NOT NULL;
UPDATE amp_ahsurvey_question AS q, amp_ahsurvey_indicator AS i SET q.tmp=1 WHERE q.amp_indicator_id=i.amp_indicator_id;
DELETE FROM amp_ahsurvey_question WHERE tmp=0;
ALTER TABLE amp_ahsurvey_question DROP COLUMN tmp;

ALTER TABLE amp_ahsurvey_response ADD tmp bigint(20)NOT NULL;
UPDATE amp_ahsurvey_response AS r, amp_ahsurvey_question AS q SET r.tmp=1 WHERE r.amp_question_id=q.amp_question_id;
DELETE FROM amp_ahsurvey_response WHERE tmp=0;
ALTER TABLE amp_ahsurvey_response DROP COLUMN tmp;