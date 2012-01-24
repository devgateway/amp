DELIMITER $$
DROP FUNCTION IF EXISTS getProgramSettingId$$


CREATE FUNCTION getProgramSettingId(programId BIGINT(20)) RETURNS bigint(20)
BEGIN
         declare ret bigint(20);
         declare parentId bigint(20);
         declare currentProgramId bigint(20);
         set parentId = 1;
         WHILE parentId is not null 
         DO 
         	set currentProgramId = programId;
         	select parent_theme_id into parentId from amp_theme where amp_theme_id=programId;
         	set programId = parentId;
         end WHILE;
         select amp_program_settings_id into ret from amp_program_settings where default_hierarchy=currentProgramId ;
    return ret;
    
    
    
    end $$


