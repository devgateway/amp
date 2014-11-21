<?php
/**
 * @file
 * Custom layout template.
 */
?>

<div class="custom-layout custom-doublelayout clearfix" <?php if (!empty($css_id)) { print ' id="' . $css_id . '"'; } ?>>

  <div class="group group-left<?php print isset($left_classes) ? $left_classes : ''; ?>">
    <?php print $content['left']; ?>
  </div>

  <div class="group group-right<?php print isset($right_classes) ? $right_classes : ''; ?>">
    <?php print $content['right']; ?>
  </div>

</div>
