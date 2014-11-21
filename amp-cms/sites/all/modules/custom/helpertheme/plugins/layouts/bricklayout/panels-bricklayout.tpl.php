<?php
/**
 * @file
 * Custom layout template.
 */
?>

<div class="custom-layout custom-bricklayout clearfix" <?php if (!empty($css_id)) { print ' id="' . $css_id . '"'; } ?>>

  <?php if ($content['col_a1']): ?>
    <div class="cl-row cl-row-a cols-1 clearfix">
      <div class="col col-1"><div class="col-inner<?php print isset($col_a1_classes) ? $col_a1_classes : ''; ?>">
        <?php print $content['col_a1']; ?>
      </div></div>
    </div>
  <?php endif; ?>

  <?php if ($content['col_b1']): ?>
    <div class="cl-row cl-row-b cols-1 clearfix">
      <div class="col col-1"><div class="col-inner<?php print isset($col_b1_classes) ? $col_b1_classes : ''; ?>">
        <?php print $content['col_b1']; ?>
      </div></div>
    </div>
  <?php endif; ?>

  <?php if ($content['col_c1'] || $content['col_c2']): ?>
    <div class="cl-row cl-row-c cols-2 clearfix">
      <div class="col col-1"><div class="col-inner<?php print isset($col_c1_classes) ? $col_c1_classes : ''; ?>">
        <?php print $content['col_c1']; ?>
      </div></div>
      <div class="col col-2"><div class="col-inner<?php print isset($col_c2_classes) ? $col_c2_classes : ''; ?>">
        <?php print $content['col_c2']; ?>
      </div></div>
    </div>
  <?php endif; ?>

  <?php if ($content['col_d1'] || $content['col_d2']): ?>
    <div class="cl-row cl-row-d cols-2 clearfix">
      <div class="col col-1"><div class="col-inner<?php print isset($col_d1_classes) ? $col_d1_classes : ''; ?>">
        <?php print $content['col_d1']; ?>
      </div></div>
      <div class="col col-2"><div class="col-inner<?php print isset($col_d2_classes) ? $col_d2_classes : ''; ?>">
        <?php print $content['col_d2']; ?>
      </div></div>
    </div>
  <?php endif; ?>

</div>
