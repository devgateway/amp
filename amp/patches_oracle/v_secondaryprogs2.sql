CREATE OR REPLACE VIEW v_secondaryprogram_all_level AS 
select a.amp_activity_id AS amp_activity_id,a.program_percentage AS program_percentage,
b.amp_theme_id AS amp_program_id1, b.name AS n1,b.level_ AS l1,
b1.amp_theme_id AS amp_program_id2, b1.name AS n2, b1.level_ AS l2,
b2.amp_theme_id AS amp_program_id3, b2.name AS n3,b2.level_ AS l3,
b3.amp_theme_id AS amp_program_id4, b3.name AS n4,b3.level_ AS l4,
b4.amp_theme_id AS amp_program_id5, b4.name AS n5,b4.level_ AS l5,
b5.amp_theme_id AS amp_program_id6, b5.name AS n6,b5.level_ AS l6,
b6.amp_theme_id AS amp_program_id7, b6.name AS n7,b6.level_ AS l7,
b7.amp_theme_id AS amp_program_id8, b7.name AS n8,b7.level_ AS l8 
from ((((((((amp_activity_program a join amp_theme b 
on((a.amp_program_id = b.amp_theme_id))) left join amp_theme b1 
on((b1.amp_theme_id = b.parent_theme_id))) left join amp_theme b2 
on((b2.amp_theme_id = b1.parent_theme_id))) left join amp_theme b3 
on((b3.amp_theme_id = b2.parent_theme_id))) left join amp_theme b4 
on((b4.amp_theme_id = b3.parent_theme_id))) left join amp_theme b5
 on((b5.amp_theme_id = b4.parent_theme_id))) left join amp_theme b6 
on((b6.amp_theme_id = b5.parent_theme_id))) left join amp_theme b7
 on((b7.amp_theme_id = b6.parent_theme_id))) where (getProgramSettingId(a.amp_program_id) = 3);

CREATE OR REPLACE VIEW v_secondaryprogram_level_0 AS 
select v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,
(case 0 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end) AS name,
(case 0 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 end) AS amp_program_id,
sum(v_secondaryprogram_all_level.program_percentage) AS program_percentage 
from v_secondaryprogram_all_level  
group by v_secondaryprogram_all_level.amp_activity_id,
(case 0 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end),
(case 0 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 end) having (
(case 0 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end)
is not null);

CREATE OR REPLACE VIEW v_secondaryprogram_level_1 AS 
select v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,
(case 1 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end) AS name,
(case 1 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 end) AS amp_program_id,
sum(v_secondaryprogram_all_level.program_percentage) AS program_percentage 
from v_secondaryprogram_all_level  
group by
 v_secondaryprogram_all_level.amp_activity_id,
(case 1 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end),
(case 1 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 end)
having ((case 1 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end)  is not null);


CREATE OR REPLACE VIEW v_secondaryprogram_level_2 AS 
select v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,
(case 2 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end) AS name,
(case 2 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 end) AS amp_program_id,
sum(v_secondaryprogram_all_level.program_percentage) AS program_percentage 
from v_secondaryprogram_all_level  
group by 
v_secondaryprogram_all_level.amp_activity_id,
(case 2 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end),
(case 2 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 end)
having ((case 2 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end) is not null);


CREATE OR REPLACE VIEW v_secondaryprogram_level_3 AS 
select v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,
(case 3 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end) AS name,
(case 3 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 end) AS amp_program_id,
sum(v_secondaryprogram_all_level.program_percentage) AS program_percentage 
from v_secondaryprogram_all_level  
group by 
v_secondaryprogram_all_level.amp_activity_id,
(case 3 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end),
(case 3 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 end)
having ((case 3 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end) is not null);

CREATE OR REPLACE VIEW v_secondaryprogram_level_4 AS 
select v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,
(case 4 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end) AS name,
(case 4 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 end) AS amp_program_id,
sum(v_secondaryprogram_all_level.program_percentage) AS program_percentage 
from v_secondaryprogram_all_level  
group by
v_secondaryprogram_all_level.amp_activity_id,
(case 4 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end),
(case 4 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 end)
having ((case 4 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end) is not null);


CREATE OR REPLACE VIEW v_secondaryprogram_level_5 AS 
select v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,
(case 5 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end) AS name,
(case 5 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 end) AS amp_program_id,
sum(v_secondaryprogram_all_level.program_percentage) AS program_percentage 
from v_secondaryprogram_all_level  
group by v_secondaryprogram_all_level.amp_activity_id,
(case 5 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end),
(case 5 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 end)
having ((case 5 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end) is not null);

CREATE OR REPLACE VIEW v_secondaryprogram_level_6 AS 
select v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,
(case 6 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end) AS name,
(case 6 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 end) AS amp_program_id,
sum(v_secondaryprogram_all_level.program_percentage) AS program_percentage 
from v_secondaryprogram_all_level  
group by v_secondaryprogram_all_level.amp_activity_id,
(case 6 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end),
(case 6 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 end)
having ((case 6 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end)  is not null);

CREATE OR REPLACE VIEW v_secondaryprogram_level_7 AS 
select v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,
(case 7 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end) AS name,
(case 7 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 end) AS amp_program_id,
sum(v_secondaryprogram_all_level.program_percentage) AS program_percentage 
from v_secondaryprogram_all_level  
group by  v_secondaryprogram_all_level.amp_activity_id,
(case 7 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end),
(case 7 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 end)
having ((case 7 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end) is not null);

CREATE OR REPLACE VIEW v_secondaryprogram_level_8 AS 
select v_secondaryprogram_all_level.amp_activity_id AS amp_activity_id,
(case 8 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end) AS name,
(case 8 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 end) AS amp_program_id,
sum(v_secondaryprogram_all_level.program_percentage) AS program_percentage 
from v_secondaryprogram_all_level  
group by 
v_secondaryprogram_all_level.amp_activity_id,
(case 8 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end),
(case 8 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.amp_program_id1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.amp_program_id2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.amp_program_id3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.amp_program_id4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.amp_program_id5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.amp_program_id6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.amp_program_id7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.amp_program_id8 end)
having ((case 8 
when v_secondaryprogram_all_level.l1 then v_secondaryprogram_all_level.n1 
when v_secondaryprogram_all_level.l2 then v_secondaryprogram_all_level.n2 
when v_secondaryprogram_all_level.l3 then v_secondaryprogram_all_level.n3 
when v_secondaryprogram_all_level.l4 then v_secondaryprogram_all_level.n4 
when v_secondaryprogram_all_level.l5 then v_secondaryprogram_all_level.n5 
when v_secondaryprogram_all_level.l6 then v_secondaryprogram_all_level.n6 
when v_secondaryprogram_all_level.l7 then v_secondaryprogram_all_level.n7 
when v_secondaryprogram_all_level.l8 then v_secondaryprogram_all_level.n8 end) is not null);



update amp_columns set relatedContentPersisterClass='org.digijava.module.aim.dbentity.AmpTheme' where columnName like 'Secondary Program%';
