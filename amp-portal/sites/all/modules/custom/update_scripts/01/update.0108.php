<?php

// Provide a list of modules to be installed.
$modules = array(
  'recaptcha',
	'recaptcha_mailhide',
);

// Install modules.
_us_install_modules($modules);

db_merge('captcha_points')
      ->key(array('form_id' => 'feedback_form'))
      ->fields(array('module' => 'recaptcha', 'captcha_type' => 'reCAPTCHA'))
      ->execute();
      
_us_revert_feature('ampp_general');