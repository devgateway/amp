<?php
// $Id: page_header_tpl.php,v 1.12 2013/10/14 20:31:39 vamirbekyan Exp $

global $language;
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
  <div id="top-nav">
    <?php
      $amp_login_text = trim(variable_get('amp_login_text', 'Login Here'));
      if (!empty($amp_login_text)) :
    ?>
      <div class="login"><a href="<?php print str_replace ( '%lang_code%' , $language->language , variable_get('amp_login_url', ''));?>"><?php print t(variable_get('amp_login_text', 'Login Here'));?></a></div>
    <?php endif; ?>
    <div class="header-links"><?php print $header_links; ?></div>
  </div>
</div>

<div class="clear"></div>

<div class="main-menu-container">
  <?php print theme('links__system_main_menu', array('links' => $main_menu, 'attributes' => array('id' => 'main-menu', 'class' => array('links', 'inline', 'clearfix')))); ?>
  <?php //print $main_links_menu; ?>
</div>
<div class="amp-links-container">
  <?php if ($amp_links) : ?><?php print $amp_links; ?><?php endif; ?>
</div>
<div class="language-switch">
  <?php print ampp_lang_switch(); ?>
</div>
