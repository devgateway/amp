<?php

/**
 * @file
 * Custom DS CSS Media Object layout template.
 */
?>
<<?php print $layout_wrapper; print $layout_attributes; ?> class="custom-layout mobject <?php print $classes;?>">

  <?php if (isset($title_suffix['contextual_links'])): ?>
  <?php print render($title_suffix['contextual_links']); ?>
  <?php endif; ?>

  <?php if (isset($above)): ?>
  <<?php print $above_wrapper ?> class="group-above <?php print $above_classes; ?>">
    <?php print $above; ?>
  </<?php print $above_wrapper ?>>
  <?php endif; ?>

  <div class="group-mobject clearfix">
    <?php if (isset($side)): ?>
    <<?php print $side_wrapper ?> class="group-side <?php print $side_classes; ?>">
      <?php print $side; ?>
    </<?php print $side_wrapper ?>>
    <?php endif; ?>

    <<?php print $main_wrapper ?> class="group-main <?php print $main_classes; ?>">
      <?php print $main; ?>

      <?php if (isset($drupal_render_children)): ?>
        <?php print $drupal_render_children ?>
      <?php endif; ?>
    </<?php print $main_wrapper ?>>
  </div>

  <?php if (isset($below)): ?>
  <<?php print $below_wrapper ?> class="group-below <?php print $below_classes; ?>">
    <?php print $below; ?>
  </<?php print $below_wrapper ?>>
  <?php endif; ?>

</<?php print $layout_wrapper ?>>
