<?php
/**
 * @file
 * Cusotm code for the AMPP content types feature.
 */

/**
 * Implementation of hook_form_alter().
 */
function ampp_content_types_form_alter(&$form, $form_state, $form_id) {
  // hiding not needed fields for custom content types
  if ($form_id == 'page_node_form') {
    unset($form['options']['promote']);
    unset($form['options']['sticky']);
  }
  if ($form_id == 'homepage_slideshow_node_form') {
    unset($form['options']['promote']);
    unset($form['options']['sticky']);
  }
  if ($form_id == 'donor_profile_node_form') {
    unset($form['options']['promote']);
    unset($form['options']['sticky']);
  }
  if ($form_id == 'map_node_form') {
    unset($form['options']['promote']);
    unset($form['options']['sticky']);
  }
  if ($form_id == 'news_node_form' || $form_id == 'event_node_form' || $form_id == 'imported_item_node_form') {
    unset($form['options']['promote']);
  }

  // renaming "Sticky at top of lists" for News and Events
  if ($form_id == 'news_node_form' || $form_id == 'event_node_form' || $form_id == 'imported_item_node_form') {
    $form['options']['sticky']['#title'] = t('Sticky at top of the list on the front page. Also should not be in any nodequeue.');
  }

  // removing not needed fields for Feed content type
  if ($form_id == 'feed_node_form') {
    unset($form['options']['promote']);
    unset($form['options']['sticky']);
  }

  /*
  // adding slideshow valiant preview to news, events and feed_item types
  // renaming "Sticky at top of lists" for News and Events
  if ($form_id == 'news_node_form' || $form_id == 'event_node_form' || $form_id == 'imported_item_node_form') {
    $form['slideshow_preview'] = array(
      '#weight' => 2,
      '#type' => 'markup',
      '#markup' => theme('image_style', array('style_name' => 'homepage_news_slideshow', 'path' => $form['#node']->field_image['und']['0']['uri'])),
    );
  }
  */
}