CREATE INDEX amp_currency_to_currency_code ON `amp_currency_rate`(to_currency_code);
CREATE INDEX amp_currency_exchange_rate_date ON `amp_currency_rate`(exchange_rate_date);
CREATE INDEX amp_currency_exchange_rate_date_to_currency_idx ON `amp_currency_rate`(exchange_rate_date, to_currency_code);


