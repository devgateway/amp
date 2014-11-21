<?php
/**
 * @file
 * Custom layout template.
 */
?>

<div class="custom-layout custom-simplelayout clearfix" <?php if (!empty($css_id)) { print ' id="' . $css_id . '"'; } ?>>

  <?php if ($content['main']): ?>
  <div class="group-main<?php print isset($main_classes) ? $main_classes : ''; ?>">
    <?php print $content['main']; ?>
  </div>
  <?php endif; ?>

</div>
