INSERT INTO amp_category_class(category_name,is_multiselect,is_ordered, keyName)
VALUES('Pledges Names', b'0', b'1', 'pledges_names');

INSERT INTO amp_category_value ( category_value, amp_category_class_id, index_column) 
SELECT temp.title, temp.id, @rownum:=@rownum+1 AS rownum 
FROM (SELECT @rownum:=-1) r,
((SELECT DISTINCT fp.title as title, acc.id as id FROM amp_funding_pledges fp, amp_category_class acc WHERE acc.category_name ='Pledges Names') as temp);

UPDATE amp_funding_pledges afp, amp_category_value acv 
    SET afp.title = acv.id 
    WHERE afp.title = acv.category_value;

ALTER TABLE `amp_funding_pledges` MODIFY COLUMN `title` BIGINT(20)  DEFAULT NULL;

CREATE OR REPLACE VIEW `v_pledges_titles` AS 
SELECT 
    `p`.`id` AS `pledge_id`,
    `val`.`category_value` AS `title`,
    `val`.`id` AS `title_id` 
FROM 
  	(`amp_funding_pledges` `p` join `amp_category_value` `val` on (`p`.`title` = `val`.`id`))
ORDER BY 
    `p`.`id`;
    
    