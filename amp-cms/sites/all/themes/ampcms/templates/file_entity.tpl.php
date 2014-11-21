<?php

/**
 * @file
 * Default theme implementation to display a file.
 */
?>

<?php
  // We hide the links now so that we can render them later.
  hide($content['links']);
  print render($content);
?>
