<?php

/**
 * @file
 * Custom DS doublelayout layout template.
 *
 * NOTE: It requires a "Class for layout" in the entity display settings.
 */
?>
<<?php print $layout_wrapper; print $layout_attributes; ?> class="custom-layout custom-doublelayout clearfix <?php print $classes;?>">

  <?php if (isset($title_suffix['contextual_links'])): ?>
  <?php print render($title_suffix['contextual_links']); ?>
  <?php endif; ?>

  <<?php print $left_wrapper ?> class="group-left <?php print $left_classes; ?>">
    <?php print $left; ?>
  </<?php print $left_wrapper ?>>

  <<?php print $right_wrapper ?> class="group-right <?php print $right_classes; ?>">
    <?php print $right; ?>
  </<?php print $right_wrapper ?>>

  <?php if (isset($drupal_render_children)): ?>
    <div class="extra">
      <?php print $drupal_render_children ?>
    </div>
  <?php endif; ?>
</<?php print $layout_wrapper ?>>
