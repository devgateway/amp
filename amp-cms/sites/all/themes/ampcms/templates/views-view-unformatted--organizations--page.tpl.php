<?php

/**
 * @file
 * Default simple view template to display a list of rows.
 *
 * @ingroup views_templates
 */
?>
<div class="group-wrapper clearfix">
  <?php if (!empty($title)): ?>
    <?php /*
    @HACK: The organization group taxonomy does not have translation enabled.
    And because this feature HAS to be done in 8h (which is why we use the
    taxonomy.module in the first place) we have to manually call t().
    */ ?>
    <h2 class="group-title"><?php print t($title); ?></h2>
  <?php endif; ?>
  <?php foreach ($rows as $id => $row): ?>
    <div<?php if ($classes_array[$id]) { print ' class="' . $classes_array[$id] .'"';  } ?>>
      <?php print $row; ?>
    </div>
  <?php endforeach; ?>
</div>
