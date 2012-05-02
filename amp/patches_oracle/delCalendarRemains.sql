delete from dg_recurr_event where CALENDAR_ID in (select ID from dg_calendar where ID not in (select CALENDAR_ID from amp_calendar));
delete from dg_calendar_item where CALENDAR_ID in (select ID from dg_calendar where ID not in (select CALENDAR_ID from amp_calendar));
delete from dg_calendar where ID not in (select CALENDAR_ID from amp_calendar);
