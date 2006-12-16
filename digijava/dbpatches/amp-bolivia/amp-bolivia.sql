ALTER TABLE amp_me_indicators DROP value_type;
ALTER TABLE amp_me_indicators DROP creation_date;
ALTER TABLE amp_me_indicators DROP type;
ALTER TABLE amp_me_indicators DROP np_indicator;
ALTER TABLE amp_me_indicators DROP category;
ALTER TABLE amp_theme_indicators MODIFY np_indicator TINYINT(1);
ALTER TABLE amp_theme_indicators DROP value_type;
ALTER TABLE amp_theme_indicators DROP creation_date;
