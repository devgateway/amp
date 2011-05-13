CREATE OR REPLACE VIEW v_regions AS 
select ra.amp_activity_id AS amp_activity_id,cvl.location_name AS region,l.region_location_id AS region_id,
sum(ra.location_percentage) AS location_percentage 
from amp_activity_location ra , amp_location l , amp_category_value_location cvl 
where l.region_location_id is not null and ra.amp_location_id = l.amp_location_id and l.region_location_id = cvl.id
group by ra.amp_activity_id ,cvl.location_name,l.region_location_id
order by ra.amp_activity_id ,cvl.location_name,l.region_location_id;