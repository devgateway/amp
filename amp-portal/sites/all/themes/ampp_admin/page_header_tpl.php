<?php
// $Id: page_header_tpl.php,v 1.4 2012/02/13 19:43:22 vamirbekyan Exp $
?>

<div class="header-container">
  <div class="logo-block">
    <?php if ($toggle_logo): ?><?php print $header_logo; ?><?php endif; ?>
    <div class="logo-headings">
      <?php $site_name = variable_get('site_name', ''); ?>
      <span class="site_name"><?php if ($site_name): ?><?php print l($site_name, '<front>', array('alt' => $site_name, 'title' => $site_name)); ?><?php endif; ?></span>
      <?php $site_slogan = variable_get('site_slogan', ''); ?>
      <span class="slogan"><?php if ($site_slogan): ?><?php print l($site_slogan, '<front>', array('alt' => $site_name, 'title' => $site_name)); ?><?php endif; ?></span>
    </div>
  </div>
</div>

<div class="clear"></div>
