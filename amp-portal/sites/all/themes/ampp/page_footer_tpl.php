<?php
// $Id: page_footer_tpl.php,v 1.6 2012/08/13 14:18:00 vamirbekyan Exp $
?>

<div class="footer-container">
  <div class="social-footer">
  <h2 class="social-links">
    <?php print $share_links; ?>
  </h2>
  <div class="footer-links">
    <?php print $footer_links; ?>
  </div>
  </div>
  <div class="footer-logo">
    <?php print $footer_logo; ?><?php print t($footer_text); ?>
  </div>
  <div class="dg-footer-container">
    <?php if ($dg_footer) : ?><?php print $dg_footer; ?><?php endif; ?>
  </div>
</div>