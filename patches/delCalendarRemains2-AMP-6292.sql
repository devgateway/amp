DELETE FROM `amp_calendar_attendee`
WHERE `amp_calendar_attendee`.`CALENDAR_ID` NOT IN (SELECT `dg_recurr_event`.`CALENDAR_ID` FROM `dg_recurr_event`);
DELETE FROM `amp_calendar_event_organisatio`
WHERE `amp_calendar_event_organisatio`.`CALENDAR_ID` NOT IN (SELECT `dg_recurr_event`.`CALENDAR_ID` FROM `dg_recurr_event`);
DELETE FROM `amp_calendar`
WHERE `amp_calendar`.`CALENDAR_ID` NOT IN (SELECT `dg_recurr_event`.`CALENDAR_ID` FROM `dg_recurr_event`);
DELETE FROM `dg_calendar_item`
WHERE `dg_calendar_item`.`CALENDAR_ID` NOT IN (SELECT `dg_recurr_event`.`CALENDAR_ID` FROM `dg_recurr_event`);
DELETE FROM `dg_calendar`
WHERE `dg_calendar`.`ID` NOT IN (SELECT `dg_recurr_event`.`CALENDAR_ID` FROM `dg_recurr_event`);