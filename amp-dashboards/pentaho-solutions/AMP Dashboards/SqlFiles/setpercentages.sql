-- Set percentage

select 'update cached_v_responsible_organisation set percentage=' || (100/count(amp_activity_id)) || ' where amp_activity_id = ' || amp_activity_id || ';' as query
from cached_v_responsible_organisation
where percentage is null
group by amp_activity_id;

select 'update cached_v_implementing_agency set percentage=' || (100/count(amp_activity_id)) || ' where amp_activity_id = ' || amp_activity_id || ';' as query
from cached_v_implementing_agency
where percentage is null
group by amp_activity_id;

select 'update cached_v_beneficiary_agency set percentage=' || (100/count(amp_activity_id)) || ' where amp_activity_id = ' || amp_activity_id || ';' as query
from cached_v_beneficiary_agency
where percentage is null
group by amp_activity_id;

select 'update v_zones_special set percentage=' || (100/count(amp_activity_id)) || ' where amp_activity_id = ' || amp_activity_id || ';' as query
from v_zones_special
where location_percentage is null
group by amp_activity_id;

-- Poverty data update
SELECT 'update pentaho_poverty_data set zone = ''' || acvl2.location_name || ''' where district = ''' || ppd.district || ''';'
from pentaho_poverty_data ppd, amp_category_value_location acvl, amp_category_value_location acvl2
where acvl.location_name like '%' || ppd.district  || '%' and acvl2.id = acvl.parent_location
group by ppd.district, acvl2.location_name
order by acvl2.location_name;

update pentaho_poverty_data set region = 'Mid-Western (Madhya Pashchimanchal)' where region = 'Mid-Western';
update pentaho_poverty_data set region = 'Far-Western (Sudur Pashchimanchal)' where region = 'Far-Western';
update pentaho_poverty_data set region = 'Central (Madhyamanchal)' where region = 'Central';
update pentaho_poverty_data set region = 'Western (Pashchimanchal)' where region = 'West';
update pentaho_poverty_data set region = 'Eastern (Purwanchal)' where region = 'East';




