#!/bin/bash

# amp-clean-gen: AMP Clean Database generator script
# Copyright (c) 2008 Development Gateway Foundation
# @author - Mihai Postelnicu - mpostelnicu@dginternational.org

version=0.2
usage="usage: $0 [mysqldump extra params] database"

if [ ! -n "$1" ]; then
echo $usage
exit 0;
fi
sdads
extra=""
while [ "$#" -gt "1" ]
do
  extra+=$1" "
  shift
done 

database=$1

echo -- amp-clean-gen AMP Clean Database Generator version $version
echo -- mysqldump extra parameters=$extra
echo -- dbatabase=$database
echo -- Use this script to fill an empty AMP database created with 'ant database' target
echo -- Starting AMP dump...

#DUMPING THE CORE TABLES
mysqldump --hex-blob -R --default-character-set=utf8 --single-transaction -c -e --no-create-info $extra $database --tables amp_category_class amp_category_value amp_columns amp_columns_order amp_columns_filters amp_currency amp_global_settings amp_level amp_measures amp_org_type amp_quartz_job_class amp_role amp_widget amp_widget_column amp_widget_place amp_widget_value dg_countries dg_item_status dg_locale dg_module_instance dg_principal_permission dg_principal_permission_param dg_site dg_site_domain patch_file amp_currency_rate dg_message amp_templates_visibility amp_modules_visibility amp_features_visibility amp_fields_visibility dg_site_trans_lang_map dg_site_user_lang_map amp_team_member_roles amp_fields_templates amp_features_templates amp_modules_templates amp_program_settings;

#DUMPING THE ADMIN USER
mysqldump --hex-blob  --default-character-set=utf8 --single-transaction -c -e --no-create-info $extra $database --tables dg_user --where 'email="admin@amp.org"'
mysqldump --hex-blob  --default-character-set=utf8 --single-transaction -c -e --no-create-info $extra $database --tables dg_group --where 'id IN (SELECT ug.group_id FROM dg_user_group ug, dg_user u where u.id=ug.user_id and u.email="admin@amp.org")';
mysqldump --hex-blob  --default-character-set=utf8 --single-transaction -c -e --no-create-info $extra $database --tables dg_user_group --where 'user_id IN (SELECT ug.user_id FROM dg_user_group ug, dg_user u where u.id=ug.user_id and u.email="admin@amp.org")';
mysqldump --hex-blob  --default-character-set=utf8 --single-transaction -c -e --no-create-info $extra $database --tables dg_user_lang_preferences --where 'user_id IN (SELECT ug.user_id FROM dg_user_group ug, dg_user u where u.id=ug.user_id and u.email="admin@amp.org")';

#DUMPING THE APPLICATION SETTINGS
mysqldump --default-character-set=utf8 --single-transaction -c -e --no-create-info $extra $database --tables amp_application_settings --where 'fis_cal_id IN (SELECT id FROM amp_global_settings WHERE settingsName="Default Calendar")';

#DUMPING THE PROGRAM SETTINGS
mysqldump --default-character-set=utf8 --single-transaction -c -e --no-create-info $extra $database --tables amp_program_settings --where 'default_hierarchy=2';
mysqldump --hex-blob  --default-character-set=utf8 --single-transaction -c -e --no-create-info $extra $database --tables amp_theme --where 'amp_theme_id IN (SELECT at.amp_theme_id FROM amp_theme at, amp_program_settings aps WHERE at.amp_theme_id=aps.default_hierarchy AND aps.default_hierarchy=2)'; 

#DUMPIG EXTRA TABLES
mysqldump --hex-blob  --default-character-set=utf8 --single-transaction -c -e $extra $database --tables util_global_settings_possible_;

#DUMPIG THE VIEWS AND QRTZ TABLES
mysqldump --hex-blob  --default-character-set=utf8 --skip-add-drop-table --single-transaction -c -e $extra $database --tables \
`mysql $extra $database -e 'show tables' | grep -E '(v_|qrtz_)' | tr -d '|' | tr '\n' ' '`
