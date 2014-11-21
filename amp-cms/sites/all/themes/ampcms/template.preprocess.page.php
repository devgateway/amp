<?php
/**
 * @file
 * This file contains changes related to the overall page.
 */

/**
 * Preprocess variables for page.tpl.php
 */
function ampcms_preprocess_page(&$variables) {
  // For now hide breadcrumb.
  $variables['display_breadcrumb'] = FALSE;

  // Hide tabs on various pages and for anonymous users.
  $variables['display_tabs'] = TRUE;
  if (user_is_anonymous()) {
    $variables['display_tabs'] = FALSE;
  }
  else if (drupal_is_front_page()) {
    $variables['display_tabs'] = FALSE;
  }
  else if (helpergeneric_cleanup_ui()) {
    $variables['display_tabs'] = FALSE;
  }

  // Always display tabs on admin pages.
  if (arg(0) == 'admin') {
    $variables['display_tabs'] = TRUE;
  }

  $variables['browser_warnings'] = array(
    '#type' => 'container',
    '#attributes' => array('class' => array('browser-warnings-wrapper')),
  );

  // Inform users that they are using an outdated browser.
  $variables['browser_warnings']['oldbrowser'] = array();
  if (empty($_COOKIE['ignore_oldbrowser'])) {
    $variables['browser_warnings']['oldbrowser'] = array(
      '#type' => 'container',
      '#attributes' => array('class' => array('oldbrowser-warning')),
      '#prefix' => "<!--[if lt IE 9]>\n",
      '#suffix' => "<![endif]-->\n",
    );

    $variables['browser_warnings']['oldbrowser']['message'] = array(
      '#type' => 'html_tag',
      '#tag' => 'p',
      '#value' => t('You are using an outdated browser. <a href="http://browsehappy.com">Upgrade your browser today</a> to better experience this site.'),
    );

    $variables['browser_warnings']['oldbrowser']['ignore'] = array(
      '#type' => 'html_tag',
      '#tag' => 'a',
      '#value' => 'x',
      '#attributes' => array(
        'class' => array(
          'ignore-link',
        ),
        'href' => '#',
        'title' => t('Hide this warning.'),
      ),
    );
  }

  // Inform users that they have JavaScript disabled.
  $variables['browser_warnings']['nojs'] = array(
    '#type' => 'container',
    '#attributes' => array('class' => array('nojs-warning')),
    '#prefix' => '<noscript>',
    '#suffix' => '</noscript>',
  );
  $variables['browser_warnings']['nojs']['message'] = array(
    '#type' => 'html_tag',
    '#tag' => 'p',
    '#value' => t('Please enable JavaScript for full use of this site.'),
  );

  $style_main_content = FALSE;
  if (arg(0) == 'admin') {
    $style_main_content = TRUE;
  }
  else if (arg(2) != NULL && in_array(arg(0), array('node', 'contentblock'))) {
    $style_main_content = TRUE;
  }

  $variables['main_content_classes'] = '';
  if ($style_main_content) {
    $variables['main_content_classes'] = ' layout-block';
  }
}

/**
 * Implements hook_preprocess_html_tag().
 *
 * @see: http://wiki.whatwg.org/wiki/HTML_vs._XHTML#Element-specific_parsing
 */
function ampcms_process_html_tag(&$vars) {
  $element = &$vars['element'];

  // Make Drupal 7 HTML5 compliant.
  if (in_array($element['#tag'], array('script', 'link', 'style'))) {
    // Remove the "type" attribute.
    unset($element['#attributes']['type']);

    // Remove CDATA comments.
    if (isset($element['#value_prefix']) && ($element['#value_prefix'] == "\n<!--//--><![CDATA[//><!--\n" || $element['#value_prefix'] == "\n<!--/*--><![CDATA[/*><!--*/\n")) {
      unset($element['#value_prefix']);
    }
    if (isset($element['#value_suffix']) && ($element['#value_suffix'] == "\n//--><!]]>\n" || $element['#value_suffix'] == "\n/*]]>*/-->\n")) {
      unset($element['#value_suffix']);
    }
  }
}

/**
 * Implements hook_css_alter().
 *
 * @TODO: Cleanup CSS!
 */
function ampcms_css_alter(&$css) {
  $exclude_list = array(
    'modules',
    'sites/all/modules/contrib',
  );
  $exceptions_list = array(
    'sites/all/modules/contrib/admin_menu',
    'sites/all/modules/contrib/ctools',
    'sites/all/modules/contrib/date',
    'sites/all/modules/contrib/l10n_client',
    'sites/all/modules/contrib/media',
  );

  // The CSS_SYSTEM aggregation group doesn't make any sense and most of the
  // provided files are useless. Therefore we override almost all CSS files.
  foreach ($css as $file_path => $item) {
    if ($item['type'] != 'file') {
      continue;
    }

    if ($item['group'] == CSS_SYSTEM) {
      $item['group'] = CSS_DEFAULT;
      $item['weight'] = $item['weight'] - 100;
    }

    // We remove most of the default CSS files.
    $remove_file = FALSE;
    foreach ($exclude_list as $exclude_item) {
      if (strpos($file_path, $exclude_item) === 0) {
        $remove_file = TRUE;
        break;
      }
    }
    foreach ($exceptions_list as $exclude_item) {
      if (strpos($file_path, $exclude_item) === 0) {
        $remove_file = FALSE;
        break;
      }
    }

    if ($remove_file) {
      // Remove current file.
      unset($css[$file_path]);
    }
  }
}

/**
 * Implements hook_js_alter().
 *
 * @TODO: Cleanup JS!
 */
function ampcms_js_alter(&$js) {
  // The JS_LIBRARY aggregation group doesn't need to be in a separate file.
  foreach ($js as $file_path => $item) {
    if ($item['type'] != 'file') {
      continue;
    }

    if ($item['group'] == JS_LIBRARY) {
      $item['group'] = JS_DEFAULT;
      $item['weight'] = $item['weight'] - 100;
    }
  }
}

/**
 * Implements hook_form_alter().
 */
function ampcms_form_alter(&$form, &$form_state, $form_id) {
  // Duplicate the form ID as a class so we can reduce specificity in our CSS.
  $form['#attributes']['class'][] = drupal_clean_css_identifier($form['#id']);
}

/**
 * Preprocess variables for contentblock
 */
function ampcms_preprocess_entity(&$variables, $hook) {
  if ($variables['entity_type'] == 'contentblock' && $variables['contentblock']->type == 'predefined') {
    // widget fields are not translatable.
    if (isset($variables['contentblock']->field_cbwidget[LANGUAGE_NONE][0]['widget'])) {
      $widget = $variables['contentblock']->field_cbwidget[LANGUAGE_NONE][0]['widget'];
      $variables['classes_array'][] = drupal_clean_css_identifier('contentwidget-' . $widget);

      if (in_array($widget, array('topdonors', 'topprojects', 'lastupdated'))) {
        $variables['classes_array'][] = 'contentwidget-fullwidth';
      }
    }
  }
}
