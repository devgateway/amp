<?php

/*
 * define plugin
 */
$plugin = array(
  'single' => TRUE,
  'title' => t('Feedback'),
  'description' => t('Feedback'),
  'category' => array(t('AMPP Widgets')),
  'defaults' => array(
    'vocab' => 0
  ),
);


/**
 *
 */
function ampp_feedback_feedback_content_type_render($subtype, $conf, $panel_args, $context) {
  global $amp_db_exists;
  $block = new stdClass();
  $block->content = '';
	
  if (isset($_SESSION['feedback_form_success_message'])) {
    $block->content = $_SESSION['feedback_form_success_message'];
    unset($_SESSION['feedback_form_success_message']);
  } else {
     $block->content = drupal_get_form('feedback_form', $conf);
  }
  
  // let's persist the vocab conf value
  //variable_set('ampp_feedback_hide_vocab_' . $conf['pane_id'], $conf['vocab']);
  
  return $block;
}

function ampp_feedback_feedback_content_type_edit_form($form, &$form_state) {
  $conf = $form_state['conf'];
  
  $form['override_title_markup']['#access'] = false;
  
  $form['vocab'] = array(
    '#type' => 'checkbox',
    '#title' => t('Hide category select'),
    '#default_value' => isset($conf['vocab']) ? intval($conf['vocab']) : $form_state['plugin']['defaults']['vocab'],
  );
  
  return $form;
} 


function ampp_feedback_feedback_content_type_edit_form_submit(&$form, &$form_state) { 
   foreach (array_keys($form_state['plugin']['defaults']) as $key) {
    $form_state['conf'][$key] = $form_state['values'][$key];
  }
}