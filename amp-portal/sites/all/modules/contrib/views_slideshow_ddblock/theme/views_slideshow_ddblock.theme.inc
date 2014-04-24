<?php

/**
 * @file
 * Theme & preprocess functions for the Views Slideshow: ddblock module.
 */

/**
 *  content preprocess function.
 */
function _views_slideshow_ddblock_preprocess_views_slideshow_ddblock_main_frame(&$vars) {

  // simplify variable names
  $options = $vars['settings'];

  // DDblock template used
  if ($options['template'] <> 'none') {
    $view = $vars['view'];

    // get view result
    $rows = $view->result;
    $field_rows = $view->field;

    // Remove empty mappings
    foreach ($options['mapping_wrapper']['mappings'] as $key => $value) {
      if (empty($value['target'])) {
        unset($options['mapping_wrapper']['mappings'][$key]);
      }
    }

    // create result array
    foreach ($view->result as $row_num => $result) {
      foreach ($view->field as $id => $field) {
        $field_output = $view->style_plugin->get_field($row_num, $id);
        foreach ($options['mapping_wrapper']['mappings'] as $mapping_key => $mapping_value) {
          if ($mapping_value['source'] == $id) {
            $mapping_target = $mapping_value['target'];
            $result_rows[$row_num][$mapping_target] = $field_output;
          }
        }
      }
    }

    // sort the result_rows using ddblock, none for using order of view
    if (!empty($result_rows)) {
      switch ($options['settings']['order']) {
        case 'random':
          shuffle($result_rows);
          break;
        case 'asc':
          asort($result_rows);
          break;
        case 'desc':
          rsort($result_rows);
          break;
        case 'none':
          break;
      }

      $nr_of_items = count($result_rows);
      // set jquery cycle settings
      $settings['viewsSlideshowDdblockContent'][$view->name . '_' . $view->current_display] = array(
        'block'                      => $view->name . '_' . $view->current_display,
        'nrOfItems'                  => $nr_of_items,
        'setDimensions'              => $options['template'],
        'contentContainer'           => $options['content_container']['container'],
        'custom'                     => $options['settings']['custom']['custom_jquery'],
        'fx'                         => $options['settings']['fx'],
        'speed'                      => (int) $options['settings']['speed'],
        'timeOut'                    => (int) $options['settings']['timeout'],
        'pause'                      => (int) $options['settings']['pause'],
        'next'                       => (int) $options['settings']['next'],
        'overflow'                   => $options['content_container']['overflow'],
        'pagerToggle'                => $options['settings']['pager_toggle'],
        'pager'                      => $options['settings']['pager_settings']['pager'],
        'pagerPosition'              => $options['settings']['pager_settings']['pager_position'],
        'nrOfPagerItems'             => (int) $options['settings']['pager_settings']['nr_of_pager_items'],
        'pagerContainer'             => $options['settings']['pager_settings']['pager_container'],
        'pagerEvent'                 => $options['settings']['pager_settings']['pager_event'],
        'pagerDisableClick'          => (int) $options['settings']['pager_settings']['pager_disable_click'],
        'pagerFast'                  => (int) $options['settings']['pager_settings']['pager_fast'],
        'pagerPause'                 => (int) $options['settings']['pager_settings']['pager_pause'],
        'pager2'                     => (int) $options['settings']['pager2'],
        'pager2Event'                => $options['settings']['pager2_settings']['pager2_event'],
        'pager2PagerHide'            => $options['settings']['pager2_settings']['pager2_pager']['pager2_pager_hide'],
        'pager2SlideHide'            => $options['settings']['pager2_settings']['pager2_slide']['pager2_slide_hide'],
        'slideText'                  => (int) $options['settings']['slide_text'],
        'slideTextjQuery'            => $options['settings']['slide_text_settings']['slide_text_jquery'],
        'slideTextPosition'          => $options['settings']['slide_text_settings']['slide_text_position'],
        'slideTextContainer'         => $options['settings']['slide_text_settings']['slide_text_container'],
        'slideTextEffectBefore'      => $options['settings']['slide_text_settings']['slide_jquery']['slide_text_before_effect'],
        'slideTextEffectBeforeSpeed' => (int) $options['settings']['slide_text_settings']['slide_jquery']['slide_text_before_speed'],
        'slideTextEffectAfter'       => $options['settings']['slide_text_settings']['slide_jquery']['slide_text_after_effect'],
        'slideTextEffectAfterSpeed'  => (int) $options['settings']['slide_text_settings']['slide_jquery']['slide_text_after_speed'],
      );

      // set template variables
      $vars['views_slideshow_ddblock_slider_settings']['delta']               = $view->name . '_' . $view->current_display; //$vars['id'];
      $vars['views_slideshow_ddblock_slider_settings']['debug_info']          = $options['debug_info'];
      $vars['views_slideshow_ddblock_slider_settings']['output_type']         = 'view_fields';
      $vars['views_slideshow_ddblock_slider_settings']['nr_of_items']         = $nr_of_items;
      $vars['views_slideshow_ddblock_slider_settings']['nr_of_pager_items']   = $options['settings']['pager_settings']['nr_of_pager_items'];
      $vars['views_slideshow_ddblock_slider_settings']['pager_toggle']        = $options['settings']['pager_toggle'];
      $vars['views_slideshow_ddblock_slider_settings']['pager']               = $options['settings']['pager_settings']['pager'];
      $vars['views_slideshow_ddblock_slider_settings']['pager_position']      = $options['settings']['pager_settings']['pager_position'];
      $vars['views_slideshow_ddblock_slider_settings']['pager2']              = $options['settings']['pager2'];
      $vars['views_slideshow_ddblock_slider_settings']['pager2_position']     = $options['settings']['pager2_settings']['pager2_position'];
      $vars['views_slideshow_ddblock_slider_settings']['pager2_pager_prev']   = $options['settings']['pager2_settings']['pager2_pager']['pager2_pager_prev'];
      $vars['views_slideshow_ddblock_slider_settings']['pager2_pager_next']   = $options['settings']['pager2_settings']['pager2_pager']['pager2_pager_next'];
      $vars['views_slideshow_ddblock_slider_settings']['pager2_slide_prev']   = $options['settings']['pager2_settings']['pager2_slide']['pager2_slide_prev'];
      $vars['views_slideshow_ddblock_slider_settings']['pager2_slide_next']   = $options['settings']['pager2_settings']['pager2_slide']['pager2_slide_next'];
      $vars['views_slideshow_ddblock_slider_settings']['slide_text']          = $options['settings']['slide_text'];
      $vars['views_slideshow_ddblock_slider_settings']['slide_text_position'] = $options['settings']['slide_text_settings']['slide_text_position'];
      $vars['views_slideshow_ddblock_slider_settings']['view_name']           = $view->name;
      $vars['views_slideshow_ddblock_slider_settings']['view_display_id']     = $view->current_display;


      if ($vars['views_slideshow_ddblock_slider_settings']['slide_text_position'] == "top" ||
          $vars['views_slideshow_ddblock_slider_settings']['slide_text_position'] == "bottom") {
        $vars['views_slideshow_ddblock_slider_settings']['slide_direction'] = "horizontal";
      }
      else {
        $vars['views_slideshow_ddblock_slider_settings']['slide_direction'] = "vertical";
      }
      $vars['views_slideshow_ddblock_slider_settings']['template'] = $options['template'];
      $vars['views_slideshow_ddblock_slider_settings']['template_size'] = $options['template_size_wrapper']['template_size'];

      $vars['views_slideshow_ddblock_content'] = $result_rows;

      // Add the required JS and CSS.
      // Don't load javascript unless libraries module is present.
      if (module_exists('libraries')) {
        // Load jQuery Cycle
        $cycle_path = libraries_get_path('jquery.cycle');
        if (!empty($cycle_path) && file_exists($cycle_path . '/jquery.cycle.all.min.js')) {
          drupal_add_js($cycle_path . '/jquery.cycle.all.min.js');
        }
        elseif (!empty($cycle_path) && file_exists($cycle_path . '/jquery.cycle.all.js')) {
          drupal_add_js($cycle_path . '/jquery.cycle.all.js');
        }

        //load easing 
        $easing_path = libraries_get_path('easing');
        if (!empty($easing_path) && file_exists($easing_path . '/jquery.easing.1.1.3.js')) {
          drupal_add_js($easing_path . '/jquery.easing.1.1.3.js');
        }
        elseif (!empty($easing_path) && file_exists($easing_path . '/jquery.easing.1.1.1.js')) {
          drupal_add_js($easing_path . '/jquery.easing.1.1.1.js');
        }
      }

      // Load views_slideshow_ddblock js files
      $module_path = drupal_get_path('module', 'views_slideshow_ddblock');
      
      // add json2.pack.js file
      drupal_add_js($module_path . '/js/json2.pack.js');

      // Load views_slideshow_ddblock.js
      drupal_add_js($module_path . '/js/views_slideshow_ddblock.js');

      // Add settings for views_slideshow_ddblock.js;
      drupal_add_js($settings, array('type' => 'setting', 'scope' => JS_DEFAULT));      
      
      // additional candidate template files
      $vars['theme_hook_suggestions'][] = 'views_slideshow_ddblock_main_frame__' . str_replace('-', '_' , $options['template']);
      $vars['theme_hook_suggestions'][] = 'views_slideshow_ddblock_main_frame__' . str_replace('-', '_', $vars['views_slideshow_ddblock_slider_settings']['delta']);

      // pager content settings
      $pager_settings['delta']               = $view->name . '_' . $view->current_display;
      $pager_settings['debug_info']          = $options['debug_info'];
      $pager_settings['output_type']         = 'view_fields';
      $pager_settings['pager']               = $options['settings']['pager_settings']['pager'];
      $pager_settings['pager_container']     = $options['settings']['pager_settings']['pager_container'];
      $pager_settings['pager_event']         = $options['settings']['pager_settings']['pager_event'];
      $pager_settings['pager_position']      = $options['settings']['pager_settings']['pager_position'];
      $pager_settings['pager2']              = $options['settings']['pager2'];
      $pager_settings['pager2_position']     = $options['settings']['pager2_settings']['pager2_position'];
      $pager_settings['pager2_pager_prev']   = $options['settings']['pager2_settings']['pager2_pager']['pager2_pager_prev'];
      $pager_settings['pager2_pager_next']   = $options['settings']['pager2_settings']['pager2_pager']['pager2_pager_next'];
      $pager_settings['pager2_slide_prev']   = $options['settings']['pager2_settings']['pager2_slide']['pager2_slide_prev'];
      $pager_settings['pager2_slide_next']   = $options['settings']['pager2_settings']['pager2_slide']['pager2_slide_next'];

      $pager_settings['template']            = $options['template'];
      $pager_settings['view_name']           = $view->name;
      $pager_settings['view_display_id']     = $view->current_display;
      $pager_settings['nr_of_items']         = $nr_of_items;
      $pager_settings['nr_of_pager_items']   = $options['settings']['pager_settings']['nr_of_pager_items'];


      $vars['views_slideshow_ddblock_pager_content'] = theme(
        'views_slideshow_ddblock_pager_content',
        array(
          'views_slideshow_ddblock_pager_settings' => $pager_settings,
          'views_slideshow_ddblock_pager_content' => $vars['views_slideshow_ddblock_content'],
        )
      );

      views_slideshow_ddblock_show_content_debug_info($vars);
      $vars['views_slideshow_ddblock_slider_items'] = $vars['views_slideshow_ddblock_content'];

      // set pager variable for javascript
      $settings = array();
      $settings['ddblockCustomTemplate'] = array(
        'pager' => $pager_settings['pager'],
        'pagerContainer' => $pager_settings['pager_container'],
        'pagerPosition' => $pager_settings['pager_position'],
      );

      drupal_add_js($settings, array('type' => 'setting', 'scope' => JS_DEFAULT));

    }
  }
}
/**
 *  Pager-items preprocess function.
 */
function _views_slideshow_ddblock_preprocess_views_slideshow_ddblock_pager_content(&$vars) {
//  drupal_set_message('<pre>pager_content: ' . var_export($vars['settings'], true) . '</pre>');
  if (!empty($vars['views_slideshow_ddblock_pager_content'])) {

    //simplify variable
    $settings = $vars['views_slideshow_ddblock_pager_settings'];

    // additional candidate template files
    $vars['theme_hook_suggestions'][] = 'views_slideshow_ddblock_pager_content__' . str_replace('-', '_', $settings['template']);
    $vars['theme_hook_suggestions'][] = 'views_slideshow_ddblock_pager_content__' . str_replace('-', '_', $settings['delta']);

    // for showing debug info
    views_slideshow_ddblock_show_pager_debug_info($vars);
    $vars['views_slideshow_ddblock_pager_items'] = $vars['views_slideshow_ddblock_pager_content'];
  }
}