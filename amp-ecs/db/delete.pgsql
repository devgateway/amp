ALTER TABLE errors DROP CONSTRAINT errOccurrencesKey;

drop table lastOccurrences;
drop table occurrences cascade;
drop table scenes cascade;
drop table errors cascade;
drop table users cascade;
drop table servers cascade;
drop table errorGroup cascade;
