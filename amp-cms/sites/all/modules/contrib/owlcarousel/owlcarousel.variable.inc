<?php

/**
 * @file
 * owlcarousel.variable.inc
 */
 
/**
 * Implements hook_variable_type_info().
 */
function owlcarousel_variable_type_info() {
  $type['instance_id'] = array(
    'title' => t('Instance ID'),
    'options callback' => 'owlcarousel_instance_callback_list',
    'type' => 'select',
  );
  $type['width_items'] = array(
    'title' => t('Browser Items'),
    'multiple' => array('width' => t('Width'), 'items' => t('Items')),
    'build callback' => 'owlcarousel_variable_build_browser_width',
    'localize' => TRUE,
    'type' => 'multiple',
  );

  return $type;
}
 
/**
 * Implements hook_variable_group_info().
 */
function owlcarousel_variable_group_info() {
  $groups['owlcarousel'] = array(
    'title' => t('Owl Carousel'),
    'description' => t('Define instance settings for each owl carousel.'),
    'access' => 'administer owlcarousel settings',
    'path' => array('admin/config/user-interface/owlcarousel'),
  );

  return $groups;
}

/**
 * Implements hook_variable_info().
 */
function owlcarousel_variable_info($options) {
  $variable['owlcarousel_items_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Items', array(), $options),
    'description' => t('This variable allows you to set the maximum amount of items displayed at a time with the widest browser width', array(), $options),
    'repeat' => array(
      'type' => 'number',
      'default' => 5,
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_items_desktop_[instance_id]_[itemwidth_part]'] = array(
    'type' => 'multiple',
    'title' => t('Items Desktop', array(), $options),
    'description' => t('This allows you to preset the number of slides visible with a particular browser width.', array(), $options),
    'repeat' => array(
      'type' => 'width_items',
      'default' => array(
        'width' => t(1199, array(), $options),
        'items' => t(4, array(), $options),
      ),
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_items_desktop_small_[instance_id]_[itemwidth_part]'] = array(
    'type' => 'multiple',
    'title' => t('Items Desktop Small', array(), $options),
    'description' => t('This allows you to preset the number of slides visible with a particular browser width.', array(), $options),
    'repeat' => array(
      'type' => 'width_items',
      'default' => array(
        'width' => t(979, array(), $options),
        'items' => t(3, array(), $options),
      ),
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_items_tablet_[instance_id]_[itemwidth_part]'] = array(
    'type' => 'multiple',
    'title' => t('Items Desktop Tablet', array(), $options),
    'description' => t('This allows you to preset the number of slides visible with a particular browser width.', array(), $options),
    'repeat' => array(
      'type' => 'width_items',
      'default' => array(
        'width' => t(768, array(), $options),
        'items' => t(2, array(), $options),
      ),
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_items_tablet_small_[instance_id]_[itemwidth_part]'] = array(
    'type' => 'multiple',
    'title' => t('Items Desktop Tablet Small', array(), $options),
    'description' => t('This allows you to preset the number of slides visible with a particular browser width.', array(), $options),
    'repeat' => array(
      'type' => 'width_items',
      'default' => array(
        'width' => t(0, array(), $options),
        'items' => t(0, array(), $options),
      ),
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_items_mobile_[instance_id]_[itemwidth_part]'] = array(
    'type' => 'multiple',
    'title' => t('Items Desktop Mobile', array(), $options),
    'description' => t('This allows you to preset the number of slides visible with a particular browser width.', array(), $options),
    'repeat' => array(
      'type' => 'width_items',
      'default' => array(
        'width' => t(479, array(), $options),
        'items' => t(1, array(), $options),
      ),
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_single_item_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Single Item', array(), $options),
    'description' => t('Display only one item.', array(), $options),
    'repeat' => array(
      'type' => 'boolean',
      'default' => FALSE,
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_items_scale_up_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Items Scale Up', array(), $options),
    'description' => t('Option to not stretch items when it is less than the supplied items.', array(), $options),
    'repeat' => array(
      'type' => 'boolean',
      'default' => FALSE,
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_slide_speed_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Slide Speed', array(), $options),
    'description' => t('Slide speed in milliseconds.', array(), $options),
    'repeat' => array(
      'type' => 'number',
      'default' => 200,
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_pagination_speed_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Pagination Speed', array(), $options),
    'description' => t('Pagination speed in milliseconds.', array(), $options),
    'repeat' => array(
      'type' => 'number',
      'default' => 800,
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_rewind_speed_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Rewind Speed', array(), $options),
    'description' => t('Rewind speed in milliseconds.', array(), $options),
    'repeat' => array(
      'type' => 'number',
      'default' => 1000,
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_auto_play_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Auto Play', array(), $options),
    'description' => t('Change to any integrer for example 5000 to play every 5 seconds.', array(), $options),
    'repeat' => array(
      'type' => 'number',
      'default' => 5000,
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_stop_on_hover_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Stop on Hover', array(), $options),
    'description' => t('Stop autoplay on mouse hover.', array(), $options),
    'repeat' => array(
      'type' => 'boolean',
      'default' => FALSE,
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_navigation_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Navigation', array(), $options),
    'description' => t('Display "next" and "prev" buttons.', array(), $options),
    'repeat' => array(
      'type' => 'boolean',
      'default' => TRUE,
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_navigation_text_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Navigation Text', array(), $options),
    'description' => t('You can customize your own text for navigation.', array(), $options),
    'repeat' => array(
      'type' => 'array',
      'default' => array('prev', 'next'),
    ),
    'localize' => TRUE,
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_rewind_nav_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Rewind Nav', array(), $options),
    'description' => t('Slide to first item. Use rewindSpeed to change animation speed.', array(), $options),
    'repeat' => array(
      'type' => 'boolean',
      'default' => TRUE,
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_scroll_per_page_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Scroll Per Page', array(), $options),
    'description' => t('Scroll per page not per item.', array(), $options),
    'repeat' => array(
      'type' => 'boolean',
      'default' => FALSE,
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_pagination_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Pagination', array(), $options),
    'description' => t('Show pagination.', array(), $options),
    'repeat' => array(
      'type' => 'boolean',
      'default' => TRUE,
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_pagination_numbers_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Pagination Numbers', array(), $options),
    'description' => t('Show numbers inside pagination buttons', array(), $options),
    'repeat' => array(
      'type' => 'boolean',
      'default' => FALSE,
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_responsive_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Responsive', array(), $options),
    'description' => t('You can use Owl Carousel on desktop-only websites too! Just change this to "false" to disable resposive capabilities.', array(), $options),
    'repeat' => array(
      'type' => 'boolean',
      'default' => TRUE,
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_responsive_refresh_rate_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Responsive Refresh Rate', array(), $options),
    'description' => t('Check window width changes every 200ms for responsive actions.', array(), $options),
    'repeat' => array(
      'type' => 'number',
      'default' => 200,
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_responsive_base_width_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Responsive Base Width', array(), $options),
    'description' => t('Owl Carousel check window for browser width changes.', array(), $options),
    'repeat' => array(
      'type' => 'string',
      'default' => 'window',
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_base_class_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Base Class', array(), $options),
    'description' => t("Automaticly added class for base CSS styles. Best not to change it if you don't need to.", array(), $options),
    'repeat' => array(
      'type' => 'string',
      'default' => 'owl-carousel',
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_theme_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Theme', array(), $options),
    'description' => t('Default Owl CSS styles for navigation and buttons. Change it to match your own theme.', array(), $options),
    'repeat' => array(
      'type' => 'string',
      'default' => 'owl-theme',
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_lazy_load_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Lazy Load', array(), $options),
    'description' => t("Delays loading of images. Images outside of viewport won't be loaded before user scrolls to them.", array(), $options),
    'repeat' => array(
      'type' => 'boolean',
      'default' => FALSE,
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_lazy_follow_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Lazy Follow', array(), $options),
    'description' => t('When pagination used, it skips loading the images from pages that got skipped. It only loads the images that get displayed in viewport.', array(), $options),
    'repeat' => array(
      'type' => 'boolean',
      'default' => TRUE,
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_lazy_effect_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Lazy Effect', array(), $options),
    'description' => t('Default is fadeIn on 400ms speed. Use false to remove that effect.', array(), $options),
    'repeat' => array(
      'type' => 'string',
      'default' => 'fadeIn',
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_auto_height_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Auto Height', array(), $options),
    'description' => t('Add height to owl-wrapper-outer so you can use diffrent heights on slides. Use it only for one item per page setting.', array(), $options),
    'repeat' => array(
      'type' => 'boolean',
      'default' => FALSE,
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_json_path_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('JSON Path', array(), $options),
    'description' => t('Allows you to load directly from a jSon file. The JSON structure you use needs to match the owl JSON structure used here. To use custom JSON structure see jsonSuccess option.', array(), $options),
    'repeat' => array(
      'type' => 'string',
      'default' => t('', array(), $options),
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_json_success_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('JSON Success', array(), $options),
    'description' => t('Success callback for $.getJSON build in into carousel.', array(), $options),
    'repeat' => array(
      'type' => 'string',
      'default' => t('', array(), $options),
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_drag_before_anim_finish_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Drag before animation finish', array(), $options),
    'description' => t('Ignore whether a transition is done or not (only dragging).', array(), $options),
    'repeat' => array(
      'type' => 'boolean',
      'default' => TRUE,
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_mouse_drag_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Mouse Drag', array(), $options),
    'description' => t('Turn off/on mouse events.', array(), $options),
    'repeat' => array(
      'type' => 'boolean',
      'default' => TRUE,
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_touch_drag_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Touch Drag', array(), $options),
    'description' => t('Turn off/on touch events.', array(), $options),
    'repeat' => array(
      'type' => 'boolean',
      'default' => TRUE,
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_add_class_active_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Add Class Active', array(), $options),
    'description' => t('Add "active" classes on visible items. Works with any numbers of items on screen.', array(), $options),
    'repeat' => array(
      'type' => 'boolean',
      'default' => FALSE,
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_transition_style_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Transition Style', array(), $options),
    'description' => t('Add CSS3 transition style. Works only with one item on screen.', array(), $options),
    'repeat' => array(
      'type' => 'string',
      'default' => t('', array(), $options),
    ),
    'group' => 'owlcarousel',
  );
  $variable['owlcarousel_override_ajax_pagination_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Override Views AJAX Pagination', array(), $options),
    'description' => t('Override views default pagination; appends new slide (row) by AJAX. Please note, this option requires a view with AJAX enabled & the carousel pager selected. (Experimental feature)', array(), $options),
    'repeat' => array(
      'type' => 'boolean',
      'default' => FALSE,
    ),
    'group' => 'owlcarousel',
  );

  return $variable;
}

/**
 * Variable build callback for browser/items.
 */
function owlcarousel_variable_build_browser_width($variable, $options = array()) {
  $index = str_replace('[instance_id]', $variable['index'], $variable['parent']);
  $name = str_replace('[itemwidth_part]', '', $index);

  foreach ($variable['multiple'] as $key => $child) {
    $repeat_name = $name . $key;

    $variable['name'] = $repeat_name;
    $variable['children'][$repeat_name]['title'] = $child;
    $variable['children'][$repeat_name]['index'] = $key;
    $variable['children'][$repeat_name]['type'] = 'number';
    $variable['children'][$repeat_name]['default'] = $variable['default'][$key];

    $variable = variable_build_multiple($variable, $options);
  }

  return $variable;
}
