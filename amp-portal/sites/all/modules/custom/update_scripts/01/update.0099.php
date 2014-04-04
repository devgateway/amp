<?php

/**
 * Fix errors in feeds module hook_cron().
 */
$field_name = 'field_feed_item_description';
if (!db_table_exists('field_data_' . $field_name)) {
  $info = field_info_field($field_name);
  field_sql_storage_field_storage_create_field($info);
}

_us_revert_feature('ampp_content_types');

// Conditional fields module is no longer used.
_us_uninstall_module('conditional_fields');
