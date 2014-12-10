<?php
/**
 * @file
 * Helper functions for administrative pages.
 */

/**
 * Helper function for contentblock_field_display_contentblock_alter().
 *
 * @param $display
 *   The display settings that will be used to display the field values, as
 *   found in the 'display' key of $instance definitions.
 * @param $context
 *   An associative array containing:
 *   - entity_type: The entity type; e.g., 'node' or 'user'.
 *   - field: The field being rendered.
 *   - instance: The instance being rendered.
 *   - entity: The entity being rendered.
 *   - view_mode: The view mode, e.g. 'full', 'teaser'...
 *
 * @see hook_field_display_alter()
 */
function contentblock_field_display_contentblock_alter__generic(&$display, $context) {
  if ($context['field']['field_name'] == 'title_field') {
    $display = array(
      'label' => 'hidden',
      'module' => 'title',
      'settings' => array(
        'title_class' => 'contentblock-title',
        'title_link' => '',
        'title_style' => 'h2',
      ),
      'type' => 'title_linked',
      'weight' => 1,
    );
  }

  if ($context['field']['field_name'] == 'field_cbimage') {
    $display = array(
      'label' => 'hidden',
      'module' => 'image',
      'settings' => array(
        'image_link' => '',
        'image_style' => 'medium',
      ),
      'type' => 'image',
      'weight' => 0,
    );
  }

  if ($context['field']['field_name'] == 'field_cblinks') {
    $display = array(
      'label' => 'hidden',
      'settings' => array(),
      'type' => 'hidden',
    );
  }

  if ($context['field']['field_name'] == 'field_cbblocks') {
    $display = array(
      'label' => 'hidden',
      'type' => 'entityreference_entity_view',
      'module' => 'entityreference',
      'settings' => array(
        'view_mode' => 'default',
        'links' => 0,
      ),
      'weight' => 0,
    );
  }

  if ($context['field']['field_name'] == 'field_cbbody') {
    $display = array(
      'label' => 'hidden',
      'module' => 'text',
      'settings' => array(),
      'type' => 'text_default',
      'weight' => 2,
    );
  }
}

/**
 * contentblock_field_display_contentblock_alter() helper function for sections.
 */
function contentblock_field_display_contentblock_alter__section(&$display, $context) {
  if ($context['field']['field_name'] == 'title_field') {
    $display = array(
      'label' => 'hidden',
      'settings' => array(),
      'type' => 'hidden',
    );
  }
}

/**
 * contentblock_field_display_contentblock_alter() helper function for 'welcome'.
 */
function contentblock_field_display_contentblock_alter__welcome(&$display, $context) {
  if ($context['field']['field_name'] == 'field_cbimage') {
    $display['settings']['image_style'] = 'img-hfeatured';
  }
}

/**
 * contentblock_field_display_contentblock_alter() helper function for 'linkedmap'.
 */
function contentblock_field_display_contentblock_alter__linkedmap(&$display, $context) {
  if ($context['field']['field_name'] == 'title_field') {
    $display = array(
      'label' => 'hidden',
      'module' => 'title',
      'settings' => array(
        'title_class' => '',
        'title_link' => '',
        'title_style' => '',
      ),
      'type' => 'title_linked',
      'weight' => 0,
    );
  }

  if ($context['field']['field_name'] == 'field_cbimage') {
    $display['weight'] = '1';
    if ($context['view_mode'] == 'wide') {
      $display['settings']['image_style'] = 'liwblock';
    }
    else {
      $display['settings']['image_style'] = 'lisblock';
    }
  }
}

/**
 * Contentblock entity render callback for sections.
 */
function contentblock_cbreference_render__section($entity, $pane_conf) {
  $view_mode = 'default';
  $entity_type = $entity->entityType();
  field_attach_prepare_view($entity_type, array($entity->identifier() => $entity), $view_mode);
  $view = field_attach_view($entity_type, $entity, $view_mode);

  return $view;
}

/**
 * Contentblock entity render callback for 'welcome'.
 */
function contentblock_cbreference_render__welcome($entity, $pane_conf) {
  $view_mode = 'default';
  $entity_type = $entity->entityType();
  field_attach_prepare_view($entity_type, array($entity->identifier() => $entity), $view_mode);
  $view = field_attach_view($entity_type, $entity, $view_mode);

  $children_keys = element_children($view);
  if (empty($children_keys)) {
    return;
  }

  // Wrap Title and body fields in a DIV.
  // @TODO: Find a better way to do this.
  $view['title_field'][0]['#prefix'] = '<div class="welcome-content-wrapper">';
  $view['field_cbbody']['#suffix'] = '</div>';

  // Load the metadata wrapper.
  $wrapper = entity_metadata_wrapper($entity->entityType(), $entity);

  // Get the link.
  $link = $wrapper->field_cblinks->value();
  if (!empty($link['url'])) {
    $view['field_cbimage'][0]['#path']['path'] = $link['url'];
    $view['field_cbimage'][0]['#path']['options']['attributes'] = $link['attributes'];

    $svgicon = helpertheme_get_svg_icons('add', array('width' => '3em', 'height' => '3em')) . ' ';
    $link_title = $svgicon . '<span class="text">' . t('read more') . '</span>';

    $view['view_link'] = array(
      '#theme' => 'link',
      '#path' => $link['url'],
      '#text' => $link_title,
      '#options' => array(
        'html' => TRUE,
        'attributes' => array(
          'class' => array('more-link'),
          'title' => t('read more'),
        ) + $link['attributes'],
      ),
      '#weight' => 50,
    );
  }

  return $view;
}

/**
 * Contentblock entity render callback for 'linkedmap'.
 */
function contentblock_cbreference_render__linkedmap($entity, $pane_conf) {
  // Set up preview file information.
  $view_mode = 'normal';
  if (!empty($pane_conf['style']) && $pane_conf['style'] == 'wide') {
    $view_mode = 'wide';
  }
  $entity_type = $entity->entityType();
  field_attach_prepare_view($entity_type, array($entity->identifier() => $entity), $view_mode);
  $view = field_attach_view($entity_type, $entity, $view_mode);
  $view['#view_mode'] = $view_mode;

  if (empty($view['title_field']) || empty($view['field_cbimage'])) {
    return;
  }

  // Load the metadata wrapper.
  $wrapper = entity_metadata_wrapper($entity->entityType(), $entity);

  // Get the link.
  $link = $wrapper->field_cblinks->value();
  if (empty($link['url'])) {
    return;
  }

  $link['attributes']['target'] = '_blank';
  $view['field_cbimage'][0]['#path']['path'] = $link['url'];
  $view['field_cbimage'][0]['#path']['options']['attributes'] = $link['attributes'];

  if (isset($view['title_field'][0]['#markup'])) {
    $svgicon = helpertheme_get_svg_icons('maps', array('width' => '2em', 'height' => '2em'));
    $view['title_field'][0]['#prefix'] = '<div class="title-wrapper">';
    $view['title_field'][0]['#suffix'] = '</div>';

    $new_title = $svgicon;
    $new_title .= $view['title_field'][0]['#markup'];

    if (empty($link['url'])) {
      $view['title_field'][0]['#markup'] = $new_title;
    }
    else {
      $view['title_field'][0]['#markup'] = l($new_title, $link['url'], array('html' => TRUE) + $link);
    }
  }

  return $view;
}

/**
 * Contentblock entity render callback for 'totalactivities'.
 */
function contentblock_cbreference_render__totalactivities($entity, $pane_conf) {
  // Set up preview file information.
  $view = array();

  // Get the type info.
  $plugin = contentwidget_get_predefined_widget('totalactivities');
  if ($function = ctools_plugin_get_function($plugin, 'render callback')) {
    // Because of entity_translation, the provided $langcode is the original entity language.
    $entity_langcode = entity_translation_get_existing_language($entity->entitYType(), $entity);
    $view[] = $function($plugin, array(), $entity, $entity_langcode);
  }

  return $view;
}
