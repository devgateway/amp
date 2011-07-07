ALTER TABLE amp_regional_funding MODIFY COLUMN region_id BIGINT(20)  DEFAULT NULL;
ALTER TABLE amp_category_value ADD INDEX `cv_index`(`category_value`);

INSERT INTO amp_category_value_location(location_name, description, gs_lat, gs_long, geo_code, code,
parent_location, parent_category_value)
    SELECT r.name, r.description, r.gs_lat, r.gs_long, r.geo_code, r.region_code,
    l.id, v.id FROM amp_region r, dg_countries c,
        amp_category_value_location l, amp_category_value v
        WHERE r.country_id=c.iso AND l.iso=c.iso AND v.category_value="Region";


INSERT INTO amp_category_value_location(location_name, description, gs_lat, gs_long, geo_code, code,
parent_location, parent_category_value)
    SELECT z.name, z.description, z.gs_lat, z.gs_long, z.geo_code, z.zone_code,
    l.id, v.id FROM amp_zone z, amp_region r,
        amp_category_value_location l, amp_category_value v
        WHERE r.amp_region_id=z.region_id AND l.location_name=r.name AND v.category_value="Zone";


INSERT INTO amp_category_value_location(location_name, description, gs_lat, gs_long, geo_code, code,
parent_location, parent_category_value)
    SELECT w.name, w.description, w.gs_lat, w.gs_long, w.geo_code, w.woreda_code,
    l.id, v.id FROM amp_woreda w, amp_zone z,
        amp_category_value_location l, amp_category_value v
        WHERE w.zone_id=z.amp_zone_id AND l.location_name=z.name AND v.category_value="District";


update amp_regional_funding f,amp_region r,  amp_category_value_location l set f.region_location_id = l.id
    WHERE l.location_name = r.name AND f.region_id=r.amp_region_id;




UPDATE amp_location al, amp_category_value_location l, amp_category_value v, dg_countries c
    SET al.location_id = l.id
    WHERE ( al.country = c.country_name OR al.country_id = c.iso ) AND c.iso = l.iso
    AND l.parent_category_value=v.id AND v.category_value="Country"; 


UPDATE amp_location al, amp_category_value_location l, amp_category_value v, amp_region r
    SET al.location_id = l.id, al.region_location_id = l.id
    WHERE ( al.region_id = r.amp_region_id ) AND r.name = l.location_name
    AND l.parent_category_value=v.id AND v.category_value="Region";

UPDATE amp_location al, amp_category_value_location l, amp_category_value v, amp_zone z
    SET al.location_id = l.id
    WHERE ( al.zone_id = z.amp_zone_id ) AND z.name = l.location_name
    AND l.parent_category_value=v.id AND v.category_value="Zone";

UPDATE amp_location al, amp_category_value_location l, amp_category_value v, amp_woreda w
    SET al.location_id = l.id
    WHERE ( al.woreda_id = w.amp_woreda_id ) AND w.name = l.location_name
    AND l.parent_category_value=v.id AND v.category_value="District";


CREATE OR REPLACE  VIEW `v_regional_funding` AS 
 select `f`.`activity_id` AS `amp_activity_id`,
 `f`.`amp_regional_funding_id` AS `amp_regional_funding_id`,
 `f`.`amp_regional_funding_id` AS `amp_fund_detail_id`,
 `r`.`location_name` AS `region_name`,
 `f`.`transaction_type` AS `transaction_type`,
 `f`.`adjustment_type` AS `adjustment_type`,`f`.`transaction_date` AS `transaction_date`,
 `f`.`transaction_amount` AS `transaction_amount`,
 `c`.`currency_code` AS `currency_code`,`f`.`region_id` AS `region_id` 
 from (((`amp_regional_funding` `f` join `amp_category_value_location` `r`) join `amp_currency` `c`) 
   join amp_category_value v) 
 where ((`c`.`amp_currency_id` = `f`.`currency_id`) and (`f`.`region_location_id` = `r`.`id`) and 
   (r.parent_category_value = v.id) and (v.category_value = "Region") ) 
 order by `f`.`activity_id`;


CREATE OR REPLACE VIEW `v_regions` AS 
select `ra`.`amp_activity_id` AS `amp_activity_id`,
`l`.`region` AS `region`,`l`.`region_location_id` AS `region_id`,
sum(`ra`.`location_percentage`) AS `location_percentage` 
from ((`amp_activity_location` `ra` join `amp_location` `l` 
on((`ra`.`amp_location_id` = `l`.`amp_location_id`))) join amp_category_value_location cvl) 
where (`l`.`region_id` is not null AND l.location_id = cvl.id) 
group by `ra`.`amp_activity_id`,`l`.`region_id` 
order by `ra`.`amp_activity_id`,`l`.`name`;




