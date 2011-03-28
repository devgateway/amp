delimiter :
DROP PROCEDURE IF EXISTS updateActivityFY:
CREATE PROCEDURE updateActivityFY()  MODIFIES SQL DATA 
 BEGIN 

 DECLARE done INT DEFAULT 0; 
 DECLARE oldFY VARCHAR(20); 
 DECLARE startFY bigint(20);  
 DECLARE endFY bigint(20); 
 DECLARE x  INT; 
 DECLARE str  VARCHAR(255); 
 DECLARE cur1 CURSOR FOR SELECT fy FROM amp_activity WHERE fy REGEXP '^[0-9]{4}-[0-9]{4}$'; 
 DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1; 

  OPEN cur1; 
  REPEAT 
  FETCH cur1 INTO oldFY; 
  IF NOT done THEN 
   SET startFY = (select CONVERT(SUBSTRING(oldFY,1,locate('-',oldFY)-1), signed) from dual);
   SET endFY = (select CONVERT(SUBSTRING(oldFY,locate('-',oldFY)+1,4), signed) from dual);
   SET x = startFY;
   SET str =  '';
   WHILE x<= endFY DO 
       	SET  str = CONCAT(str,x,',');
        SET  x = x + 1; 
   END WHILE;
   SET str=SUBSTRING(str,1,length(str)-1); 
   update amp_activity a set a.fy=str where a.fy like oldFY; 
 END IF; 
 UNTIL done END REPEAT; 
 CLOSE cur1;
 
END:
call updateActivityFY():
drop PROCEDURE updateActivityFY:
 