<?php
// $Id: ampp-two-23.tpl.php,v 1.2 2012/02/02 19:02:59 eabrahamyan Exp $
?>
<div class="dgtwo-layout" <?php if (!empty($css_id)) { print ' id="' . $css_id . '"'; } ?>>
  <div class="dgtwo-header margin-top-10">
    <div class="grid_12 alpha omega"><?php print $content['header']; ?></div>
    <div class="clear"></div>
  </div>

  <div class="dgtwo-columns margin-top-10"><div class="dgtwo-columns-inner">
    <div class="col col2 grid_9 alpha"><?php print $content['col_2']; ?></div>
    <div class="col col3 grid_3 omega"><?php print $content['col_3']; ?></div>
    <div class="clear"></div>
  </div></div>

  <div class="dgtwo-footer margin-top-10">
    <div class="grid_12 alpha omega"><?php print $content['footer']; ?></div>
    <div class="clear"></div>
  </div>
</div>
<div class="clear"></div>
