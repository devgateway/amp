delimiter $
DROP PROCEDURE IF EXISTS updateMessageStates$
CREATE PROCEDURE updateMessageStates()  MODIFIES SQL DATA 
 BEGIN

 DECLARE done INT DEFAULT 0;
 DECLARE memberId bigint(20);
 DECLARE cur1 CURSOR FOR SELECT DISTINCT member_Id FROM amp_message_state;
 DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

 UPDATE amp_message_settings SET msg_storage_per_msg_type=20;

 CREATE TEMPORARY TABLE tempStateId(message_state_id bigint(20) NOT NULL);

 UPDATE amp_message_state set   is_message_hidden=true where is_message_hidden is NULL;



 OPEN cur1; 


REPEAT 
FETCH cur1 INTO memberId; 
IF NOT done THEN 

INSERT INTO tempStateId (select message_state_Id from amp_message_state 
inner join amp_message on amp_message_Id=message_id 
where member_Id=memberId  and (is_message_hidden=false or is_message_hidden is NULL) 
and   message_clazz='a' 
order by creation_date asc LIMIT 20);    

INSERT INTO tempStateId (select message_state_Id from amp_message_state 
inner join amp_message on amp_message_Id=message_id 
where member_Id=memberId  and (is_message_hidden=false or is_message_hidden is NULL) 
and   message_clazz='c' 
order by creation_date asc LIMIT 20); 

INSERT INTO tempStateId (select message_state_Id from amp_message_state 
inner join amp_message on amp_message_Id=message_id 
where member_Id=memberId  and (is_message_hidden=false or is_message_hidden is NULL) 
and   message_clazz='p' 
order by creation_date asc LIMIT 20); 

INSERT INTO tempStateId (select message_state_Id from amp_message_state 
inner join amp_message on amp_message_Id=message_id 
where member_Id=memberId  and (is_message_hidden=false or is_message_hidden is NULL) 
and   message_clazz='u' 
order by creation_date asc LIMIT 20); 

END IF; 
UNTIL done END REPEAT; 
CLOSE cur1; 

UPDATE amp_message_state set   is_message_hidden=true where  message_state_Id not in (SELECT message_state_id from tempStateId); 

DROP TABLE tempStateId; 

END$ 
call updateMessageStates()$
drop PROCEDURE updateMessageStates$
 
