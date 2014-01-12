<?php
// $Id: template.preprocess.blocks.php,v 1.1 2012/01/03 22:10:20 vamirbekyan Exp $


/**
 * Implements template_preprocess_block().
 */
function ampp_preprocess_block(&$vars) {
  $function = '__' . __FUNCTION__ . '__' . $vars['block']->module . '_' . $vars['block']->delta;
  if (function_exists($function)) {
    $function($vars);
  }
}


/*
 * Implements template_preprocess_block() for Feedback block
 */
function __ampp_preprocess_block__feedback_form(&$vars) {
   $vars['block']->subject = '<span class="feedback-link">' . t('Feedback') . '<img id="feedback-close" src="/sites/all/themes/ampp/images/cancel_icon.gif"></span>';
}