update amp_fiscal_calendar set is_fiscal=true;
update amp_fiscal_calendar set is_fiscal=false where lower(name) like '%gregorian%' || lower(name) like '%ethiopian%';