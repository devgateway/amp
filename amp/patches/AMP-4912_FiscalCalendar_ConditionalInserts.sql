/* Create a basic calendar if not exist with id = 1 */
BEGIN;

INSERT INTO amp_fiscal_calendar (amp_fiscal_cal_id, start_month_num, year_offset, start_day_num, name, description, base_cal)
VALUES(1, 1, 0, 1, 'Gregorian Calendar', '', 'GREG-CAL')
ON DUPLICATE KEY UPDATE amp_fiscal_cal_id = amp_fiscal_cal_id;

COMMIT;

/* Use that calendar */
UPDATE amp_application_settings SET fis_cal_id = 1 WHERE fis_cal_id IS NULL;