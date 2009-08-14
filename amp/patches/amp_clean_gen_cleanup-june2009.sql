-- dropping garbage country id refernece in currency table
alter table amp_currency drop foreign key `FK6A2F2C4C50537E03`, drop foreign key `FK6A2F2C4CA07702E`, drop column country_id;
