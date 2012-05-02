CREATE OR REPLACE FUNCTION getProgramSettingId(prId NUMBER) RETURN NUMBER
IS
BEGIN
         declare 
         ret NUMBER;
         parentId NUMBER;
         currentProgramId NUMBER;
         programId NUMBER;
	BEGIN
	         parentId := 1;
	         programId := prId;
	         WHILE parentId is not null LOOP
				currentProgramId := programId;
	         	select parent_theme_id into parentId from amp_theme where amp_theme_id=programId;
	         	programId := parentId;
			 END LOOP;
	 		 select amp_program_settings_id into ret from amp_program_settings where default_hierarchy=currentProgramId ;
	 		 return ret;
	END;
END;