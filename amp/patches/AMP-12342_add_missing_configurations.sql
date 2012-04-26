INSERT INTO amp_program_settings (allow_multiple, name)
SELECT true, 'Primary Program' from dual where not exists (SELECT * FROM amp_program_settings where name='Primary Program');
INSERT INTO amp_program_settings (allow_multiple, name)
SELECT true, 'Secondary Program' from dual where not exists (SELECT * FROM amp_program_settings where name='Secondary Program');