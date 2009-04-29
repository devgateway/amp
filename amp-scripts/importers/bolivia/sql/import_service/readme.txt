1) Run prepare database, this will perform the changes needed on the database structure, only run it the first time. 
2) Run the procedure_import_bolivia.sql, this will create the procedure for copy the data from SISFIN/SISSIN db to AMP database
3) Setup the import tool editing MigrationToolConsole.exe.config 
4) To rollback changes run undo_database.sql
