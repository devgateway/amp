update amp_fiscal_calendar set is_fiscal=false;
update amp_fiscal_calendar set is_fiscal=true where lower(name) like '%fiscal%';