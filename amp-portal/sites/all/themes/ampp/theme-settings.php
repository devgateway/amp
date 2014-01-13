<?php
// $Id: theme-settings.php,v 1.10 2013/11/01 20:39:44 vamirbekyan Exp $


/**
* Implementation of hook_settings() for themes.
*/
function ampp_form_system_theme_settings_alter(&$form, $form_state) {
  
  //print_r($form);
  $form['theme_settings']['toggle_amp_menu'] = array(
    '#type' => 'checkbox',
    '#title' => t('AMP links menu'),
    '#default_value' => theme_get_setting('toggle_amp_menu', 'ampp'),
  );
  
  $form['theme_settings']['toggle_dg_footer'] = array(
    '#type' => 'checkbox',
    '#title' => t('DG footer'),
    '#default_value' => theme_get_setting('toggle_dg_footer', 'ampp'),
  );
  
  $form['theme_settings']['toggle_share_links'] = array(
    '#type' => 'checkbox',
    '#title' => t('Share links'),
    '#default_value' => theme_get_setting('toggle_share_links', 'ampp'),
  );
  
  // *****
  // Adding image preview for LOGO image
  $default_used = false;
  if (theme_get_setting('default_logo', 'ampp')) {
    // default logo is used
    $default_used = true;
    $logo_path = drupal_get_path('theme', 'ampp') . '/logo.png';
  } else {
    // custom logo is used
    $logo_path = theme_get_setting('logo_path', 'ampp');
    if (file_uri_scheme($logo_path) == 'public') {
      $logo_path = file_uri_target($logo_path);
    }
  }
  if (!empty($logo_path)) {
    $form['logo']['logo_preview'] = array(
      '#weight' => -10,
      '#type' => 'markup',
      '#markup' => $default_used ? theme('image', array('path' => $logo_path)) : theme('image_style', array('style_name' => 'logo', 'path' => $logo_path)),
    );
  }
  // overwrite description for logo
  $form['logo']['#description'] = t('The following image is being used.');
  $form['logo']['settings']['logo_path']['#description'] = t('The path to the file you would like to use as your logo file instead of the default logo.') . '<br>' . t('The image will be re-sized to fit 150x35 pixels block.');
  
  // *****
  // Adding image preview for FAVICON image
  if (strpos(theme_get_setting('favicon', 'ampp'), drupal_get_path('theme', 'ampp'))) {
    // default favicon is used
    $favicon_path = drupal_get_path('theme', 'ampp') . '/favicon.ico';
  } else {
    // custom logo is used
    $favicon_path = theme_get_setting('favicon_path', 'ampp');
    if (file_uri_scheme($favicon_path) == 'public') {
      $favicon_path = variable_get('file_public_path', 'sites/default/files') . '/' . file_uri_target($favicon_path);
    }
  }
  if (!empty($favicon_path)) {
    $form['favicon']['favicon_preview'] = array(
      '#weight' => -10,
      '#type' => 'markup',
      '#markup' => theme('image', array('path' => $favicon_path)),
    );
  }
  $form['favicon']['settings']['favicon_path']['#description'] = t('The path to the image file you would like to use as your custom shortcut icon.') . '<br>' . t('The image file should be 16x16 pixels and have .ico extension.');
 
  // *****
  // Custom FOOTER LOGO
  $form['footer_logo'] = array(
    '#type' => 'fieldset',
    '#title' => t('Footer logo image settings'),
    '#description' => t('The following logo is being used.'),
  );
  
  $default_used = false;
  if (theme_get_setting('default_footer_logo', 'ampp')) {
    // default footer logo is used
    $default_used = true;
    $footer_logo_path = drupal_get_path('theme', 'ampp') . '/footer-logo.png';
  } else {
    // custom footer logo is used
    $footer_logo_path = theme_get_setting('footer_logo_path', 'ampp');
    if (file_uri_scheme($footer_logo_path) == 'public') {
      $footer_logo_path = file_uri_target($footer_logo_path);
    }
  }
  if (!empty($footer_logo_path)) {
    $form['footer_logo']['logo_preview'] = array(
      '#weight' => -10,
      '#type' => 'markup',
      '#markup' => $default_used ? theme('image', array('path' => $footer_logo_path)) : theme('image_style', array('style_name' => 'footer_logo', 'path' => $footer_logo_path)),
    );
  }
  $form['footer_logo']['default_footer_logo'] = array(
    '#type' => 'checkbox',
    '#title' => t('Use the default footer logo'),
    '#default_value' => theme_get_setting('default_footer_logo', 'ampp'),
    '#tree' => FALSE,
    '#description' => t('Check here if you want the theme to use the logo supplied with it.'),
  );
  $form['footer_logo']['settings'] = array(
    '#type' => 'container',
    '#states' => array(
    // Hide the logo settings when using the default logo.
    'invisible' => array(
      'input[name="default_footer_logo"]' => array('checked' => TRUE),
      ),
    ),
  ); 
  $form['footer_logo']['settings']['footer_logo_path'] = array(
    '#type' => 'textfield',
    '#title' => t('Path to custom logo'),
    '#default_value' => $footer_logo_path,
    '#description' => t('The path to the file you would like to use as your logo file instead of the default logo.') . '<br>' . t('The image will be re-sized to fit 150x25 pixels block.'),
  );
  $form['footer_logo']['settings']['footer_logo_upload'] = array(
    '#type' => 'file',
    '#title' => t('Upload logo image'),
    '#maxlength' => 40,
    '#description' => t("If you don't have direct file access to the server, use this field to upload your logo.")
  );
  $form['#submit'][] = 'ampp_settings_submit';

  // *****
  // Custom BLOCKS LOGO
  $form['blocks_logo'] = array(
    '#type' => 'fieldset',
    '#title' => t('Blocks logo image settings'),
    '#description' => t('The following image is being used.'),
  );
  $default_used = false;
  if (theme_get_setting('default_blocks_logo', 'ampp')) {
    $default_used = true;
    // default blocks logo is used
    $blocks_logo_path = drupal_get_path('theme', 'ampp') . '/blocks-logo.png';
  }
  // custom footer logo is used
  $blocks_logo_path = theme_get_setting('blocks_logo_path', 'ampp');
  if (file_uri_scheme($blocks_logo_path) == 'public') {
    $blocks_logo_path = file_uri_target($blocks_logo_path);
  }
  if (!$default_used && !empty($blocks_logo_path)) {
    $form['blocks_logo']['logo_preview'] = array(
      '#weight' => -10,
      '#type' => 'markup',
      '#markup' => $default_used ? theme('image', array('path' => $blocks_logo_path)) : theme('image_style', array('style_name' => 'blocks_logo', 'path' => $blocks_logo_path)),
    );
  }
  $form['blocks_logo']['default_blocks_logo'] = array(
    '#type' => 'checkbox',
    '#title' => t('Use the default block logo'),
    '#default_value' => theme_get_setting('default_blocks_logo', 'ampp'),
    '#tree' => FALSE,
    '#description' => t('Check here if you want the theme to use the image supplied with it.')
  );
  $form['blocks_logo']['settings'] = array(
    '#type' => 'container',
    '#states' => array(
    // Hide the logo settings when using the default logo.
    'invisible' => array(
      'input[name="default_blocks_logo"]' => array('checked' => TRUE),
      ),
    ),
  );
  $form['blocks_logo']['settings']['blocks_logo_path'] = array(
    '#type' => 'textfield',
    '#title' => t('Path to custom logo'),
    '#default_value' => $blocks_logo_path,
    '#description' => t('The path to the file you would like to use as your logo file instead of the default logo.') . '<br>' . t('The image will be re-sized to fit 35x30 pixels block.'),
  );
  $form['blocks_logo']['settings']['blocks_logo_upload'] = array(
    '#type' => 'file',
    '#title' => t('Upload logo image'),
    '#maxlength' => 40,
    '#description' => t("If you don't have direct file access to the server, use this field to upload your logo.")
  );
  // *****
  // Custom BACKGROUND IMAGE
  $form['bg_image'] = array(
    '#type' => 'fieldset',
    '#title' => t('Home page background image settings'),
  );
  $not_used = false;
  if (theme_get_setting('no_bg_image', 'ampp')) {
    $not_used = true;
  }
  // custom footer logo is used
  $bg_image_path = theme_get_setting('bg_image_path', 'ampp');
  if (file_uri_scheme($bg_image_path) == 'public') {
    $bg_image_path = file_uri_target($bg_image_path);
  }
  if (!$not_used && !empty($bg_image_path)) {
    $form['bg_image']['#description'] = t('The following image is being used.');
    $form['bg_image']['bg_preview'] = array(
      '#weight' => -10,
      '#type' => 'markup',
      '#markup' => $not_used ? theme('image', array('path' => $bg_image_path)) : theme('image_style', array('style_name' => 'bg_image', 'path' => $bg_image_path)),
    );
  }
  $form['bg_image']['no_bg_image'] = array(
    '#type' => 'checkbox',
    '#title' => t('Do not use any background image'),
    '#default_value' => theme_get_setting('no_bg_image', 'ampp'),
    '#tree' => FALSE,
  );
  $form['bg_image']['settings'] = array(
    '#type' => 'container',
    '#states' => array(
    // Hide the img settings when using no bg image.
    'invisible' => array(
      'input[name="no_bg_image"]' => array('checked' => TRUE),
      ),
    ),
  );
  $form['bg_image']['settings']['bg_image_path'] = array(
    '#type' => 'textfield',
    '#title' => t('Path to custom background image'),
    '#default_value' => $bg_image_path,
    '#description' => t('The path to the file you would like to use as your logo file instead of the default logo.') . '<br>' . t('The image will be re-sized to fit 35x30 pixels block.'),
  );
  $form['bg_image']['settings']['bg_image_upload'] = array(
    '#type' => 'file',
    '#title' => t('Upload background image'),
    '#maxlength' => 40,
    '#description' => t("If you don't have direct file access to the server, use this field to upload your logo.")
  );
}


/**
* Implementation of hook_settings_submit() for themes.
*/
function ampp_settings_submit($form, &$form_state) {

  $settings = array();

  $previous_destination = 'public://' . $form['footer_logo']['settings']['footer_logo_path']['#default_value'];
  // Check for a new uploaded footer_logo file, and use that if available.
  if ($file = file_save_upload('footer_logo_upload')) {
    $parts = pathinfo($file->filename);
    $destination = 'public://' . $parts['basename'];
    $file->status = FILE_STATUS_PERMANENT;  
  
    if (file_copy($file, $destination, FILE_EXISTS_REPLACE)) {
      $_POST['footer_logo_path'] = $form_state['values']['footer_logo_path'] = $destination;
      // If new file has a different name than the old one, delete the old
      if ($destination != $previous_destination) {
        drupal_unlink($previous_destination);
      }
    }
  }
  
  $previous_destination = 'public://' . $form['blocks_logo']['settings']['blocks_logo_path']['#default_value'];
  // Check for a new uploaded blocks_logo file, and use that if available.
  if ($file = file_save_upload('blocks_logo_upload')) {
    $parts = pathinfo($file->filename);
    $destination = 'public://' . $parts['basename'];
    $file->status = FILE_STATUS_PERMANENT;  
  
    if (file_copy($file, $destination, FILE_EXISTS_REPLACE)) {
      $_POST['blocks_logo_path'] = $form_state['values']['blocks_logo_path'] = $destination;
      // If new file has a different name than the old one, delete the old
      if ($destination != $previous_destination) {
        drupal_unlink($previous_destination);
      }
    }
  }
  
  $previous_destination = 'public://' . $form['bg_image']['settings']['bg_image_path']['#default_value'];
  // Check for a new uploaded blocks_logo file, and use that if available.
  if ($file = file_save_upload('bg_image_upload')) {
    $parts = pathinfo($file->filename);
    $destination = 'public://' . $parts['basename'];
    $file->status = FILE_STATUS_PERMANENT;
  
    if (file_copy($file, $destination, FILE_EXISTS_REPLACE)) {
      $_POST['bg_image_path'] = $form_state['values']['bg_image_path'] = $destination;
      // If new file has a different name than the old one, delete the old
      if ($destination != $previous_destination) {
        drupal_unlink($previous_destination);
      }
    }
  }
}
?>