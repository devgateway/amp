<?php
// $Id: ampp-one.tpl.php,v 1.2 2013/03/04 20:41:52 vamirbekyan Exp $
?>
<div class="dgone-layout" <?php if (!empty($css_id)) { print ' id="' . $css_id . '"'; } ?>>
  <div class="dgone-header margin-top-10">
    <div class="grid_12 alpha omega"><?php print $content['header']; ?></div>
    <div class="clear"></div>
  </div>
  
  <div class="dgone-column margin-top-10"><div class="dgone-column-inner">
    <div class="col col1 grid_12 alpha"><?php print $content['col_1']; ?></div>
    <div class="clear"></div>
  </div></div>

  <div class="dgone-footer margin-top-10">
    <div class="grid_12 alpha omega"><?php print $content['footer']; ?></div>
    <div class="clear"></div>
  </div>
</div>
<div class="clear"></div>