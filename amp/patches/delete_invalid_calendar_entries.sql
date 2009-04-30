delete from amp_calendar_donors_map where calendar_id in
(
select calendar_id from amp_calendar where user_id not in
(select id from dg_user)
);

delete from amp_calendar_attendee where calendar_id in
(
select calendar_id from amp_calendar where user_id not in
(select id from dg_user)
);

delete from amp_calendar where user_id not in
(select id from dg_user);