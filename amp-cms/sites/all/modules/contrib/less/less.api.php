<?php

/**
 * @file
 * Hooks provided by the LESS module.
 */

/**
 * @addtogroup hooks
 * @{
 */

/**
 * Define LESS variables.
 * 
 * Should return flat associative array, where key is variable name.
 * 
 * Variables are lazy evaluated, so variables that depend on others do not have
 * to appear in order.
 * 
 * Variables returned by this function are cached.
 * 
 * @see hook_less_variables_alter().
 * @see hook_less_variables_SYSTEM_NAME_alter().
 */
function hook_less_variables() {
  return array(
    '@variable_name_1' => '#ccc',
    '@variable_name_2' => 'darken(@variable_name_1, 30%)',
  );
}

/**
 * Alter LESS variables provided by other modules or themes.
 * 
 * @param $less_variables
 *   Flat associative array of variables, where key is variable name.
 * @param $system_name
 *   A string of the system_name of the module or theme that this applies to.
 * 
 * @see hook_less_variables().
 * @see hook_less_variables_SYSTEM_NAME_alter().
 */
function hook_less_variables_alter(&$less_variables, $system_name) {
  
}

/**
 * Alter LESS variables provided by other modules or themes.
 * 
 * @param $less_variables
 *   Flat associative array of variables, where key is variable name.
 * 
 * @see hook_less_variables().
 * @see hook_less_variables_alter().
 */
function hook_less_variables_SYSTEM_NAME_alter(&$less_variables) {
  
}

/**
 * Define LESS functions.
 * 
 * @return
 *   An associative where keys are LESS functions and values are PHP function
 *   names or anonymous functions. Anonymous functions require PHP >= 5.3.
 * 
 * @see http://leafo.net/lessphp/docs/#custom_functions
 */
function hook_less_functions() {
  return array(
    'less_func_1' => 'php_func_1',
    'less_func_2' => function ($arg) {
      list($type, $delimeter, $value) = $arg;
      
      return array($type, $delimeter, $value);
    },
  );
}

/**
 * Implements hook_less_functions_alter().
 * 
 * @param $less_functions
 *   Flat associative array of functions, where key is LESS function name and
 *   value is PHP function name or Anonymous function: 
 *   (http://php.net/manual/en/functions.anonymous.php)
 * @param $system_name
 *   A string of the system_name of the module or theme that this applies to.
 * 
 * @see http://leafo.net/lessphp/docs/#custom_functions
 */
function hook_less_functions_alter(&$less_functions, $system_name) {
  
}

/**
 * Implements hook_less_functions_SYSTEM_NAME_alter().
 * 
 * @param $less_functions
 *   Flat associative array of functions, where key is variable and value is
 *   function name or Anonymous function: 
 *   (http://php.net/manual/en/functions.anonymous.php)
 * 
 * @see http://leafo.net/lessphp/docs/#custom_functions
 */
function hook_less_functions_SYSTEM_NAME_alter(&$less_functions) {
  
}

/**
 * @} End of "addtogroup hooks".
 */
