<?php

/**
 * @file
 * Contains functions that handle WYSIWYG module integration.
 */

/**
 * Implements hook_wysiwyg_editor_settings_alter().
 * Check the css_content WYSIWYG setting for LESS files and replace with 
 * generated CSS files where necessary.
 */
function less_wysiwyg_editor_settings_alter(&$settings, $context) {
  
  $wysiwyg = $context['editor']['name'];
  $stylesheets = array();
  
  switch ($wysiwyg) {
    case 'tinymce':
      if (!empty($settings['content_css'])) {
        $stylesheets = explode(',', $settings['content_css']);
      }
      break;
    case 'fckeditor':
      if (!empty($settings['EditorAreaCSS'])) {
        $stylesheets = explode(',', $settings['EditorAreaCSS']);
      }
      break;
    case 'ckeditor':
      if (!empty($settings['contentsCss'])) {
        $stylesheets = $settings['contentsCss'];
      }
      break;
  }
  
  if (!empty($stylesheets)) {
    
    $styles = array(
      '#items' => array(),
    );
    
    foreach ($stylesheets as $stylesheet) {
      $styles['#items'][$stylesheet] = array(
        // Paths are expected to be relative to DRUPAL_ROOT.
        'data' => trim($stylesheet, '/'),
      );
    }
    
    $styles = _less_pre_render($styles);

    $processed_stylesheets = array();

    foreach ($styles['#items'] as $file) {
      $processed_stylesheets[] = file_create_url($file['data']);
    }
    
    switch ($wysiwyg) {
      case 'tinymce':
        $settings['content_css'] = implode(',', $processed_stylesheets);
        break;
      case 'fckeditor':
        $settings['EditorAreaCSS'] = implode(',', $processed_stylesheets);
        break;
      case 'ckeditor':
        $settings['contentsCss'] = $processed_stylesheets;
        break;
    }
  }
}

function less_form_wysiwyg_profile_form_alter(&$form, $form_state, $form_id) {
  $form['css']['css_path']['#description'] .= '<br />' . t('You may enter a path to a LESS file and it will be parsed automatically.');
}
