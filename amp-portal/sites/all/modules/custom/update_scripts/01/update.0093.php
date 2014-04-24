<?php

// Fix ampp_cal date format, it might be stored as an array.
$value = variable_get('date_format_ampp_cal', '');
if (is_array($value) && !empty($value['format'])) {
  variable_set('date_format_ampp_cal', $value['format']);
}
