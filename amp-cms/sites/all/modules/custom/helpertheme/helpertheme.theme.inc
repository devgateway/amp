<?php
/**
 * @file
 * Exposes custom theme functions.
 *
 * @TODO: Add caching.
 */

/**
 * Returns HTML for the svg icons.
 */
function theme_svg_sprite($elements) {
  if (empty($elements['symbol'])) {
    return NULL;
  }
  $elements['#attributes']['class'] = 'symbol ' . drupal_clean_css_identifier('symbol-' . $elements['symbol']);

  // Fallback SVG sprite.
  if (empty($elements['path'])) {
    $elements['path'] = drupal_get_path('module', 'helpertheme') . '/images/sprite.svg';
  }

  // Set a default attributes.
  foreach (array('height', 'width', 'viewBox') as $key) {
    if (array_key_exists($key, $elements) && is_null($elements[$key])) {
      continue;
    }

    if (!empty($elements[$key])) {
      $elements['#attributes'][$key] = $elements[$key];
      continue;
    }

    switch ($key) {
      case 'width':
      case 'height':
        $elements['#attributes'][$key] = '2em';
        break;
    }
  }

  // @TODO: improve accessibility.
  $elements['#attributes']['role'] = 'presentation';

  if (!is_null($elements['symbol'])) {
    $args = array(
      'fragment' => $elements['symbol'],
    );
  }

  // A dummy query-string is added to filenames, to gain control over
  // browser-caching. The string changes on every update or full cache
  // flush, forcing browsers to load a new copy of the files, as the
  // URL changed. Files that should not be cached (see drupal_add_js())
  // get REQUEST_TIME as query-string instead, to enforce reload on every
  // page request.
  $default_query_string = variable_get('css_js_query_string', '0');
  $args['query'][$default_query_string] = NULL;

  $elements['#use']['xlink:href'] = url($elements['path'], $args);

  return '<svg' . drupal_attributes($elements['#attributes']) . '><use'. drupal_attributes($elements['#use']) . '/></svg>';
}

/**
 * Returns HTML for the header logo.
 */
function theme_header_logo() {
  global $theme;

  // Text for homepage header logo link.
  $site_link_title = t(variable_get('site_link_title', 'Visit the main page'));
  $site_name = filter_xss_admin(variable_get('site_name', 'Drupal'));

  $items['#prefix'] = '<div class="site-name-wrapper contextual-links-region">';
  $items['#suffix'] = '</div>';

  // Initialize the template variable as a renderable array.
  $items['contextual_links'] = array(
    '#type' => 'contextual_links',
    '#contextual_links' => array(
      'menu' => array('admin/settings/generic', array('edit')),
    ),
  );

  $items['header']['#prefix'] = '<h1 class="site-name">';
  $items['header']['#suffix'] = '</h1>';

  $items['header']['logo_markup'] = array(
    '#prefix' => '<a href="' . url('<front>') . '" title="' . $site_link_title . '" class="link" rel="home">',
    '#suffix' => "</a>",
  );

  // Get the logo paths if it has been changed.
  $logo_path = theme_get_setting('logo_path', $theme);

  $file_path = drupal_realpath($logo_path);
  if (file_exists($file_path)) {
    $items['header']['logo_markup']['png'] = array(
      '#theme' => 'image',
      // '#path' => url($logo_path),
      '#path' => file_create_url($logo_path),
      '#alt' => '',
      '#attributes' => array(
        'role' => 'presentation',
      ),
    );
  }
  else {
    $items['header']['logo_markup']['svg'] = array(
      '#theme' => 'image',
      '#path' => url(drupal_get_path('theme', $theme) . '/logo.svg'),
      '#alt' => '',
      '#attributes' => array(
        'role' => 'presentation',
      ),
      '#prefix' => "<!--[if (gte IE 9)|!(IE)]><!-->",
      '#suffix' => "<!--<![endif]-->",
    );
    $items['header']['logo_markup']['png'] = array(
      '#theme' => 'image',
      '#path' => url(drupal_get_path('theme', $theme) . '/logo.png'),
      '#alt' => '',
      '#attributes' => array(
        'role' => 'presentation',
      ),
      '#prefix' => "<!--[if lt IE 9 ]>",
      '#suffix' => "<![endif]-->",
    );
  }


  $items['header']['logo_markup']['site_name'] = array(
    '#markup' => $site_name,
    '#prefix' => '<span class="text">',
    '#suffix' => "</span>",
  );

  // Render the HTML.
  return drupal_render($items);
}

/**
 * Returns HTML for the header items.
 */
function theme_header_items() {
  $menu_elements = array();

  $menu_elements = array(
    '#type' => 'container',
    '#attributes' => array(
      'class' => array(
        'header-items',
      ),
    ),
  );

  // Add the language switcher links.
  if (drupal_multilingual()) {
    $path = drupal_is_front_page() ? '<front>' : $_GET['q'];
    $lang_links = language_negotiation_get_switch_links('language', $path);
    if (isset($lang_links->links)) {
      $menu_elements['lang']['#prefix'] = '<nav role="navigation" id="language-switcher" class="language-switcher">';
      $menu_elements['lang']['#suffix'] = '</nav>';

      $menu_elements['lang']['subject'] = array(
        '#type' => 'html_tag',
        '#tag' => 'h2',
        '#value' => 'Languages',
        '#attributes' => array('class' => array('element-invisible')),
      );

      // Add language info to all links.
      $languages = language_list();
      foreach ($lang_links->links as &$link) {
        if (isset($link['attributes']['lang']) && isset($languages[$link['attributes']['lang']])) {
          $link['language'] = $languages[$link['attributes']['lang']];
        }
      }

      $menu_elements['lang']['links'] = array(
        '#theme' => 'links__language_switcher',
        '#links' => $lang_links->links,
        '#attributes' => array(
          'class' => array(
            'language-switcher-' . $lang_links->provider,
            'header-links',
            'clearfix',
          )
        ),
      );
    }
  }

  // Get the menu links.
  $menu_name = 'user-menu';
  if ($links = helpertheme_menu_navigation_links($menu_name, 1)) {
    // Mark this element as potentially having contextual links attached to it.
    $menu_elements['nav']['#prefix'] = '<nav role="navigation" id="user-navigation" class="user-navigation contextual-links-region">';
    $menu_elements['nav']['#suffix'] = '</nav>';

    $menu_elements['nav']['subject'] = array(
      '#type' => 'html_tag',
      '#tag' => 'h2',
      '#value' => 'User Menu',
      '#attributes' => array('class' => array('element-invisible')),
    );

    // Initialize the template variable as a renderable array.
    // $menu_elements['nav']['contextual_links'] = array(
    //   '#type' => 'contextual_links',
    //   '#contextual_links' => array(
    //     'menu' => array('admin/structure/menu/manage', array('user-menu')),
    //   ),
    // );

    $menu_elements['nav']['links'] = array(
      '#theme' => 'links__enhanced',
      '#links' => $links,
      '#links_prefix' => 'action-link',
      '#attributes' => array(
        'class' => array(
          'header-links',
          'links',
          'clearfix',
        )
      ),
    );
  }

  // Render the HTML.
  $output = drupal_render($menu_elements);

  // Remove whitespace between items. Helps with styling in-line elements.
  return preg_replace('/\>\s+\</', '><', $output);
}

/**
 * Returns HTML for main navigation menu.
 */
function theme_main_navigation() {
  $menu_elements = array(
    '#type' => 'container',
    '#attributes' => array(
      'class' => array(
        'navigation-toplevel',
      ),
    ),
  );

  // Initialize the template variable as a renderable array.
  $menu_elements['contextual_links'] = array(
    '#type' => 'contextual_links',
    '#contextual_links' => array(
      'menu' => array('admin/structure/menu/manage', array('main-menu')),
    ),
  );
  // Mark this element as potentially having contextual links attached to it.
  $menu_elements['#attributes']['class'][] = 'contextual-links-region';

  $menu_elements['header'] = array(
    '#type' => 'html_tag',
    '#tag' => 'h2',
    '#value' => 'Main Menu',
    '#attributes' => array('class' => array('element-invisible')),
  );

  $menu_tree_callback = (function_exists('i18n_menu_translated_tree')) ? 'i18n_menu_translated_tree' : 'menu_tree';
  $menu_elements['navigation'] = $menu_tree_callback('main-menu');

  // Change the wrapper class.
  $menu_elements['navigation']['#theme_wrappers'] = array('menu_tree__main_menu__toplevel');

  // Render the menu.
  $output = drupal_render($menu_elements);

  // Remove whitespace between items. Helps with styling in-line elements.
  return preg_replace('/\>[\n\ ]+\</', '><', $output);
}

/**
 * Returns HTML for the footer items.
 */
function theme_footer_items() {
  $footer_elements = array();

  $footer_elements = array(
    '#type' => 'container',
    '#attributes' => array(
      'class' => array(
        'footer-items',
      ),
    ),
  );

  // Load the ctools content type plugin.
  ctools_include('plugins');
  ctools_include('content');
  $content_type = ctools_get_content_type('cbreference');
  $content_subtype = ctools_content_get_subtype($content_type, 'cbreference');

  $configuration = ctools_content_get_defaults($content_type, $content_subtype);
  $configuration['machine_name'] = 'footer';

  // Render the footer content.
  $content = ctools_content_render('cbreference', 'cbreference', $configuration, array());
  $footer_elements['content']['#markup'] = theme('panels_pane', array('content' => $content));

  // Render the HTML.
  $output = drupal_render($footer_elements);

  // Remove whitespace between items. Helps with styling in-line elements.
  return preg_replace('/\>\s+\</', '><', $output);
}

/**
 * Returns HTML containing information about the report.
 */
function theme_amp_report_info($variables) {
  $elements = array(
    '#prefix' => '<div class="activity-search-info">',
    '#suffix' => '</div>',
  );

  // Display number of pages if necessary.
  if ($variables['total_items'] > $variables['items_per_page']) {
    // The number of the first item on the page.
    $start_item = ($variables['current_page'] * $variables['items_per_page']) + 1;

    // The number of the last item on the page.
    $end_item_on_page = $variables['current_page'] * $variables['items_per_page'] + $variables['items_per_page'];
    $end_item = min($end_item_on_page, $variables['total_items']);

    $t_args= array(
      '@total' => $variables['total_items'],
      '@start' => $start_item,
      '@end' => $end_item,
    );

    $summary_markup = t('Displaying activities @start-@end of @total', $t_args);
  }
  else {
    $summary_markup = format_plural($variables['total_items'], 'Displaying one activity', 'Displaying @count activities');
  }

  $elements['totals'] = array(
    '#markup' => $summary_markup,
    '#prefix' => '<span class="activity-search-summary">',
    '#suffix' => '</span>',
  );

  $elements['actions'] = array(
    '#prefix' => '<div class="activity-search-actions">',
    '#suffix' => '</div>',
  );

  $actions = array();

  $svgicon = helpertheme_get_svg_icons('export', array('width' => '1.2em', 'height' => '1.2em'));
  $l_args = array(
    'html' => TRUE,
    'external' => TRUE,
    'fragment' => 'export',
    'attributes' => array(
      'class' => array('action-link', 'action-export'),
    ),
  );
  $actions[] = l($svgicon . t('Export'), '', $l_args);

  $svgicon = helpertheme_get_svg_icons('link', array('width' => '1.2em', 'height' => '1.2em'));
  $l_args = array(
    'html' => TRUE,
    'external' => TRUE,
    'fragment' => 'permalink',
    'attributes' => array(
      'class' => array('action-link', 'action-permalink'),
    ),
  );
  $actions[] = l($svgicon . t('Permalink'), '', $l_args);

  $elements['actions']['list'] = array(
    '#markup' => implode('', $actions),
  );

  $elements['actions']['extra'] = array(
    '#prefix' => '<div class="activity-search-actions-extra">',
    '#suffix' => '</div>',
  );

  // @TODO: Use libraries API.
  $module_path = drupal_get_path('module', 'helpertheme');
  drupal_add_js($module_path . '/scripts/helpertheme.searchactions.js');
  drupal_add_library('system', 'drupal.progress');

  return drupal_render($elements);
}

/**
 * Returns HTML for the activities search footer totals.
 */
function theme_amp_report_totals($variables) {
  $elements = array();

  $totals = $variables['totals'];
  $currency = $variables['currency'];

  $elements['#prefix'] = '<div class="activity-search-totals clearfix">';
  $elements['#suffix'] = '</div>';

  $header = array('');
  $data = array(t('Totals'));
  foreach (array('Actual Commitments', 'Actual Disbursements') as $key) {
    if (!isset($totals[$key]['amount'])) {
      continue;
    }
    $label = $totals[$key]['header'];
    if (!empty($currency)) {
    	$label .= ' (' . check_plain($currency) . ')';
    }
    $header[] = $label;
    $data[] = $totals[$key]['amount'];
  }

  $elements['totals'] = array(
    '#theme' => 'table',
    '#sticky' => FALSE,
    '#header' => $header,
    '#rows' => array($data),
    '#attributes' => array('class' => array('totals-table')),
  );

  return drupal_render($elements);
}
