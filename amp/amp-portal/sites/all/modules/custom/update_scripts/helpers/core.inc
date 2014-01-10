<?php

/**
 * @file
 * Update Scripts helper functions.
 */

function db_set_next_id($tablename, $value) {
  $current = db_next_id(db_query("SELECT MAX(vid) FROM {" . $tablename . "}")->fetchField());
  
  if ($current < $value) {
    db_query("ALTER TABLE {" . $tablename . "} AUTO_INCREMENT = " . $value);
    return db_next_id(db_query("SELECT MAX(vid) FROM {" . $tablename . "}")->fetchField());
  }
}
function ____db_set_next_id($tablename, $value) {
  $current = db_next_id(db_query("SELECT MAX(vid) FROM {" . $tablename . "}")->fetchField());
  
  if ($current < $value) {
    db_query("ALTER TABLE {" . $tablename . "} AUTO_INCREMENT = " . $value);
    $updated = db_next_id(db_query("SELECT MAX(vid) FROM {" . $tablename . "}")->fetchField());
  }
  return $current . ' > ' . $updated . ' > ' . $value;
}

