CREATE OR REPLACE VIEW `v_accession_instruments` AS 
select acc.`amp_activity_id` AS amp_activity_id,
acv.category_value AS `name` from amp_activities_categoryvalues acc, amp_category_value acv,
amp_category_class ac where acv.id=amp_categoryvalue_id and ac.id=acv.amp_category_class_id and
ac.keyName='accessioninstr';