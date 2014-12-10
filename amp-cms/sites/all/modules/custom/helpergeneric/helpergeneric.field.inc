<?php

/**
 * @file
 * Implement a custom field formaters.
 */


/**
 * Implements hook_field_formatter_info().
 */
function helpergeneric_field_formatter_info() {
  return array(
    'title_with_actions' => array(
      'label' => t('Linked with Actions'),
      'field types' => array('text'),
      'settings' => array('title_style' => '', 'title_link' => '', 'title_class' => '', 'show_action' => FALSE, 'show_social' => FALSE),
    ),
  );
}

/**
 * Implements hook_field_formatter_settings_form().
 */
function helpergeneric_field_formatter_settings_form($field, $instance, $view_mode, $form, &$form_state) {
  $settings = $instance['display'][$view_mode]['settings'];
  $element = array();

  $wrap_tags = array(
    '_none' => t('- None -'),
    'div' => t('DIV'),
    'h1' => t('H1'),
    'h2' => t('H2'),
    'h3' => t('H3'),
    'h4' => t('H4'),
    'h5' => t('H5'),
    'h6' => t('H6'),
    'span' => t('SPAN'),
  );
  $element['title_style'] = array(
    '#title' => t('Wrap title in tag'),
    '#type' => 'select',
    '#default_value' => !empty($settings['title_style']) ? $settings['title_style'] : '_none',
    '#options' => $wrap_tags,
  );

  $link_types = array(
    'content' => t('Content'),
  );
  $element['title_link'] = array(
    '#title' => t('Link title to'),
    '#type' => 'select',
    '#default_value' => $settings['title_link'],
    '#empty_option' => t('Nothing'),
    '#options' => $link_types,
  );

  $element['title_class'] = array(
    '#title' => t('Tag classes'),
    '#type' => 'textfield',
    '#description' => t('A CSS class to use in the wrapper tag for the title.'),
    '#default_value' => $settings['title_class'],
    '#element_validate' => array('_helpergeneric_validate_class'),
  );

  $element['show_action'] = array(
    '#title' => t('Show Action Links'),
    '#type' => 'checkbox',
    '#default_value' => $settings['show_action'],
  );

  $element['show_social'] = array(
    '#title' => t('Show Social Links'),
    '#type' => 'checkbox',
    '#default_value' => $settings['show_social'],
  );

  return $element;
}

/**
 * Implements hook_field_formatter_settings_summary().
 */
function helpergeneric_field_formatter_settings_summary($field, $instance, $view_mode) {
  $settings = $instance['display'][$view_mode]['settings'];
  $summary = array();

  $tag = isset($settings['title_style']) && $settings['title_style'] != '' && $settings['title_style'] != '_none' ? $settings['title_style'] : t('- None -');
  $summary[] = t('Title wrap tag: @tag', array('@tag' => $tag));

  $link_types = array(
    'content' => t('Linked to content'),
  );
  // Display this setting only if field is linked.
  if (isset($link_types[$settings['title_link']])) {
    $summary[] = $link_types[$settings['title_link']];
  }

  // Display this setting only if wrapper has a class.
  if (isset($settings['title_class']) && $settings['title_class'] != '_none' && $settings['title_class'] != '') {
    $summary[] = t('Wrap tag classes: @classes', array('@classes' => $settings['title_class']));
  }

  // Display this action links visibility.
  if (isset($settings['show_action'])) {
    if (empty($settings['show_action'])) {
      $summary[] = t('Action Links: not visible');
    }
    else {
      $summary[] = t('Action Links: visible');
    }
  }

  // Display this social links visibility.
  if (isset($settings['show_social'])) {
    if (empty($settings['show_social'])) {
      $summary[] = t('Social Links: not visible');
    }
    else {
      $summary[] = t('Social Links: visible');
    }
  }

  return implode('<br />', $summary);
}

/**
 * Implements hook_field_formatter_view().
 */
function helpergeneric_field_formatter_view($entity_type, $entity, $field, $instance, $langcode, $items, $display) {
  $settings = $display['settings'];
  $output = isset($items[0]) ? $items[0]['safe_value'] : '';
  $element = array();

  if (!empty($output)) {
    if ($settings['title_link'] == 'content') {
      $uri = entity_uri($entity_type, $entity);
      $output = l($output, $uri['path'], array('html' => TRUE));
    }

    $wrap_tag = empty($settings['title_style']) ? '_none' : $settings['title_style'];

    if ($wrap_tag != '_none') {
      $variables = array(
        'element' => array(
          '#tag' => $wrap_tag,
          '#value' => $output,
        ),
      );

      if (!empty($settings['title_class'])) {
        $variables['element']['#attributes'] = array('class' => $settings['title_class']);
      }

      $output = theme('html_tag', $variables);
    }

    $title = array(
      '#markup' => $output,
    );

    $action_links = array();
    if (!empty($settings['show_action'])) {
      $action_links = array(
        '#prefix' => '<ul class="title-links action-links">',
        '#suffix' => '</ul>',
      ) + menu_local_actions();
    }

    $social_links = array();
    if (!empty($settings['show_social'])) {
      $social_links = array(
        '#prefix' => '<ul class="title-links social-links">',
        '#suffix' => '</ul>',
      );

      $query_parameters = drupal_get_query_parameters();
      $query_parameters = array_filter($query_parameters);
      $current_url = url($_GET['q'], array('query' => $query_parameters, 'absolute' => TRUE));

      $page_title = strip_tags($output);
      foreach (_helpergeneric_get_social_links($current_url, $page_title) as $key => $value) {
        $social_links[$key] = array(
          '#prefix' => '<li>',
          '#suffix' => '</li>',
          '#markup' => $value,
        );
      }
    }

    $element[] = array(
      $action_links,
      $title,
      $social_links,
    );
  }
  return $element;
}

/**
 * Validate that a space-separated list of values are lowercase and appropriate for use as HTML classes.
 *
 * @see title_field_formatter_settings_form()
 */
function _helpergeneric_validate_class($element, &$form_state) {
  $value = drupal_array_get_nested_value($form_state['values'], $element['#parents']);
  $classes = explode(' ', $value);

  foreach ($classes as $class) {
    if ($class != drupal_html_class($class)) {
      form_error($element, t('Wrapper classes contain illegal characters; classes should be lowercase and may contain letters, numbers, and dashes.'));
      return;
    }
  }
}
