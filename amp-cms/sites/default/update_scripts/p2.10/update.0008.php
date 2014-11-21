<?php

// Provide a list of modules to be installed.
$modules = array(
  'wysiwyg',
    // 'jquery_update',
    // Enable WYSIWYG related media submodules.:
    'media_wysiwyg',
    'media_wysiwyg_view_mode',
);
_us_module__install($modules);

// Clear system caches.
drupal_flush_all_caches();

// Disable the default view for the media browser library tab.
_us_views__disable('media_default');

// Make sure the compressed version of jQuery and jQuery UI libraries are loaded.
// variable_set('jquery_update_compression_type', 'min');

// Do not use any CDN for jQuery and jQuery UI
// variable_set('jquery_update_jquery_cdn', 'none');

// Make sure that jQuery version 1.8.x is used on the frontend of the website.
// variable_set('jquery_update_jquery_version', '1.8');

// Make sure that jQuery version 1.8.x is used on the admin interface.
// Requried by the new version of media.module
// variable_set('jquery_update_jquery_admin_version', '1.8');

// Set the fallback format to plain text.
variable_set('filter_fallback_format', 'filtered_html');

// Limit the teaser length.
variable_set('teaser_length', 360);

// Set the "Add media" wysiwyg button title.
variable_set('media_wysiwyg_wysiwyg_icon_title', 'Add/Update media');

// Set the default view mode to narrow.
variable_set('media_wysiwyg_wysiwyg_default_view_mode', 'narrow');

// File directory for media uploaded via the WYSIWYG editor.
variable_set('media_wysiwyg_wysiwyg_upload_directory', 'inline');

// Limit allowed media types in the WYSIWYG editor.
$wysiwyg_allowed_types = array(
  // 'audio',
  'document',
  'image',
  'video',
);
variable_set('media_wysiwyg_wysiwyg_allowed_types', $wysiwyg_allowed_types);

// CKEditor likes to add width and height to the media file preview. We need to remove these attributes because we use
// different view modes for images and videos and these have different sizes.
$wysiwyg_allowed_attributes = array(
  'alt',
  'title',
  'hspace',
  'vspace',
  'border',
  'align',
  'style',
  'class',
  'id',
  'usemap',
  'data-picture-group',
  'data-picture-align',
  'data-picture-mapping',
);
variable_set('media_wysiwyg_wysiwyg_allowed_attributes', $wysiwyg_allowed_attributes);

// CKEditor likes to add width and height to the media file preview. We need to remove these attributes because we use
// different view modes for images and videos and these have different sizes.
$wysiwyg_browser_plugins = array(
  'upload',
  // 'media_library--audio_library',
  'media_library--document_library',
  'media_library--image_library',
  // 'media_library--video_library',
  // 'media_library--video_library',
  'media_internet',
);
variable_set('media_wysiwyg_wysiwyg_browser_plugins', $wysiwyg_browser_plugins);

// Force file view mode inside the WYSIWYG editor..
$default_wysiwyg_view_mode = 'wysiwyg';
$file_types = array(
  'audio',
  'document',
  // 'image',
  'video',
);
foreach ($file_types as $type) {
  $status_var = 'media_wysiwyg_view_mode_' . $type . '_file_wysiwyg_view_mode_status';
  $mode_var = 'media_wysiwyg_view_mode_' . $type . '_file_wysiwyg_view_mode';
  variable_set($status_var, 1);
  variable_set($mode_var, $default_wysiwyg_view_mode);
}


$file_types = array(
  'audio',
  'document',
  'image',
  'video',
);
foreach ($file_types as $type) {
  $status_var = 'media_wysiwyg_view_mode_' . $type . '_wysiwyg_restricted_view_modes_status';
  $mode_var = 'media_wysiwyg_view_mode_' . $type . '_wysiwyg_restricted_view_modes';

  switch ($type) {
    case 'image':
      $view_modes = array(
        'default' => 'default',
        'full' => 'full',
        'original' => 'original',
        'preview' => 'preview',
        'rss' => 'rss',
        'teaser' => 'teaser',
        'token' => 'token',
        'wysiwyg' => 'wysiwyg',
        'narrow' => 0,
        'narrow_linked' => 0,
        'normal' => 0,
        'normal_linked' => 0,
        'wide' => 0,
        'wide_linked' => 0,
      );
      break;

    default:
      $view_modes = array(
        'full' => 'full',
        'narrow' => 'narrow',
        'narrow_linked' => 'narrow_linked',
        'normal' => 'normal',
        'normal_linked' => 'normal_linked',
        'original' => 'original',
        'preview' => 'preview',
        'rss' => 'rss',
        'teaser' => 'teaser',
        'token' => 'token',
        'wide' => 'wide',
        'wide_linked' => 'wide_linked',
        'wysiwyg' => 'wysiwyg',
        'default' => 0,
      );
      break;
  }
  variable_set($status_var, 1);
  variable_set($mode_var, $view_modes);
}
