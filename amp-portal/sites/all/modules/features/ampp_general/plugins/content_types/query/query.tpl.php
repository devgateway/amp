<?php
  if ($vars['success'] == 0) {
    print '<table class="query_plugin_table"><tr><td>' . (isset($vars['error_msg'])) ? $vars['error_msg'] : '' . '</td></tr></table>';
  } else {
    print '<table class="query_plugin_table"><tr>';
    foreach ($vars['labels'] as $k => $v) {
      print '<th class="heading-' . $v . '">' . $v . '</th>';
    }
    print '</tr>';
    foreach ($vars['values'] as $k => $v) {
      print '<tr>';
      foreach ($v as $kk => $vv) {
        print '<td class="col-' . $vv . '">' . $vv . '</td>';
      }
      print '</tr>';
    }
    print '</table>';
 }
?>
