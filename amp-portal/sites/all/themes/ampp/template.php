<?php
// $Id: template.php,v 1.63.2.1 2014/01/07 22:23:46 vamirbekyan Exp $

include_once('template.preprocess.page.php');
include_once('template.preprocess.views.php');
include_once('template.pre_render.views.php');
include_once('template.preprocess.blocks.php');

// Suppresing admin menu for popup pages
if ((isset($_GET['printer']) && $_GET['printer'] == 1) || (arg(0) && arg(0)=='feedback') || (arg(0) && arg(0)=='mobile')) {
  module_invoke('admin_menu', 'suppress');
}


/**
 * Implementation of hook_system_themes_page_alter().
 */
function ampp_system_themes_page_alter(&$themes) {
  unset($themes['disabled']);
}


/**
 *
 */
function ampp_page_alter(&$page) {
 if(isset($_GET['format']) && $_GET['format'] == 'simple') {
  	unset($page['page_top']['toolbar']);  	
  }
}


/**
 * Implementation of hook_menu_alter().
 */
function ampp_menu_alter(&$items) { 
  unset($items['admin/appearance/settings/global']);
  unset($items['admin/appearance/settings/ampp_admin']);
  
  $items['admin/appearance/settings/ampp']['page callback'] = 'drupal_get_form';
  $items['admin/appearance/settings/ampp']['weight'] = 20;
  $items['admin/appearance/settings'] = $items['admin/appearance/settings/ampp'];
  
  $items['node/%node/panelizer']['title'] = t('Layout / Content');
}


/**
 * Implementation of hook_theme().
 */
function ampp_theme() {
  $items = array();
  
  $items['header_links_menu'] = array(
    'arguments' => array(),
  );

  $items['amp_links_menu'] = array(
    'arguments' => array(),
  );
  
  $items['footer_links_menu'] = array(
    'arguments' => array(),
  );
  
  $items['status_messages'] = array(
    'arguments' => array('display'),
  );

  $items['links'] = array(
    'arguments' => array(),
  );
  
  $items['panels_rounded_corners_region'] = array(
    'arguments' => array('content' => NULL),
    'path' => drupal_get_path('theme', 'ampp') . '/panel_templates',
    'template' => 'panels-rounded-corners-region--ampp',
  );
  
  $items['panels_rounded_corners_pane'] = array(
    'arguments' => array('content' => NULL),
    'path' => drupal_get_path('theme', 'ampp') . '/panel_templates',
    'template' => 'panels-rounded-corners-pane--ampp',
  );
  
  $items['panels_pane_withlogo__ampp'] = array(
    'arguments' => array('content' => NULL),
    'path' => drupal_get_path('theme', 'ampp') . '/panel_templates',
    'template' => 'panels-pane-withlogo--ampp',
  );

  return $items;
}


/**
 * Preprocess function for content-field.tpl.php
 */
function ampp_preprocess_content_field($vars) {
  $fields = array(
    'field_left_column_blocks',
    'field_center_column_blocks',
    'field_right_column_blocks',
  );
  if (!in_array($vars['field_name'], $fields) || empty($vars['items'])) {
    return;
  }

  $total = count($vars['items']);
  foreach ($vars['items'] as $delta => $item) {
    $item['#total'] = $total;
    $item['#field_name'] = $vars['field_name'];
    $vars['items'][$delta]['output'] = theme('dgc_render_pane', $item);
  }
}


/*
 * Overriding status_messages theme
 */
function ampp_status_messages($vars, $show_close_icon = TRUE) {
  $display = $vars['display'];
  $output = '';
  if(isset($_SESSION['feedback_messages'])) {
    $_SESSION['messages']['error'] = array_diff($_SESSION['messages']['error'], $_SESSION['feedback_messages']);
  }
  
  // Call drupal_get_messages clears queue by default,
  // and as it is being called in template_preprocess_page 
  // messages array is not availabe in preprocess hooks.
  // We will need this $_SESSION['temp_messages'] variable when 
  // we have to process messages using preprocess hooks.
  $m = $_SESSION['temp_messages'] = drupal_get_messages($display);
  
  $status_heading = array(
    'status' => t('Status message'), 
    'error' => t('Error message'), 
    'warning' => t('Warning message'),
  );
  foreach ($m as $type => $messages) {
    if (count($messages)) {
      $output .= "<div class=\"messages $type\">";
      if (!empty($status_heading[$type])) {
        $output .= '<h2 class="element-invisible">' . $status_heading[$type] . "</h2>";
      }
      if (count($messages) > 1) {
        $output .= " <ul>";
        foreach ($messages as $message) {
          $output .= '  <li>' . $message . "</li>";
        }
        $output .= " </ul>";
      }
      else {
        $output .= $messages[0];
      }
      if($show_close_icon) {
        $output .= '<span><img src="/sites/all/themes/ampp/images/cancel_icon.gif" id="close"></span>';
      }
      $output .= "</div>";
    }
  }
  return $output;
}


/*
 * A preprocess function for theme('links').
 */
function ampp_links($variables) {
  $links = $variables['links'];
  $attributes = $variables['attributes'];
  $heading = $variables['heading'];
  global $language_url;
  $output = '';

  if (count($links) > 0) {
    $output = '';

    // Treat the heading first if it is present to prepend it to the
    // list of links.
    if (!empty($heading)) {
      if (is_string($heading)) {
        // Prepare the array that will be used when the passed heading
        // is a string.
        $heading = array(
          'text' => $heading,
          // Set the default level of the heading. 
          'level' => 'h2',
        );
      }
      $output .= '<' . $heading['level'];
      if (!empty($heading['class'])) {
        $output .= drupal_attributes(array('class' => $heading['class']));
      }
      $output .= '>' . check_plain($heading['text']) . '</' . $heading['level'] . '>';
    }
    $output .= '<ul' . drupal_attributes($attributes) . '>';
    $num_links = count($links);
    $i = 1;

    foreach ($links as $key => $link) {
      $class = array($key);
      $separator_html = '';
      
      // Add first, last and active classes to the list of links to help out themers.
      if ($i == 1) {
        $class[] = 'first';
      }
      if ($i == $num_links) {
        $class[] = 'last';       
      } else {
        if (isset($variables['separator'])) $separator_html = '<span class="links_separator">' . $variables['separator'] . '</span>';
      }
      if (isset($link['href']) && ($link['href'] == $_GET['q'] || ($link['href'] == '<front>' && drupal_is_front_page()))
           && (empty($link['language']) || $link['language']->language == $language_url->language)) {
        $class[] = 'active';
      }
      $output .= '<li' . drupal_attributes(array('class' => $class)) . '>';

      if (isset($link['href'])) {
        if (variable_get('menu_edit_item_' . $key, 0)) $link['attributes']['target'] = '_blank';        
        // Pass in $link as $options, they share the same keys.
        $output .= l($link['title'], $link['href'], $link);
      }
      elseif (!empty($link['title'])) {
        // Some links are actually not links, but we wrap these in <span> for adding title and class attributes.
        if (empty($link['html'])) {
          $link['title'] = check_plain($link['title']);
        }
        $span_attributes = '';
        if (isset($link['attributes'])) {
          $span_attributes = drupal_attributes($link['attributes']);
        }
        $output .= '<span' . $span_attributes . '>' . $link['title'] . '</span>';
      }
      $i++;
      $output .= "</li>\n";
      if (!empty($separator_html)) $output .= $separator_html;
    }
    $output .= '</ul>';
  }
  return $output;
}


/**
 * A preprocess function for theme('header_links_menu').
 */
function ampp_header_links_menu() {
  return theme('links', array('links' => i18n_menu_navigation_links('menu-header-links'), 'separator' => '|'));
}


/**
 * A preprocess function for theme('amp_links_menu').
 */
function ampp_amp_links_menu() {
  return theme('links', array('links' => i18n_menu_navigation_links('menu-amp-links')));
}


/**
 * A preprocess function for theme('footer_links_menu').
 */
function ampp_footer_links_menu(&$vars) {
  return theme('links', array('links' => i18n_menu_navigation_links('menu-footer-links'), 'separator' => '|'));
}


/*
 * Custom langauge switch
 */
function ampp_lang_switch() {
  
  // return if only one langauge is enabled
  if (!drupal_multilingual()) {
    return '';
  }
  
  global $language;

  $path = drupal_is_front_page() ? '<front>' : $_GET['q'];
  
  // we get the query string
  $query_arr = array();
  parse_str($_SERVER['QUERY_STRING'], $query_arr);

  $type = variable_get('translation_language_type', LANGUAGE_TYPE_INTERFACE);
  
  $languages_links = language_negotiation_get_switch_links($type, $path);

  $element = array();
  $element['#attributes'] = array (
    'id' => 'langauge_select',
    'onchange' => 'javascript: window.location = this.options[selectedIndex].value;',
  );
  
  foreach ($languages_links->links as $key => $lang) {
    // if href is not set then this language version is not available and we fall over to the default language version
    // to be able to do this though we need to compose /node/# form with language prefix.
    if (!isset($lang['href'])) {
      $url = url($path, array('query' => $query_arr, 'language' => $lang['language']));
    } else {
      $url = url($lang['href'], array('query' => $query_arr, 'language' => $lang['language']));
    }

    $element['#options'][$url] = t($lang['title']);
    if ($lang['language']->language == $language->language) {
      $element['#value'] = $url;
    }
  }

  return t('Language') . theme('select', array('element' => $element));
}


/*
 * Custom site information form
 */
function get_amp_programs() {
  db_set_active("amp_projects");
  $items = array();   
  
  $sub_query = db_select('amp_theme')->fields('amp_theme', array('parent_theme_id'));
  $query = db_select('amp_theme', 'program');
  $query->fields('program', array('amp_theme_id', 'name'));
  $query->condition('program.amp_theme_id', $sub_query, 'IN');
  $query->orderBy('program.name', 'ASC');
  $result = $query->distinct()->execute()->fetchAll();

  foreach ($result as $record) {
    $items[$record->amp_theme_id] = $record->name;
  }
    
  db_set_active();
  return $items;
}


/*
 * Custom site information form
 */
function ampp_form_alter(&$form, $form_state, $form_id) {
  
  // hiding adnministration form settings
  if ($form_id == 'system_themes_admin_form') {
    $form = array();
  }
  
  if ($form_id == 'system_site_information_settings') {
    
    $form['site_information']['amp_login_text'] = array(
      '#type' => 'textfield',
      '#title' => t('AMP login text'),
      '#default_value' => variable_get('amp_login_text', 'Login Here'), 
      '#description' => t('Max 30 characters. Leave blank to hide the button.'),
      '#size' => 60,   
      '#maxlength' => 30,
    );
    
    $form['site_information']['amp_login_url'] = array(
      '#type' => 'textfield',
      '#title' => t('AMP login url'),
      '#default_value' => variable_get('amp_login_url', ''), 
      '#description' => t('Use "%lang_code%" placeholder for language code. Example: http://amis.mof.gov.np/login?language=%lang_code%'),
      '#size' => 60,   
    );
    
    $form['site_information']['amp_activity_url'] = array(
      '#type' => 'textfield',
      '#title' => t('Full URI to AMP activity details page'),
      '#default_value' => variable_get('amp_activity_url', ''), 
      '#description' => t('Use "%aid%" marker to be replaced with activity id. Example: https://liberia.ampdev.net/wicket/onepager/activity/%aid%'),
      '#size' => 60,   
    );
    
    $form['site_information']['amp_activity_popup_str'] = array(
      '#type' => 'textfield',
      '#title' => t('AMP activity details page popup specs'),
      '#default_value' => variable_get('amp_activity_popup_str', ''), 
      '#description' => t('window.open(URL,name,specs,replace) function "specs" parameter\'s string value.'),
      '#size' => 60,   
    );
    
    $form['site_information']['amp_no_search_results_text'] = array(
      '#type' => 'textarea',
      '#title' => t('No search results text'),
      '#default_value' => variable_get('amp_no_search_results_text', ''), 
      '#rows' => 5,   
    );
    
    $form['site_information']['amp_programs_level'] = array(
      '#type' => 'radios',
      '#title' => t('Choose programs level'),
      '#default_value' => variable_get('amp_programs_level', 2),
      '#options' => array(1 => 'National', 2 => 'Primary', 3 => 'Secondary'),  
    );
    
    $programs = get_amp_programs();
    $programs = array('0' => t('None')) + $programs;
    $form['site_information']['amp_programs_root'] = array(
      '#type' => 'select',
      '#title' => t('Root program'),
      '#default_value' => variable_get('amp_programs_root', 0),
      '#options' => $programs,
      '#description' => t('This will serve as an entry point programs selection interface. Please note that this and "Choose programs level" selection conditions are combined.'),  
    );
    
    $form['site_information']['amp_limit_programs_to_activities'] = array(
      '#type' => 'checkbox',
      '#title' => t('Limit programs to only those that are mapped to activities'),
      '#default_value' => variable_get('amp_limit_programs_to_activities', 1),  
    );
    
    $form['front_page'] = array(
      '#type' => 'fieldset',
      '#title' => t('Front page'),
      'site_frontpage' => array(
        '#type' => 'textfield',
        '#title' => t('Default front page'),
        '#default_value' =>  variable_get('site_frontpage', t('homepage')), 
        '#size' => 60,
      ),
      'site_frontpage_block_1_title' => array(
        '#type' => 'textfield',
        '#title' => t('Front second row left block title'),
        '#default_value' => variable_get('site_frontpage_block_1_title', t('Donor profile')), 
        '#size' => 60,
      ),
      'site_frontpage_block_2_title' => array(
        '#type' => 'textfield',
        '#title' => t('Front second row right block title'),
        '#default_value' => variable_get('site_frontpage_block_2_title', t('Map')), 
        '#size' => 60,
      ),
      /*
      'site_frontpage_news_teaser_records' => array(
        '#type' => 'textfield',
        '#title' => t('News records on the front page'),
        '#default_value' => variable_get('site_frontpage_news_teaser_records', 6),
        '#description' => t('Number of news records in the home page teaser'), 
        '#size' => 2,
        '#maxlength' => 2,
        '#element_validate' => array('_news_records_validation'),
      ),
      */     
    );
    
    $default_dg_footer = '<a href="http://www.developmentgateway.org" target="_blank"><img src="/sites/all/themes/ampp/images/dgf_logo_bottom.gif"/></a><br/>1889 F Street, NW, Second Floor, Washington, D.C. 20006, USA<br/>info@developmentgateway.org, Tel: +1.202.572.9200, Fax: +1 202.572.9290';
    $form['footer'] = array(
      '#type' => 'fieldset',
      '#title' => t('Footer'),
      'site_share_label' => array(
        '#type' => 'textfield',
        '#title' => t('Social sharing icons lable'),
        '#default_value' => variable_get('site_share_label', ''), 
        '#size' => 50,
        '#maxlength' => 50,
      ),
      'site_footer_text' => array(
        '#type' => 'textfield',
        '#title' => t('Footer text'),
        '#default_value' => variable_get('site_footer_text', ''), 
        '#size' => 150,
        '#maxlength' => 1000,
      ),
      'dg_footer_html' => array(
        '#type' => 'textarea',
        '#title' => t('DG footer html'),
        '#default_value' => variable_get('dg_footer_html', $default_dg_footer), 
        '#description' => t('HTML for DG footer. To toggle DG footer go !link', array ( '!link' => '<a href="/admin/appearance/settings/ampp">' . t('here') . '</a>')),
        '#rows' => 5,
      ),    
    );
    
    $form['error_page'] = array(
      '#type' => 'hidden',
      '#title' => t('Error pages'),
      'site_403' => array(
        '#type' => 'textfield',
        '#title' => t('Default 403 (access denied) page'),
        '#default_value' => variable_get('site_403', ''),
        '#size' => 60,
        '#description' => t('This page is displayed when the requested document is denied to the current user. Leave blank to display a generic "access denied" page.'),
      ),
      'site_404' => array(
        '#type' => 'textfield',
        '#title' => t('Default 404 (access denied) page'),
        '#default_value' => variable_get('site_404', ''),
        '#size' => 60,
        '#description' => t('This page is displayed when no other content matches the requested document. Leave blank to display a generic "page not found" page.'),
      ),
    );
  }
}


/*
 * Validation callback function
 */
function _news_records_validation($element, &$form_state) {
  if (!is_numeric($element['#value']) || !intval($element['#value'])) { 
    form_error($element, t('The "!name" value should be numeric and greater than zero.', array ('!name' => t($element['#title']))));
  }
}


/*
 * Custom panes preprocessor
 */
function ampp_preprocess_panels_pane(&$vars) {

  // suggest custom tempaltes
  if (isset($vars['pane']->style['style'])) {
    switch ($vars['pane']->style['style']) {
      case 'rounded_corners' :
        $vars['theme_hook_suggestions'][] = 'panels_pane_withlogo__ampp';
        break;
      default :
        $vars['theme_hook_suggestions'][] = 'panels_pane__ampp';
        break;
    }
  }
  
  // configurable titles
  if (isset($vars['pane']->panel)) {
    switch ($vars['pane']->panel) {
      case 'donor_profile' :
        $vars['title'] = t(variable_get('site_frontpage_block_1_title', ''));
        break;
      case 'map' :
        $vars['title'] = t(variable_get('site_frontpage_block_2_title', ''));
        break;
    }
  }
  
  // blocks logo
  $default_used = false;
  if (theme_get_setting('default_blocks_logo', 'ampp')) {
    // default blocks logo is used
    $default_used = true;
    $blocks_logo_path = drupal_get_path('theme', 'ampp') . '/blocks-logo.png';
  } else {
    // custom blocks logo is used
    $blocks_logo_path = theme_get_setting('blocks_logo_path', 'ampp');
    if (file_uri_scheme($blocks_logo_path) == 'public') {
      $blocks_logo_path = file_uri_target($blocks_logo_path);
    }
  }
  $vars['blocks_logo_url'] = '';
  if ($default_used) {
    $vars['blocks_logo_url'] = '/' . $blocks_logo_path;
  } elseif (!empty($blocks_logo_path)) {
    $vars['blocks_logo_url'] = image_style_url('blocks_logo', $blocks_logo_path); 
  }
  
  return $vars;
}


/**
 * Render callback.
 *
 * @ingroup themeable
 */
function ampp_panels_rounded_corners_style_render_region($vars) {
  $display = $vars['display']; 
  $region_id = $vars['region_id'];
  $panes = $vars['panes'];
  $settings = $vars['settings'];

  $output = '';

  // Determine where to put the box. If empty or 'pane' around each pane. If
  // 'panel' then just around the whole panel.
  $where = empty($settings['corner_location']) ? 'pane' : $settings['corner_location'];

  $print_separator = FALSE;
  foreach ($panes as $pane_id => $pane) {
    // Add the separator if we've already displayed a pane.
    if ($print_separator) {
      $output .= '<div class="panel-separator">&nbsp;</div>';
    }

    if ($where == 'pane') {
      $output .= theme('panels_rounded_corners_pane', array('content' => $pane));
    }
    else {
      $output .= $pane;
      $print_separator = TRUE;
    }
  }

  if ($where == 'panel') {
    $output = theme('panels_rounded_corners_region', array('content' => $output));
  }

  //ampp_panels_add_rounded_corners_css($display, $where);

  return $output;
}


/**
 * Render callback for a single pane.
 */
function ampp_panels_rounded_corners_style_render_pane($vars) {
  $content = $vars['content'];
  $pane = $vars['pane'];
  $display = $vars['display'];

  if (empty($content->content)) {
    return;
  }

  $output = theme('panels_pane', array('content' => $content, 'pane' => $pane, 'display' => $display));

  // Just stick a box around the standard theme_panels_pane.
  $output = theme('panels_rounded_corners_pane', array('content' => $output));
  // ampp_panels_add_rounded_corners_css($display, 'pane');
  return $output;
}


/**
 * Implements hook_process_html().
 */
function ampp_process_html(&$vars) {
  // Hook into color.module.
  if (module_exists('color')) {
    _color_html_alter($vars);
  }
}