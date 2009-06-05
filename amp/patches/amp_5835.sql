delete from amp_report_hierarchy where columnId in
(select columnId from amp_columns where columnName="Project Title");

insert into amp_report_column (amp_report_id, columnId, order_id, cv_level_id )
select amp_report_id, c1.columnId, max(order_id)+1, cv.id 
 from amp_report_column r, amp_columns c1, amp_category_value cv, amp_category_class cc 
 where 
 cv.amp_category_class_id=cc.id AND cc.keyName="activity_level" AND cv.category_value="Level 1" AND
 c1.columnName="Project Title" AND	 
 (select count(amp_report_id) from amp_report_column rc, amp_columns c2 
   where amp_report_id=r.amp_report_id AND rc.columnId=c2.columnId AND 
    c2.columnName not in 
    ("Uncommitted Cumulative Balance","Undisbursed Cumulative Balance","Cumulative Commitment","Cumulative Disbursement") )=
 (select count(amp_report_id) from amp_report_hierarchy where amp_report_id=r.amp_report_id) group by r.amp_report_id;