<?php
// $Id: ampp-three.tpl.php,v 1.1 2012/01/03 19:56:21 vamirbekyan Exp $
?>
<div class="dgthree-layout" <?php if (!empty($css_id)) { print ' id="' . $css_id . '"'; } ?>>
  <div class="dgthree-header margin-top-10">
    <div class="grid_12 alpha omega"><?php print $content['header']; ?></div>
    <div class="clear"></div>
  </div>

  <div class="dgthree-columns margin-top-10"><div class="dgthree-columns-inner">
    <div class="col col1 grid_3 alpha"><?php print $content['col_1']; ?></div>
    <div class="col col2 grid_6"><?php print $content['col_2']; ?></div>
    <div class="col col3 grid_3 omega"><?php print $content['col_3']; ?></div>
    <div class="clear"></div>
  </div></div>

  <div class="dgthree-footer margin-top-10">
    <div class="grid_12 alpha omega"><?php print $content['footer']; ?></div>
    <div class="clear"></div>
  </div>
</div>
<div class="clear"></div>
