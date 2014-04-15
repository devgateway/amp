<?php
// $Id: page_footer_tpl.php,v 1.3 2012/02/29 21:40:32 vamirbekyan Exp $
?>

<div class="footer-container">
  <div class="footer-logo">
    <?php print $footer_logo; ?><?php print t($footer_text); ?>
  </div>
  <div class="dg-footer-container">
    <?php if ($dg_footer) : ?><?php print $dg_footer; ?><?php endif; ?>
  </div>
</div>