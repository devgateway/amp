delete  from AMP_REPORT_COLUMN where COLUMNID in (select x.columnid from amp_columns x where x.extractorView='v_nationalobjectives_parent');
delete  from AMP_REPORT_HIERARCHY where COLUMNID in (select x.columnid from amp_columns x where x.extractorView='v_nationalobjectives_parent');
delete from amp_columns  x where x.extractorView='v_nationalobjectives_parent';