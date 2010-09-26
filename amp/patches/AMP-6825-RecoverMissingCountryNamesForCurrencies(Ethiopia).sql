UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'United Kingdom')
WHERE currency_code LIKE 'GBP' AND country_location_id IS NULL;

UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'Australia')
WHERE currency_code LIKE 'AUD' AND country_location_id IS NULL;

UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'Canada')
WHERE currency_code LIKE 'CAD' AND country_location_id IS NULL;

UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'Congo, Democratic Republic Of')
WHERE currency_code LIKE 'CDF' AND country_location_id IS NULL;

UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'Switzerland')
WHERE currency_code LIKE 'CHF' AND country_location_id IS NULL;

UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'China')
WHERE currency_code LIKE 'CNY' AND country_location_id IS NULL;

UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'Japan')
WHERE currency_code LIKE 'JPY' AND country_location_id IS NULL;

UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'Kuwait')
WHERE currency_code LIKE 'KWD' AND country_location_id IS NULL;

UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'Norway')
WHERE currency_code LIKE 'NOK' AND country_location_id IS NULL;

UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'South Africa')
WHERE currency_code LIKE 'RSA' AND country_location_id IS NULL;

UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'Sweden')
WHERE currency_code LIKE 'SEK' AND country_location_id IS NULL;

UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'United States')
WHERE currency_code LIKE 'USD' AND country_location_id IS NULL;

UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'India')
WHERE currency_code LIKE 'INR' AND country_location_id IS NULL;

UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'Ethiopia')
WHERE currency_code LIKE 'ETB' AND country_location_id IS NULL;

UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'Denmark')
WHERE currency_code LIKE 'DKK' AND country_location_id IS NULL;

UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'Czech Republic')
WHERE currency_code LIKE 'CZK' AND country_location_id IS NULL;

UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'Iran')
WHERE currency_code LIKE 'IRR' AND country_location_id IS NULL;

UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'Hungary')
WHERE currency_code LIKE 'HUF' AND country_location_id IS NULL;

UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'Korea (South)')
WHERE currency_code LIKE 'KRW' AND country_location_id IS NULL;

UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'New Zealand (Aotearoa)')
WHERE currency_code LIKE 'NZD' AND country_location_id IS NULL;

UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'Poland')
WHERE currency_code LIKE 'PLN' AND country_location_id IS NULL;

UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'Russian Federation')
WHERE currency_code LIKE 'RUB' AND country_location_id IS NULL;

UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'Saudi Arabia')
WHERE currency_code LIKE 'SAR' AND country_location_id IS NULL;

UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'Slovak Republic')
WHERE currency_code LIKE 'SKK' AND country_location_id IS NULL;

UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'Turkey')
WHERE currency_code LIKE 'TRL' AND country_location_id IS NULL;

UPDATE amp_currency SET country_location_id = (SELECT id FROM amp_category_value_location WHERE location_name LIKE 'United Arab Emirates')
WHERE currency_code LIKE 'AED' AND country_location_id IS NULL;