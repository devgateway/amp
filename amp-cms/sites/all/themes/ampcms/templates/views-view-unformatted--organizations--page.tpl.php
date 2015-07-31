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
    <h2 class="group-title"><?php print $title; ?></h2>
  <?php endif; ?>
  <?php foreach ($rows as $id => $row): ?>
    <div<?php if ($classes_array[$id]) { print ' class="' . $classes_array[$id] .'"';  } ?>>
      <?php print $row; ?>
    </div>
  <?php endforeach; ?>
</div>
