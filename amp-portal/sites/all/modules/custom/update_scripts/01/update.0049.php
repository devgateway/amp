<?php
// $Id: update.0049.php,v 1.2 2012/03/28 03:55:45 vamirbekyan Exp $

// Provide a list of modules to be disabled and uninstalled.
if (PROJECT_ENVIRONMENT == 'LOCAL' || PROJECT_ENVIRONMENT == 'STAGING' || PROJECT_ENVIRONMENT == 'PREPROD') {
  $modules = array(
    'reroute_email',
  );
  // Install modules
  _us_install_modules($modules);
  variable_set('reroute_email_enable', 1);
  variable_set('reroute_email_enable_message', 1);
  variable_set('reroute_email_toall', 1);
}