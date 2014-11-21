<?php

/**
 * @file
 * Shared classes and interfaces for the API mapper system.
 */

/**
 * Defines the common interface for all API mapper classes.
 *
 * @TODO: Document!
 */
interface activityMapperInterface {
  public static function propertyInfo($plugin);
  public static function filter($plugin, &$api_parameters, &$request_body, $condition);
  public static function sort($plugin, &$api_parameters, &$request_body, $order);
  public static function getValue($plugin, $column);
}

class activityMapperException extends Exception {}

/**
 * Defines the base implementation for all API mappers.
 */
class activityMapperBase implements activityMapperInterface {
  /**
   * Property Info handler.
   */
  public static function propertyInfo($plugin) {
    $info = array(
      'label' => $plugin['title'],
      'description' => $plugin['description'],
    );
    $info['property info'] = $plugin['property info'];

    return array($plugin['property name'] => $info);
  }

  /**
   * Filter handler.
   */
  public static function filter($plugin, &$api_parameters, &$request_body, $condition) {
    if (empty($condition['value'])) {
      return;
    }

    if (empty($condition['operator'])) {
      $condition['operator'] = is_array($condition['value']) ? 'IN' : '=';
    }

    if (isset($plugin['report filter key'])) {
      $key = $plugin['report filter key'];
    }
    else {
      $key = $plugin['report field key'];
    }
    switch ($condition['operator']) {
      case 'IN':
      case '=':
        if ($plugin['report field group'] == 'columns') {
          $request_body['filters']['columnFilters'][$key] = $condition['value'];
        }
        else {
          $request_body['filters']['otherFilters'][$key] = $condition['value'];
        }
        break;

      default:
        $operator = check_plain($condition['operator']);
        throw new ampAPIEntityQueryHelperException("Operator \"$operator\" is not implemented for \"{$plugin['title']}\"");
    }
  }

  /**
   * Sort handler.
   */
  public static function sort($plugin, &$api_parameters, &$request_body, $order) {
    $key = $plugin['report field key'];
    $request_body['sorting'][] = array(
      'columns' => array($key),
      'asc' => $order['direction'] == 'asc' ? TRUE : FALSE,
    );
  }

  /**
   * Value handler.
   */
  public static function getValue($plugin, $column, $formatter = NULL) {
    if ($formatter && $plugin['report field group'] == 'measures') {
      return $formatter->format($column->value);
    }
    return filter_xss($column->displayedValue, array());
  }
}
