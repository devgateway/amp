<?php
$date_types = array(
    'Calendar' => array (
        'machine_name' => 'ampp_cal',
        'format' => 'm/d/Y'),
);

$existing_date_formats = system_get_date_formats('custom');

//check date format types

foreach ($date_types as $date_type => $date_format) {
    $format = array(    'format' => $date_format['format'],
                        'type' => 'ampp_cal',
                        'locked' => 1,
                        'is_new' => 1
                     );
    $dfid = 0;
    if(!empty($existing_date_formats) && array_key_exists($date_format['format'], $existing_date_formats)) {
        $format['is_new'] = 0;
        $dfid = $existing_date_formats[$date_format]['dfid'];
    }

    system_date_format_save($format, $dfid);
}

$existing_date_types = system_get_date_types();
foreach ($date_types as $date_type => $format) {
    if(!empty($existing_date_types) && array_key_exists($format['machine_name'], $existing_date_types)) {
          variable_set('date_format_' . $format['machine_name'], $format['format']);
          continue;
    }

    $format_type = array();
    $format_type['title'] = $date_type;
    $format_type['type'] = $format['machine_name'];
    $format_type['locked'] = 1;
    $format_type['is_new'] = 1;
    $format_type['module'] = 'ampp_general';

    system_date_format_type_save($format_type);
    variable_set('date_format_' . $format['machine_name'], $format['format']);
}
